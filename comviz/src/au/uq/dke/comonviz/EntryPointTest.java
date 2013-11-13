package au.uq.dke.comonviz;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EntryPointTest {
	EntryPoint entryPoint = new EntryPoint();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEntryPointInit() {
		entryPoint.start();
	}
	

}
