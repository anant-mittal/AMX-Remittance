-- JAX field table
CREATE TABLE JAX_FIELD 
(
  NAME VARCHAR2(50 BYTE) NOT NULL 
, REQUIRED VARCHAR2(1 BYTE) DEFAULT 'N' 
, TYPE VARCHAR2(50 BYTE) DEFAULT 'text' NOT NULL 
, DEFAULT_VALUE VARCHAR2(100 BYTE) 
, MAX_LENGTH NUMBER(*, 0) 
, MIN_LENGTH NUMBER(*, 0) 
, CONSTRAINT JAX_FIELD_PK PRIMARY KEY 
  (
    NAME 
  )
  USING INDEX 
  (
      CREATE UNIQUE INDEX JAX_FIELD_PK ON JAX_FIELD (NAME ASC) 
      LOGGING 
      TABLESPACE USRTAB 
      PCTFREE 10 
      INITRANS 2 
      STORAGE 
      ( 
        INITIAL 65536 
        NEXT 1048576 
        MINEXTENTS 1 
        MAXEXTENTS UNLIMITED 
        BUFFER_POOL DEFAULT 
      ) 
      NOPARALLEL 
  )
  ENABLE 
) 
LOGGING 
TABLESPACE USRTAB 
PCTFREE 10 
INITRANS 1 
STORAGE 
( 
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1 
  MAXEXTENTS UNLIMITED 
  BUFFER_POOL DEFAULT 
) 
NOCOMPRESS 
NOPARALLEL;

-- validation regex table
CREATE TABLE JAX_VALIDATION_REGEX 
(
  KEY VARCHAR2(50 BYTE) NOT NULL 
, VALUE VARCHAR2(50 BYTE) NOT NULL 
, DESCRIPTION VARCHAR2(50 BYTE) 
, CONSTRAINT JAX_VALIDATION_REGEX_PK PRIMARY KEY 
  (
    KEY 
  )
  USING INDEX 
  (
      CREATE UNIQUE INDEX JAX_VALIDATION_REGEX_PK ON JAX_VALIDATION_REGEX (KEY ASC) 
      LOGGING 
      TABLESPACE USRTAB 
      PCTFREE 10 
      INITRANS 2 
      STORAGE 
      ( 
        INITIAL 65536 
        NEXT 1048576 
        MINEXTENTS 1 
        MAXEXTENTS UNLIMITED 
        BUFFER_POOL DEFAULT 
      ) 
      NOPARALLEL 
  )
  ENABLE 
) 
LOGGING 
TABLESPACE USRTAB 
PCTFREE 10 
INITRANS 1 
STORAGE 
( 
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1 
  MAXEXTENTS UNLIMITED 
  BUFFER_POOL DEFAULT 
) 
NOCOMPRESS 
NOPARALLEL;

-- conditional field rule table

CREATE TABLE JAX_CONDITIONAL_FIELD_RULE 
(
  ENTITY_NAME VARCHAR2(50 BYTE) NOT NULL 
, FIELD_NAME VARCHAR2(50 BYTE) NOT NULL 
, CONDITION_KEY VARCHAR2(50 BYTE) 
, CONDITION_VALUE VARCHAR2(100 BYTE) 
, JAX_COND_FIELD_RULE_SEQ_ID NUMBER NOT NULL 
, CONSTRAINT JAX_CONDITIONAL_FIELD_RULE_PK PRIMARY KEY 
  (
    JAX_COND_FIELD_RULE_SEQ_ID 
  )
  USING INDEX 
  (
      CREATE UNIQUE INDEX JAX_CONDITIONAL_FIELD_RULE_PK ON JAX_CONDITIONAL_FIELD_RULE (JAX_COND_FIELD_RULE_SEQ_ID ASC) 
      LOGGING 
      TABLESPACE USRTAB 
      PCTFREE 10 
      INITRANS 2 
      STORAGE 
      ( 
        INITIAL 65536 
        NEXT 1048576 
        MINEXTENTS 1 
        MAXEXTENTS UNLIMITED 
        BUFFER_POOL DEFAULT 
      ) 
      NOPARALLEL 
  )
  ENABLE 
) 
LOGGING 
TABLESPACE USRTAB 
PCTFREE 10 
INITRANS 1 
STORAGE 
( 
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1 
  MAXEXTENTS UNLIMITED 
  BUFFER_POOL DEFAULT 
) 
NOCOMPRESS 
NOPARALLEL;

ALTER TABLE JAX_CONDITIONAL_FIELD_RULE
ADD CONSTRAINT JAX_CONDITIONAL_FIELD_FK1 FOREIGN KEY
(
  FIELD_NAME 
)
REFERENCES JAX_FIELD
(
  NAME 
)
ENABLE;

-- field regex mapping table

CREATE TABLE JAX_FIELD_REGEX_MAPPING 
(
  FIELD_NAME VARCHAR2(50 BYTE) NOT NULL 
, REGEX_KEY VARCHAR2(50 BYTE) NOT NULL 
) 
LOGGING 
TABLESPACE USRTAB 
PCTFREE 10 
INITRANS 1 
STORAGE 
( 
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1 
  MAXEXTENTS UNLIMITED 
  BUFFER_POOL DEFAULT 
) 
NOCOMPRESS 
NOPARALLEL;
