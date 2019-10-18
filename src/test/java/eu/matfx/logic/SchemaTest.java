package eu.matfx.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.matfx.logic.data.impl.FunctionElement;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.data.impl.SensorElement;
import eu.matfx.logic.data.impl.container.AndContainer;
import eu.matfx.logic.data.impl.container.OrContainer;
import eu.matfx.logic.data.impl.container.RSFlipFlopContainer;
import eu.matfx.logic.database.SchemeDataStorage;
import eu.matfx.logic.database.XMLAccess;

public class SchemaTest {

	@Test
	public void testSchemeMap()
	{
		SchemeDataStorage.initSchemeDataStorage();
		
		
		Scheme scheme = new Scheme();
		scheme.setId(SchemeList.calculatedNextFreeId());
		scheme.setDescriptionName("Test-Schema");
		
		scheme.addElementAtMap(new SensorElement());
		
		assertTrue("Schememap filled at Index 0?", (scheme.getWorkflowMap().get(Scheme.START_INDEX)!= null));
		
		
		AndContainer andContainer  = new AndContainer();
		andContainer.setIndex(2);
		
		FunctionElement functionElement = new FunctionElement();
		functionElement.setIndex(3);
		//add two Elements with a gap in map
		scheme.putElementAtMap(andContainer.getIndex(), andContainer);
		scheme.putElementAtMap(functionElement.getIndex(), functionElement);
		
		assertTrue("Schememap is empty at Index 1?", (scheme.getWorkflowMap().get(1) == null));
		
		LineConnector lineConnect = new LineConnector();
		lineConnect.setIndex(1);
		lineConnect.setMasteridOutput(0);
		lineConnect.setMasteridInput(2);
		
		scheme.putElementAtMap(lineConnect.getIndex(), lineConnect);
		assertTrue("Schememap is not filled at Index 1?", (scheme.getWorkflowMap().get(1) != null));
		
		//set at index 2 a new OrContainer the other elements muss be moved
		OrContainer orContainer = new OrContainer();
		orContainer.setIndex(2);
		scheme.putElementAtMap(orContainer.getIndex(),  orContainer);
		
		//result of the movement 
		assertTrue("value at index 2 must be OrContainer", (scheme.getWorkflowMap().get(2) instanceof OrContainer));
		assertTrue("value at index 3 must be AndContainer", (scheme.getWorkflowMap().get(3) instanceof AndContainer));
		assertTrue("value at index 4 must be FunctionElement", (scheme.getWorkflowMap().get(4) instanceof FunctionElement));
	
		
		RSFlipFlopContainer rsContainer = new RSFlipFlopContainer();
		scheme.addElementAtMap(rsContainer);
	
		int countedElements = scheme.getWorkflowMap().size();
	
		
		SchemeList schemeList = new SchemeList();
		
		schemeList.getSchemeList().add(scheme);
		schemeList.setActiveSchemeOnScreen(scheme.getId());
		
		System.out.println("Test " + schemeList.getSchemeList().get(0).getWorkflowMap());
		//write definied xml in file
		XMLAccess.writeObjectToFile(schemeList);
		
		//abfrage ob vorhanden?
		
		
		SchemeList readedSchemeList = (SchemeList) XMLAccess.readObjectFromFile(new SchemeList());
		
		assertTrue("readed scheme list must be " + countedElements + " elements", (scheme.getWorkflowMap().size() == countedElements));
		
		
		
		
		
	}

}
