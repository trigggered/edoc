LOAD DATA
CHARACTERSET CL8MSWIN1251 
 TRUNCATE INTO TABLE DF.UT_SAP_HR_PERS_ABSENT
 FIELDS TERMINATED BY "|" OPTIONALLY ENCLOSED BY '"'
 TRAILING NULLCOLS
 ( 	PERSNO, 
	AWART, 
	ABWTXT, 
	BEGDA, 
	ENDDA 
 )