OPTIONS (SKIP=1)
LOAD DATA
CHARACTERSET CL8MSWIN1251 
 TRUNCATE INTO TABLE DF.UT_SAP_HR_GJOB
 FIELDS TERMINATED BY "|" OPTIONALLY ENCLOSED BY '"'
 TRAILING NULLCOLS
 (      GJOB, 
         DATE_B  "TO_DATE(:DATE_B, 'DD.MM.YYYY')",
         DATE_E  "TO_DATE(:DATE_E, 'DD.MM.YYYY')" 
)