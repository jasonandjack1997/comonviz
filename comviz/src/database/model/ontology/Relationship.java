package database.model.ontology;

import java.sql.Timestamp;



public class Relationship{
	
	private String discription;

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}
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
