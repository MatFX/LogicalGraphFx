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
	
	private int subIndexInput = 0;
	
	private int subIndexOutput = 0;
	
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

	public int getSubIndexInput() {
		return subIndexInput;
	}

	public int getSubIndexOutput() {
		return subIndexOutput;
	}

	public void setSubIndexInput(int subIndexInput) {
		this.subIndexInput = subIndexInput;
	}

	public void setSubIndexOutput(int subIndexOutput) {
		this.subIndexOutput = subIndexOutput;
	}
}
