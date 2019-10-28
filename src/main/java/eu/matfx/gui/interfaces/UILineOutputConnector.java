package eu.matfx.gui.interfaces;

import eu.matfx.gui.component.impl.UILineConnector;

public interface UILineOutputConnector 
{
	public void setUIOutputConnector(UILineConnector uiLineConnector);
	
	public boolean isUIOutputOccupied();
	
	public void removeUIOutputConnector();
	
	public boolean isUIOutputOccupied(int withIndex);

}
