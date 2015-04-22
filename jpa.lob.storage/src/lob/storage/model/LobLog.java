package lob.storage.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LOB_LOG database table.
 * 
 */
@Entity
@Table(name="LOB_LOG", schema ="LS")
public class LobLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOB_LOG_IDLOG_GENERATOR", sequenceName="SEQ_LOB_STORAGE", schema ="LS", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOB_LOG_IDLOG_GENERATOR")
	@Column(name="ID_LOG")
	private long idLog;

	@Column(name="\"ACTION\"")
	private String action;

	@Temporal(TemporalType.DATE)
	@Column(name="CHANGE_DATE")
	private Date changeDate;

	@Column(name="ID_ATTRIBUTE")
	private BigDecimal idAttribute;

	public LobLog() {
	}

	public long getIdLog() {
		return this.idLog;
	}

	public void setIdLog(long idLog) {
		this.idLog = idLog;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public BigDecimal getIdAttribute() {
		return this.idAttribute;
	}

	public void setIdAttribute(BigDecimal idAttribute) {
		this.idAttribute = idAttribute;
	}

}