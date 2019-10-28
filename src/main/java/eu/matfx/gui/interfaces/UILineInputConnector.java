package eu.matfx.gui.interfaces;

import eu.matfx.gui.component.impl.UILineConnector;

public interface UILineInputConnector 
{
	
	public void setUIInputConnector(UILineConnector uiLineConnector);

	public boolean isUIInputOccupied();
	
	public void removeUIInputConnector();
	
	public boolean isUIInputOccupied(int withIndex);
}
