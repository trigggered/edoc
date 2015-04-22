SELECT ID_DEP,
  DEP_CODE,
  DEP_ABBR,
  DEP_NAME,
  OFFICER_NUM,
  ID_EMP,
  FIRST_NAME,
  SECOND_NAME,
  MIDDLE_NAME,
  FULL_NAME,
  RANG,
  JOB_NAME,
  E_MAILE
FROM DF.V_ENT_EMPLOYEE 
where second_name like '%олстой%'



create or replace view df.V_sap_hr_job AS
  select  a.*, 
  b.name JOB_NAME, C.NAME GJOB_NAME
  from   DF.sap_hr_job a,
              DF.sap_hr_job_name b,
              DF.SAP_HR_GJOB_NAME C
  where a.job = b.job
  AND A.GJOB = C.GJOB
  and b.lang='UK'
  and C.lang='UK';
  
create or replace view df.v_sap_hr_officer as
SELECT  
        A.OFFICER_NUM,
        CASE  a.STATUS
        
            WHEN 0 THEN 'уволился'
            WHEN 1 THEN 'приостановлено'
            WHEN 2 THEN 'пенсионер (не используется)'
            WHEN 3 THEN 'активный'
          END  STATUS,
          a.STATUS status_id,
  a.DEP_PREV,
  a.DEP,
  a.JOB_PREV,
  a.JOB,
  a.TIN,
  a.EMPLOYMENT_STATUS,
  a.RANG,
  a.REGION,
  a.REGION_NAME,
  
  a.POST_CODE,
  a.CITY_NAME,
  a.STR_NAME,
  a.HOUSE_NO,
  a.DATE_B,
  a.DATE_E,
  a.E_MAILE,
  
  B.SECOND_NAME,
  B.FIRST_NAME,
  B.MIDDLE_NAME,  
   B.SECOND_NAME||' '||B.FIRST_NAME||' '|| B.MIDDLE_NAME FULL_NAME,
  c.HTEXT_V_ID,
  c.HTEXT_V,
  c.HTEXT_ID,
  c.HTEXT,
  c.WERKS_ID,
  c.WERKS

 
  FROM DF.sap_hr_officer A,
             DF.sap_hr_officer_name B,
             df.SAP_HR_PERS_STRUCT c
  WHERE a.officer_num=b.officer_num 
  and a.officer_num= c.pernr  
  AND B.LANG = 'UA'
  ;


CREATE OR REPLACE VIEW DF.V_SAP_HR_DEP AS  
SELECT A.* , b.dep_name
FROM   (
                  SELECT  LEVEL L, DEP, DEP_PARENT,DATE_B,DATE_E,
                      connect_by_root DEP ROOT_DEP
                  FROM   DF.SAP_HR_DEP   
                  CONNECT BY PRIOR DEP = DEP_PARENT 
                  START WITH DEP_PARENT = 60000909
                  ) A,
              DF.SAP_HR_DEP_NAME B
              
WHERE A.DEP=B.DEP 
  AND B.LANG ='UK';
  
  
  /*
  CREATE OR REPLACE VIEW  DF.V_ENT_DEP
  SELECT A. DEP SAP_DEP,  B.ID_DEP, B.ID_DEP_PARENT,  B.CODE, B.NAME, B.ABBREVIATION,  DEP_NAME SAP_DEP_NAME, R.ID_MAP_DEP,
  A.L TREE_LEVEL
FROM   DF.V_SAP_HR_DEP A,                            
              DF.DOC_MAP_ENT_DEP R,
              DF.ENT_DEPARTMENTS B
WHERE A.ROOT_DEP =R.SAP_DEP 
  AND  R.ID_DEP = B.ID_DEP;

*/
  
  create or replace view DF.V_SAP_HR_GJOB AS
SELECT J.GJOB, JN.NAME
FROM DF.SAP_HR_GJOB_NAME JN,
  DF.SAP_HR_GJOB J
WHERE LANG = 'UK' 
AND J.GJOB = JN.GJOB
AND DATE_E = TO_DATE('31.12.9999', 'DD.MM.YYYY')

;

CREATE OR REPLACE VIEW DF.V_ENT_EMPLOYEE AS
  SELECT a.HTEXT_V_ID id_dep,  
              a.HTEXT_V  DEP_CODE, 
              a.HTEXT_V  DEP_ABBR, 
               a.HTEXT_V DEP_NAME,
       A.OFFICER_NUM,
       A.OFFICER_NUM ID_EMP,
       A.FIRST_NAME, 
       A.SECOND_NAME, 
       A.MIDDLE_NAME,
       A.SECOND_NAME||' '||A.FIRST_NAME||' '||A.middle_name FULL_NAME ,
       a.rang,
       --C.SAP_DEP_NAME,
       --C.ROOT_DEP,
       B.GJOB,
       B.GJOB_NAME,
       B.JOB,       
       B.JOB_NAME,
       A.E_MAILE      

FROM  
      DF.v_sap_hr_officer A,
      DF.V_sap_hr_job B
      
      
WHERE  A.JOB= B.JOB  
  and a.date_e is null;

  

CREATE OR REPLACE VIEW DF.V_CORRESPONDENCE_TYPE_TREE AS
SELECT LEVEL L,  ID_CORR ID, ID_CORR_PARENT ID_PARENT,
  CONNECT_BY_ISLEAF ISLEAF, 
    id_corr, id_corr_parent,code, name,
    --CODE||'  '||NAME TITLE,
    NAME TITLE,
    sys_connect_by_path(NAME,'/') NODE_PATH,
    connect_by_root ID_CORR root_id,
    connect_by_root CODE root_CODE
 from  df.DOC_DIC_CORR_TYPE

  CONNECT BY PRIOR ID_CORR = id_corr_parent
  START WITH id_corr_parent IS NULL
ORDER SIBLINGS BY CODE;


CREATE OR REPLACE VIEW DF.V_CORRESPONDENCE_TYPE_TREE_2 AS
SELECT LEVEL L,  ID_CORR ID, ID_CORR_PARENT_2 ID_PARENT,
  CONNECT_BY_ISLEAF ISLEAF, 
    id_corr, id_corr_parent,code, name,
    --CODE||'  '||NAME TITLE,
    NAME TITLE,
    sys_connect_by_path(CODE,'/') NODE_PATH,
    connect_by_root ID_CORR root_id,
    connect_by_root CODE root_CODE
 from  df.DOC_DIC_CORR_TYPE

  CONNECT BY PRIOR ID_CORR = id_corr_parent_2
  START WITH id_corr_parent_2 IS NULL
ORDER SIBLINGS BY CODE;


CREATE OR REPLACE VIEW DF.V_PRODUCT_TREE AS
SELECT LEVEL L, CONNECT_BY_ISLEAF ISLEAF, 
          id_prod id, 
          id_prod_parent id_parent,
          id_prod, 
          id_prod_parent,
          name,
          name TITLE,
          sys_connect_by_path(NAME,'/') prod_PATH,
          connect_by_root ID_prod root_id
FROM DF.DOC_DIC_PROD
  CONNECT BY PRIOR ID_prod = id_prod_parent
  START WITH id_prod_parent IS NULL
ORDER SIBLINGS BY name;


create or replace view df.v_status_by_corr as
SELECT A.*,
CONNECT_BY_ISLEAF ISLEAF, 
connect_by_root CODE root_CODE,
connect_by_root ID root_id

FROM 
(
    SELECT NULL ID_CTS, ID_CORR ID, ID_CORR_PARENT ID_PARENT, null id_status,  CODE, CODE||' '||NAME TITLE ,  0 IS_DEFAULT
    FROM  DF.V_CORRESPONDENCE_TYPE_TREE 
    WHERE ISLEAF = 0  AND ID_PARENT IS NULL
    UNION ALL
    SELECT ID_CTS, ROWNUM ID,  RS.ID_CORR ,  S.id_status, NULL CODE,  S.NAME, S.IS_DEFAULT
    FROM  DF.DOC_DIC_STATUS  S,
                DF.DOC_CT_REL_STAT RS
                WHERE S.ID_STATUS = RS.ID_STATUS
) A
CONNECT BY PRIOR ID = id_parent
START WITH id_parent IS NULL 
ORDER SIBLINGS BY ID_STATUS;



CREATE OR REPLACE VIEW DF.V_DOC_CARD AS
SELECT DC.ID_DOC, 
          DC.ID_INFO_TYPE,
          DC.ID_CORR,
          c.code corr_type,  /*Вид корреспонденции, тип документа*/
          case 
              when c.code   like '1.3.2%' then 'Порядок'
                else  c.name 
          end CORR_TYPE_FULL,  /*Вид корреспонденции, тип документа*/
          DC.CODE DOC_CODE,            /*Код*/
          DC.NAME,            /*Наименование*/
          DC.DATE_IN,         /*Дата регистрации*/
          OUT_NUM,            /*Номер исходящий */
          
          DC.OUT_DATE,      /*Дата исходящая */
          NUM_OF_LEGAL,       /*Номер юридического дела*/
          ID_SENDER,             /*Отправитель*/
          OSE.NAME OUT_SENDER,             /*Отправитель НАИМЕНОВАНИЕ*/
          ID_RECIPIENT,          /*Получатель*/
          ORE.NAME  OUT_RECIPIENT,          /*Получатель НАИМЕНОВАНИЕ*/
          DC.ID_EMP_EXEC,
          EE.FULL_NAME EXEC_FULL_NAME,       /*Исполнитель  ФИО*/
          EE.ID_DEP ID_DEPT_EXEC,
          EE.DEP_NAME EXEC_DEP_NAME,        /*Исполнитель  подразделение*/                    
          DC.ID_STATUS,
          S.NAME STATUS,      /*Статус*/
          DC.DATE_EXEC_PLAN,       /*Срок исполнения план*/           
          DC.DATE_EXEC_FACT,       /*Срок исполнения факт*/          
          DC.DATE_REMINDER,   /*Напоминание*/
          
          DC.ID_EMP_REG,          
          ER.FULL_NAME REG_FULL_NAME,       /*Регистратор   ФИО*/
          ER.ID_DEP ID_DEPT_REG, 
          ER.DEP_NAME  REG_DEP_NAME,        /*Регистратор подразделение*/                    
          
          DC.ID_EMP_AUTHOR,               /*ID Автор докуменита, только для Внутренней корреспонденции*/
          EA.FULL_NAME AS EMP_AUTHOR,        /*ФИО Автор документа , только для Внутренней корреспонденции*/
          EA.E_MAILE  AS AUTHOR_E_MAILE,   /*E_MAILE Автор документа */          
          DC.ID_DEP_OWNER ,             /* ID ВЛАДЕЛЕЦ подразделение, только для Внутренней корреспонденции*/
          DEP.ABBREVIATION||'-'||DEP.NAME AS OWNER_DEP_NAME,      /*Владелец  подразделение, наименование*/                    
          
          DC.NOTE,            /*Примечания/ Аннотация*/ 
          DC.ID_PROD,
          --P.NAME PRODUCT,     /*Продукт*/
          NULL PRODUCT,
          DC.DATE_PUB, /*Дата публикации*/                            
          DC.DATE_EFFECTIVE,  /*Дата вступления в силу*/
          DC.DATE_EXPIRE,      /*Дата окончания действия*/
          DC.IS_CLOSED,   /*Закрытый доступ*/
          C.root_id CORR_ROOT_ID,
          C.ROOT_CODE CORR_ROOT_CODE,
          DC.SIGN,
          DC.SIGN_OFFICER_NUM,
          ES.E_MAILE  SIGN_E_MAILE,
          DC.NUM_IN_DEP
          

  FROM     DF.DOC_CARD DC,
           DF.V_CORRESPONDENCE_TYPE_TREE_2 C,
           DF.DOC_DIC_STATUS S,           
           DF.ENT_DEPARTMENTS DEP,
           DF.V_ENT_EMPLOYEE EE,
           DF.V_ENT_EMPLOYEE ER,
           DF.V_ENT_EMPLOYEE EA,
           DF.V_ENT_EMPLOYEE ES,
           DF.DIC_OUT_COUNTERPATIES OSE,
           DF.DIC_OUT_COUNTERPATIES ORE
        
  WHERE 
          DC.ID_CORR = C.ID_CORR
      AND DC.ID_STATUS = S.ID_STATUS
      
      AND DC.ID_EMP_EXEC = EE.ID_EMP(+)      
      AND DC.ID_EMP_REG =ER.ID_EMP(+)
      AND DC.SIGN_OFFICER_NUM =ES.ID_EMP(+)
      AND DC.ID_EMP_AUTHOR = EA.ID_EMP(+) 
      AND DC.ID_SENDER = OSE.ID_COUNTERPATIES(+)
      AND DC.ID_RECIPIENT = ORE.ID_COUNTERPATIES(+)
      AND DC.ID_DEP_OWNER = DEP.ID_DEP(+)
;
      
CREATE OR REPLACE VIEW DF.V_DOC_CARD_ADD AS
SELECT 
  DC.ID_DOC,
  DC.ID_DOC_ADD, 
  ID_ORDER_TYPE,
  DC.ID_RISK,   
  R.NAME RISK_NAME,
  
  DC.ID_SCOUPE, 
  S.NAME SCOUPE_NAME,  
  
  DC.DATE_SH_UPD, 
  DC.DATE_LAST_UPD, 
  DC.ORD_VERSION,
  DC.ORD_LEVEL
  
 
FROM DF.DOC_CARD_ADD DC,     
  
      DF.DOC_DIC_RISK R,
      DF.DOC_DIC_SCOUPE S

      
WHERE     
          DC.ID_RISK = R.ID_RISK(+)
 AND DC.ID_SCOUPE = S.ID_SCOUPE(+);
 
 
 
 
create or replace view df.v_tree_corrtype_status as
  select * 
  from 
  (
  
  select id, id_parent, code||'-'||name  TITLE, id ID_CORR, CODE CORR_CODE,  NULL ID_STATUS
  from df.v_correspondence_type_tree
  
  union all
  
  SELECT   ROWNUM id,  ct.id_corr id_parent, s.name,  ct.id_corr,  CT.CODE CORR_CODE,  s.id_status 
  FROM    df.v_correspondence_type_tree ct,
              df.doc_dic_status s,
              DF.DOC_CT_REL_STAT RS
  WHERE RS.id_corr = ct.root_id   
        AND ct.isleaf<>0  
        AND S.ID_STATUS = RS.ID_STATUS
  )
  
  connect by prior id=id_parent 
  start with id_parent is null
  ORDER SIBLINGS BY ID;
  
  
  
  
  create or replace VIEW df.v_tree_status_corrtype AS
  select * 
  from 
  (
  
  select   s.id_status id,  NULL id_parent, s.name TITLE, s.id_status, NULL ID_CORR, NULL CORR_CODE
  FROM df.doc_dic_status s

  UNION ALL      
  
  select    ROWNUM+100000   id,  s.id_status id_parent, ct.code||'-'||CT.name, s.id_status , ct.id_corr, ct.CODE CORR_CODE
  from    df.v_correspondence_type_tree ct,
              df.doc_dic_status s,
              DF.DOC_CT_REL_STAT RS
  where RS.id_corr = ct.root_id   
  and ct.isleaf<>0  
  AND S.ID_STATUS = RS.ID_STATUS  
  )
  
  connect by prior id=id_parent 
  start with id_parent is null
  
  ORDER SIBLINGS BY ID; 
  
 

CREATE OR REPLACE VIEW DF.V_DOC_IN_FLOW AS
SELECT  
     F.ID_FLOW
    ,F.ID_FLOW_STAGE
    ,F.ID_DOC
    ,E.FULL_NAME  EMP_AUTHOR 
    ,DC.code DOC_CODE
    ,DC.NAME
    ,T.NAME  CORR_TYPE_FULL
    ,T.CODE CORR_TYPE
    ,DC.ID_STATUS
    --,DC.STATUS
    ,FS.NAME_STAGE
    ,F.INITIATOR 
    --,E.FULL_NAME INITIATOR_NAME
    ,F.DATE_IN    
    ,DF.PKG_DF_FLOW.GetDeadLineDate (F.DATE_IN , FS.POSSIBLE_DAYS_IN)  DEADLINE
    ,DECODE (F.COMPLETED, 0
          ,trunc(DF.PKG_DF_FLOW.GetDeadLineDate (F.DATE_IN , FS.POSSIBLE_DAYS_IN)  - SYSDATE)
          ,null)     DAYS_LEFT          
    ,F.DATE_OUT
    ,DECODE(F.COMPLETED, 0, ' В процессе', 1, 'Завершен', 2, 'Завершен не удачно', 3, 'Отменен БА') COMPLETED,
     F.COMPLETED  ID_COMPLETED,
    F.INFO_MSG
  
FROM  DF.DOC_FLOW F,
            DF.DOC_FLOW_STAGE FS,
            DF.V_ENT_EMPLOYEE E,
            DF.DOC_CARD DC,
            DF.DOC_DIC_CORR_TYPE T
            
WHERE FS.ID_FLOW_STAGE = F.ID_FLOW_STAGE
      AND DC.ID_CORR = T.ID_CORR

      AND E.OFFICER_NUM = DC.ID_EMP_AUTHOR
      AND F.ID_DOC = DC.ID_DOC
      
      AND NOT EXISTS (
              SELECT A.ID_DOC 
                FROM   DF.DOC_CARD A 
                WHERE A.ID_STATUS IN (9, 10,11)
                        AND  F.ID_DOC = A.ID_DOC                         
                        )
   ;
   
   
CREATE OR REPLACE VIEW DF.V_DOC_EMP_BA AS
   SELECT ID_USER_ROLE, ID_USER_ROLE ID_EMP_BA, ID_USER OFFICER_NUM,  NVL2( DATE_E,0,1 ) IS_ACTIVE  
   FROM DF.SEC_USER_ROLE 
   WHERE ID_ROLE = 1
   ;


CREATE OR REPLACE VIEW DF.V_TREE_DOC_STORAGE AS
SELECT LEVEL L, 
connect_by_root ID_STOR ROOT_STOR_ID,
CONNECT_BY_ISLEAF ISLEAF,
DS.* 
FROM  DF.DOC_STORAGE   DS
--WHERE id_doc=:id_doc
start with id_stor_prev is null
connect   by PRIOR id_stor = id_stor_prev
;


create or replace view df.V_DOC_RECIPIENTS as
select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID,  full_name NAME, R.RECIPIENTS_TYPE, R.ID_DOC
from df.DOC_GR_RECIPIENTS r,
  DF.V_ENT_EMPLOYEE e
where r.VALUE_ID = e.id_emp 
AND R.ID_DIC_GR_RECIT = 0

UNION ALL

select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID,  HTEXT_V NAME, R.RECIPIENTS_TYPE, R.ID_DOC
from DF.DOC_GR_RECIPIENTS R,
         DF.SAP_HR_PERS_HTEXT_V V
where R.VALUE_ID = V.HTEXT_V_ID
AND R.ID_DIC_GR_RECIT = 1

UNION ALL

select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID,  WERKS NAME, R.RECIPIENTS_TYPE , R.ID_DOC
from DF.DOC_GR_RECIPIENTS R,
         DF.SAP_HR_PERS_WERKS V
where R.VALUE_ID = V.WERKS_ID
AND R.ID_DIC_GR_RECIT = 2

UNION ALL

select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID,  REGION NAME, R.RECIPIENTS_TYPE , R.ID_DOC
from DF.DOC_GR_RECIPIENTS R,
         DF.DIC_REGION V
where R.VALUE_ID = V.REGION_ID
AND R.ID_DIC_GR_RECIT = 3

UNION ALL

select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID,  ORGEH NAME, R.RECIPIENTS_TYPE , R.ID_DOC
from DF.DOC_GR_RECIPIENTS R,
         DF.SAP_HR_PERS_ORGEH V
where R.VALUE_ID = V.ORGEH_ID
AND R.ID_DIC_GR_RECIT = 4

UNION ALL

select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID,  'Ранг '|| VALUE_ID NAME, R.RECIPIENTS_TYPE , R.ID_DOC
from DF.DOC_GR_RECIPIENTS R         
where R.ID_DIC_GR_RECIT = 5

UNION ALL

select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID,  'Все сотрудники банка' NAME, R.RECIPIENTS_TYPE , R.ID_DOC
from DF.DOC_GR_RECIPIENTS R         
where R.ID_DIC_GR_RECIT = 6

UNION ALL

select R.ID_DIC_GR_RECIT ID_GR, r.ID_GR_REC, VALUE_ID, J.NAME, R.RECIPIENTS_TYPE , R.ID_DOC
from DF.DOC_GR_RECIPIENTS R,
  DF.V_SAP_HR_GJOB  J
where R.ID_DIC_GR_RECIT = 7
AND R.VALUE_ID = J.GJOB
;


CREATE OR REPLACE VIEW  DF.V_DOC_EMP_RECIPIENTS AS
/*По персоналиям*/
SELECT GR.ID_DIC_GR_RECIT, GR.ID_DOC, GR.RECIPIENTS_TYPE, PERNR OFFICER_NUM
FROM DF.SAP_HR_PERS_STRUCT , 
           DF.DOC_GR_RECIPIENTS  GR
WHERE ID_DIC_GR_RECIT = 0 AND PERNR = VALUE_ID           


UNION 
/*Направления*/
SELECT ID_DIC_GR_RECIT, ID_DOC, RECIPIENTS_TYPE, PERNR OFFICER_NUM--ENAME,    USRID  E_MAIL
FROM DF.SAP_HR_PERS_STRUCT , 
           DF.DOC_GR_RECIPIENTS  GR
WHERE ID_DIC_GR_RECIT = 1 AND HTEXT_V_ID = VALUE_ID           

UNION 

/*Регионы*/
SELECT ID_DIC_GR_RECIT, ID_DOC, RECIPIENTS_TYPE, PERNR OFFICER_NUM--ENAME,    USRID  E_MAIL
FROM DF.SAP_HR_PERS_STRUCT , 
           DF.DOC_GR_RECIPIENTS  GR
WHERE ID_DIC_GR_RECIT = 2 AND WERKS_ID = VALUE_ID           


UNION 

/*Обасти*/
SELECT ID_DIC_GR_RECIT, ID_DOC, RECIPIENTS_TYPE, PERNR OFFICER_NUM--ENAME,    USRID  E_MAIL
FROM DF.SAP_HR_PERS_STRUCT , 
           DF.DOC_GR_RECIPIENTS  GR
WHERE ID_DIC_GR_RECIT = 3 AND REGION_ID = VALUE_ID           


UNION 

/*Подразделения*/
SELECT ID_DIC_GR_RECIT, ID_DOC, RECIPIENTS_TYPE, PERNR OFFICER_NUM--ENAME,    USRID  E_MAIL
FROM DF.SAP_HR_PERS_STRUCT , 
           DF.DOC_GR_RECIPIENTS  GR
WHERE ID_DIC_GR_RECIT = 4 AND ORGEH_ID = VALUE_ID           
                                

UNION 

/*По рангам */
SELECT ID_DIC_GR_RECIT, ID_DOC, RECIPIENTS_TYPE, PERNR OFFICER_NUM
FROM DF.SAP_HR_PERS_STRUCT , 
           DF.DOC_GR_RECIPIENTS  GR
WHERE ID_DIC_GR_RECIT = 5 AND ERGRU = VALUE_ID           

UNION 
/*Все*/
                                
SELECT GR.ID_DIC_GR_RECIT, GR.ID_DOC, GR.RECIPIENTS_TYPE, PERNR OFFICER_NUM
FROM DF.SAP_HR_PERS_STRUCT , 
           DF.DOC_GR_RECIPIENTS  GR
WHERE ID_DIC_GR_RECIT = 6 


/*По должностям*/

SELECT GR.ID_DIC_GR_RECIT, GR.ID_DOC, GR.RECIPIENTS_TYPE, OFFICER_NUM
FROM DF.V_ENT_EMPLOYEE, 
           DF.DOC_GR_RECIPIENTS  GR           
WHERE ID_DIC_GR_RECIT = 7
AND GR.VALUE_ID = GJOB
;

create or replace  view df.V_DOC_EMP_SIGNERS AS
SELECT ID_HDELEG ID_SIGN,  
  OFFICER_NUM,
  DATE_B,
  DATE_E
FROM DF.DOC_DELEG_HOW 
WHERE ID_DELEG = 1
;

create or replace  view DF.V_DOC_EMP_APPROVALS AS
SELECT ID_HDELEG ID_APPR,  
  OFFICER_NUM,
  DATE_B,
  DATE_E
FROM DF.DOC_DELEG_HOW 
WHERE ID_DELEG = 2
;

create or replace view DF.V_SAP_HR_GJOB AS
SELECT J.GJOB, JN.NAME
FROM DF.SAP_HR_GJOB_NAME JN,
  DF.SAP_HR_GJOB J
WHERE LANG = 'UK' 
AND J.GJOB = JN.GJOB
AND DATE_E = TO_DATE('31.12.9999', 'DD.MM.YYYY');


create or replace view df.V_FAV_GR_EMP AS

select R.ID_FAV_REC , R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID,  E.full_name NAME
from DF.DIC_FAV_EMP r,
  DF.V_ENT_EMPLOYEE e
where r.VALUE_ID = e.id_emp 
AND R.ID_DIC_GR_RECIT = 0

UNION ALL

select R.ID_FAV_REC , R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID,  HTEXT_V NAME
from DF.DIC_FAV_EMP R,
         DF.SAP_HR_PERS_HTEXT_V V
where R.VALUE_ID = V.HTEXT_V_ID
AND R.ID_DIC_GR_RECIT = 1

UNION ALL

select R.ID_FAV_REC , R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID,  WERKS NAME
from DF.DIC_FAV_EMP R,
         DF.SAP_HR_PERS_WERKS V
where R.VALUE_ID = V.WERKS_ID
AND R.ID_DIC_GR_RECIT = 2

UNION ALL

select R.ID_FAV_REC , R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID,  REGION NAME
from DF.DIC_FAV_EMP R,
         DF.DIC_REGION V
where R.VALUE_ID = V.REGION_ID
AND R.ID_DIC_GR_RECIT = 3

UNION ALL

select R.ID_FAV_REC , R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID,  ORGEH NAME
from DF.DIC_FAV_EMP R,
         DF.SAP_HR_PERS_ORGEH V
where R.VALUE_ID = V.ORGEH_ID
AND R.ID_DIC_GR_RECIT = 4

UNION ALL

select R.ID_FAV_REC ,  R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID,  'Ранг '|| VALUE_ID NAME
from DF.DIC_FAV_EMP R         
where R.ID_DIC_GR_RECIT = 5

UNION ALL

select R.ID_FAV_REC , R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID,  'Все сотрудники банка' NAME
from DF.DIC_FAV_EMP R         
where R.ID_DIC_GR_RECIT = 6

UNION ALL

select R.ID_FAV_REC , R.ID_DIC_GR_RECIT ID_GR, r.ID_FAV_GR_EMP, VALUE_ID, J.NAME
from DF.DIC_FAV_EMP R,
  DF.V_SAP_HR_GJOB  J
where R.ID_DIC_GR_RECIT = 7
AND R.VALUE_ID = J.GJOB
;