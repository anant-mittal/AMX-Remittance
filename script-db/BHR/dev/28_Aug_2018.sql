create or replace PROCEDURE       EX_GET_ROUTING_SET_UP_OTH_RATE
    (P_APPLICATION_COUNTRY_ID    IN  NUMBER,
        P_USER_TYPE                  IN  VARCHAR2,
    P_BENE_COUNTRY_ID            IN  NUMBER,
    P_BENE_BANK_ID               IN  NUMBER,
    P_BENE_BANK_BRANCH_ID        IN  NUMBER,
        P_BENE_BANK_ACCOUNT          IN  VARCHAR2,
        P_CUSTOMER_ID                IN  NUMBER,
    P_SERVICE_GROUP_CODE         IN  VARCHAR2,
    P_CURRENCY_ID                IN  NUMBER,
    P_FC_AMT                     IN  NUMBER,           ----- Added on 9th Aug 2018
    P_LOCAL_CUR_AMT              IN  NUMBER,           ----- Added on 9th Aug 2018
        P_SERVICE_MASTER_ID          OUT NUMBER,
        P_ROUTING_COUNTRY_ID         OUT NUMBER,
        P_ROUTING_BANK_ID            OUT NUMBER,
        P_ROUTING_BANK_BRANCH_ID     OUT NUMBER,
        P_REMITTANCE_MODE_ID         OUT NUMBER,
        P_DELIVERY_MODE_ID           OUT NUMBER,
        P_SWIFT                      OUT VARCHAR2,
        P_DERIVED_SELL_RATE          OUT NUMBER,
    P_ERROR_MESSAGE              OUT VARCHAR2) IS


---------Modified to fetch Best Rate offered by routing bank for other than Country India  on 9th Aug 2018

W_MESSAGE          VARCHAR2(200);
W_SERVICE_MASTER_ID       NUMBER;
W_ROUTING_COUNTRY_ID      NUMBER;
W_ROUTING_BANK_ID         NUMBER;
W_ROUTING_BANK_BRANCH_ID  NUMBER;
W_SERVICE_CODE            NUMBER;
W_REMITTANCE_MODE_ID      NUMBER;
W_DELIVERY_MODE_ID        NUMBER;
W_CORSBNK                 VARCHAR2(10);
W_CORSBNK_ID              NUMBER;
W_BENE_BANK_CODE          VARCHAR2(10);
W_CORSBRCH                NUMBER;
W_CORSBRCH_ID             NUMBER;
W_BENE_BANK_BRANCH_CODE   NUMBER;
W_CORCNTY                 VARCHAR2(10);
W_BENE_COUNTRY_CODE       VARCHAR2(10);
W_BENE_CURRENCY_CODE      VARCHAR2(10);
W_REMTMOD                 VARCHAR2(10);
W_DELVMOD                 VARCHAR2(10);
W_CUSREF                  NUMBER;
W_CORCNTY_ID              NUMBER;
W_NOBANK_CODE             VARCHAR2(10);
W_SWIFT                   VARCHAR2(30);
---- From here added by Parasuraman
W_DERIVED_SELL_RATE       NUMBER;
W_APROX_EXC_RATE          NUMBER;
W_APROX_FC_AMT            NUMBER;
---- From here added by Parasuraman
BEGIN
    W_CORSBNK      :=  '';
    W_CORSBNK_ID   :=  '';
    W_DERIVED_SELL_RATE := 0;
    W_APROX_EXC_RATE    := 0;
    W_APROX_FC_AMT      := 0;
   BEGIN
        SELECT CUSTOMER_REFERENCE   INTO  W_CUSREF
        FROM   FS_CUSTOMER
        WHERE  CUSTOMER_ID   =  P_CUSTOMER_ID;
   EXCEPTION
         WHEN OTHERS   THEN  NULL;
   END;

   BEGIN
        SELECT A.BANK_CODE    INTO   W_BENE_BANK_CODE
        FROM   EX_BANK_MASTER A
        WHERE  A.BANK_ID        =   P_BENE_BANK_ID
        AND    NVL(A.ISACTIVE,' ')  =  'Y';
    EXCEPTION
         WHEN NO_DATA_FOUND THEN  NULL;
    END;

    BEGIN
        SELECT A.BRANCH_CODE   INTO  W_BENE_BANK_BRANCH_CODE
        FROM   EX_BANK_BRANCH A
        WHERE  BANK_ID          =  P_BENE_BANK_ID
        AND    BANK_BRANCH_ID   =  P_BENE_BANK_BRANCH_ID
        AND    NVL(A.ISACTIVE,' ')  =  'Y';
    EXCEPTION
         WHEN NO_DATA_FOUND THEN  NULL;
    END;

    BEGIN
        SELECT COUNTRY_CODE  INTO  W_BENE_COUNTRY_CODE
        FROM   FS_COUNTRY_MASTER
        WHERE  COUNTRY_ID    =   P_BENE_COUNTRY_ID;
    EXCEPTION
         WHEN NO_DATA_FOUND THEN  NULL;
    END;

    BEGIN
       SELECT CURRENCY_CODE    INTO   W_BENE_CURRENCY_CODE
       FROM   EX_CURRENCY_MASTER
       WHERE  CURRENCY_ID   =  P_CURRENCY_ID;
    EXCEPTION
       WHEN NO_DATA_FOUND THEN  NULL;
    END;


     dbms_output.put_line('TRF CUSREF '  ||       W_CUSREF);
     dbms_output.put_line('TRF BENE BANK CODE '  ||  W_BENE_BANK_CODE);
     dbms_output.put_line('TRF BENE_BANK_BRANCH_CODE   '  ||  W_BENE_BANK_BRANCH_CODE);
     dbms_output.put_line('TRF BENE_BANK_ACCOUNT   '  ||  P_BENE_BANK_ACCOUNT);
     dbms_output.put_line('TRF BENE_COUNTRY_CODE  '  ||  W_BENE_COUNTRY_CODE);
     dbms_output.put_line('TRF BENE_CURRENCY_CODE  '  ||  W_BENE_CURRENCY_CODE);


    W_REMITTANCE_MODE_ID      :=   '';
    W_DELIVERY_MODE_ID        :=   '';
    W_SERVICE_MASTER_ID       :=   '';
    W_CORSBNK_ID              :=   '';
    W_CORSBRCH_ID             :=   '';
    W_CORCNTY_ID              :=   '';
    W_SWIFT                   :=   '';

        DECLARE   CURSOR   C_TRANSFER  IS
            SELECT A.CORSBNK, A.CORSBRCH, A.COUNTRY, A.REMTMOD, A.DELVMOD, A.BNFINTB1_SWIFT
            FROM   V_TRANSFER A, V_REMT B
            WHERE  A.CUSREF             =  W_CUSREF
            AND    A.BNFBNKCOD          =  W_BENE_BANK_CODE
            AND    A.BNFBRCHCOD         =  W_BENE_BANK_BRANCH_CODE
            AND    NVL(A.BNFACNO,' ')   =  NVL(P_BENE_BANK_ACCOUNT,' ')
            AND    A.CORCNTY            =  W_BENE_COUNTRY_CODE
            AND    A.CURTRNS            =  W_BENE_CURRENCY_CODE
            AND    A.TAGSTS            IS  NULL
            AND    A.CORSBNK          NOT IN  ('WU')
            AND    NVL(A.CANSTS,' ')    =  ' '
            AND    NVL(A.STOPSTS,' ')   =  ' '
            AND    NVL(A.TRNFSTS,' ') NOT IN ('C','S')
            AND    A.REMTMOD            =  B.REMTMOD
            AND    A.DELVMOD           <>  10
            AND  ((P_SERVICE_GROUP_CODE =  'B' AND B.SERV_ID IN (1,2))
                   OR
                  (P_SERVICE_GROUP_CODE =  'C' AND B.SERV_ID = 3))
            ORDER  BY  A.DOCDAT DESC;
        BEGIN
            FOR C1  IN  C_TRANSFER  LOOP
                W_CORSBNK   :=   C1.CORSBNK;
                W_CORSBRCH  :=   C1.CORSBRCH;
                W_SWIFT     :=   C1.BNFINTB1_SWIFT;
                W_CORCNTY   :=   C1.COUNTRY;
                W_REMTMOD   :=   C1.REMTMOD;
                W_DELVMOD   :=   C1.DELVMOD;
                EXIT;
            END LOOP;
        END;

     dbms_output.put_line('SWIFT1 '  || W_SWIFT);

     IF  NVL(W_REMTMOD,' ')      =   ' '    AND
         P_SERVICE_GROUP_CODE    =   'C'    THEN
         dbms_output.put_line('old transactions ');
         W_NOBANK_CODE        :=  '';
         BEGIN
             SELECT BNKCOD   INTO  W_NOBANK_CODE
             FROM   CORBANK
             WHERE  COUNTRY          =  W_BENE_COUNTRY_CODE
             AND    BNKCOD        LIKE  'NBANK%'
             AND    NVL(RECSTS,' ')  =  ' ';
         EXCEPTION
              WHEN NO_DATA_FOUND THEN NULL;
              WHEN TOO_MANY_ROWS THEN NULL;
         END;

         DECLARE   CURSOR   C_TRANSFER  IS
            SELECT A.CORSBNK, A.CORSBRCH, A.COUNTRY, A.REMTMOD, A.DELVMOD, A.BNFINTB1_SWIFT
            FROM   V_TRANSFER A, V_REMT B
            WHERE  A.CUSREF             =  W_CUSREF
            AND    A.BNFBNKCOD          =  W_NOBANK_CODE
           -- AND    A.BNFBRCHCOD         =  W_BENE_BANK_BRANCH_CODE
            AND    NVL(A.BNFACNO,' ')   =  NVL(P_BENE_BANK_ACCOUNT,' ')
            AND    A.CORCNTY            =  W_BENE_COUNTRY_CODE
            AND    A.CURTRNS            =  W_BENE_CURRENCY_CODE
            AND    A.TAGSTS            IS  NULL
            AND    A.CORSBNK            =  W_BENE_BANK_CODE
            AND    NVL(A.CANSTS,' ')    =  ' '
            AND    NVL(A.STOPSTS,' ')   =  ' '
            AND    NVL(A.TRNFSTS,' ') NOT IN ('C','S')
            AND    A.REMTMOD            =  B.REMTMOD
            AND    A.DELVMOD           <>  10
            ORDER  BY  A.DOCDAT DESC;
         BEGIN
            FOR C1  IN  C_TRANSFER  LOOP
                W_CORSBNK   :=   C1.CORSBNK;
                W_CORSBRCH  :=   C1.CORSBRCH;
                W_SWIFT     :=   C1.BNFINTB1_SWIFT;
                W_CORCNTY   :=   C1.COUNTRY;
                W_REMTMOD   :=   C1.REMTMOD;
                W_DELVMOD   :=   C1.DELVMOD;
                EXIT;
            END LOOP;
        END;
     END IF;

  dbms_output.put_line('SWIFT2 '  || W_SWIFT);

     IF  NVL(W_CORSBNK,' ')   <>  ' ' THEN
         BEGIN
             SELECT REMITTANCE_MODE_ID   INTO   W_REMITTANCE_MODE_ID
             FROM   EX_rEMITTANCE_MODE
             WHERE  REMITTANCE_CODE   =  W_REMTMOD;
         EXCEPTION
              WHEN NO_DATA_FOUND THEN  NULL;
         END;

         BEGIN
             SELECT DELIVERY_MODE_ID       INTO   W_DELIVERY_MODE_ID
             FROM   EX_DELIVERY_MODE
             WHERE  DELIVERY_CODE   =  W_DELVMOD;
         EXCEPTION
              WHEN NO_DATA_FOUND THEN  NULL;
         END;

         BEGIN
             SELECT BANK_ID   INTO   W_CORSBNK_ID
             FROM   EX_BANK_MASTER
             WHERE  BANK_CODE   =  W_CORSBNK;
         EXCEPTION
              WHEN NO_DATA_FOUND THEN  NULL;
         END;

         BEGIN
             SELECT A.BANK_BRANCH_ID    INTO  W_CORSBRCH_ID
             FROM   EX_BANK_BRANCH A
             WHERE  BANK_ID      =  W_CORSBNK_ID
             AND    BRANCH_CODE  =  W_CORSBRCH
             AND    NVL(A.ISACTIVE,' ')  =  'Y';
         EXCEPTION
              WHEN NO_DATA_FOUND THEN  NULL;
         END;

         BEGIN
             SELECT COUNTRY_ID   INTO   W_CORCNTY_ID
             FROM   FS_COUNTRY_MASTER
             WHERE  COUNTRY_CODE    =   W_CORCNTY;
         EXCEPTION
              WHEN NO_DATA_FOUND THEN  NULL;
         END;

     dbms_output.put_line('TRF CORBANK  '  ||       W_CORSBNK  || '  ' || W_CORSBNK_ID);
     dbms_output.put_line('TRF CORBANK BRCH  '  ||  W_CORSBRCH || '  ' || W_CORSBRCH_ID);
     dbms_output.put_line('TRF CORCNTY   '  ||  W_CORCNTY      || '  ' || W_CORCNTY_ID );
     dbms_output.put_line('TRF REMITMOD   '  ||  W_REMTMOD     || '  ' || W_REMITTANCE_MODE_ID);
     dbms_output.put_line('TRF DELVMOD  '  ||  W_DELVMOD       || '  ' || W_DELIVERY_MODE_ID);


         BEGIN
             SELECT DISTINCT  D.SERVICE_MASTER_ID  INTO   W_SERVICE_MASTER_ID
             FROM   V_EX_BENESERV_AVAIL D, EX_ROUTING_HEADER F,EX_ROUTING_DETAILS G
             WHERE  D.BANK_ID              =  P_BENE_BANK_ID
             AND    D.BANK_BRANCH_ID       =  P_BENE_BANK_BRANCH_ID
             AND    D.COUNTRY_ID           =  P_BENE_COUNTRY_ID
             AND    D.COUNTRY_ID           =  F.COUNTRY_ID
             AND    D.CURRENCY_ID          =  F.CURRENCY_ID
             AND    D.SERVICE_MASTER_ID    =  F.SERVICE_MASTER_ID
             AND    F.ROUTING_HEADER_ID    =  G.ROUTING_HEADER_ID
             AND    D.REMITTANCE_MODE_ID   =  W_REMITTANCE_MODE_ID
             AND    D.DELIVERY_MODE_ID     =  W_DELIVERY_MODE_ID
             AND  ((D.SERVICE_MASTER_ID    =  101 AND D.BANK_ID = F.ROUTING_BANK_ID)
                   OR
                  (D.SERVICE_MASTER_ID     =  103)
                   OR
                  (D.SERVICE_MASTER_ID     =  102 AND D.BANK_ID <> F.ROUTING_BANK_ID)
                 )
             AND    F.ROUTING_BANK_ID      =  W_CORSBNK_ID
             AND    G.BANK_BRANCH_ID       =  W_CORSBRCH_ID
             AND    F.CURRENCY_ID          =  P_CURRENCY_ID
             AND    G.ROUTING_COUNTRY_ID   =  W_CORCNTY_ID
             AND    NVL(F.ISACTIVE,' ')    =  'Y'
             AND    NVL(G.ISACTIVE,' ')    =  'Y';
         EXCEPTION
              WHEN NO_DATA_FOUND  THEN NULL;
         END;
         
         
            ---- ZZZ  ---Parasuraman from here  ON 9th Aug 2018  --- 
         ----   IF( NVL(W_IMPS,' ') <> 'Y' ) THEN
          ------zzzz---
          IF( P_BENE_COUNTRY_ID <> 94 ) THEN
          
              IF( P_SERVICE_GROUP_CODE        <>  'C' ) THEN 
                W_SERVICE_MASTER_ID := 0;
              END IF;
          ---  END IF;
          END IF;
            -- Parasuraman till  here --- 

         IF  NVL(W_SERVICE_MASTER_ID,0)  >  0  THEN
             W_SERVICE_MASTER_ID      :=  W_SERVICE_MASTER_ID;
             W_ROUTING_COUNTRY_ID     :=  W_CORCNTY_ID;
             W_ROUTING_BANK_ID        :=  W_CORSBNK_ID;
             W_ROUTING_BANK_BRANCH_ID :=  W_CORSBRCH_ID;
             W_REMITTANCE_MODE_ID     :=  W_REMITTANCE_MODE_ID;
             W_DELIVERY_MODE_ID       :=  W_DELIVERY_MODE_ID;
             DBMS_OUTPUT.PUT_LINE('IN HERE 1');

             GOTO  END_PROCESS;
         END IF;

     END IF;


     IF  NVL(W_SERVICE_MASTER_ID,0)  =  0      AND
         P_SERVICE_GROUP_CODE        =  'C'    THEN
         W_MESSAGE                 :=  'Service is not available - Please go to manned counter';
         W_SERVICE_MASTER_ID      :=  '';
         W_ROUTING_COUNTRY_ID     :=  '';
         W_ROUTING_BANK_ID        :=  '';
         W_ROUTING_BANK_BRANCH_ID :=  '';
         W_ROUTING_COUNTRY_ID     :=  '';
         W_REMITTANCE_MODE_ID     :=  '';
         W_DELIVERY_MODE_ID       :=  '';
         GOTO  END_PROCESS;
     END IF;

     W_REMITTANCE_MODE_ID   :=  '';
     W_DELIVERY_MODE_ID     :=  '';
     W_SERVICE_MASTER_ID    :=  '';


     dbms_output.put_line('P_BENE_BANK_ID  '  ||      P_BENE_BANK_ID);
     dbms_output.put_line('P_BENE_BANK_BRANCH_ID  '  ||     P_BENE_BANK_BRANCH_ID);
     dbms_output.put_line('P_BENE_COUNTRY_ID  '  ||      P_BENE_COUNTRY_ID);
     dbms_output.put_line('P_CURRENCY_ID  '  ||      P_CURRENCY_ID);
     dbms_output.put_line('P_BENE_BANK_ID  '  ||      P_BENE_BANK_ID);
     dbms_output.put_line('W_CORSBNK_ID  '  ||      W_CORSBNK_ID);


     IF    P_SERVICE_GROUP_CODE = 'B' THEN
           --IF BANKING CHANNEL CHECK TO SEE IF EFT IS AVAILABLE. IF NOT AVAILABLE THEN CHECK TO SEE IF TT IS AVAILABLE
      /*
           BEGIN
               SELECT MIN(D.SERVICE_MASTER_ID)    INTO   W_SERVICE_MASTER_ID
               FROM   V_EX_BENESERV_AVAIL D,EX_ROUTING_HEADER F, EX_ROUTING_DETAILS G
               WHERE  D.BANK_ID             =   P_BENE_BANK_ID
               AND    D.BANK_BRANCH_ID      =   P_BENE_BANK_BRANCH_ID
               AND    D.COUNTRY_ID          =   P_BENE_COUNTRY_ID
               AND    D.COUNTRY_ID          =   F.COUNTRY_ID
               AND    D.CURRENCY_ID         =   F.CURRENCY_ID
               AND    D.SERVICE_MASTER_ID   =   F.SERVICE_MASTER_ID
               AND    F.ROUTING_HEADER_ID   =   G.ROUTING_HEADER_ID
               AND    D.SERVICE_MASTER_ID   =   101
               AND    D.BANK_ID             =   F.ROUTING_BANK_ID
               AND    D.REMITTANCE_MODE_ID  =   NVL(F.REMITTANCE_MODE_ID,D.REMITTANCE_MODE_ID)
               AND    D.DELIVERY_MODE_ID    =   NVL(F.DELIVERY_MODE_ID,D.DELIVERY_MODE_ID)
               AND    F.CURRENCY_ID         =   P_CURRENCY_ID
               AND    F.ROUTING_BANK_ID     =   W_CORSBNK_ID
               AND    NVL(F.ISACTIVE,' ')   =   'Y'
               AND    NVL(G.ISACTIVE,' ')   =   'Y';

               DBMS_OUTPUT.PUT_LINE('IN HERE 1111 ' ||   W_SERVICE_MASTER_ID);
           EXCEPTION
                WHEN NO_DATA_FOUND THEN  NULL;
           END;

           IF  NVL(W_SERVICE_MASTER_ID,0)   =  0  THEN
               DBMS_OUTPUT.PUT_LINE('IN HERE 1112');
               BEGIN
                   SELECT MIN(D.SERVICE_MASTER_ID)    INTO   W_SERVICE_MASTER_ID
                   FROM   V_EX_BENESERV_AVAIL D,EX_ROUTING_HEADER F, EX_ROUTING_DETAILS G
                   WHERE  D.BANK_ID             =   P_BENE_BANK_ID
                   AND    D.BANK_BRANCH_ID      =   P_BENE_BANK_BRANCH_ID
                   AND    D.COUNTRY_ID          =   P_BENE_COUNTRY_ID
                   AND    D.COUNTRY_ID          =   F.COUNTRY_ID
                   AND    D.CURRENCY_ID         =   F.CURRENCY_ID
                   AND    D.SERVICE_MASTER_ID   =   F.SERVICE_MASTER_ID
                   AND    F.ROUTING_HEADER_ID   =   G.ROUTING_HEADER_ID
                   AND    D.SERVICE_MASTER_ID   =   102
                   AND    D.REMITTANCE_MODE_ID  =   NVL(F.REMITTANCE_MODE_ID,D.REMITTANCE_MODE_ID)
                   AND    D.DELIVERY_MODE_ID    =   NVL(F.DELIVERY_MODE_ID,D.DELIVERY_MODE_ID)
                   AND    F.CURRENCY_ID         =   P_CURRENCY_ID
                   AND    D.BANK_ID             =   F.ROUTING_BANK_ID
                   AND    F.ROUTING_BANK_ID     =   NVL(W_CORSBNK_ID,F.ROUTING_BANK_ID)
                   AND    NVL(F.ISACTIVE,' ')   =   'Y'
                   AND    NVL(G.ISACTIVE,' ')   =   'Y';
                EXCEPTION
                    WHEN NO_DATA_FOUND THEN  NULL;
                END;
            END IF;
            */
            IF  NVL(W_SERVICE_MASTER_ID,0)   =  0  THEN
  DBMS_OUTPUT.PUT_LINE('IN HERE 1113');
                BEGIN
                    SELECT MIN(D.SERVICE_MASTER_ID)    INTO   W_SERVICE_MASTER_ID
                    FROM   V_EX_BENESERV_AVAIL D,EX_ROUTING_HEADER F, EX_ROUTING_DETAILS G
                    WHERE  D.BANK_ID             =   P_BENE_BANK_ID
                    AND    D.BANK_BRANCH_ID      =   P_BENE_BANK_BRANCH_ID
                    AND    D.COUNTRY_ID          =   P_BENE_COUNTRY_ID
                    AND    D.COUNTRY_ID          =   F.COUNTRY_ID
                    AND    D.CURRENCY_ID         =   F.CURRENCY_ID
                    AND    D.SERVICE_MASTER_ID   =   F.SERVICE_MASTER_ID
                    AND    F.ROUTING_HEADER_ID   =   G.ROUTING_HEADER_ID
                    AND  ((D.SERVICE_MASTER_ID=101 AND D.BANK_ID = F.ROUTING_BANK_ID)
                           OR
                          (D.SERVICE_MASTER_ID = 102 AND D.BANK_ID <> F.ROUTING_BANK_ID))
                    AND    D.REMITTANCE_MODE_ID  =   NVL(F.REMITTANCE_MODE_ID,D.REMITTANCE_MODE_ID)
                    AND    D.DELIVERY_MODE_ID    =   NVL(F.DELIVERY_MODE_ID,D.DELIVERY_MODE_ID)
                    AND    F.CURRENCY_ID         =   P_CURRENCY_ID
                    AND    NVL(F.ISACTIVE,' ')   =   'Y'
                    AND    NVL(G.ISACTIVE,' ')   =   'Y';
                EXCEPTION
                     WHEN NO_DATA_FOUND THEN
                          W_MESSAGE   :=   'No services for this branch';
                          GOTO END_PROCESS;
                END;
            END IF;
 DBMS_OUTPUT.PUT_LINE('IN HERE 1111 ' ||   W_SERVICE_MASTER_ID);
     ELSIF P_SERVICE_GROUP_CODE = 'C' THEN
               BEGIN
                   SELECT MIN(D.SERVICE_MASTER_ID)    INTO   W_SERVICE_MASTER_ID
                   FROM   V_EX_BENESERV_AVAIL D,EX_ROUTING_HEADER F, EX_ROUTING_DETAILS G
                   WHERE  D.BANK_ID             =   P_BENE_BANK_ID
                   AND    D.BANK_BRANCH_ID      =   P_BENE_BANK_BRANCH_ID
                   AND    D.COUNTRY_ID          =   P_BENE_COUNTRY_ID
                   AND    D.COUNTRY_ID          =   F.COUNTRY_ID
                   AND    D.CURRENCY_ID         =   F.CURRENCY_ID
                   AND    D.SERVICE_MASTER_ID   =   F.SERVICE_MASTER_ID
                   AND    F.ROUTING_HEADER_ID   =   G.ROUTING_HEADER_ID
                   AND    D.SERVICE_MASTER_ID   =   103
                   AND    D.REMITTANCE_MODE_ID  =   NVL(F.REMITTANCE_MODE_ID,D.REMITTANCE_MODE_ID)
                   AND    D.DELIVERY_MODE_ID    =   NVL(F.DELIVERY_MODE_ID,D.DELIVERY_MODE_ID)
                   AND    F.CURRENCY_ID         =   P_CURRENCY_ID
                   AND    D.BANK_ID             =   F.ROUTING_BANK_ID
                   AND    F.ROUTING_BANK_ID     =   NVL(P_BENE_BANK_ID,F.ROUTING_BANK_ID)
                   AND    NVL(F.ISACTIVE,' ')   =   'Y'
                   AND    NVL(G.ISACTIVE,' ')   =   'Y';
               EXCEPTION
                   WHEN NO_DATA_FOUND THEN
                        W_MESSAGE   :=   'No services for this branch';
                        GOTO END_PROCESS;

                   WHEN TOO_MANY_ROWS THEN
                        W_MESSAGE   :=   'Too many services for this branch';
                        GOTO END_PROCESS;
               END;
     ELSE
         W_MESSAGE := 'INVALID SERVICE GROUP '||P_SERVICE_GROUP_CODE;
         GOTO END_PROCESS;
     END IF;


     DBMS_OUTPUT.PUT_LINE('W_SERVICE_MASTER_ID '||W_SERVICE_MASTER_ID);

     IF  NVL(W_SERVICE_MASTER_ID,0)   =   0  THEN
         W_MESSAGE   :=   'INVALID SERVICE MASTER';
         GOTO END_PROCESS;
     END IF;

     IF  NVL(W_SERVICE_MASTER_ID,0)  >  0  THEN
     
   
         BEGIN
              SELECT MIN(F.ROUTING_COUNTRY_ID)    INTO   W_ROUTING_COUNTRY_ID
              FROM   V_EX_ROUTING_DETAILS F
              WHERE  BENE_BANK_ID        = P_BENE_BANK_ID
              AND    BENE_BANK_BRANCH_ID = P_BENE_BANK_BRANCH_ID
              AND    F.COUNTRY_ID        = P_BENE_COUNTRY_ID
              AND    F.CURRENCY_ID       = P_CURRENCY_ID
              AND    F.SERVICE_MASTER_ID = W_SERVICE_MASTER_ID
              AND    F.ROUTING_BANK_ID   = DECODE(W_SERVICE_MASTER_ID,101,P_BENE_BANK_ID,F.ROUTING_BANK_ID);
         EXCEPTION
              WHEN NO_DATA_FOUND THEN  NULL;
         END;

         IF  NVL(W_ROUTING_COUNTRY_ID,0)   =  0  THEN
             W_MESSAGE   :=   'INVALID ROUTING COUNTRY  ';
             GOTO END_PROCESS;
         END IF;


     END IF;
    W_DERIVED_SELL_RATE := 0;
        DBMS_OUTPUT.PUT_LINE('HERE2');

     IF  NVL(W_ROUTING_COUNTRY_ID,0) > 0 THEN
     
       IF( W_ROUTING_COUNTRY_ID = 94 ) THEN   ---- if India --- by Parasuraman on 9th Aug 2018
         BEGIN
             SELECT MIN(F.ROUTING_BANK_ID)     INTO   W_ROUTING_BANK_ID
             FROM   V_EX_ROUTING_DETAILS F
             WHERE  BENE_BANK_ID          =  P_BENE_BANK_ID
             AND    BENE_BANK_BRANCH_ID   =  P_BENE_BANK_BRANCH_ID
             AND    F.COUNTRY_ID          =  P_BENE_COUNTRY_ID
             AND    F.CURRENCY_ID         =  P_CURRENCY_ID
             AND    F.SERVICE_MASTER_ID   =  W_SERVICE_MASTER_ID
             AND    F.ROUTING_COUNTRY_ID  =  W_ROUTING_COUNTRY_ID
             AND    F.ROUTING_BANK_ID     =  DECODE(W_SERVICE_MASTER_ID,101,
                                                              P_BENE_BANK_ID,F.ROUTING_BANK_ID);
         EXCEPTION
              WHEN NO_DATA_FOUND THEN
                   W_MESSAGE   :=   'INVALID ROUTING BANK ';
                   GOTO END_PROCESS;
         END;

         IF  NVL(W_ROUTING_BANK_ID,0)   =  0  THEN
             W_MESSAGE   :=   'INVALID ROUTING BANK ';
             GOTO END_PROCESS;
         END IF;
      ELSE
         ---- ZZZ  ---Parasuraman from here  ON 9th Aug 2018  --- 
         IF( NVL(P_FC_AMT,0) >  0 ) THEN
         
         
         BEGIN
         
         SELECT BANK_ID,DERIVED_SELL_RATE INTO  W_ROUTING_BANK_ID, W_DERIVED_SELL_RATE
         FROM
           ( SELECT PM.BANK_ID,PM.DERIVED_SELL_RATE  
             FROM   EX_PIPS_MASTER PM
             WHERE  PM.COUNTRY_ID          =  W_ROUTING_COUNTRY_ID
             AND    PM.CURRENCY_ID         =  P_CURRENCY_ID
             AND    PM.COUNTRY_BRANCH_ID = 78
             AND    PM.BANK_ID IN (
                                            SELECT F.ROUTING_BANK_ID
                                                   FROM   V_EX_ROUTING_DETAILS F
                                                   WHERE  BENE_BANK_ID          =  P_BENE_BANK_ID
                                                    AND    BENE_BANK_BRANCH_ID   =  P_BENE_BANK_BRANCH_ID
                                                    AND    F.COUNTRY_ID          =  P_BENE_COUNTRY_ID
                                                    AND    F.CURRENCY_ID         =  P_CURRENCY_ID
                                                    AND    F.SERVICE_MASTER_ID   =  W_SERVICE_MASTER_ID
                                                    AND    F.ROUTING_COUNTRY_ID  =  W_ROUTING_COUNTRY_ID
                                                    AND    F.ROUTING_BANK_ID     =  DECODE(W_SERVICE_MASTER_ID,101,
                                                              P_BENE_BANK_ID,F.ROUTING_BANK_ID)  
                                           )
            
              AND ( P_FC_AMT BETWEEN  PM.FROM_AMOUNT AND  PM.TO_AMOUNT )
              AND NVL(PM.DERIVED_SELL_RATE,0) <> 0
              ORDER BY PM.DERIVED_SELL_RATE ASC)
            WHERE ROWNUM = 1;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
                 W_MESSAGE   :=   'ROUTING BANK NOT AVAILABLE IN EX_PIPS_MASTER ';
                 GOTO END_PROCESS;
          END;
            IF  NVL(W_ROUTING_BANK_ID,0)   =  0  THEN
                 W_MESSAGE   :=   'INVALID ROUTING BANK ';
                 GOTO END_PROCESS;
            END IF;
             IF  NVL(W_DERIVED_SELL_RATE,0)   =  0  THEN
                 W_MESSAGE   :=   'INVALID EXCHANGE RATE';
                 GOTO END_PROCESS;
            END IF;
            
         ELSE
           IF( NVL( P_LOCAL_CUR_AMT,0) <= 0 ) THEN
                 W_MESSAGE   :=   'INVALID LOCAL CURRENCY AMOUNT';
                 GOTO END_PROCESS;
           END IF;
         BEGIN  
           SELECT DERIVED_SELL_RATE   INTO W_APROX_EXC_RATE
           FROM
          ( SELECT PM.DERIVED_SELL_RATE
             FROM   EX_PIPS_MASTER PM
             WHERE  PM.COUNTRY_ID          =  W_ROUTING_COUNTRY_ID
             AND    PM.CURRENCY_ID         =  P_CURRENCY_ID
             AND    PM.COUNTRY_BRANCH_ID = 78
             AND    PM.BANK_ID IN (
                                            SELECT F.ROUTING_BANK_ID
                                                   FROM   V_EX_ROUTING_DETAILS F
                                                   WHERE  BENE_BANK_ID          =  P_BENE_BANK_ID
                                                    AND    BENE_BANK_BRANCH_ID   =  P_BENE_BANK_BRANCH_ID
                                                    AND    F.COUNTRY_ID          =  P_BENE_COUNTRY_ID
                                                    AND    F.CURRENCY_ID         =  P_CURRENCY_ID
                                                    AND    F.SERVICE_MASTER_ID   =  W_SERVICE_MASTER_ID
                                                    AND    F.ROUTING_COUNTRY_ID  =  W_ROUTING_COUNTRY_ID
                                                    AND    F.ROUTING_BANK_ID     =  DECODE(W_SERVICE_MASTER_ID,101,
                                                              P_BENE_BANK_ID,F.ROUTING_BANK_ID)  
                                           )
            
              AND NVL(PM.DERIVED_SELL_RATE,0) <> 0
              ORDER BY PM.DERIVED_SELL_RATE ASC )
              WHERE ROWNUM = 1;
               EXCEPTION
            WHEN NO_DATA_FOUND THEN
                 W_MESSAGE   :=   'ROUTING BANK NOT AVAILABLE IN EX_PIPS_MASTER';
                 GOTO END_PROCESS;
          END;
              
              IF ( NVL(W_APROX_EXC_RATE,0) = 0 ) THEN
                 W_MESSAGE   :=   'INVALID EXCHANGE RATE -B ';
                 GOTO END_PROCESS;
              END IF;
         
         
              W_APROX_FC_AMT := NVL(P_LOCAL_CUR_AMT,0) / NVL(W_APROX_EXC_RATE,0);
              
           SELECT BANK_ID,DERIVED_SELL_RATE INTO  W_ROUTING_BANK_ID, W_DERIVED_SELL_RATE
           FROM
           ( SELECT PM.BANK_ID,PM.DERIVED_SELL_RATE  
             FROM   EX_PIPS_MASTER PM
             WHERE  PM.COUNTRY_ID          =  W_ROUTING_COUNTRY_ID
             AND    PM.CURRENCY_ID         =  P_CURRENCY_ID
             AND    PM.COUNTRY_BRANCH_ID = 78
             AND    PM.BANK_ID IN (
                                            SELECT F.ROUTING_BANK_ID
                                                   FROM   V_EX_ROUTING_DETAILS F
                                                   WHERE  BENE_BANK_ID          =  P_BENE_BANK_ID
                                                    AND    BENE_BANK_BRANCH_ID   =  P_BENE_BANK_BRANCH_ID
                                                    AND    F.COUNTRY_ID          =  P_BENE_COUNTRY_ID
                                                    AND    F.CURRENCY_ID         =  P_CURRENCY_ID
                                                    AND    F.SERVICE_MASTER_ID   =  W_SERVICE_MASTER_ID
                                                    AND    F.ROUTING_COUNTRY_ID  =  W_ROUTING_COUNTRY_ID
                                                    AND    F.ROUTING_BANK_ID     =  DECODE(W_SERVICE_MASTER_ID,101,
                                                              P_BENE_BANK_ID,F.ROUTING_BANK_ID)  
                                           )
            
              AND ( W_APROX_FC_AMT BETWEEN  PM.FROM_AMOUNT AND  PM.TO_AMOUNT )
              AND NVL(PM.DERIVED_SELL_RATE,0) <> 0
              ORDER BY PM.DERIVED_SELL_RATE ASC)
            WHERE ROWNUM = 1;
            
            IF  NVL(W_ROUTING_BANK_ID,0)   =  0  THEN
                 W_MESSAGE   :=   'INVALID ROUTING BANK ';
                 GOTO END_PROCESS;
            END IF;
             IF  NVL(W_DERIVED_SELL_RATE,0)   =  0  THEN
                 W_MESSAGE   :=   'INVALID EXCHANGE RATE';
                 GOTO END_PROCESS;
            END IF;
              
             
         END IF;
          ---- ZZZ  ---Parasuraman till here  ON 9th Aug 2018  --- 
      END IF;
     END IF;

         DBMS_OUTPUT.PUT_LINE('HERE3');

     IF  NVL(W_ROUTING_BANK_ID,0) > 0 THEN
          W_REMITTANCE_MODE_ID := '';
          W_DELIVERY_MODE_ID   := '';
          DBMS_OUTPUT.PUT_LINE('BENE COUNTRY '||P_BENE_COUNTRY_ID);
          DBMS_OUTPUT.PUT_LINE('P_CURRENCY_ID '||P_CURRENCY_ID);
          DBMS_OUTPUT.PUT_LINE('W_SERVICE_MASTER_ID '||W_SERVICE_MASTER_ID);
          DBMS_OUTPUT.PUT_LINE('W_ROUTING_COUNTRY_ID '||W_ROUTING_COUNTRY_ID);
          DBMS_OUTPUT.PUT_LINE('W_ROUTING_BANK_ID '||W_ROUTING_BANK_ID);

          BEGIN
              SELECT MIN(F.REMITTANCE_MODE_ID)   INTO   W_REMITTANCE_MODE_ID
              FROM   V_EX_ROUTING_DETAILS F
              WHERE  BENE_BANK_ID           =  P_BENE_BANK_ID
              AND    BENE_BANK_BRANCH_ID    =  P_BENE_BANK_BRANCH_ID
              AND    F.COUNTRY_ID           =  P_BENE_COUNTRY_ID
              AND    F.CURRENCY_ID          =  P_CURRENCY_ID
              AND    F.SERVICE_MASTER_ID    =  W_SERVICE_MASTER_ID
              AND    F.ROUTING_COUNTRY_ID   =  W_ROUTING_COUNTRY_ID
              AND    F.ROUTING_BANK_ID      =  W_ROUTING_BANK_ID;
          EXCEPTION
              WHEN NO_DATA_FOUND THEN
                   W_MESSAGE   :=   'UNABLE TO GET REMITTANCE MODE';
                   GOTO END_PROCESS;
          END;
      END IF;

      IF  NVL(W_REMITTANCE_MODE_ID,0) > 0 THEN
          BEGIN
              SELECT MIN(F.DELIVERY_MODE_ID)   INTO   W_DELIVERY_MODE_ID
              FROM   V_EX_ROUTING_DETAILS F
              WHERE  BENE_BANK_ID           =  P_BENE_BANK_ID
              AND    BENE_BANK_BRANCH_ID    =  P_BENE_BANK_BRANCH_ID
              AND    F.COUNTRY_ID           =  P_BENE_COUNTRY_ID
              AND    F.CURRENCY_ID          =  P_CURRENCY_ID
              AND    F.SERVICE_MASTER_ID    =  W_SERVICE_MASTER_ID
              AND    F.ROUTING_COUNTRY_ID   =  W_ROUTING_COUNTRY_ID
              AND    F.ROUTING_BANK_ID      =  W_ROUTING_BANK_ID
              AND    F.REMITTANCE_MODE_ID   =  W_REMITTANCE_MODE_ID;
          EXCEPTION
               WHEN NO_DATA_FOUND THEN
                    W_MESSAGE := 'UNABLE TO GET REMITTANCE MODE';
                    GOTO END_PROCESS;
          END;
      END IF;

      IF  NVL(W_DELIVERY_MODE_ID,0) > 0 THEN
          BEGIN
                SELECT MIN(F.BANK_BRANCH_ID)    INTO   W_ROUTING_BANK_BRANCH_ID
                FROM   V_EX_ROUTING_DETAILS F
                WHERE  BENE_BANK_ID           =   P_BENE_BANK_ID
                AND    BENE_BANK_BRANCH_ID    =   P_BENE_BANK_BRANCH_ID
                AND    F.COUNTRY_ID           =   P_BENE_COUNTRY_ID
                AND    F.CURRENCY_ID          =   P_CURRENCY_ID
                AND    F.SERVICE_MASTER_ID    =   W_SERVICE_MASTER_ID
                AND    F.ROUTING_COUNTRY_ID   =   W_ROUTING_COUNTRY_ID
                AND    F.ROUTING_BANK_ID      =   W_ROUTING_BANK_ID
                AND    F.REMITTANCE_MODE_ID   =   W_REMITTANCE_MODE_ID
                AND    F.DELIVERY_MODE_ID     =   W_DELIVERY_MODE_ID;
          EXCEPTION
                WHEN NO_DATA_FOUND THEN
                     W_MESSAGE := 'INVALID ROUTING BANK BRANCH';
                     GOTO END_PROCESS;
          END;
      END IF;


<<END_PROCESS>>
      P_ERROR_MESSAGE           :=  W_MESSAGE;
      P_SERVICE_MASTER_ID       :=  W_SERVICE_MASTER_ID;
      P_ROUTING_COUNTRY_ID      :=  W_ROUTING_COUNTRY_ID;
      P_ROUTING_BANK_ID         :=  W_ROUTING_BANK_ID;
      P_ROUTING_BANK_BRANCH_ID  :=  W_ROUTING_BANK_BRANCH_ID ;
      P_ROUTING_COUNTRY_ID      :=  W_ROUTING_COUNTRY_ID;
      P_REMITTANCE_MODE_ID      :=  W_REMITTANCE_MODE_ID;
      P_DELIVERY_MODE_ID        :=  W_DELIVERY_MODE_ID;
      P_SWIFT                   :=  W_SWIFT;
      P_DERIVED_SELL_RATE       := W_DERIVED_SELL_RATE;
END; 