package au.uq.dke.comonviz.model;

import database.model.ontology.OntologyClass;
import database.model.ontology.OntologyRelationship;

public class DatabaseModelListener {
	
	public void databaseCleared() {
	}
	
	public void databaseRelationshipAdded(OntologyRelationship relationship) {
	}

	public void databaseRelationshipRemoved(OntologyRelationship relationship) {
	}

	public void databaseClassAdded(OntologyClass cls) {
	}

	public void databaseClassRemoved(OntologyClass cls) {
	}

	
	public void databaseAxiomAdded(Object axiom) {
	}
	
	public void databaseAxiomRemoved(Object axiom) {
	}
}
