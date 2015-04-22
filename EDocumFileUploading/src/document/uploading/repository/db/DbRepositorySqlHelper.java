/**
 * 
 */
package document.uploading.repository.db;

import java.util.logging.Logger;

/**
 * @author azhuk
 * Creation date: Aug 4, 2014
 *
 */
public class DbRepositorySqlHelper {
	public static final Logger _logger = Logger
			.getLogger(DbRepositorySqlHelper.class.getCanonicalName());
	
	public static  String SQL_INS_DOC_CARD =
			"BEGIN INSERT INTO DF.DOC_CARD "+
					"( id_doc,      ID_CORR, IS_CLOSED,  NAME, CODE, DATE_IN, ID_STATUS, NOTE, DATE_PUB,    DATE_EFFECTIVE,    ID_DEP_OWNER, ID_INFO_TYPE  ) "+
					  "VALUES  (DF.DOC_CARD_ID_DOC_SEQ.NEXTVAL,   ?,0,?,?,sysdate,9,?,TO_DATE( ?, 'DD.MM.YYYY'),TO_DATE( ?, 'DD.MM.YYYY'),?, 1  ) RETURNING ID_DOC INTO ?; END;";
	
	public static  String 	SQL_INS_ORDER =
	"BEGIN INSERT INTO DF.DOC_CARD_ADD "+
	  "( ID_DOC_ADD,    ID_DOC,  ORD_VERSION,    ORD_LEVEL  ) "+
	  "VALUES (DF.DOC_CARD_ADD_ID_DOC_ADD_SEQ.NEXTVAL,  ?,  1,   3) RETURNING ID_DOC_ADD INTO ?; END;";
	

	public static  String SQL_INS_LOB_ATTR =
	"BEGIN INSERT INTO LS.LOB_ATTRIBUTE "+
	  "(  ID_ATTRIBUTE,  ID_STORE_TYPE,  ID_CONTENT_TYPE, NAME_LOB, NAME_AUTHOR  ) "+
	  "VALUES	( LS.SEQ_LOB_STORAGE.NEXTVAL,  1,1, ?,? ) RETURNING ID_ATTRIBUTE INTO ?; END;";

	public static  String SQL_INS_DB_STORAGE =
	"begin INSERT INTO LS.LOB_DB_STORAGE (ID_LOB, ID_ATTRIBUTE,    DATA  )"+
	  "VALUES (LS.SEQ_LOB_STORAGE.NEXTVAL, ?, empty_blob() ) returning data into ?; end;";
	
	
	public static  String SQL_INS_DOC_STORAGE =
	"INSERT INTO DF.DOC_STORAGE "+
    	"(  ID_DOC,   ID_LOB_ATTR   ) "+
    		"VALUES  ( ?,?)";	

	
}
