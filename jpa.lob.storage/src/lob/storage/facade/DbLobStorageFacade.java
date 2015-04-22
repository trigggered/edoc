/**
 * 
 */
package lob.storage.facade;

import java.util.logging.Logger;

import javax.ejb.Stateless;

import lob.storage.model.ILobStorage;
import lob.storage.model.LobAttribute;
import lob.storage.model.LobDbStorage;

/**
 * @author azhuk
 * Creation date: Aug 30, 2013
 *
 */
@Stateless
public class DbLobStorageFacade extends ModelManagerHelper implements ILobStorageFacade {
	
	/**
	 * @param entityClass
	 */
	
	
	private static final Logger _logger = Logger
			.getLogger(DbLobStorageFacade.class.getCanonicalName());
	


	/* (non-Javadoc)
	 * @see lob.storage.facade.ILobStorage#setLob(byte[])
	 */
	@Override
	public ILobStorage createLob(LobAttribute attribute, byte[] value, String hash) {
		_logger.info("Try write byte[] to strorage");
		ILobStorage lob	=  new LobDbStorage();
		lob.setData(value);
		lob.setHash(hash);	
		lob.setLobAttribute(attribute);
		return lob;
	}
	

	
	/* (non-Javadoc)
	 * @see lob.storage.facade.ILobStorageFacade#getLobData(long)
	 */
	@Override
	public LobData getLobData(long id) {
		_logger.info("Read Resourced by path:"+id);		
		
		LobData  toReturn = null;
		
		LobAttribute attribute = getEntityManager().find(LobAttribute.class, id);
		
		if(attribute!=null) {
			
			toReturn = new LobData();											
			toReturn.setLobAttributeData(attribute);
			
			for ( LobDbStorage attachment :  attribute.getAttachments() ) {
				toReturn.setLobData(attachment);			
			}
			
		}	 
		
	
		return toReturn;	
	}
}
