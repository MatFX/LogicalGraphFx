package eu.matfx.logic;

import static org.junit.Assert.*;

import org.junit.Test;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.data.impl.SensorElement;
import eu.matfx.logic.data.impl.container.AndContainer;
import eu.matfx.logic.data.impl.container.RSFlipFlopContainer;
import eu.matfx.logic.database.SchemeDataStorage;
import eu.matfx.logic.database.XMLAccess;

public class SchemaTest {

	@Test
	public void testSchemeMap()
	{
		SchemeDataStorage.initSchemeDataStorage();
		
		
		Scheme scheme = new Scheme();
		scheme.setId(SchemeListContainer.calculatedNextFreeId());
		scheme.setDescriptionName("Test-Schema");
		
		SensorElement sensorElement = new SensorElement();
		scheme.addElementAtList(sensorElement);
		
		assertTrue("Schememap filled at Index 0?", (scheme.getElementWithId(Scheme.START_INDEX)!= null));
		
	
		AndContainer andContainer  = new AndContainer();
		scheme.addElementAtList(andContainer);
		
		assertTrue("Schememap is empty at Index 1?", (scheme.getElementWithId(andContainer.getIndex()) != null));
		
		LineConnector lineConnect = new LineConnector();
		lineConnect.setIndex(1);
		lineConnect.setMasteridOutput(sensorElement.getIndex());
		lineConnect.setMasteridInput(andContainer.getIndex());
		
		scheme.addElementAtList(lineConnect);
		assertTrue("Schememap is not filled at Index 1?", (scheme.getElementWithId(lineConnect.getIndex()) != null));
		
		RSFlipFlopContainer rsContainer = new RSFlipFlopContainer();
		scheme.addElementAtList(rsContainer);
		
		
		
		
		SchemeListContainer schemeList = new SchemeListContainer();
		
		schemeList.getSchemeList().add(scheme);
		schemeList.setActiveSchemeOnScreen(scheme.getId());
		
		//System.out.println("Test " + schemeList.getSchemeList().get(0).getWorkflowMap());
		//write definied xml in file
		XMLAccess.writeObjectToFile(schemeList);
		
		//abfrage ob vorhanden?
		
		
		SchemeListContainer readedSchemeList = (SchemeListContainer) XMLAccess.readObjectFromFile(new SchemeListContainer());
		int countedElements = scheme.getSortedList().size();
		assertTrue("readed scheme list must be " + countedElements + " elements", (scheme.getSortedList().size() == countedElements));
		
		/*
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
		
		
		SchemeListContainer schemeList = new SchemeListContainer();
		
		schemeList.getSchemeList().add(scheme);
		schemeList.setActiveSchemeOnScreen(scheme.getId());
		
		System.out.println("Test " + schemeList.getSchemeList().get(0).getWorkflowMap());
		//write definied xml in file
		XMLAccess.writeObjectToFile(schemeList);
		
		//abfrage ob vorhanden?
		
		
		SchemeListContainer readedSchemeList = (SchemeListContainer) XMLAccess.readObjectFromFile(new SchemeListContainer());
		
		assertTrue("readed scheme list must be " + countedElements + " elements", (scheme.getWorkflowMap().size() == countedElements));
		
		*/
		
		
		
	}

}
