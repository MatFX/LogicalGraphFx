package eu.matfx.gui.component;

import eu.matfx.gui.helper.Coordinate;
import eu.matfx.logic.data.ALogicElement;
import javafx.geometry.Point2D;

public abstract class AUIDoubleInputOneOutputElement<T extends ALogicElement> extends AUIElement<T> 
{

	protected AUIDoubleInputOneOutputElement(T logicElement) {
		super(logicElement);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isInputArea(Point2D point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOutputArea(Point2D point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point2D getInputCenterPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point2D getOutputCenterPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate getInputCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate getOutputCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPointInArea(Point2D point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point2D getCenterPointFromArea() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recalcualteCenterPoint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Coordinate getCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}


}
