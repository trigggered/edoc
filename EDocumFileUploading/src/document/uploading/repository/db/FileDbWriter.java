/**
 * 
 */
package document.uploading.repository.db;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import document.uploading.enums.EDevisions;
import document.uploading.enums.EDocumentTypes;

/**
 * @author azhuk
 * Creation date: Aug 1, 2014
 *
 */
public class FileDbWriter {
	private static final Logger _logger = Logger.getLogger(FileDbWriter.class
			.getCanonicalName());
	Connection _conn;
	
	public long  write ( EDocumentTypes docType, EDevisions devisions, int year,	
	
			String name,String docCode, byte[] data) throws SQLException {
		 	
		initConnection();
		
			long idDoc = writeDocCard(docType, devisions, year,name, docCode);
			
			if (docType == EDocumentTypes.Orders) {
				 writeDocCardAdd (idDoc );
			}
				
			
			writeAdditions ( idDoc,  name,docCode,  data);
			
			commitAndClose();
			
		return idDoc;
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void commitAndClose() throws SQLException {
		_conn.commit();
		_conn.close();
		_conn = null;
	}

	/**
	 * 
	 */
	private void initConnection() {
		if (_conn == null ) {
			_conn =DbHelper.getConnection();
		}
	}

	public void  writeAdditions ( long docId, String fileName, String docCode,  byte[] data) throws SQLException {
		 	
			initConnection();				
			long idAtt = writeLobAtt (fileName, docCode);
			
			 writeLobDbStorage (idAtt, data);
			 
			 if (docId != 0) {
				 writeDocStorage (idAtt, docId);
			 }
		
	}
	
	public long  writeDocCard ( EDocumentTypes docType, EDevisions devisions, int year,
			String name, String docCode) throws SQLException {			

				CallableStatement cstm  = _conn.prepareCall(DbRepositorySqlHelper.SQL_INS_DOC_CARD);
				cstm.setInt(1, docType.getValue()); //ID_CORR
				cstm.setString(2, name); //NAME
				cstm.setString(3, docCode); // CODE	
				cstm.setString(4, name); //NOTE
				
				String dta =  "01.01."+year;
				
				cstm.setString(5, dta); //DATE_PUB
				cstm.setString(6, dta); //DATE_EFFECTIVE
				cstm.setInt(7, devisions.getValue());
				
				cstm.registerOutParameter(8, java.sql.Types.NUMERIC);	
				
				cstm.execute();		
				
				long id = cstm.getInt(8);
				cstm.close();
				_logger.info("ID_DOC "+id );
				return id;	
	}
	
	public long  writeDocCardAdd (long idDoc) throws SQLException {			

				CallableStatement cstm  = _conn.prepareCall(DbRepositorySqlHelper.SQL_INS_ORDER);
				cstm.setLong(1, idDoc); //ID_DOC				
				
				cstm.registerOutParameter(2, java.sql.Types.NUMERIC);	
				
				cstm.execute();		
				
				long id =cstm.getLong(2);
				cstm.close();
				_logger.info("ID_DOC_ADD "+id );
				return id;	
	}	
	
	
	public long  writeLobAtt  (String fileName, String docCode) throws SQLException {			

		CallableStatement cstm  = _conn.prepareCall(DbRepositorySqlHelper.SQL_INS_LOB_ATTR);
		cstm.setString(1, fileName); //NAME_LOB				
		cstm.setString(2, docCode); //NAME_author
		
		cstm.registerOutParameter(3, java.sql.Types.NUMERIC);	
		
		cstm.execute();		
		
		long id =cstm.getLong(3);
		cstm.close();
		_logger.info("ID_ATTRIBUTE "+id );
		return id;	
	}	
	
	public void  writeLobDbStorage (long idAtt, byte[] data) throws SQLException {			

		if ( data!= null) {
		CallableStatement  cstm  = _conn.prepareCall(DbRepositorySqlHelper.SQL_INS_DB_STORAGE);		
		cstm.setLong(1, idAtt); //ID_ATTRIBUTE
		cstm.registerOutParameter(2, java.sql.Types.BLOB);
		cstm.execute();
			Blob blob = cstm.getBlob(2);			 			
			blob.setBytes(1, data);
			cstm.setBlob(2, blob);			
			cstm.close();
		}  
			
		
		
		
	}
	
	public void  writeDocStorage (long idAtt, long idDoc) throws SQLException {
		
		PreparedStatement cstm  = _conn.prepareStatement(DbRepositorySqlHelper.SQL_INS_DOC_STORAGE);
		cstm.setLong(1, idDoc); //ID_DOC
		cstm.setLong(2, idAtt); //ID_ATTRIBUTE					
		
		cstm.execute();				
		
	}	
	
}
