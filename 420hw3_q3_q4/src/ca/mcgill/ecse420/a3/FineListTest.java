package ca.mcgill.ecse420.a3;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class FineListTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	@Test
	public void testContains() {
		FineList l = new FineList();
		l.add(1);
		l.add("a");
		
		assertTrue(l.contains(1));
		assertTrue(l.contains("a"));
		assertFalse(l.contains(5));		
		assertFalse(l.contains("b"));			
	}
	
}
