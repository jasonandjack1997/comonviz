package comonviz;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
