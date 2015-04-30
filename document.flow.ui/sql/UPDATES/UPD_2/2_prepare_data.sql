
UPDATE DF.ENT_DEPARTMENTS 
SET HTEXT_V_ID = 8
WHERE ID_DEP  = 11;

declare 
   l_idCorParent number;
   l_idSatus number;
begin


INSERT INTO DF.DOC_DIC_CORR_TYPE
  (    ID_CORR,ID_CORR_PARENT,CODE,NAME,ABBREVIATION,ID_CORR_PARENT_2)
  VALUES(495 ,null,'2','Финансы',null,null) ;
  
     insert into df.DOC_DIC_CORR_TYPE (ID_CORR, id_corr_parent, code, name, ABBREVIATION, id_corr_parent_2)
        values (496, 495, '2.1.', 'Бухгалтерская модель ', 'БМ', 495);
        
            INSERT INTO DF.DOC_CT_REL_STAT (ID_CORR, ID_STATUS)
              SELECT l_idCorParent, ID_STATUS FROM DF.DOC_DIC_STATUS 
              WHERE ID_STATUS BETWEEN 5 AND 12;
              
  commit;
  end;
  
/*################################################################*/
/*################################################################*/
SELECT * FROM DF.ENT_DEP_VISION 

  INSERT INTO  DF.ENT_DEP_VISION 
 VALUES(1, 'Для PMO' );

update DF.ENT_DEPARTMENTS 
 set ID_VISION = 1;
 
 INSERT INTO  DF.ENT_DEP_VISION 
 VALUES(2, 'Для FIN' )

/
/*################################################################*/
/*################################################################*/

INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('ДФ','Департамент финансов',SYSDATE,'ДФ',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('ДВА','Департамент внутреннего аудита',SYSDATE,'ДВА',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('ДКК','Департамент комплаенс контроля',SYSDATE,'ДКК',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('ДКОД','Департамент кредитных операций и документооборота',SYSDATE,'ДКОД',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('УСТИО','Управление сопровождения торговых и инвестиционных операций',SYSDATE,'УСТИО',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('ДОР','Департамент операционных расчетов',SYSDATE,'ДОР',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('УОПК','Управление операций с платежными картами',SYSDATE,'УОПК',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('УУПНД','Управление учетных процедур небанковской деятельности',SYSDATE,'УУПНД',2);
INSERT INTO DF.ENT_DEPARTMENTS  (CODE, NAME, DATE_B,    ABBREVIATION,ID_VISION  )   VALUES   ('ДМП','Департамент менеджмент персонала',SYSDATE,'ДМП',2);


/*################################################################*/
/*################################################################*/

INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (1,'1-1','Общие принципы УП и системы учета');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (2,'1-1-1','Налоговый учет');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (3,'1-2','Финансовая отчетность');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (4,'1-3','Принципы учета при консолидации компаний');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (5,'1-4','Общие принципы оценки финансовых инструментов');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (6,'2-1','Кредиты и дебиторская задолженность');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (7,'2-2','Ценные бумаги');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (8,'2-3','Инвестиции в ассоциированные и дочерние компании');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (9,'2-4','Аренда (лизинг)');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (10,'2-5','Инвестиционная недвижимость');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (11,'2-6','Основные средства (необоротные материальные активы)');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (12,'2-7','Необоротные активы на продажу и прекращенная деятельность');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (13,'2-8','Нематериальные активы');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (14,'2-9','Запасы материальных ценностей');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (15,'3-1','Собственный капитал');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (16,'3-2','Депозиты');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (17,'3-3','Выплаты сотрудникам');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (18,'3-4','Резервы под обязательства и условные обязательства');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (19,'3-5','Ценные бумаги собственной эмиссии');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (20,'4-1','Деривативы (за исключением деривативов хеджирования и встроенных финансовых инструментов)');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (21,'4-2','Гарантии и прочие гарантийные обязательства (документарные операции)');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (22,'5-1','Безналичные расчеты в иностранной валюте через систему корреспондентских счетов');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (23,'5-2','Операции в иностранной валюте и банковских металлах');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (24,'5-3','Операции с платежными картами (ПК)');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (25,'5-4','Внебалансовые операции');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (26,'6','Признание доходов и расходов от банковской деятельности (без учета доходов, связанных с финансовыми активами/обязательствами и дивидендами)');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (27,'7-1','Операции с наличными и другими ценностями операционной кассы');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (28,'7-2','Расчетное обслуживание клиентов');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (29,'8','Хозяйственная (внутрибанковская) деятельность');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (30,'9','Контроль операционной деятельности банкаё');
INSERT INTO DF.DIC_FIN_POLICIES  (  ID_FIN_POLICY,   CODE,   NAME )  VALUES  (31,NULL,'Прочее ');

    
/*################################################################*/
/*################################################################*/

SELECT ID_FIN_OPERT, CODE, NAME FROM DF.DIC_FIN_OPERT ;

INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (1,'001','Депозиты');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (2,'002','Кредиты ДПК');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (3,'003','Кредиты КБ');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (4,'004','Факторинг');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (5,'005','Документарные');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (6,'006','Валюта');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (7,'007','Касса');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (8,'008','Межбанковские');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (9,'009','Револьверные кредитные карты');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (10,'010','Пластиковые карты');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (11,'011','Ценные бумаги');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (12,'012','Переводы');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (13,'013','Резервы');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (14,'014','Партнеры');
INSERT INTO DF.DIC_FIN_OPERT  (    ID_FIN_OPERT,    CODE,    NAME  )  VALUES  (15,'015','Небанковские');
