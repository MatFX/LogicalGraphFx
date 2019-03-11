package eu.matfx.gui.helper;

import javafx.scene.shape.Line;

/**
 * extended line needed when user draw a input/ouput connection
 * @author m.goerlich
 *
 */
public class TempLine extends Line
{
	private int outputIndex;
	
	private int inputIndex;
	
	public TempLine(int startIndexFromWorkflowMap)
	{
		this.outputIndex = startIndexFromWorkflowMap;
	}
	
	public int getOutputIndex()
	{
		return outputIndex;
	}
	
	public int getInputIndex()
	{
		return inputIndex;
	}
	
	public void setInputIndex(int inputIndex)
	{
		this.inputIndex = inputIndex;
	}

}
