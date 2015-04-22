package lob.storage.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the LOB_STORAGE_TYPE database table.
 * 
 */
@Entity
@Table(name="LOB_STORAGE_TYPE", schema ="LS")
public class LobStorageType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOB_STORAGE_TYPE_IDSTORETYPE_GENERATOR", sequenceName="SEQ_LOB_STORAGE", schema ="LS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOB_STORAGE_TYPE_IDSTORETYPE_GENERATOR")
	@Column(name="ID_STORE_TYPE")
	private int idStoreType;

	@Column(name="NAME_STORE")
	private String nameStore;

	//bi-directional many-to-one association to LobAttribute
	@OneToMany(mappedBy="lobStorageType")
	private List<LobAttribute> lobAttributes;

	public LobStorageType() {
	}

	public long getIdStoreType() {
		return this.idStoreType;
	}

	public void setIdStoreType(int idStoreType) {
		this.idStoreType = idStoreType;
	}

	public String getNameStore() {
		return this.nameStore;
	}

	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}

	public List<LobAttribute> getLobAttributes() {
		return this.lobAttributes;
	}

	public void setLobAttributes(List<LobAttribute> lobAttributes) {
		this.lobAttributes = lobAttributes;
	}

	public LobAttribute addLobAttribute(LobAttribute lobAttribute) {
		getLobAttributes().add(lobAttribute);
		lobAttribute.setLobStorageType(this);

		return lobAttribute;
	}

	public LobAttribute removeLobAttribute(LobAttribute lobAttribute) {
		getLobAttributes().remove(lobAttribute);
		lobAttribute.setLobStorageType(null);

		return lobAttribute;
	}

}