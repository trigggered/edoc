CREATE TABLE DF.DIC_FIN_CODES
  (
    ID   NUMBER (10) NOT NULL ,
    DEPT VARCHAR2 (3) ,
    DEVI VARCHAR2 (3) ,
    UNIT VARCHAR2 (3)
  ) ;
COMMENT ON TABLE DF.DIC_FIN_CODES
IS
  'Справочникок кодов для формирования КОДА БМ' ;
  COMMENT ON COLUMN DF.DIC_FIN_CODES.DEPT
IS
  'департамент' ;
  COMMENT ON COLUMN DF.DIC_FIN_CODES.DEVI
IS
  'управление' ;
  COMMENT ON COLUMN DF.DIC_FIN_CODES.UNIT
IS
  'подразделение' ;
  ALTER TABLE DF.DIC_FIN_CODES ADD CONSTRAINT DIC_FIN_CODE_GEN_PK PRIMARY KEY ( ID ) ;
CREATE SEQUENCE DF.DIC_FIN_CODES_ID_SEQ START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER DF.DIC_FIN_CODES_ID_TRG BEFORE
  INSERT ON DF.DIC_FIN_CODES FOR EACH ROW WHEN (NEW.ID IS NULL) BEGIN :NEW.ID := DF.DIC_FIN_CODES_ID_SEQ.NEXTVAL;
END;

/




ALTER  TABLE DF.ENT_DEPARTMENTS
  ADD (    
    ID_VISION     NUMBER (10),
    HTEXT_V_ID NUMBER(10)
  ) ;
  
  
  CREATE TABLE DF.ENT_DEP_VISION
    (
      ID_VISION NUMBER (10) NOT NULL ,
      NAME      VARCHAR2 (50)
    ) ;    
  COMMENT ON TABLE DF.ENT_DEP_VISION
IS
  'Разрезы структуры' ;
  ALTER TABLE DF.ENT_DEP_VISION ADD CONSTRAINT ENT_DEP_VISION_PK PRIMARY KEY ( ID_VISION ) ;

  
 ALTER TABLE DF.ENT_DEPARTMENTS ADD CONSTRAINT ENT_DEP_ENT_DEP_VISION_FK FOREIGN KEY ( ID_VISION ) REFERENCES DF.ENT_DEP_VISION ( ID_VISION ) ;

CREATE SEQUENCE DF.ENT_DEP_VISION_ID_VISION_SEQ START WITH 1 NOCACHE ORDER ;

CREATE OR REPLACE TRIGGER DF.ENT_DEP_VISION_ID_VISION_TRG BEFORE
  INSERT ON DF.ENT_DEP_VISION FOR EACH ROW WHEN (NEW.ID_VISION IS NULL) BEGIN :NEW.ID_VISION := DF.ENT_DEP_VISION_ID_VISION_SEQ.NEXTVAL;
END;



/*##################################################################*/
/*##################################################################*/

CREATE TABLE DF.DIC_FIN_OPERT
  (
    ID_FIN_OPERT NUMBER (10) NOT NULL ,
    CODE         VARCHAR2 (3) ,
    NAME         VARCHAR2 (100)
  ) ;
COMMENT ON TABLE DF.DIC_FIN_OPERT
IS
  'Виды операций' ;
  
  
  ALTER TABLE DF.DIC_FIN_OPERT ADD CONSTRAINT DIC_FIN_OPER_TYPE_PK PRIMARY KEY ( ID_FIN_OPERT ) ;
  CREATE TABLE DF.DIC_FIN_POLICIES
    (
      ID_FIN_POLICY NUMBER (10)      
      CONSTRAINT NNC_ID_FIN_OPERT NOT NULL ,
      CODE VARCHAR2 (10) ,
      NAME VARCHAR2 (100)
    ) ;
  COMMENT ON TABLE DF.DIC_FIN_POLICIES
IS
  'УЧЕТНАЯ ПОЛИТИКА' ;
  ALTER TABLE DF.DIC_FIN_POLICIES ADD CONSTRAINT DIC_FIN_OPER_TYPEv1_PK PRIMARY KEY ( ID_FIN_POLICY ) ;
  
  CREATE TABLE DF.DOC_CARD_FIN
    (
      ID_DOC_CARD_FIN NUMBER NOT NULL ,
      ID_DOC          NUMBER NOT NULL ,
      ID_FIN_OPERT    NUMBER (10) NOT NULL,
      ID_FIN_POLICY   NUMBER (10) NOT NULL,
      ID_DEP_CUSTOMER NUMBER (10) NOT NULL
    ) ;
  ALTER TABLE DF.DOC_CARD_FIN ADD CONSTRAINT DOC_CARD_FIN_PK PRIMARY KEY ( ID_DOC_CARD_FIN ) ;
  ALTER TABLE DF.DOC_CARD_FIN ADD CONSTRAINT DCARD_FIN_DIC_FIN_OPERT_FK FOREIGN KEY ( ID_FIN_OPERT ) REFERENCES DF.DIC_FIN_OPERT ( ID_FIN_OPERT ) ;
  ALTER TABLE DF.DOC_CARD_FIN ADD CONSTRAINT DCARD_FIN_DIC_FIN_POLI_FK FOREIGN KEY ( ID_FIN_POLICY ) REFERENCES DF.DIC_FIN_POLICIES ( ID_FIN_POLICY ) ;
  ALTER TABLE DF.DOC_CARD_FIN ADD CONSTRAINT DOC_CARD_FIN_DOC_CARD_FK FOREIGN KEY ( ID_DOC ) REFERENCES DF.DOC_CARD ( ID_DOC ) ;
CREATE SEQUENCE DF.DIC_FIN_OPERT_ID_FIN_OPERT_SEQ START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER DF.DIC_FIN_OPERT_ID_FIN_OPERT_TRG BEFORE
  INSERT ON DF.DIC_FIN_OPERT FOR EACH ROW WHEN (NEW.ID_FIN_OPERT IS NULL) BEGIN :NEW.ID_FIN_OPERT := DF.DIC_FIN_OPERT_ID_FIN_OPERT_SEQ.NEXTVAL;
END;
/
CREATE SEQUENCE DF.DIC_FIN_POLICIES_ID_FIN_POLICY START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER DF.DIC_FIN_POLICIES_ID_FIN_POLICY BEFORE
  INSERT ON DF.DIC_FIN_POLICIES FOR EACH ROW WHEN (NEW.ID_FIN_POLICY IS NULL) BEGIN :NEW.ID_FIN_POLICY := DF.DIC_FIN_POLICIES_ID_FIN_POLICY.NEXTVAL;
END;
/
CREATE SEQUENCE DF.DOC_CARD_FIN_ID_DOC_CARD_FIN START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER DF.DOC_CARD_FIN_ID_DOC_CARD_FIN BEFORE
  INSERT ON DF.DOC_CARD_FIN FOR EACH ROW WHEN (NEW.ID_DOC_CARD_FIN IS NULL) BEGIN :NEW.ID_DOC_CARD_FIN := DF.DOC_CARD_FIN_ID_DOC_CARD_FIN.NEXTVAL;
END;
/
