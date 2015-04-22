package lob.storage.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the LOB_ATTRIBUTE database table.
 * 
 */
@Entity
@Table(name="LOB_ATTRIBUTE", schema ="LS")
public class LobAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@SequenceGenerator(name="LOB_ATTRIBUTE_IDATTRIBUTE_GENERATOR", sequenceName="SEQ_LOB_STORAGE", schema ="LS",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="LOB_ATTRIBUTE_IDATTRIBUTE_GENERATOR")	
	
	@Column(name="ID_ATTRIBUTE")
	private long idAttribute;

	@Column(name="NAME_AUTHOR")
	private String nameAuthor;

	@Column(name="NAME_LOB")
	private String nameLob;

	//bi-directional many-to-one association to LobContentType
	
	@Column(name="ID_CONTENT_TYPE")
	private long idContentType;

	//bi-directional many-to-one association to LobStorageType
	@ManyToOne
	@JoinColumn(name="ID_STORE_TYPE")
	private LobStorageType lobStorageType;


	@OneToMany(mappedBy="lobAttribute")
	private List<LobDbStorage> _attachments; 
	
	public LobAttribute() {
	}

	
	public List<LobDbStorage>  getAttachments() {
		return _attachments;
	}
	
	public void addAttachments(LobDbStorage attachment) {
		attachment.setLobAttribute(this);
		_attachments.add(attachment);
	}
	
	public long getIdAttribute() {
		return this.idAttribute;
	}

	public void setIdAttribute(long idAttribute) {
		this.idAttribute = idAttribute;
	}

	public String getNameAuthor() {
		return this.nameAuthor;
	}

	public void setNameAuthor(String nameAuthor) {
		this.nameAuthor = nameAuthor;
	}

	public String getNameLob() {
		return this.nameLob;
	}

	public void setNameLob(String nameLob) {
		this.nameLob = nameLob;
	}

	public long getLobContentType() {
		return this.idContentType;
	}

	public void setLobContentType(long value) {
		this.idContentType = value;
	}

	public LobStorageType getLobStorageType() {
		return this.lobStorageType;
	}

	public void setLobStorageType(LobStorageType lobStorageType) {
		this.lobStorageType = lobStorageType;
	}		

}