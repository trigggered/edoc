package lob.storage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the LOB_CONTENT_TYPE database table.
 * 
 */
@Entity
@Table(name="LOB_CONTENT_TYPE" , schema ="LS")
public class LobContentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOB_CONTENT_TYPE_IDCONTENTTYPE_GENERATOR", sequenceName="SEQ_LOB_STORAGE", schema ="LS")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOB_CONTENT_TYPE_IDCONTENTTYPE_GENERATOR")
	@Column(name="ID_CONTENT_TYPE")
	private long idContentType;

	@Column(name="MASK_CONTENT")
	private String maskContent;

	@Column(name="NAME_CONTENT")
	private String nameContent;



	public LobContentType() {
	}

	public long getIdContentType() {
		return this.idContentType;
	}

	public void setIdContentType(long idContentType) {
		this.idContentType = idContentType;
	}

	public String getMaskContent() {
		return this.maskContent;
	}

	public void setMaskContent(String maskContent) {
		this.maskContent = maskContent;
	}

	public String getNameContent() {
		return this.nameContent;
	}

	public void setNameContent(String nameContent) {
		this.nameContent = nameContent;
	}

}