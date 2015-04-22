/**
 * 
 */
package lob.storage.facade;

import java.util.logging.Logger;

import lob.storage.model.ILobStorage;
import lob.storage.model.LobAttribute;

/**
 * @author azhuk
 * Creation date: Dec 18, 2014
 *
 */
public class LobData {
	private static final Logger _logger = Logger.getLogger(LobData.class
			.getCanonicalName());
	
	ILobStorage lobData;	
	LobAttribute lobAttributeData;
	
	/**
	 * @param lobData the lobData to set
	 */
	public void setLobData(ILobStorage lobData) {
		this.lobData = lobData;
	}

	/**
	 * @param lobAttributeData the lobAttributeData to set
	 */
	public void setLobAttributeData(LobAttribute lobAttributeData) {
		this.lobAttributeData = lobAttributeData;
	}

	
	
	
	
	/**
	 * @return the lobData
	 */
	public ILobStorage getLobData() {
		return lobData;
	}

	/**
	 * @return the lobAttributeData
	 */
	public LobAttribute getLobAttributeData() {
		return lobAttributeData;
	}


	

}
