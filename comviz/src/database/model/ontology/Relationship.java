package database.model.ontology;

import java.sql.Timestamp;

import database.model.Trackable;

public class Relationship extends Trackable{
	
	private Long srcClassId;
	private Long dstClassId;
	private Long axiomId;
	private boolean isBidirection;
	public Long getSrcClassId() {
		return srcClassId;
	}
	public Long getDstClassId() {
		return dstClassId;
	}
	public Long getAxiomId() {
		return axiomId;
	}
	public boolean isBidirection() {
		return isBidirection;
	}
	public void setSrcClassId(Long srcClassId) {
		this.srcClassId = srcClassId;
	}
	public void setDstClassId(Long dstClassId) {
		this.dstClassId = dstClassId;
	}
	public void setAxiomId(Long axiomId) {
		this.axiomId = axiomId;
	}
	public void setBidirection(boolean isBidirection) {
		this.isBidirection = isBidirection;
	}

}
