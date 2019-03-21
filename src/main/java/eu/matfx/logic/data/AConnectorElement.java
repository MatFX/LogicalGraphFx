package eu.matfx.logic.data;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import eu.matfx.gui.helper.GenericPair;
import eu.matfx.logic.helper.GenericPairAdapter;

/**
 * a connector element combine two different other elements.
 * @author m.goerlich
 *
 */
public abstract class AConnectorElement extends ALogicElement
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8609730388460042248L;

	/**
	 * left side identification number of node and right ident number of output channel (normally 0)
	 */
	@XmlJavaTypeAdapter(GenericPairAdapter.class)
	private GenericPair<Integer, Integer> outputIndex  = new GenericPair<Integer, Integer>(Integer.MIN_VALUE, 0);

	@XmlJavaTypeAdapter(GenericPairAdapter.class)
	private GenericPair<Integer, Integer> inputIndex  = new GenericPair<Integer, Integer>(Integer.MIN_VALUE, 0);

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
		if(outputIndex == null || outputIndex.getLeft() == Integer.MIN_VALUE)
			return true;
		return false;
	}

	public boolean isInputEmpty() {
		if(inputIndex == null || inputIndex.getLeft() == Integer.MIN_VALUE)
			return true;
		return false;
	}

	public void deleteIdOutput() {
		outputIndex = null;
	}

	public void deleteIdInput() {
		inputIndex = null;
	}

	public void setMasteridOutput(int newIndexNext) 
	{
		if(outputIndex == null)
			outputIndex = new GenericPair<Integer, Integer>(Integer.MIN_VALUE, 0);
		outputIndex.setLeft(newIndexNext);
	}

	public void setMasteridInput(int newIndexNext) 
	{
		if(inputIndex == null)
			inputIndex = new GenericPair<Integer, Integer>(Integer.MIN_VALUE, 0);
		inputIndex.setLeft(newIndexNext);
	}
	
	public void setMasteridInputWithSubindex(int newIndexNext, int subindex)
	{
		if(inputIndex == null)
			inputIndex = new GenericPair<Integer, Integer>(Integer.MIN_VALUE, 0);
		inputIndex.setLeft(newIndexNext);
		inputIndex.setRight(subindex);
	}
	
	
	public GenericPair<Integer, Integer> getOutputId()
	{
		return outputIndex;
	}
	
	public GenericPair<Integer, Integer> getInputId()
	{
		return inputIndex;
	}
	
}
