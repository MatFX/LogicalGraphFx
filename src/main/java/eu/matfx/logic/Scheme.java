package eu.matfx.logic;

import java.io.Serializable;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


import eu.matfx.logic.data.ALogicElement;
import eu.matfx.logic.data.impl.LineConnector;

import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Scheme is the template and representing the graphical instance
 * <br>The scheme contains the complete definied workflow.
 * @author m.goerlich
 *
 */
@XmlRootElement(name = "Scheme")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"descriptionName", "workflowMap"})
public class Scheme implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5282704414480106497L;

	public static final int START_INDEX = 0;
	
	private String descriptionName = "";
	
	/**
	 * describes the complete workflow of an event. The first element is ever at index 0 (START_INDEX)
	 * <br>The indices are continuously without a numerical gap.
	 */
	private TreeMap<Integer, ALogicElement> workflowMap = new TreeMap<Integer, ALogicElement>();
	
	public void addElementAtMap(ALogicElement logicElement)
	{
		logicElement.setIndex(workflowMap.size());
		workflowMap.put(logicElement.getIndex(), logicElement);
	}

	public void deleteElementMap(int index)
	{
		//TODO after delete consistency check?
		workflowMap.remove(index);
		
		//after remove the map set the indizes new
		workflowMap = restructureMap(workflowMap);
	}
	
	
	
	private TreeMap<Integer, ALogicElement> restructureMap(TreeMap<Integer, ALogicElement> restructMap) 
	{
		TreeMap<Integer, ALogicElement> newMap = new TreeMap<Integer, ALogicElement>();
		
		int startIndex = 0; 
		
		for(Entry<Integer, ALogicElement> entry : restructMap.entrySet())
		{
			//startindex ist different from the logic index
			if(startIndex != entry.getValue().getIndex())
			{
				int oldIndex = entry.getValue().getIndex();
				//check the line element, if line element has the index in input or output it must be changed
				checkLineElementsInOutIndex(oldIndex, startIndex);
				//set new index
				entry.getValue().setIndex(startIndex);
			}
			newMap.put(startIndex, entry.getValue());
			startIndex++;
		}
		return newMap;
	}

	private void checkLineElementsInOutIndex(int oldIndex, int newIndex)
	{
		for(Entry<Integer, ALogicElement> entry : workflowMap.entrySet())
		{
			if(entry.getValue() instanceof LineConnector)
			{
				
				LineConnector lineConnector = (LineConnector) entry.getValue();
				if(!lineConnector.isOutputEmpty() && lineConnector.isMasterIdOutput(oldIndex))
				{
					lineConnector.setMasteridOutput(newIndex);
				}
				
				if(!lineConnector.isInputEmpty() && lineConnector.isMasterIdInput(oldIndex))
				{
					lineConnector.setMasteridInput(newIndex);
				}
			}
		}
	}

	/**
	 * with this method the logicElement will be set on a specified index. If the position
	 * <br>taken all elements will be moved. 
	 * @param indexToSet
	 * @param logicElement
	 */
	public void putElementAtMap(int indexToSet, ALogicElement logicElement)
	{
	
		if(workflowMap.get(indexToSet) != null)
		{
			//move action
			int newIndex = indexToSet + 1;
			ALogicElement valueToMove = workflowMap.get(indexToSet);
			valueToMove.setIndex(newIndex);
			
			//TODO line new 
			
			//set the param from method
			workflowMap.put(indexToSet, logicElement);
			//hop to the new index with the old value
			this.putElementAtMap(newIndex, valueToMove);
		}
		else
		{
			workflowMap.put(indexToSet, logicElement);
		}
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

	public SortedMap<Integer, ALogicElement> getWorkflowMap() 
	{
		return workflowMap;
	}

	public String getDescriptionName() {
		return descriptionName;
	}

	public void setDescriptionName(String descriptionName) {
		this.descriptionName = descriptionName;
	}
	
	
	public String toString()
	{
		return this.getDescriptionName();
	}
	
	public int getIndexFromLogicElement(ALogicElement logicElement)
	{
		for(Entry<Integer, ALogicElement> entry : workflowMap.entrySet())
		{
			if(entry.getValue() == logicElement)
				return entry.getKey();
		}
		return Integer.MIN_VALUE;
	}
	

}
