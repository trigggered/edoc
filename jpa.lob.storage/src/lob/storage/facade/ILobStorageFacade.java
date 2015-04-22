/**
 * 
 */
package lob.storage.facade;

import javax.ejb.Local;

import lob.storage.model.ILobStorage;
import lob.storage.model.LobAttribute;


/**
 * @author azhuk
 * Creation date: Aug 30, 2013
 *
 */
@Local
public interface ILobStorageFacade {	
	
	
	public LobData getLobData(long id);
	
	public ILobStorage  createLob (LobAttribute idAtribute, byte[] value,String hash);	
		

}
