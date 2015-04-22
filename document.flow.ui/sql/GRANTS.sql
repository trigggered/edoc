/*grant  ls to df; */

GRANT EXECUTE ON LS.PKG_LS_UTILS TO DF;

grant select ON  LS.SEQ_LOB_STORAGE to df;

grant select ON LS.LOB_ATTRIBUTE to df;
grant select ON LS.LOB_CONTENT_TYPE to df;
grant select ON LS.LOB_DB_STORAGE to df;
grant select ON LS.LOB_FS_STORAGE to df;
grant select ON LS.LOB_LOG to df;
grant select ON LS.LOB_STORAGE_TYPE to df;
grant select ON LS.LOB_VERSION to df;

grant INSERT ON LS.LOB_ATTRIBUTE to df;
grant INSERT ON LS.LOB_CONTENT_TYPE to df;
grant INSERT ON LS.LOB_DB_STORAGE to df;
grant INSERT ON LS.LOB_FS_STORAGE to df;
grant INSERT ON LS.LOB_LOG to df;
grant INSERT ON LS.LOB_STORAGE_TYPE to df;
grant INSERT ON LS.LOB_VERSION to df;

grant UPDATE ON LS.LOB_ATTRIBUTE to df;
grant UPDATE ON LS.LOB_CONTENT_TYPE to df;
grant UPDATE ON LS.LOB_DB_STORAGE to df;
grant UPDATE ON LS.LOB_FS_STORAGE to df;
grant UPDATE ON LS.LOB_LOG to df;
grant UPDATE ON LS.LOB_STORAGE_TYPE to df;
grant UPDATE ON LS.LOB_VERSION to df;

grant DELETE ON LS.LOB_ATTRIBUTE to df;
grant DELETE ON LS.LOB_CONTENT_TYPE to df;
grant DELETE ON LS.LOB_DB_STORAGE to df;
grant DELETE ON LS.LOB_FS_STORAGE to df;
grant DELETE ON LS.LOB_LOG to df;
grant DELETE ON LS.LOB_STORAGE_TYPE to df;
grant DELETE ON LS.LOB_VERSION to df;

/*grant  mdb to df */
grant select on mdb.APP_MENU to df;
grant select on mdb.APP_MODULE to df;
grant select on mdb.APP_USERS to df;
grant select on mdb.MDB_DENTITYS to df;
grant select on mdb.MDB_DENTITYS_ACTION  to df;
grant select on mdb.MDB_DENTITY_KEYS  to df;
grant select on mdb.MDB_EXT_METHODS to df;
grant select on mdb.MDB_EXT_METHODS_LANG  to df;
grant select on mdb.MDB_FLD  to df;
grant select on mdb.MDB_FLD_TYPE  to df;
grant select on mdb.MDB_HISTORY  to df;
grant select on mdb.MDB_METHODS_DENTITY  to df;
grant select on mdb.MDB_SEQ  to df;
grant select on mdb.MDB_SUMM_KIND to df;
grant select on mdb.MDB_TABLES  to df;
grant select on mdb.MDB_XML_DATA_CONV to df;
grant select on mdb.MDB_XML_DATA_CONV to df;

grant select on mdb.VIEW_KEYS_DENTITY to df;
grant select on mdb.VIEW_METHODS_FOR_DENTITY to df;
grant select on mdb.V_METHODS_DEFINITION to df;
grant select on mdb.V_METHODS_FOR_DENTITY to df;
grant select on mdb.V_METHODS_TREE to df;
grant select on mdb.V_METHOD_DEFINITION to df;


GRANT  SELECT  ON mdb.APP_MENU_ID_MENU_SEQ TO df;
GRANT  SELECT  ON mdb.ID_DENTITY_ACTION_SEQ TO df;
GRANT  SELECT  ON mdb.ID_DENTITY_KEY_SEQ TO df;
GRANT  SELECT  ON mdb.ID_DENTITY_SEQ TO df;
GRANT  SELECT  ON mdb.ID_EXT_METHODS_LANG_SEQ TO df;
GRANT  SELECT  ON mdb.ID_EXT_METHOD_SEQ TO df;
GRANT  SELECT  ON mdb.ID_FLD_SEQ TO df;
GRANT  SELECT  ON mdb.ID_FLD_TYPE_SEQ TO df;
GRANT  SELECT  ON mdb.ID_HISTORY_SEQ TO df;
GRANT  SELECT  ON mdb.ID_METHODS_DENTITY_SEQ TO df;
GRANT  SELECT  ON mdb.ID_SEQ_SEQ TO df;
GRANT  SELECT  ON mdb.ID_SH_DEF_PARAM_VAL_SEQ TO df;
GRANT  SELECT  ON mdb.ID_TABLE_SEQ TO df;
GRANT  SELECT  ON mdb.ID_TXT_SHT_SEQ TO df;

grant insert on mdb.APP_MENU to df;
grant insert on mdb.APP_MODULE to df;
grant insert on mdb.APP_USERS to df;
grant insert on mdb.MDB_DENTITYS to df;
grant insert on mdb.MDB_DENTITYS_ACTION  to df;
grant insert on mdb.MDB_DENTITY_KEYS  to df;
grant insert on mdb.MDB_EXT_METHODS to df;
grant insert on mdb.MDB_EXT_METHODS_LANG  to df;
grant insert on mdb.MDB_FLD  to df;
grant insert on mdb.MDB_FLD_TYPE  to df;
grant insert on mdb.MDB_HISTORY  to df;
grant insert on mdb.MDB_METHODS_DENTITY  to df;
grant insert on mdb.MDB_SEQ  to df;
grant insert on mdb.MDB_SUMM_KIND to df;
grant insert on mdb.MDB_TABLES  to df;
grant insert on mdb.MDB_XML_DATA_CONV to df;


grant update on mdb.APP_MENU to df;
grant update on mdb.APP_MODULE to df;
grant update on mdb.APP_USERS to df;
grant update on mdb.MDB_DENTITYS to df;
grant update on mdb.MDB_DENTITYS_ACTION  to df;
grant update on mdb.MDB_DENTITY_KEYS  to df;
grant update on mdb.MDB_EXT_METHODS to df;
grant update on mdb.MDB_EXT_METHODS_LANG  to df;
grant update on mdb.MDB_FLD  to df;
grant update on mdb.MDB_FLD_TYPE  to df;
grant update on mdb.MDB_HISTORY  to df;
grant update on mdb.MDB_METHODS_DENTITY  to df;
grant update on mdb.MDB_SEQ  to df;
grant update on mdb.MDB_SUMM_KIND to df;
grant update on mdb.MDB_TABLES  to df;
grant update on mdb.MDB_XML_DATA_CONV to df;


grant delete on mdb.APP_MENU to df;
grant delete on mdb.APP_MODULE to df;
grant delete on mdb.APP_USERS to df;
grant delete on mdb.MDB_DENTITYS to df;
grant delete on mdb.MDB_DENTITYS_ACTION  to df;
grant delete on mdb.MDB_DENTITY_KEYS  to df;
grant delete on mdb.MDB_EXT_METHODS to df;
grant delete on mdb.MDB_EXT_METHODS_LANG  to df;
grant delete on mdb.MDB_FLD  to df;
grant delete on mdb.MDB_FLD_TYPE  to df;
grant delete on mdb.MDB_HISTORY  to df;
grant delete on mdb.MDB_METHODS_DENTITY  to df;
grant delete on mdb.MDB_SEQ  to df;
grant delete on mdb.MDB_SUMM_KIND to df;
grant delete on mdb.MDB_TABLES  to df;
grant delete on mdb.MDB_XML_DATA_CONV to df;



