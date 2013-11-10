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
	public int getOrder() {
		return order;
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
	public void setOrder(int order) {
		this.order = order;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	private String iri;
	private int level;
	private int order;
	private Long branchId;
	
}
