package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import au.uq.dke.comonviz.model.AnnotationManager;

public class AnnotationManagerTest {

	AnnotationManager am;
	@Before
	public void setUp() throws Exception {
		
		am = new AnnotationManager();
	}

	@Test
	public void testGetStylizedAnnotation() {
		String result = am.getStylizedAnnotation("relation... part of ,.. type of");
		return;
	}

}
