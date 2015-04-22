/**
 * 
 */
package lob.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import lob.storage.environment.Property;
import lob.storage.facade.ILobStorageFacade;
import lob.storage.facade.LobData;
import lob.storage.facade.ModelManagerHelper;
import lob.storage.model.ILobStorage;
import lob.storage.model.LobAttribute;
import lob.storage.model.LobStorageType;
import lob.storage.model.LobVersion;
import mdb.core.shared.gw.lob.ILobManagerRemote;
import mdb.core.shared.logger.MdbLogger;
import mdb.core.shared.transport.LobStoredResult;
import mdb.core.shared.transport.LobStoredResult.EStoringStatus;
import mdb.core.shared.utils.zip.ZipHelper;
import mdb.sign.EUSignHelperBean;

/**
 * @author azhuk
 * Creation date: Aug 30, 2013
 *
 */

@Stateless
@WebService
public class LobManager extends ModelManagerHelper implements ILobManagerRemote{
	private static final Logger _logger = Logger
			.getLogger(LobManager.class.getName());
	
	
	@EJB
	EUSignHelperBean _signBean;
	
	
	/**
	 * 
	 */
	public LobManager() {
		// TODO Auto-generated constructor stub
	}	

	
	@Override
	@WebMethod
	@Interceptors (MdbLogger.class)	
	public LobStoredResult putLob(String lobName, String  author, long contentType,  byte[]  lobBytes ) {
				
		lobName = getFileNameFromPath(lobName);
		
		_logger.info(String.format("Put lob to storage. Lob Name: %s, Author: %s, size: %s", lobName, author, lobBytes.length));
		ILobStorageFacade lobStorage = getLobStorageFacade( Property.getDefaultStorageType() );
		LobStoredResult toReturn = null;
		if (lobStorage == null) {
			_logger.severe("Lob storageFacade is NULL !!!");
			toReturn = new LobStoredResult();
			toReturn.setStoredStatus(EStoringStatus.Faile);
			return toReturn ;		
		}
		
		LobAttribute  lobAttribute = new LobAttribute();
		
		LobStorageType lobStorageType = getEntityManager().find(LobStorageType.class, Property.getDefaultStorageType().getValue());		
		
		lobAttribute.setLobStorageType(lobStorageType);
		lobAttribute.setLobContentType(contentType);
		lobAttribute.setNameLob(lobName);
		lobAttribute.setNameAuthor(author);
		
		_logger.info("New Lob attribute=" +lobAttribute.getIdAttribute());
		
		ILobStorage newLob =  lobStorage.createLob(lobAttribute, lobBytes, _signBean.getHashData(lobBytes));
		
		
		LobVersion version =  new LobVersion();		
		version.setVersionNum("1");
		
		
		return  persist(lobAttribute, newLob, version);		
	}
	
	
	private String  getFileNameFromPath(String filePath) {
		String winPathSeparator = "\\";
		String linuxPathSeparator = "/";
		
		int idx = filePath.lastIndexOf(winPathSeparator);				
		
		if (  idx > 0 ) {
			filePath = idx >= 0 ? filePath.substring(idx + 1) : filePath;
		}		
		else {
			
			idx = filePath.lastIndexOf(linuxPathSeparator);			
			filePath = idx >= 0 ? filePath.substring(idx + 1) : filePath;
		}				
		
		return filePath;
	}
	
	
	private  LobStoredResult   persist(LobAttribute attribute, ILobStorage lob,  LobVersion version ) {
		_logger.info("Begin Persist entity");

		
		getEntityManager().persist(attribute);		
		long idAttribute = attribute.getIdAttribute();
		_logger.info( String.format("Lob attribute id = %s" , idAttribute ));
		
		
		getEntityManager().persist(lob);		
		long idLob = lob.getIdLob();		
		_logger.info( String.format("Lob  id = %s" , idLob  ));
		
		version.setIdAttribute(attribute.getIdAttribute());
		getEntityManager().persist(version);		

		
		LobStoredResult  toReturn = new  LobStoredResult ();
		
		toReturn.setAttributeId(idAttribute) ;
		toReturn.setLobId(idLob) ; 
		toReturn.setVersionId(version.getIdVersion());		
		toReturn.setLobName(attribute.getNameLob() );
		toReturn.setStoredStatus(EStoringStatus.Ok );
		_logger.info("End Persist entity");
		_logger.info("All flush");
		
		getEntityManager().flush();
		return toReturn;
	}
	
	public ILobStorageFacade getLobStorageFacade(Property.StorageType storageType) {
		ILobStorageFacade toReturn = null;
		
		
		switch (storageType) {
		case DbStorage:
			try {
				
				toReturn  = (ILobStorageFacade)new InitialContext().lookup("java:module/DbLobStorageFacade");
			} catch (NamingException e) {
				_logger.severe(e.getMessage());
			}
			break;
		case	FsStorage:
			try {
				toReturn  = (ILobStorageFacade)new InitialContext().lookup("java:module/FsLobStorageFacade");
			} catch (NamingException e) {
				_logger.severe(e.getMessage());
			}
			break;
		}			
		
		return toReturn;
	}


	/* (non-Javadoc)
	 * @see mdb.core.shared.gw.lob.ILobManagerRemote#getLob(long)
	 */
	@Override
	@WebMethod
	public LobStoredResult getLob(long idLob) {
		
		LobStoredResult result = new LobStoredResult();
		
		ILobStorageFacade lobStorage = getLobStorageFacade( Property.getDefaultStorageType() );
		
		LobData data= lobStorage.getLobData(idLob);
		String fileName = data.getLobAttributeData().getNameLob();
		
		if (  data.getLobData().getSign() != null) {
			result.setData(getLobWithSign(data) );
			result.setLobName(fileName.replaceFirst("[.][^.]+$", ".zip"));
		}
		else {
			result.setData(data.getLobData().getData());
			result.setLobName(fileName);
		}
		
		return result;
		
			
	}

	private byte[] getLobWithSign(LobData data) {
		
		byte[] toReturn = null;
		
			String fileName = data.getLobAttributeData().getNameLob();				
			String signInfo = _signBean.verify(data.getLobData().getSign(), data.getLobData().getHash());
			
			
			HashMap<String, byte[]>  map = new HashMap<String, byte[]>();
			 
			String signFileName =String.format( "Додаток до %s.sign", fileName.replaceFirst("[.][^.]+$", "")); 
			String signTxtFileName =String.format( "Додаток ЭЦП до %s.txt", fileName.replaceFirst("[.][^.]+$", ""));
			
			map.put(fileName, data.getLobData().getData());
			map.put(signFileName, data.getLobData().getSign().getBytes());
			if (signInfo!= null) {
				map.put(signTxtFileName, signInfo.getBytes());
			}
			
			
			try {
				toReturn =ZipHelper.compress(map);
			} catch (IOException e) {
				_logger.severe(e.getMessage());
				toReturn = data.getLobData().getData();
			}						
			return toReturn;				
		
	}

	/* (non-Javadoc)
	 * @see mdb.core.shared.gw.lob.ILobManagerRemote#remove(long)
	 */
	@Override
	@WebMethod
	public boolean remove(long idLob) {
		// TODO Auto-generated method stub
		return false;
	}
}
