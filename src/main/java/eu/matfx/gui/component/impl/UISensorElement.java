package eu.matfx.gui.component.impl;


import eu.matfx.gui.component.AUIOutputElement;
import eu.matfx.logic.data.impl.SensorElement;


public class UISensorElement extends AUIOutputElement<SensorElement>
{

	public UISensorElement(SensorElement logicElement) 
	{
		super(logicElement);
	}

	public void setGroupedMovementX(double changedRectangleStartX)
	{ 
		this.setTranslateX(changedRectangleStartX + movementCoords.getX());
		this.recalcualteCenterPoint();
	}
	
	public void setGroupedMovementY(double changedRectangleStartY)
	{
		this.setTranslateY(changedRectangleStartY + movementCoords.getY());
		this.recalcualteCenterPoint();
	}
	


}
