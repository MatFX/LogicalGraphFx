package eu.matfx.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import eu.matfx.logic.data.ALogicElement;


/**
 * Scheme is the template and representing the graphical instance
 * <br>The scheme contains the complete definied workflow.
 * @author m.goerlich
 *
 */
@XmlRootElement(name = "Scheme")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"id", "descriptionName", "workflowList"})
public class Scheme implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5282704414480106497L;
	
	/**
	 * eindeutige Bezeichnung für das Schema. Dieses wird dann bei dem zur Zeit aktivierten Schema hinterlegt.
	 */
	private int id;

	public static final int START_INDEX = 0;
	
	private String descriptionName = "";
	
	private List<ALogicElement> workflowList = new ArrayList<ALogicElement>();
	
	public void addElementAtList(ALogicElement logicElement)
	{
		//searching for the next free id
		logicElement.setIndex(getNextFreeIndexFromMap());
		workflowList.add(logicElement);
	}
	
	public ALogicElement getElementWithId(int indexFromElementContainer)
	{
		for(int i = 0; i < workflowList.size(); i++)
		{
			if(workflowList.get(i).getIndex() == indexFromElementContainer)
				return workflowList.get(i);
		}
		return null;
	}
	
	private int getNextFreeIndexFromMap() 
	{
		int rueckgabe = START_INDEX;
		
		Collections.sort(workflowList, new Comparator<ALogicElement>(){

			@Override
			public int compare(ALogicElement o1, ALogicElement o2) 
			{
				Integer i1 = o1.getIndex();
				Integer i2 = o2.getIndex();
				return i1.compareTo(i2);
			}
		});
		
		for(int i = 0;  i < workflowList.size(); i++)
		{
			if(workflowList.get(i).getIndex() != rueckgabe)
				return rueckgabe;
			else
				rueckgabe++;
		}
		return rueckgabe;
	}

	public void removeElementAtMap(ALogicElement aLogicElement)
	{
		workflowList.remove(aLogicElement);
	}

	public void deleteElementMap(int indexFromList)
	{
		workflowList.remove(indexFromList);
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
		
		for(int i = 0; i < workflowList.size(); i++)
		{
			
			if(workflowList.get(i).getIndex() == logicElement.getIndex())
			{
				return i;
			}
		}
		System.out.println("logicElement NotFound " + logicElement.getIndex());
		return Integer.MIN_VALUE;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public List<ALogicElement> getSortedList() {

		Collections.sort(workflowList, new Comparator<ALogicElement>(){

			@Override
			public int compare(ALogicElement o1, ALogicElement o2) 
			{
				Integer i1 = o1.getIndex();
				Integer i2 = o2.getIndex();
				return i1.compareTo(i2);
			}
		});
		return workflowList;
	}
	

}
