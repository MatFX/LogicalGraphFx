package eu.matfx.gui.component;

import eu.matfx.gui.component.impl.UILineConnector;
import eu.matfx.gui.component.parts.CircleComponent;
import eu.matfx.gui.helper.Coordinate;
import eu.matfx.gui.interfaces.UILineInputConnector;
import eu.matfx.logic.data.ALogicElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//extends from abstract output
public abstract class AUIInputOutputElement<T extends ALogicElement> extends AUIOutputElement<T> implements UILineInputConnector
{
	private CircleComponent circleLeft;
	
	/**
	 * uiLineConnector; when component is moving give coords to the connector.
	 */
	private UILineConnector uiLineInputConnector;
	
	protected ChangeListener<Number> changeListenerInputX, changeListenerInputY;
	
	
	

	protected AUIInputOutputElement(T logicElement) {
		super(logicElement);
		
//		Rectangle canvas = new Rectangle();
//		canvas.setWidth(150);
//		canvas.setHeight(150);
//		canvas.setArcHeight(15);
//		canvas.setArcWidth(15);
//		canvas.setFill(Color.web("#74aa7400"));
//		ds = new DropShadow();
//		ds.setOffsetY(0.1f);
//		ds.setColor(Color.web("#304f30"));
		
		//circleRight = new CircleComponent(5, 140, 25, Color.web("#304f30"));

	  	circleLeft = new CircleComponent(5, 10, 25, Color.web("#304f30"));
		
//		r = new Rectangle();
//		r.setX(10);
//	    r.setY(10);
//	    r.setWidth(130);
//	    r.setHeight(130);
//	    r.setArcHeight(15);
//		r.setArcWidth(15);
//	    r.setFill(Color.web("#74aa74"));
//	  	r.setEffect(ds);
	  	
	    //this.getChildren().addAll(canvas, r, circleRight, circleLeft);
	    this.getChildren().addAll(circleLeft);
	}
	
	@Override
	public boolean isInputArea(Point2D point) {
		return circleLeft.isPointInArea(point);
	}

	@Override
	public boolean isOutputArea(Point2D point) {
		return circleRight.isPointInArea(point);
	}

	@Override
	public Point2D getInputCenterPoint() {
		return circleLeft.getCenterPointFromArea();
	}

	@Override
	public Point2D getOutputCenterPoint() {
		return circleRight.getCenterPointFromArea();
	}

	@Override
	public Coordinate getInputCenterCoordinate() {
		return circleLeft.getCenterCoordinate();
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
	public void recalcualteCenterPoint() {
		circleLeft.recalcualteCenterPoint();
		circleRight.recalcualteCenterPoint();
		
	}

	@Override
	public Coordinate getCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setUIInputConnector(UILineConnector uiLineConnector) {
		
		
		if(uiLineConnector != null)
		{	
			this.uiLineInputConnector = uiLineConnector;
			

			uiLineInputConnector.setInputX(circleLeft.getCenterCoordinate().getX());
			uiLineInputConnector.setInputY(circleLeft.getCenterCoordinate().getY());
			
			changeListenerInputX = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineInputConnector.setInputX(newValue.doubleValue());
					
				}
				
			};
			
			
			
			
			circleLeft.getCenterCoordinate().getX_Property().addListener(changeListenerInputX);
			
			
			changeListenerInputY = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineInputConnector.setInputY(newValue.doubleValue());
					
				}
				
			};
			
			circleLeft.getCenterCoordinate().getY_Property().addListener(changeListenerInputY);
		}
		else
		{
			//TODO listener as member variable to delete them
			//circleRight.getCenterCoordinate().getX_Property().dele
			
		}
		
		
		//this.uiLineOutputConnector = uiLineConnector;
		
	}

	public boolean isUIInputOccupied()
	{
		if(uiLineInputConnector != null)
			return true;
		else
			return false;
	}
	
	public void removeUIInputConnector()
	{
		circleLeft.getCenterCoordinate().getX_Property().removeListener(changeListenerInputX);
		circleLeft.getCenterCoordinate().getY_Property().removeListener(changeListenerInputY);
		uiLineInputConnector = null;
	}


}
