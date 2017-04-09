package tn.hich.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = Operation.TABLE)
@NamedQueries({
		@NamedQuery(name = Constants.C1, query = "SELECT e FROM Operation e WHERE e.check='1C' ORDER BY e.taskCard ASC"),
		@NamedQuery(name = Constants.C2, query = "SELECT e FROM Operation e WHERE e.check in ('1C','2C') ORDER BY e.taskCard ASC"),
		@NamedQuery(name = Constants.C3, query = "SELECT e FROM Operation e WHERE e.check in ('1C','3C') ORDER BY e.taskCard ASC"),
		@NamedQuery(name = Constants.C4, query = "SELECT e FROM Operation e WHERE e.check in ('1C','2C','4C') ORDER BY e.taskCard ASC"),
		@NamedQuery(name = Constants.C6, query = "SELECT e FROM Operation e WHERE e.check in ('1C','2C','3C') ORDER BY e.taskCard ASC"),
		@NamedQuery(name = Constants.C12, query = "SELECT e FROM Operation e WHERE e.check in ('1C','2C','3C','4C') ORDER BY e.taskCard ASC"),
		@NamedQuery(name = Constants._6YE, query = "SELECT e FROM Operation e WHERE e.check in ('1C','4C') ORDER BY e.taskCard ASC"),
		@NamedQuery(name = Constants.ALL, query = "SELECT e FROM Operation e ORDER BY e.taskCard ASC")})
public class Operation implements Serializable {
	
	public static final String TABLE="OPBDV_";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8803587237159606358L;
	
	public static final String IDENTIFIER = "identifier";
	public static final String TASK_CARD="taskCard";
	public static final String OPERATION="operation";
	public static final String ZONE="zone";
	public static final String SKILL="skill";
	public static final String DESCRIPTION="description";

	@Id
	@GeneratedValue
	private Long identifier;

	@Column(name = "OPERATION_")
	private String operation;

	@Column(name = "TASK_CARD_")
	private String taskCard;

	@Column(name = "DESCRIPTION_")
	private String description;

	@Column(name = "PHASE_")
	private String phase;

	@Column(name = "ATA_")
	private String ata;

	@Column(name = "ZONE_")
	private String zone;

	@Column(name = "SKILL_")
	private String skill;

	@Column(name = "CHECK_")
	private String check;

	@Column(name = "TYPE_OPERATION_")
	private String operationType;

	@Column(name = "T_CONS_")
	private String tcons;

	@Column(name = "T_COMP")
	private String tcomp;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getTaskCard() {
		return taskCard;
	}

	public void setTaskCard(String taskCard) {
		this.taskCard = taskCard;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getAta() {
		return ata;
	}

	public void setAta(String ata) {
		this.ata = ata;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getTcons() {
		return tcons;
	}

	public void setTcons(String tcons) {
		this.tcons = tcons;
	}

	public String getTcomp() {
		return tcomp;
	}

	public void setTcomp(String tcomp) {
		this.tcomp = tcomp;
	}

	public Long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

}
