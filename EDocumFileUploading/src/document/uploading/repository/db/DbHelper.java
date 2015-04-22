/**
 * 
 */
package document.uploading.repository.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import oracle.jdbc.driver.OracleDriver;


/**
 * @author azhuk
 * Creation date: Aug 4, 2014
 *
 */
public class DbHelper {
	private static final Logger _logger = Logger.getLogger(DbHelper.class
			.getCanonicalName());
	
	
	public static Connection getConnection() {
		
		return getConnection("10.91.64.73",
				"1527", 
				"wfw1ua10", "P00090730", "P00090730");
	}
	
	public static Connection getConnection(String db, String port, String sid, String user, String pwd) {		
		Connection conn = null;
		try {
			DriverManager.registerDriver(new OracleDriver());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+db+":"+port+":"+sid, user, pwd);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			_logger.severe(e.getMessage());
		}	               
        
        return conn;		
	}
	
	public static PreparedStatement getStatement (String sql) {
		
		PreparedStatement pstmt = null;
		try {
			pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//pstmt = getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			_logger.severe(e.getMessage());
		}
		return pstmt;
	}

}
