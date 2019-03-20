package eu.matfx.gui.interfaces;

import eu.matfx.gui.component.impl.UILineConnector;

/**
 * when component has two input channel, than implement this interface
 * @author m.goerlich
 *
 */
public interface UILineSecondInputConnector extends UILineInputConnector
{
	public void setUISecondInputConnector(UILineConnector uiLineConnector);

	public boolean isUISecondInputOccupied();
	
	public void removeUISecondInputConnector();
}
