package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import util.OwlApi;

public class OwlApiTest {

    private static OWLUtil owlTest = new OWLUtil();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testShouldAddAxiom() throws OWLOntologyCreationException, OWLOntologyStorageException{
		owlTest.shouldAddAxiom();
		
	}
	//@Test
	public void testShouldLoad() throws OWLOntologyCreationException{
		owlTest.shouldLoad();
	}
	@Test
	public void testShouldLoadAndSave() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException{
		owlTest.shouldLoadAndSave();
	}
	@Test
	public void testparseOntology() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException{
		owlTest.parseOntology();
	}
	
}
