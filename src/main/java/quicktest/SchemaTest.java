package quicktest;

import static org.junit.Assert.assertTrue;

import eu.matfx.logic.Scheme;
import eu.matfx.logic.SchemeList;
import eu.matfx.logic.data.impl.FunctionElement;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.data.impl.SensorElement;
import eu.matfx.logic.data.impl.container.AndContainer;
import eu.matfx.logic.data.impl.container.OrContainer;
import eu.matfx.logic.database.XMLAccess;

public class SchemaTest {

	
	public static void main(String[] args)
	{

		Scheme scheme = new Scheme();
		scheme.setDescriptionName("Test-Schema");
		
		scheme.addElementAtMap(new SensorElement());
		
		
		
		AndContainer andContainer  = new AndContainer();
		andContainer.setIndex(2);
		
		FunctionElement functionElement = new FunctionElement();
		functionElement.setIndex(3);
		//add two Elements with a gap in map
		scheme.putElementAtMap(andContainer.getIndex(), andContainer);
		scheme.putElementAtMap(functionElement.getIndex(), functionElement);
		
		
		LineConnector lineConnect = new LineConnector();
		lineConnect.setIndex(1);
		lineConnect.setMasteridOutput(0);
		lineConnect.setMasteridInput(2);
		
		scheme.putElementAtMap(lineConnect.getIndex(), lineConnect);
		
		//set at index 2 a new OrContainer the other elements muss be moved
		OrContainer orContainer = new OrContainer();
		orContainer.setIndex(2);
		scheme.putElementAtMap(orContainer.getIndex(),  orContainer);
		
		
		
		SchemeList schemeList = new SchemeList();
		
		schemeList.getSchemeList().add(scheme);
		
		schemeList.setActiveSchemeOnScreen(schemeList.getSchemeList().size()-1);
		//write definied xml in file
		XMLAccess.writeObjectToFile(schemeList);
		
		//abfrage ob vorhanden?
		
		
		SchemeList readedSchemeList = (SchemeList) XMLAccess.readObjectFromFile(new SchemeList());
		
		
		
	}
}
