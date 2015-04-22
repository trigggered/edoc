package lob.storage.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the LOB_DB_STORAGE database table.
 * 
 */
@Entity
@Table(name="LOB_DB_STORAGE" , schema ="LS")
public class LobDbStorage implements Serializable, ILobStorage {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOB_DB_STORAGE_IDLOB_GENERATOR", sequenceName="SEQ_LOB_STORAGE", schema ="LS",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOB_DB_STORAGE_IDLOB_GENERATOR")
	@Column(name="ID_LOB")
	private long idLob;
	
	@Column(name="ID_ATTRIBUTE", insertable=false, updatable=false)
	private long idLobAttribute;

	@Lob
	@Column(name="\"DATA\"")
	private byte[] data;

	@Column(name="\"HASH_DATA\"")
	private String _hash;
	
	@Column(name="\"SIGN_DATA\"")
	private String _sign;	

	@Column(name="\"SIGN_ID\"")
	private Integer _signId;
	
	
	//bi-directional many-to-one association to LobAttribute
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_ATTRIBUTE")
	private LobAttribute lobAttribute;

	public LobDbStorage() {
	}

	@Override
	public long getIdLob() {
		return this.idLob;
	}

	@Override
	public void setIdLob(long idLob) {
		this.idLob = idLob;
	}

	@Override
	public byte[] getData() {
		return this.data;
	}

	@Override
	public void setData(byte[] data) {
		this.data = data;		
	}

		
	
	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#getHash()
	 */
	@Override
	public String getHash() {
		return _hash;
	}

	/* (non-Javadoc)
	 * @see lob.storage.model.ILobStorage#setHash(java.lang.String)
	 */
	@Override
	public void setHash(String value) {
		_hash = value;		
	}
	/**
	 * @return the _sign
	 */
	@Override
	public String getSign() {
		return _sign;
	}

	/**
	 * @return the _signId
	 */
	public Integer getSignId() {
		return _signId;
	}

	/**
	 * @param _sign the _sign to set
	 */
	public void setSign(String value) {
		_sign = value;
	}

	/**
	 * @param _signId the _signId to set
	 */
	public void setSignId(Integer value) {
		_signId = value;
	}

	public LobAttribute getLobAttribute() {	
		return lobAttribute;
	}
	
	@Override
	public void setLobAttribute(LobAttribute attribute) {
		lobAttribute = attribute;	
	}	
	

}