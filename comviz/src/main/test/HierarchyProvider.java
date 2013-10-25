package main.test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import main.test.extractor.ChildClassExtractor;
import main.test.extractor.ParentClassExtractor;

import org.protege.owlapi.inference.orphan.Relation;
import org.protege.owlapi.inference.orphan.TerminalElementFinder;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

public class HierarchyProvider{
	
    private ChildClassExtractor childClassExtractor;
    private ParentClassExtractor parentClassExtractor;
    private volatile OWLClass root;
    private TerminalElementFinder<OWLClass> rootFinder;
    public static final OWLClass OWL_THING = new OWLClassImpl(OWLRDFVocabulary.OWL_THING.getIRI());
   
    private OWLOntology ontology;
    
    public HierarchyProvider(OWLOntology ont){
    	this.ontology = ont;
    	if(root == null){
    		root = OWL_THING;
    	}
        rootFinder = new TerminalElementFinder<OWLClass>(new Relation<OWLClass>() {
            public Collection<OWLClass> getR(OWLClass cls) {
                Collection<OWLClass> parents = getParents(cls);
                parents.remove(root);
                return parents;
            }
        });
        parentClassExtractor = new ParentClassExtractor();
        childClassExtractor = new ChildClassExtractor();

    }

    public Set<OWLClass> getParents(OWLClass object) {
    	try {
    		// If the object is thing then there are no
    		// parents
    		if (object.equals(root)) {
    			return Collections.emptySet();
    		}
    		Set<OWLClass> result = new HashSet<OWLClass>();
    		// Thing if the object is a root class
    		if (rootFinder.getTerminalElements().contains(object)) {
    			result.add(root);
    		}
    		// Not a root, so must have another parent
    		parentClassExtractor.reset();
    		parentClassExtractor.setCurrentClass(object);
    			for (OWLAxiom ax : ontology.getAxioms(object)) {
    				ax.accept(parentClassExtractor);
    			}
    		result.addAll(parentClassExtractor.getResult());
    		return result;
    	}
    	finally {
    	}
    }

    public Set<OWLClass> getChildren(OWLClass object) {
    	try {
    		Set<OWLClass> result;
    		if (object.equals(root)) {
    			result = new HashSet<OWLClass>();
    			result.addAll(rootFinder.getTerminalElements());
    			result.addAll(extractChildren(object));
    			result.remove(object);
    		}
    		else {
    			result = extractChildren(object);
    			for (Iterator<OWLClass> it = result.iterator(); it.hasNext();) {
    				OWLClass curChild = it.next();
    				if (getAncestors(object).contains(curChild)) {
    					it.remove();
    				}
    			}
    		}

    		return result;
    	}
    	finally {
    	}
    }

    private Set<OWLClass> extractChildren(OWLClass parent) {
        childClassExtractor.setCurrentParentClass(parent);
        for (OWLAxiom ax : ontology.getReferencingAxioms(parent)) {
            if (ax.isLogicalAxiom()) {
                ax.accept(childClassExtractor);
            }
        }
        return childClassExtractor.getResult();
    }
    
    public Set<OWLClass> getAncestors(OWLClass object) {
    	try {
    		Set<OWLClass> results = new HashSet<OWLClass>();
    		getAncestors(results, object);
    		return results;
    	}
    	finally {
    	}
    }


    private void getAncestors(Set<OWLClass> results, OWLClass object) {
        for (OWLClass parent : getParents(object)) {
            if (!results.contains(parent)) {
                results.add(parent);
                getAncestors(results, parent);
            }
        }
    }



}
