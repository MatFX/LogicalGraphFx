package eu.matfx.logic;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import eu.matfx.logic.data.ALogicElement;

/**
 * Scheme is the template and representing the graphical instance
 * <br>The scheme contains the complete definied workflow.
 * @author m.goerlich
 *
 */
public class Scheme {
	
	public static final int START_INDEX = 0;
	
	/**
	 * describes the complete workflow of an event. The first element is ever at index 0 (START_INDEX)
	 * <br>The indices are continuously without a numerical gap.
	 */
	private SortedMap<Integer, ALogicElement> workflowMap = new TreeMap<Integer, ALogicElement>();
	
	public void addElementAtMap(ALogicElement logicElement)
	{
		workflowMap.put(workflowMap.size(), logicElement);
	}

	public void deleteElementMap(int index)
	{
		workflowMap.remove(index);
	}
	
	/**
	 * check the map; only validated maps will be produce legal results 
	 * @return
	 */
	public boolean isWorkflowMapUseable()
	{
		if(workflowMap == null || workflowMap.size() <= 0)
			return false;
		
		int index = 0;
		for(Entry<Integer, ALogicElement> entry : workflowMap.entrySet())
		{
			//TODO more checks at the values
			if(entry.getKey() != index)
				return false;
			
			index++;
		}
		return true;
	}
	
	

}
