package test;

import org.protege.editor.owl.model.hierarchy.AssertedClassHierarchyProvider;
import org.protege.owlapi.apibinding.ProtegeOWLManager;
import org.protege.owlapi.model.ProtegeOWLOntologyManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class ProviderTest {
	
	ProtegeOWLOntologyManager protegemngr = ProtegeOWLManager.createOWLOntologyManager();

	
	OWLOntologyManager oom = OWLManager.createOWLOntologyManager();
	AssertedClassHierarchyProvider assertProvider = new AssertedClassHierarchyProvider(protegemngr);
	
	public static void main(String args[]){
		new ProviderTest().assertProvider.getParents(null);
		return;
	}
	

}
