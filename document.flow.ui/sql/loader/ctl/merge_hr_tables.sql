

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
       A.DATE_B=UT.DATE_B,
       A.DATE_E=UT.DATE_E      
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.OFFICER_NUM, UT.ACTION, UT.DATE_B, UT.DATE_E);
                    
                    

MERGE INTO DF.SAP_HR_DEP  A
    USING ( SELECT *
              FROM DF.UT_SAP_HR_DEP 
               ) UT
    ON ( a.DEP = ut.DEP )
    WHEN MATCHED THEN
      UPDATE SET        
       A.DEP_PARENT = UT.DEP_PARENT,       
       A.DATE_B=UT.DATE_B,
       A.DATE_E=UT.DATE_E      
    WHEN NOT MATCHED THEN
      INSERT VALUES ( UT.DEP, UT.DEP_PARENT, UT.DATE_B, UT.DATE_E);
                    
              
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
      
      
/*#### UT_SAP_HR_PERS_STRUCT*/      
      MERGE INTO  DF.SAP_HR_PERS_WERKS  A
        USING (SELECT DISTINCT WERKS_ID, WERKS		 FROM DF.UT_SAP_HR_PERS_STRUCT   where HTEXT_V_ID <> 13) UT
        ON (A.WERKS_ID = UT.WERKS_ID  )
        WHEN MATCHED THEN
          UPDATE SET        
            A.WERKS=UT.WERKS 
         WHEN NOT MATCHED THEN
            INSERT VALUES      (UT.WERKS_ID, UT.WERKS);



MERGE INTO  DF.SAP_HR_PERS_HTEXT  A
        USING (SELECT DISTINCT HTEXT_ID, HTEXT		 	 FROM DF.UT_SAP_HR_PERS_STRUCT where HTEXT_V_ID <> 13 ) UT
        ON (A.HTEXT_ID = UT.HTEXT_ID  )
        WHEN MATCHED THEN
          UPDATE SET        
            A.HTEXT=UT.HTEXT 
         WHEN NOT MATCHED THEN
            INSERT VALUES      (UT.HTEXT_ID,  UT.HTEXT		 );            


MERGE INTO  DF.SAP_HR_PERS_HTEXT_V  A
        USING (SELECT DISTINCT HTEXT_V_ID, HTEXT_V		 	 FROM DF.UT_SAP_HR_PERS_STRUCT where HTEXT_V_ID <> 13 ) UT
        ON (A.HTEXT_V_ID = UT.HTEXT_V_ID  )
        WHEN MATCHED THEN
          UPDATE SET        
            A.HTEXT_V=UT.HTEXT_V
         WHEN NOT MATCHED THEN
            INSERT VALUES      (UT.HTEXT_V_ID,  UT.HTEXT_V);
            

MERGE INTO  DF.SAP_HR_PERS_ORGEH  A
        USING (SELECT DISTINCT ORGEH_ID, ORGEH		 	 FROM DF.UT_SAP_HR_PERS_STRUCT where HTEXT_V_ID <> 13 ) UT
        ON (A.ORGEH_ID = UT.ORGEH_ID  )
        WHEN MATCHED THEN
          UPDATE SET        
            A.ORGEH=UT.ORGEH
         WHEN NOT MATCHED THEN
            INSERT VALUES      (UT.ORGEH_ID,  UT.ORGEH);



SELECT * FROM  DF.SAP_HR_PERS_STRUCT
MERGE INTO  DF.SAP_HR_PERS_STRUCT  A
        USING (SELECT DISTINCT PERNR, ENAME,USRID	,PLANS_ID, ERGRU, ORGEH_ID, HTEXT_V_ID, HTEXT_ID, WERKS_ID
                      FROM DF.UT_SAP_HR_PERS_STRUCT  where HTEXT_V_ID <> 13
                      ) UT
        ON (A.PERNR = UT.PERNR  )
        WHEN MATCHED THEN
          UPDATE SET        
            A.ENAME = UT.ENAME,
            A.USRID	= UT.USRID,
            A.PLANS_ID= UT.PLANS_ID,
            A.ERGRU= UT.ERGRU,
            A.ORGEH_ID= UT.ORGEH_ID,
            A.HTEXT_V_ID= UT.HTEXT_V_ID,
            A.HTEXT_ID= UT.HTEXT_ID,
            A.WERKS_ID= UT.WERKS_ID
         WHEN NOT MATCHED THEN
            INSERT VALUES      (UT.PERNR, UT.ENAME,UT.USRID	,UT.PLANS_ID, UT.ERGRU,UT.ORGEH_ID,UT.HTEXT_V_ID,UT.HTEXT_ID,UT.WERKS_ID);
            
MERGE INTO  DF.DIC_REGION A
        USING (SELECT DISTINCT REGION_ID, REGION		 	 FROM DF.UT_SAP_HR_PERS_STRUCT where HTEXT_V_ID <> 13) UT
        ON (A.REGION_ID = UT.REGION_ID  )
        WHEN MATCHED THEN
          UPDATE SET        
            A.REGION=UT.REGION
         WHEN NOT MATCHED THEN
            INSERT VALUES      (UT.REGION_ID,  UT.REGION);
            
            
            SELECT * FROM DF.UT_SAP_HR_PERS_STRUCT