/**
 * 
 */
package lob.storage.model;

/**
 * @author azhuk
 * Creation date: Sep 5, 2013
 *
 */
public interface ILobStorage {

	public abstract void setLobAttribute(LobAttribute attribute);		
	
	public abstract long getIdLob();

	public abstract void setIdLob(long idLob);

	public abstract byte[] getData();

	public abstract void setData(byte[] data);	
	
	public abstract String getHash();
	
	public abstract void setHash(String value);
	
	public String getSign();

}