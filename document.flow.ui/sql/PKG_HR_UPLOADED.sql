CREATE OR REPLACE PACKAGE       PKG_HR_UPLOADED AS 

  /* TODO enter package declarations (types, exceptions, methods etc) here */ 
 PROCEDURE RUN;
 
 PROCEDURE CLEAR_SAP_HR_TBL;
 
 PROCEDURE CLEAR_UT_SAP_HR_TBL;
 
END PKG_HR_UPLOADED;
/


CREATE OR REPLACE PACKAGE BODY             PKG_HR_UPLOADED AS 

  /* TODO enter package declarations (types, exceptions, methods etc) here */ 
 PROCEDURE RUN AS
 BEGIN
  
MERGE INTO DF.SAP_HR_ACTION_NAME A
    USING ( SELECT *
              FROM DF.UT_SAP_HR_ACTION_NAME             
               ) UT
    ON ( a.ACTION = ut.ACTION )
    WHEN MATCHED THEN
      UPDATE SET       
      A.NAME =NAME
            
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.ACTION, UT.NAME);


MERGE INTO DF.SAP_HR_ACTION A
    USING ( SELECT *
              FROM DF.UT_SAP_HR_ACTION             
               ) UT
    ON ( a.OFFICER_NUM = ut.OFFICER_NUM )
    WHEN MATCHED THEN
      UPDATE SET 
       A.ACTION = UT.ACTION,
       A.COL= UT.COL,
       A.DATE_B=UT.DATE_B,
       A.DATE_E=UT.DATE_E      
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.OFFICER_NUM, UT.ACTION, UT.COL,UT.DATE_B, UT.DATE_E);
                    

MERGE INTO DF.SAP_HR_DEP  A
    USING ( SELECT *
              FROM DF.UT_SAP_HR_DEP 
               ) UT
    ON ( a.DEP = ut.DEP )
    WHEN MATCHED THEN
      UPDATE SET        
       A.DEP_PARENT = UT.DEP_PARENT,       
       A.COL1 = UT.COL1, 
       A.COL2 = UT.COL2,        
       A.DATE_B=UT.DATE_B,
       A.DATE_E=UT.DATE_E,
       A.COL3 = UT.COL3, 
       A.COL4 = UT.COL4, 
       A.COL5 = UT.COL5, 
       A.COL6 = UT.COL6
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.DEP, UT.DEP_PARENT, UT.COL1, UT.COL2, UT.DATE_B, UT.DATE_E, UT.COL3, UT.COL4, UT.COL5, UT.COL6);
                    
              
    MERGE INTO DF.SAP_HR_DEP_NAME   A
    USING ( SELECT *
                      FROM DF.UT_SAP_HR_DEP_NAME  
               ) UT
    ON ( a.DEP = ut.DEP AND A.LANG = UT.LANG )
    WHEN MATCHED THEN
      UPDATE SET        
       A.DEP_NAME = UT.DEP_NAME
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.DEP,  UT.LANG, UT.DEP_NAME);
      
      
    MERGE INTO DF.SAP_HR_GJOB   A
    USING ( SELECT *
              FROM DF.UT_SAP_HR_GJOB 
               ) UT
    ON ( a.GJOB = ut.GJOB )
    WHEN MATCHED THEN
      UPDATE SET               
       A.DATE_B=UT.DATE_B,
       A.DATE_E=UT.DATE_E      
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.GJOB,  UT.DATE_B, UT.DATE_E);
      

MERGE INTO DF.SAP_HR_GJOB_NAME   A
    USING ( SELECT *
                      FROM DF.UT_SAP_HR_GJOB_NAME  
               ) UT
    ON ( a.GJOB = ut.GJOB AND A.LANG = UT.LANG )
    WHEN MATCHED THEN
      UPDATE SET        
       A.NAME = UT.NAME
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.GJOB,  UT.LANG, UT.NAME);
      
      
  
      MERGE INTO DF.SAP_HR_JOB   A
    USING ( SELECT *
              FROM DF.UT_SAP_HR_JOB 
               ) UT
    ON ( a.JOB = ut.JOB )
    WHEN MATCHED THEN
      UPDATE SET         
      A.GJOB = UT.GJOB,
       A.DATE_B=UT.DATE_B,
       A.DATE_E=UT.DATE_E      
    WHEN NOT MATCHED THEN
      INSERT VALUES (UT.JOB,  UT.GJOB,  UT.DATE_B, UT.DATE_E);
      
      
      
    MERGE INTO DF.SAP_HR_JOB_NAME   A
    USING ( SELECT *
                      FROM DF.UT_SAP_HR_JOB_NAME  
               ) UT
    ON ( a.JOB = ut.JOB AND A.LANG = UT.LANG )
    WHEN MATCHED THEN
      UPDATE SET        
       A.NAME = UT.NAME
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.JOB,  UT.LANG, UT.NAME);
      
      
         MERGE INTO DF.SAP_HR_OFFICER   A
    USING ( SELECT *
                      FROM DF.UT_SAP_HR_OFFICER  
               ) UT
    ON ( a.OFFICER_NUM = ut.OFFICER_NUM )
    WHEN MATCHED THEN
      UPDATE SET        
          A.STATUS =UT.STATUS,
          A.DEP_PREV=UT.DEP_PREV,
          A.DEP=UT.DEP,
          A.JOB_PREV=UT.JOB_PREV,
          A.JOB=UT.JOB,
          A.TIN=UT.TIN,
          A.EMPLOYMENT_STATUS=UT.EMPLOYMENT_STATUS,
          A.RANG=UT.RANG,
          A.REGION=UT.REGION,
          A.REGION_NAME=UT.REGION_NAME,
          A.POST_CODE=UT.POST_CODE,
          A.CITY_NAME=UT.CITY_NAME,
          A.STR_NAME=UT.STR_NAME,
          A.HOUSE_NO=UT.HOUSE_NO,
          A.DATE_B=UT.DATE_B,
          A.DATE_E=UT.DATE_E,
          A.E_MAILE=UT.E_MAILE
  
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.OFFICER_NUM,
                  UT.STATUS,
                  UT.DEP_PREV,
                  UT.DEP,
                  UT.JOB_PREV,
                  UT.JOB,
                  UT.TIN,
                  UT.EMPLOYMENT_STATUS,
                  UT.RANG,
                  UT.REGION,
                  UT.REGION_NAME,
                  UT.POST_CODE,
                  UT.CITY_NAME,
                  UT.STR_NAME,
                  UT.HOUSE_NO,
                  UT.DATE_B,
                  UT.DATE_E,
                  UT.E_MAILE);                                    
                  
                  
    MERGE INTO DF.SAP_HR_OFFICER_NAME   A
    USING ( SELECT *
                      FROM DF.UT_SAP_HR_OFFICER_NAME  
               ) UT
    ON ( a.OFFICER_NUM = ut.OFFICER_NUM AND A.LANG = UT.LANG )
    WHEN MATCHED THEN
      UPDATE SET        
       A.SECOND_NAME = UT.SECOND_NAME,
        A.FIRST_NAME = UT.FIRST_NAME,
        A.MIDDLE_NAME=UT.MIDDLE_NAME
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.OFFICER_NUM, UT. LANG, UT. SECOND_NAME, UT. FIRST_NAME, UT. MIDDLE_NAME);
      
      
      MERGE INTO DF.SAP_HR_DEP_CHIEF A 
      USING (	 SELECT * FROM  UT_SAP_HR_DEP_CHIEF) UT
      ON (A.DEP = UT.DEP)
      WHEN MATCHED THEN
      UPDATE SET              
        A.DEP_PARENT = UT.DEP_PARENT,
        A.COL1 =UT.COL1,
        A.DATE_B =UT.DATE_B,
        A.DATE_E = UT.DATE_E
 WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.DEP,  UT.DEP_PARENT, UT.COL1,UT.DATE_B,  UT.DATE_E);
      
      
       MERGE INTO DF.SAP_HR_FUNC_DPND A
       USING (	 SELECT * FROM  DF.UT_SAP_HR_FUNC_DPND) UT
       ON (A.DEP = UT.DEP)
        WHEN MATCHED THEN
      UPDATE SET 
        A.DEP_PARENT = UT.DEP_PARENT,        
        A.DATE_B =UT.DATE_B,
        A.DATE_E = UT.DATE_E
       WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.DEP,  UT.DEP_PARENT, UT.DATE_B,  UT.DATE_E);
      
      
       MERGE INTO DF.SAP_HR_PHONE_PERS       A
        USING (	 SELECT * FROM  DF.UT_SAP_HR_PHONE_PERS ) UT
        ON (A.OFFICER_NUM = UT.OFFICER_NUM)
        WHEN MATCHED THEN
      UPDATE SET              
        A.COL1 = UT.COL1,
        A.PHONE = UT.PHONE
      WHEN NOT MATCHED THEN
      INSERT VALUES (UT.OFFICER_NUM,UT.COL1, UT.PHONE )  ;
      
      
         MERGE INTO DF.SAP_HR_PTIME_PERS A         
            USING (	 SELECT * FROM  DF.UT_SAP_HR_PTIME_PERS  ) UT
             ON (A.OFFICER_NUM1 = UT.OFFICER_NUM1)
             
              WHEN MATCHED THEN
      UPDATE SET               
          A.OFFICER_NUM2   = UT.OFFICER_NUM2,
          A.DATE_B = UT.DATE_B,
          A.DATE_E =UT.DATE_E      
       WHEN NOT MATCHED THEN
      INSERT VALUES       (UT.OFFICER_NUM1,  UT.OFFICER_NUM2,   UT.DATE_B,  UT.DATE_E  );
      
      
 END;
 
 PROCEDURE  CLEAR_UT_SAP_HR_TBL AS
 BEGIN
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_ACTION';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_ACTION_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_DEP_NAME' ;
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_DEP_CHIEF';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_DEP';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_OFFICER_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_OFFICER';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_JOB_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_JOB';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_GJOB_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_GJOB';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_PTIME_PERS';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_PHONE_PERS';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.UT_SAP_HR_FUNC_DPND';
 END;
 
 
 PROCEDURE CLEAR_SAP_HR_TBL AS
 BEGIN 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_ACTION';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_ACTION_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_DEP_NAME' ;
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_DEP_CHIEF';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_DEP';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_OFFICER_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_OFFICER';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_JOB_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_JOB';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_GJOB_NAME';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_GJOB';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_PTIME_PERS';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_PHONE_PERS';
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DF.SAP_HR_FUNC_DPND';

 END;
 
 
END PKG_HR_UPLOADED;
/