declare
 l_appId number;
 l_menuId  number;
 l_menuId1 number;
 l_menuId2 number;
 l_menuId3 number;
 
 l_ADMIN_menuId number;
l_CREATE_DOC_menuId number;
begin

 --select id_app into l_appId from mdb.app_module
 
delete mdb.app_menu;

delete mdb.app_module;

l_appId :=1;
insert into mdb.app_module (id_app, name)
 values (l_appId, 'Document management system');
 
 /*Hot Menu*/
insert into mdb.app_menu (id_menu_parent, id_app, name, action, img_path)
  values (null, l_appId, 'Home', 'Home', 'silk/cube_green.png') returning id_menu into l_menuId;
 
 
  
 /*Документы*/ 
insert into mdb.app_menu (id_menu_parent, id_app, name)
  values (null, l_appId, 'Документы') returning id_menu  into l_menuId;
  
  insert into mdb.app_menu (id_menu_parent, id_app, name, action)
  values (l_menuId, l_appId, 'Мои документы', 'MyDocs');
  
  
  
  insert into mdb.app_menu (id_menu_parent, id_app, name, action)
  values (l_menuId, l_appId, 'Документы дня', 'DocumentsOfDay'); 
          
  /*
  insert into mdb.app_menu (id_menu_parent, id_app, name)
  values (l_menuId, l_appId, 'Просмотр') returning id_menu  into l_menuId; 
   */
   insert into mdb.app_menu (id_menu_parent, id_app, name, action, visible)
      values (l_menuId, l_appId, 'Входящая', 'InDoc', 0);             	

      insert into mdb.app_menu (id_menu_parent, id_app, name, action,visible)
        values (l_menuId, l_appId, 'Исходящая', 'OutDoc',0); 
        /*
        insert into mdb.app_menu (id_menu_parent, id_app, name, action)
          values (l_menuId, l_appId, 'Избранные документы', 	'FavoritesDoc'); 
          */
          
        insert into mdb.app_menu (id_menu_parent, id_app, name, action)
          values (l_menuid, l_appid, 'Все  документы', 	'InsideDoc'); 
          /*
          insert into mdb.app_menu (id_menu_parent, id_app, name, action)
            values (l_menuId, l_appId, 'На согласовании', 	'AcceptingProcess'); 
          */
  
  /*Поиск*/
  
  insert into mdb.app_menu (id_menu_parent, id_app, name, img_path, action, shortcut_key,visible)
  values (null, l_appId, 'Поиск', 'silk/find.png', 'Search', 'Ctrl+F', 1);  
  
  
  /*
  insert into mdb.app_menu (id_menu_parent, id_app, name, img_path, action, SHORTCUT_KEY,visible)
      values (l_menuId, l_appId, 'Расширенный поиск', 'silk/find.png', 'Search', 'Ctrl+F', 1);  
      
  insert into mdb.app_menu (id_menu_parent, id_app, name, img_path, action)
   values (l_menuId, l_appId, 'Быстрый', '', 'Search') returning id_menu  into l_menuId;  
  
   insert into mdb.app_menu (id_menu_parent, id_app, name, img_path, action, SHORTCUT_KEY)
     values (l_menuId, l_appId, 'По Ид документа', '', 'SearchById', 'Ctrl+O');
      
    insert into mdb.app_menu (id_menu_parent, id_app, name, img_path, action, SHORTCUT_KEY)
      values (l_menuId, l_appId, 'По Коду документа', '', 'SearchByCode', NULL);  
      
    */
     
      insert into mdb.app_menu (id_menu_parent, id_app, name, action)
  values (null, l_appId, 'Создать документ', '') returning id_menu  into l_menuId1;
    
    l_CREATE_DOC_menuId := l_menuId1;
        
        /*
    insert into mdb.app_menu (id_menu_parent, id_app, name, action, visible)
      values (l_menuId1, l_appId, 'Входящая корреспонденция', 'NewInDoc', 0);             	

      insert into mdb.app_menu (id_menu_parent, id_app, name, action, visible)
        values (l_menuId1, l_appId, 'Исходящая корреспонденция', 'NewOutDoc', 0); 
    
        insert into mdb.app_menu (id_menu_parent, id_app, name, action)
          values (l_menuId1, l_appId, 'Внутренняя корреспонденция', 	null) returning id_menu  into l_menuId1;
          */
          insert into mdb.app_menu (id_menu_parent, id_app, name, action)
          values (l_menuId1, l_appId, 'Приказ', 	'NewInsideCommandDoc') ;
          
          insert into mdb.app_menu (id_menu_parent, id_app, name, action)
          values (l_menuid1, l_appid, 'Порядок', 	'NewInsideOrderDoc') ;
          
          insert into mdb.app_menu (id_menu_parent, id_app, name, action)
            values (l_menuid1, l_appid, 'Уведомление', 	'NewInsideNotificationDoc') ;
  
  /*Справочники*/
 insert into mdb.app_menu (id_menu_parent, id_app, name)
  values (null, l_appId, 'Справочники') returning id_menu  into l_menuId;  
    
  insert into mdb.app_menu (id_menu_parent, id_app, name, action)
    values (l_menuId, l_appId, 'Избранные сотрудники', 'FavoritesEmp') ;
    
          
  
	
  
  /*Администрирование*/
  insert into mdb.app_menu (id_menu_parent, id_app, name)
    values (null, l_appId, 'Администрирование') returning id_menu  into l_menuId;
  
  l_ADMIN_menuId:=l_menuId;
  
  insert into mdb.app_menu (id_menu_parent, id_app, name, action)
    values (l_menuId, l_appId, 'Рабочая область БА','BAWorkspace');   
  
   insert into mdb.app_menu (id_menu_parent, id_app, name, action)
    values (l_menuId, l_appId, 'Резервирование кода документа','CodeDocReserv');   
  
  /*Справочники*/
 insert into mdb.app_menu (id_menu_parent, id_app, name)
  values (l_menuId, l_appId, 'Справочники БА') returning id_menu  into l_menuId1;
  
  
        
    insert into mdb.app_menu (id_menu_parent, id_app, name, action)    
     values (l_menuId1, l_appId, 'Подписанты и  ассистенты','SignatoryAssists');   
     
     insert into mdb.app_menu (id_menu_parent, id_app, name, action)    
     values (l_menuId1, l_appId, 'Делегирование согласования','ApprovalAssists');   
     
     insert into mdb.app_menu (id_menu_parent, id_app, name, action)
     values (l_menuId1, l_appId, 'Бизнес администраторы','DicBA');   
     
     insert into mdb.app_menu (id_menu_parent, id_app, name, action)
      values (l_menuId1, l_appId, 'Исключения из рассылки','NoMailingEmp');   
     
 insert into mdb.app_menu (id_menu_parent, id_app, name, action)
  values (l_menuId1, l_appId, 'Вид корреспонденции','DocFormOfCorrespondence'); 
  
  insert into mdb.app_menu (id_menu_parent, id_app, name, action)
  values (l_menuId1, l_appId, 'Статусы документов', 'DocStatus'); 
  
  
  insert into mdb.app_menu (id_menu_parent, id_app, name,action, VISIBLE)
  values (l_menuId1, l_appId,'Продукты и направления', 'Productions', 0); 
  
  
  
  insert into mdb.app_menu (id_menu_parent, id_app, name )
    values (l_menuId1, l_appId,'Для  "Порядок"') returning id_menu into  l_menuId2; 
  
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
   values (l_menuId2, l_appId,'Тип порядка', 'DicDocTypeOfOrder'); 
  
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
  values (l_menuId2, l_appId,'Сфера применения', 'DocScoupe'); 
  
    
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
  values (l_menuId2, l_appId,'Зоны рисков', 'DocRisk'); 
  
  /*ORG UNIT*/
  /*
  insert into mdb.app_menu (id_menu_parent, id_app, name)
    values (l_menuId1, l_appId, 'Организационные единицы') returning id_menu  into l_menuId2;
  */
    insert into mdb.app_menu (id_menu_parent, id_app, name, action)
  values (l_menuId1, l_appId, 'Подразделения','ListOfDepartments'); 
    
  insert into mdb.app_menu (id_menu_parent, id_app, name)
    values (l_menuId1, l_appId, 'SAP HR') returning id_menu  into l_menuId2;    
    
  /*Новая структура */
   insert into mdb.app_menu (id_menu_parent, id_app, name )
    values (l_menuId1, 1, 'Новая выгрузка') returning id_menu into l_menuId3; 

   /*insert into mdb.app_menu (id_menu_parent, id_app, name,action)
    values (l_menuId3, 1, 'Классификация подразделения (ГБ или сеть)', 'SAP_HR_PERS_HTEXT'); 
  */
  
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
   values (l_menuId3, 1,'Вертикаль', 'SAP_HR_PERS_HTEXT_V');  
  
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
    values (l_menuId3, 1,'Подразделения', 'SAP_HR_PERS_ORGEH');  
    
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
    values (l_menuId3, 1,'Раздел персонала (регион)', 'SAP_HR_PERS_WERKS');     
    
    insert into mdb.app_menu (id_menu_parent, id_app, name,action)
    values (l_menuId3, 1,'Областя', 'DicRegions');     
    
  
   insert into mdb.app_menu (id_menu_parent, id_app, name,action)
  values (l_menuId2, l_appId, 'Сотрудники SAP', 'SAPListOfEmployee'); 
  
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
  values (l_menuId2, l_appId,'Подразделения SAP', 'SAPListOfDepartments');  
  
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
    values (l_menuId2, l_appId,'Груповые должности SAP', 'SAPGJobs');  
    
  insert into mdb.app_menu (id_menu_parent, id_app, name,action)
    values (l_menuId2, l_appId,'Должности SAP', 'SAPJobs');     
  
  /*ORG UNIT*/

  /*  
  insert into mdb.app_menu (id_menu_parent, id_app, name, action)
    values (l_menuId, l_appId, 'Мапинг подразделений','MapListOfDepartments');   
    
  
    insert into mdb.app_menu (id_menu_parent, id_app, name)
    values (l_menuId, l_appId, 'Пользователи и роли') returning id_menu into l_menuId1;
    
    insert into mdb.app_menu (id_menu_parent, id_app, name, action)
    values (l_menuId1, l_appId, 'Роли и действия', 'RoleActions'); 
    
  insert into mdb.app_menu (id_menu_parent, id_app, name, action)
    values (l_menuId1, l_appId, 'Назначение ролей', 'AssignRoles'); 
  */
  
  /*Отчеты*//*
  insert into mdb.app_menu (id_menu_parent, id_app, name, img_path, action)
  values (null, l_appId, 'Отчеты',  'silk/printer.png', 'Reports') returning id_menu  into l_menuId;
  */

insert into mdb.app_menu (id_menu_parent, id_app, name)
    values (null, l_appId, 'Помощь') returning id_menu  into l_menuId;

insert into mdb.app_menu (id_menu_parent, id_app, name, action)    
    values (l_menuId, l_appId, 'Инстукция пользователя', 'UserGuide');    

DELETE DF.SEC_ROLE_ACTION;

insert into DF.SEC_ROLE_ACTION (ID_ROLE,    ID_ACTION,      date_b,   ID )     
select 1,1, sysdate,  id_menu
    from mdb.app_menu 
    where id_app = 1 and visible =1
    connect by prior id_menu = id_menu_parent 
    start with id_menu_parent  is null 
    
    order siblings by sort
;


insert into DF.SEC_ROLE_ACTION (ID_ROLE,    ID_ACTION,      date_b,   ID )     
select 2,1, sysdate,  id_menu
    from mdb.app_menu 
    where id_app = 1 and visible =1
    connect by prior id_menu = id_menu_parent 
    start with id_menu_parent  is null and  id_menu <>l_ADMIN_menuId
    order siblings by sort
;    

insert into DF.SEC_ROLE_ACTION (ID_ROLE,    ID_ACTION,      date_b,   ID )     
select 3,1, sysdate,  id_menu
    from mdb.app_menu 
    where id_app = 1 and visible =1
    connect by prior id_menu = id_menu_parent 
    start with id_menu_parent  is null and  id_menu not in (l_ADMIN_menuId, l_CREATE_DOC_menuId )
    order siblings by sort
    ;
    
      
end;