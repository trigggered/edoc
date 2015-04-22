package lob.storage.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the LOB_VERSION database table.
 * 
 */
@Entity
@Table(name="LOB_VERSION" , schema ="LS")
public class LobVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOB_VERSION_IDVERSION_GENERATOR", sequenceName="SEQ_LOB_STORAGE", schema ="LS",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOB_VERSION_IDVERSION_GENERATOR")
	@Column(name="ID_VERSION")
	private long idVersion;

	@Column(name="ID_ATTRIBUTE")
	private long idAttribute;

	@Temporal(TemporalType.DATE)
	@Column(name="VERSION_DATE")
	private Date versionDate;

	@Column(name="VERSION_NUM")
	private String versionNum;

	//bi-directional many-to-one association to LobVersion
	@ManyToOne
	@JoinColumn(name="ID_PREV_VERSION")
	private LobVersion lobVersion;

	//bi-directional many-to-one association to LobVersion
	@OneToMany(mappedBy="lobVersion")
	private List<LobVersion> lobVersions;

	public LobVersion() {
	}

	public long getIdVersion() {
		return this.idVersion;
	}

	public void setIdVersion(long idVersion) {
		this.idVersion = idVersion;
	}

	public long getIdAttribute() {
		return this.idAttribute;
	}

	public void setIdAttribute(long idAttribute) {
		this.idAttribute = idAttribute;
	}

	public Date getVersionDate() {
		return this.versionDate;
	}

	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}

	public String getVersionNum() {
		return this.versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public LobVersion getLobVersion() {
		return this.lobVersion;
	}

	public void setLobVersion(LobVersion lobVersion) {
		this.lobVersion = lobVersion;
	}

	public List<LobVersion> getLobVersions() {
		return this.lobVersions;
	}

	public void setLobVersions(List<LobVersion> lobVersions) {
		this.lobVersions = lobVersions;
	}

	public LobVersion addLobVersion(LobVersion lobVersion) {
		getLobVersions().add(lobVersion);
		lobVersion.setLobVersion(this);

		return lobVersion;
	}

	public LobVersion removeLobVersion(LobVersion lobVersion) {
		getLobVersions().remove(lobVersion);
		lobVersion.setLobVersion(null);

		return lobVersion;
	}

}