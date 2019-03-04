package eu.matfx.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.matfx.logic.data.impl.FunctionElement;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.data.impl.SensorElement;
import eu.matfx.logic.data.impl.container.AndContainer;
import eu.matfx.logic.data.impl.container.OrContainer;
import eu.matfx.logic.database.XMLAccess;

public class SchemaTest {

	@Test
	public void testSchemeMap()
	{
		Scheme scheme = new Scheme();
		scheme.setDescriptionName("Test-Schema");
		
		scheme.addElementAtMap(new SensorElement());
		
		assertTrue("Schememap filled at Index 0?", (scheme.getWorkflowMap().get(Scheme.START_INDEX)!= null));
		
		
		//add two Elements with a gap in map
		scheme.putElementAtMap(2, new AndContainer());
		scheme.putElementAtMap(3, new FunctionElement());
		assertTrue("Schememap is empty at Index 1?", (scheme.getWorkflowMap().get(1) == null));
		
		scheme.putElementAtMap(1, new LineConnector());
		assertTrue("Schememap is not filled at Index 1?", (scheme.getWorkflowMap().get(1) != null));
		
		//set at index 2 a new OrContainer the other elements muss be moved
		scheme.putElementAtMap(2, new OrContainer());
		
		//result of the movement 
		assertTrue("value at index 2 must be OrContainer", (scheme.getWorkflowMap().get(2) instanceof OrContainer));
		assertTrue("value at index 3 must be AndContainer", (scheme.getWorkflowMap().get(3) instanceof AndContainer));
		assertTrue("value at index 4 must be FunctionElement", (scheme.getWorkflowMap().get(4) instanceof FunctionElement));
	
	
		
		SchemeList schemeList = new SchemeList();
		
		schemeList.getSchemeList().add(scheme);
		
		schemeList.setActiveSchemeOnScreen(schemeList.getSchemeList().size()-1);
		System.out.println("Test " + schemeList.getSchemeList().get(0).getWorkflowMap());
		//write definied xml in file
		XMLAccess.writeObjectToFile(schemeList);
		
		//abfrage ob vorhanden?
		
		
		SchemeList readedSchemeList = (SchemeList) XMLAccess.readObjectFromFile(new SchemeList());
		
		assertTrue("readed scheme list must be 5 elements", (scheme.getWorkflowMap().size() == 5));
		
		
		
		
		
	}

}
