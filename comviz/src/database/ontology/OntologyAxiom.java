package database.ontology;

import java.sql.Timestamp;

import database.Trackable;
import database.TrackableDatabaseEntity;

public class OntologyAxiom extends TrackableDatabaseEntity{
	private String iri;
	public String getIri() {
		return iri;
	}
	public void setIri(String iri) {
		this.iri = iri;
	}
	
}
