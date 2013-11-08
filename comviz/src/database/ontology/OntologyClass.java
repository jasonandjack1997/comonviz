package database.ontology;

import java.sql.Timestamp;

import database.Trackable;
import database.TrackableDatabaseEntity;

public class OntologyClass  extends TrackableDatabaseEntity{
	public String getIri() {
		return iri;
	}
	public int getLevel() {
		return level;
	}
	public int getSiblingRank() {
		return siblingRank;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setIri(String iri) {
		this.iri = iri;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setSiblingRank(int order) {
		this.siblingRank = order;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	private String iri;
	private int level = -1;
	private int siblingRank;
	private Long branchId;
	
}
