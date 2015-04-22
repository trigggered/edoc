package lob.storage.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the LOB_FS_STORAGE database table.
 * 
 */
@Entity
@Table(name="LOB_FS_STORAGE", schema ="LS")
public class LobFsStorage implements Serializable, ILobStorage {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOB_FS_STORAGE_IDLOB_GENERATOR", sequenceName="SEQ_LOB_STORAGE", schema ="LS",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOB_FS_STORAGE_IDLOB_GENERATOR")
	@Column(name="ID_LOB")
	private long idLob;

	@Column(name="ID_ATTRIBUTE")
	private long idAttribute;

	@Column(name="LOB_PATH")
	private String lobPath;

		
	public LobFsStorage() {
	}

	public long getIdLob() {
		return this.idLob;
	}

	public void setIdLob(long idLob) {
		this.idLob = idLob;
	}
	

	public String getLobPath() {
		return this.lobPath;
	}

	public void setLobPath(String lobPath) {
		this.lobPath = lobPath;
	}

	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#getData()
	 */
	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#setData(byte[])
	 */
	@Override
	public void setData(byte[] data) {
		// TODO Auto-generated method stub		
	}
	

	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#getHash()
	 */
	@Override
	public String getHash() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#setHash(java.lang.String)
	 */
	@Override
	public void setHash(String value) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#getSign()
	 */
	@Override
	public String getSign() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#setLobAttribute(lob.storage.model.LobAttribute)
	 */
	@Override
	public void setLobAttribute(LobAttribute attribute) {
		// TODO Auto-generated method stub
		
	}

}