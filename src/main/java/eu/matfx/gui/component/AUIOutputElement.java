package eu.matfx.gui.component;

import eu.matfx.gui.component.impl.UILineConnector;
import eu.matfx.gui.component.parts.CircleComponent;
import eu.matfx.gui.helper.Coordinate;
import eu.matfx.gui.interfaces.UILineOutputConnector;
import eu.matfx.logic.data.ALogicElement;
import eu.matfx.logic.helper.DimensionView;
import eu.matfx.logic.helper.LocationView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class AUIOutputElement<T extends ALogicElement> extends AUIElement<T> implements UILineOutputConnector
{
	protected CircleComponent circleRight;
	
	/**
	 * uiLineConnector; when component is moving give coords to the connector.
	 */
	protected UILineConnector uiLineOutputConnector;
	
	protected ChangeListener<Number> changeListenerOutputX, changeListenerOutputY;
	
	protected Rectangle r;
	

	private DropShadow ds;

	protected AUIOutputElement(T logicElement)
	{
		super(logicElement);
		
		Rectangle canvas = new Rectangle();
		
		canvas.setWidth(w);
		canvas.setHeight(h);
		canvas.setArcHeight(h * 0.1);
		canvas.setArcWidth(w * 0.1);
		//TODO later with css style sheet
		canvas.setFill(Color.web("#74aa7400"));
		ds = new DropShadow();
		ds.setOffsetY(0.1f);
		ds.setColor(Color.web("#304f30"));
			
		circleRight = new CircleComponent(w * 0.033, w - (w * 0.066), h * 0.166, Color.web("#304f30"));
		
		r = new Rectangle();
		r.setX(w * 0.066);
	    r.setY(h * 0.066);
	    r.setWidth(w * 0.868);
	    r.setHeight(h * 0.868);
	    r.setArcHeight(h * 0.1);
		r.setArcWidth(w * 0.1);
	    r.setFill(Color.web("#74aa74"));
	  	r.setEffect(ds);
	    this.getChildren().addAll(canvas, r, circleRight);
	
	}
	
	@Override
	public boolean isInputArea(Point2D point) {
		//gibt es hier nicht
		return false;
	}

	@Override
	public boolean isOutputArea(Point2D point) {
		return circleRight.isPointInArea(point);
	}

	@Override
	public Point2D getInputCenterPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point2D getOutputCenterPoint() {
		return circleRight.getCenterPointFromArea();
	}
		

	@Override
	public Coordinate getInputCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate getOutputCenterCoordinate() {
		return circleRight.getCenterCoordinate();
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
	public void recalcualteCenterPoint() 
	{
		circleRight.recalcualteCenterPoint();
	}

	@Override
	public Coordinate getCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void setUIOutputConnector(UILineConnector uiLineConnector) 
	{
		if(uiLineConnector != null)
		{	
			this.uiLineOutputConnector = uiLineConnector;
			
			uiLineOutputConnector.setOutputX(circleRight.getCenterCoordinate().getX());
			uiLineOutputConnector.setOutputY(circleRight.getCenterCoordinate().getY());
			
			changeListenerOutputX = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineOutputConnector.setOutputX(newValue.doubleValue());
					
				}
				
			};
			
			
			
			circleRight.getCenterCoordinate().getX_Property().addListener(changeListenerOutputX);
			
			
			changeListenerOutputY = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineOutputConnector.setOutputY(newValue.doubleValue());
				}
			};
			
			
			
			circleRight.getCenterCoordinate().getY_Property().addListener(changeListenerOutputY);
		}
	}
	

	@Override
	public void changeCollectionColor() {
		if(this.isCollected)
			ds.setColor(Color.web("#ff3333"));
		else
			ds.setColor(Color.web("#304f30"));
	}


	@Override
	public boolean isUIOutputOccupied() 
	{
		if(uiLineOutputConnector != null)
			return true;
		else
			return false;
	}

	@Override
	public void removeUIOutputConnector() 
	{
		circleRight.getCenterCoordinate().getX_Property().removeListener(changeListenerOutputX);
		circleRight.getCenterCoordinate().getY_Property().removeListener(changeListenerOutputY);
		uiLineOutputConnector = null;
	}
	

	public void setSelected(boolean isSelected) 
	{
		super.setSelected(isSelected);
		if(this.isSelected || this.isCollected)
			ds.setColor(Color.web("#ff3333"));
		else if(!this.isCollected)
			ds.setColor(Color.web("#304f30"));
	}
	

	@Override
	public void saveVariables() 
	{
		this.getLogicElement().setLocationView(new LocationView(this.getTranslateX(), this.getTranslateY()));
		this.getLogicElement().setDimensionView(new DimensionView(w, h));
	}
	

	@Override
	public boolean isSecondInputArea(Point2D point) {
		//not int this component
		return false;
	}

	@Override
	public Coordinate getSecondInputCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isUIOutputOccupied(int withIndex) 
	{
		//prüfung nur wenn auch belegt ist
		if(isUIOutputOccupied())
		{
			if(uiLineOutputConnector.getLogicElement().getIndex() == withIndex)
			{
				return true;
			}
		}
		return false;
	}

	
	
}
