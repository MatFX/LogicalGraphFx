package eu.matfx.logic.data.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import eu.matfx.gui.helper.GenericPair;
import eu.matfx.logic.data.ALogicElement;

/**
 * This object connect two different LogicElements. At the ui it will be represented with a line.
 * @author m.goerlich
 *
 */
@XmlType(name = "LineConnector")
@XmlAccessorType(XmlAccessType.FIELD)
public class LineConnector extends ALogicElement
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4985338600265951913L;

	/**
	 * left side identification number of node and right ident number of output channel (normally 0)
	 */
	private GenericPair<Integer, Integer> outputIndex;

	private GenericPair<Integer, Integer> inputIndex;

	public boolean isMasterIdOutput(int index) 
	{
		if(outputIndex != null && outputIndex.getLeft().intValue() == index)
			return true;
		return false;
	}

	public boolean isMasterIdInput(int index) 
	{
		if(inputIndex != null && inputIndex.getLeft().intValue() == index)
			return true;
		return false;
	}

	public boolean isOutputEmpty() 
	{
		if(outputIndex == null)
			return true;
		return false;
	}

	public boolean isInputEmpty() {
		if(inputIndex == null)
			return true;
		return false;
	}

	public void deleteIdOutput() {
		outputIndex = null;
	}

	public void deleteIdInput() {
		inputIndex = null;
	}

	public void setMasteridOutput(int newIndexNext) {
		outputIndex.setLeft(newIndexNext);
	}

	public void setMasteridInput(int newIndexNext) {
		inputIndex.setLeft(newIndexNext);
	}
	
	
}
