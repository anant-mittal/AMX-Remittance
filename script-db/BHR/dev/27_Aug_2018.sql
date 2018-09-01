create or replace PROCEDURE EX_GET_ROUTING_SETUP_ONLINE (P_APPLICATION_COUNTRY_ID    IN  NUMBER,
                                                          P_USER_TYPE                 IN  VARCHAR2,
                                                         P_BENE_COUNTRY_ID           IN  NUMBER,
                                                         P_BENE_BANK_ID              IN  NUMBER,
                                                         P_BENE_BANK_BRANCH_ID       IN  NUMBER,
                                                         P_SERVICE_GROUP_CODE        IN  VARCHAR2,
                                                         P_CURRENCY_ID               IN  NUMBER,
                                                         P_LOCAL_AMT                 IN  NUMBER,
                                                         P_FOREIGN_AMT               IN  NUMBER,
                                                         P_SERVICE_MASTER_ID         OUT NUMBER,
                                                         P_ROUTING_COUNTRY_ID        OUT NUMBER,
                                                         P_ROUTING_BANK_ID           OUT NUMBER,
                                                         P_ROUTING_BANK_BRANCH_ID    OUT NUMBER,
                                                         P_REMITTANCE_MODE_ID        OUT NUMBER,
                                                         P_DELIVERY_MODE_ID          OUT NUMBER,
                                                         P_SWIFT                     OUT VARCHAR2,
                                                         P_ERROR_MESSAGE             OUT VARCHAR2) IS
  W_MESSAGE                   VARCHAR2(200);
  W_SERVICE_MASTER_ID         NUMBER;
  W_SERVICE_CODE              NUMBER;
  W_SWIFT                     VARCHAR2(30);
  
  W_SERVID_CNT                NUMBER := 0;
  W_AMOUNT_INDIC              VARCHAR2(1) := NULL;
  W_SALE_RATE                 NUMBER := 0;
  W_PIPS_DISC                 NUMBER := 0;
  W_CURRENT_RATE              NUMBER := 0;
  W_PREVIOUS_RATE             NUMBER := 0;
  
  W_SEL_ROUTING_COUNTRY_ID        NUMBER;
  W_SEL_ROUTING_BANK_ID           NUMBER;
  W_SEL_ROUTING_BANK_BRANCH_ID    NUMBER;
  W_SEL_REMITTANCE_MODE_ID        NUMBER;
  W_SEL_DELIVERY_MODE_ID          NUMBER;
  
  CURSOR C_SERVICE_ID(C_BENE_BNKID    NUMBER,
                      C_BENE_BRNCHID  NUMBER,
                      C_BENE_CNTRYID  NUMBER,
                      C_CURR_ID       NUMBER,
                      C_SRVGRP_CD     VARCHAR2,
                      C_APPL_CNTRY    NUMBER
                      )  IS
    SELECT DISTINCT SERVICE_CODE,SERVICE_MASTER_ID
      FROM V_EX_ROUTING_DETAILS
     WHERE BENE_BANK_ID           = C_BENE_BNKID
       AND BENE_BANK_BRANCH_ID    = C_BENE_BRNCHID
       AND COUNTRY_ID             = C_BENE_CNTRYID
       AND CURRENCY_ID            = C_CURR_ID
       AND SERVICE_GROUP_CODE     = C_SRVGRP_CD
       --AND SERVICE_MASTER_ID = 102
       AND APPLICATION_COUNTRY_ID = C_APPL_CNTRY
     ORDER BY SERVICE_MASTER_ID;

  CURSOR C_ROUTING_DTLS(C_BENE_BNKID    NUMBER,
                        C_BENE_BRNCHID  NUMBER,
                        C_BENE_CNTRYID  NUMBER,
                        C_CURR_ID       NUMBER,
                        C_SERV_MSTID    NUMBER,
                        C_APPL_CNTRY    NUMBER
                       ) IS
    SELECT DISTINCT F.ROUTING_COUNTRY_ID,
           F.ROUTING_BANK_ID,
           F.REMITTANCE_MODE_ID,
           F.DELIVERY_MODE_ID,
           F.BANK_BRANCH_ID
      FROM V_EX_ROUTING_DETAILS F
     WHERE APPLICATION_COUNTRY_ID = C_APPL_CNTRY
       AND BENE_BANK_ID           = C_BENE_BNKID
       AND BENE_BANK_BRANCH_ID    = C_BENE_BRNCHID
       AND COUNTRY_ID             = C_BENE_CNTRYID
       AND CURRENCY_ID            = C_CURR_ID
       AND SERVICE_MASTER_ID      = C_SERV_MSTID
       AND ROUTING_BANK_ID        = DECODE(C_SERV_MSTID,101,C_BENE_BNKID,F.ROUTING_BANK_ID);

BEGIN
  /*IF NVL(P_USER_TYPE,' ') <>  'ONLINE' THEN
    W_MESSAGE := 'NOT VALID USER TYPE';
    GOTO END_PROCESS;
  END IF;*/
  IF NVL(P_SERVICE_GROUP_CODE,' ') <> 'B' THEN
    W_MESSAGE := 'INVALID SERVICE GROUP '||P_SERVICE_GROUP_CODE;
    GOTO END_PROCESS;
  END IF;
  
  --To Get Swift Code for Beneficiary Bank
  BEGIN
    SELECT A.SWIFT_BIC
      INTO W_SWIFT
      FROM EX_BANK_BRANCH A
     WHERE BANK_ID          =  P_BENE_BANK_ID
       AND BANK_BRANCH_ID   =  P_BENE_BANK_BRANCH_ID
       AND NVL(A.ISACTIVE,' ')  =  'Y';
  EXCEPTION
    WHEN NO_DATA_FOUND THEN  
      NULL;
  END;
  
  W_SERVID_CNT := 0;
  
  IF NVL(P_FOREIGN_AMT,0) <> 0 THEN
    W_AMOUNT_INDIC := 'F';  --Foreign
  ELSE
    W_AMOUNT_INDIC := 'L';  --Local
  END IF;

  FOR R_SERVICE_ID IN C_SERVICE_ID(P_BENE_BANK_ID,P_BENE_BANK_BRANCH_ID,P_BENE_COUNTRY_ID,
                                     P_CURRENCY_ID ,P_SERVICE_GROUP_CODE,P_APPLICATION_COUNTRY_ID)
  LOOP
    DECLARE
      E_NEXT_SERVID  EXCEPTION;
    BEGIN
      W_SERVID_CNT        := NVL(W_SERVID_CNT,0) + 1;
      W_SERVICE_CODE      := R_SERVICE_ID.SERVICE_CODE;
      W_SERVICE_MASTER_ID := R_SERVICE_ID.SERVICE_MASTER_ID;
      
      --Reset Variable
      W_PREVIOUS_RATE              := NULL;
      W_SEL_ROUTING_COUNTRY_ID     := NULL;
      W_SEL_ROUTING_BANK_ID        := NULL;
      W_SEL_ROUTING_BANK_BRANCH_ID := NULL;
      W_SEL_REMITTANCE_MODE_ID     := NULL;
      W_SEL_DELIVERY_MODE_ID       := NULL;
      
      FOR REC_ROUTING_DTLS IN C_ROUTING_DTLS(P_BENE_BANK_ID,P_BENE_BANK_BRANCH_ID,P_BENE_COUNTRY_ID,
                                             P_CURRENCY_ID,W_SERVICE_MASTER_ID,P_APPLICATION_COUNTRY_ID)
      LOOP
        W_SALE_RATE   := 0;
        W_PIPS_DISC   := 0;
        
        DBMS_OUTPUT.put_line('-------------------------------');
        DBMS_OUTPUT.put_line('PREVIOUS BEST RATE'||W_PREVIOUS_RATE);
        DBMS_OUTPUT.put_line('ROUTING SETUP CHECKING '||REC_ROUTING_DTLS.ROUTING_COUNTRY_ID||'/'||
                                                        REC_ROUTING_DTLS.ROUTING_BANK_ID||'/'||
                                                        REC_ROUTING_DTLS.BANK_BRANCH_ID||'/'||
                                                        REC_ROUTING_DTLS.REMITTANCE_MODE_ID||'/'||
                                                        REC_ROUTING_DTLS.DELIVERY_MODE_ID);
        DECLARE
          E_NEXT_ROUTING_DTLS  EXCEPTION;
        BEGIN
          SELECT A.SELL_RATE_MAX
            INTO W_SALE_RATE
            FROM EX_EXCHANGE_RATE_MASTER_APRDET A
           WHERE A.APPLICATION_COUNTRY_ID = P_APPLICATION_COUNTRY_ID
             AND A.CURRENCY_ID            = P_CURRENCY_ID
             AND A.COUNTRY_BRANCH_ID      = 78                                   --Fixed Branch for Online
             AND A.BANK_ID                = REC_ROUTING_DTLS.ROUTING_BANK_ID     --Routing Bank
             AND A.COUNTRY_ID             = REC_ROUTING_DTLS.ROUTING_COUNTRY_ID  --Routing Country
             AND A.SERVICE_INDICATOR_ID   = W_SERVICE_MASTER_ID;
 
          BEGIN
            SELECT PIPS_NO
              INTO W_PIPS_DISC
              FROM EX_PIPS_MASTER 
             WHERE COUNTRY_ID        = REC_ROUTING_DTLS.ROUTING_COUNTRY_ID --Routing Country id
               AND COUNTRY_BRANCH_ID = 78                                  --Fixed
               AND BANK_ID           = REC_ROUTING_DTLS.ROUTING_BANK_ID    --Routing Bank Id
               AND CURRENCY_ID       = P_CURRENCY_ID
               AND FROM_AMOUNT  <= DECODE(W_AMOUNT_INDIC,'F',P_FOREIGN_AMT,(NVL(P_LOCAL_AMT,0)*W_SALE_RATE))
               AND TO_AMOUNT    >= DECODE(W_AMOUNT_INDIC,'F',P_FOREIGN_AMT,(NVL(P_LOCAL_AMT,0)*W_SALE_RATE)); 
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              W_PIPS_DISC := 0; 
            WHEN TOO_MANY_ROWS THEN
              W_MESSAGE   :=   'MULTIPLE PIPS SETUP DEFINED';
              GOTO END_PROCESS;
          END;
          
          DBMS_OUTPUT.put_line('W_SALE_RATE: '||W_SALE_RATE||'/'||'W_PIPS_DISC: '||W_PIPS_DISC);
          
          IF NVL(W_SALE_RATE,0) <> 0 THEN
            W_CURRENT_RATE := NVL(W_SALE_RATE,0) - NVL(W_PIPS_DISC,0);
          ELSE
            RAISE E_NEXT_ROUTING_DTLS;
          END IF;
          
          DBMS_OUTPUT.put_line('W_CURRENT_RATE: '||W_CURRENT_RATE);
          
          IF (W_PREVIOUS_RATE IS NULL OR NVL(W_PREVIOUS_RATE,0) > NVL(W_CURRENT_RATE,0)) THEN
            W_SEL_ROUTING_COUNTRY_ID     := REC_ROUTING_DTLS.ROUTING_COUNTRY_ID;
            W_SEL_ROUTING_BANK_ID        := REC_ROUTING_DTLS.ROUTING_BANK_ID;
            W_SEL_ROUTING_BANK_BRANCH_ID := REC_ROUTING_DTLS.BANK_BRANCH_ID;
            W_SEL_REMITTANCE_MODE_ID     := REC_ROUTING_DTLS.REMITTANCE_MODE_ID;
            W_SEL_DELIVERY_MODE_ID       := REC_ROUTING_DTLS.DELIVERY_MODE_ID;
            
            W_PREVIOUS_RATE              := W_CURRENT_RATE;  --Assign Available best minimum rate
          END IF;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            W_SALE_RATE  := 0;
            W_PIPS_DISC  := 0;  
          WHEN TOO_MANY_ROWS THEN
            W_MESSAGE   :=   'MULTIPLE RATE SETUP DEFINED';
            GOTO END_PROCESS;    
        END;
      END LOOP;
      
      IF W_SEL_ROUTING_COUNTRY_ID IS NOT NULL THEN
        GOTO END_PROCESS;
      ELSE
        RAISE E_NEXT_SERVID;
      END IF;
    EXCEPTION
      WHEN E_NEXT_SERVID THEN
        W_SERVID_CNT        := 0;
        W_SERVICE_CODE      := NULL;
        W_SERVICE_MASTER_ID := NULL;
    END;
  END LOOP;

  IF NVL(W_SERVID_CNT,0) = 0 THEN
    W_MESSAGE   :=   'NO SERVICES FOR THIS BRANCH';
    GOTO END_PROCESS;
  END IF;
 
  <<END_PROCESS>>
    P_ERROR_MESSAGE          :=  W_MESSAGE;
    P_SERVICE_MASTER_ID      :=  W_SERVICE_MASTER_ID;
    P_ROUTING_COUNTRY_ID     :=  W_SEL_ROUTING_COUNTRY_ID;
    P_ROUTING_BANK_ID        :=  W_SEL_ROUTING_BANK_ID;
    P_ROUTING_BANK_BRANCH_ID :=  W_SEL_ROUTING_BANK_BRANCH_ID ;
    P_REMITTANCE_MODE_ID     :=  W_SEL_REMITTANCE_MODE_ID;
    P_DELIVERY_MODE_ID       :=  W_SEL_DELIVERY_MODE_ID;
    P_SWIFT                  :=  W_SWIFT;
END EX_GET_ROUTING_SETUP_ONLINE; 