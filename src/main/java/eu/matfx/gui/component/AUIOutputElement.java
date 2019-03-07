package eu.matfx.gui.component;

import eu.matfx.gui.component.impl.UILineConnector;
import eu.matfx.gui.component.parts.CircleComponent;
import eu.matfx.gui.helper.Coordinate;
import eu.matfx.gui.interfaces.UILineOutputConnector;
import eu.matfx.logic.data.ALogicElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class AUIOutputElement<T extends ALogicElement> extends AUIElement<T> implements UILineOutputConnector
{
	private CircleComponent circleRight;
	
	/**
	 * uiLineConnector; when component is moving give coords to the connector.
	 */
	private UILineConnector uiLineOutputConnector;

	protected AUIOutputElement(T logicElement)
	{
		super(logicElement);
		
		Rectangle canvas = new Rectangle();
		canvas.setWidth(150);
		canvas.setHeight(150);
		canvas.setArcHeight(15);
		canvas.setArcWidth(15);
		//TODO later with css style sheet
		canvas.setFill(Color.web("#74aa7400"));
		DropShadow ds = new DropShadow();
		ds.setOffsetY(0.1f);
		ds.setColor(Color.web("#304f30"));
		//canvas.setEffect(ds);
		
		
		circleRight = new CircleComponent(5, 140, 25, Color.web("#304f30"));
		
		Rectangle r = new Rectangle();
		r.setX(10);
	    r.setY(10);
	    r.setWidth(130);
	    r.setHeight(130);
	    r.setArcHeight(15);
		r.setArcWidth(15);
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
		System.out.println(" " + circleRight.getCenterCoordinate());
		
		
		
		
		
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
			//TODO i dont know	
			this.uiLineOutputConnector = uiLineConnector;
			
			uiLineConnector.setOutputX(circleRight.getCenterCoordinate().getX());
			uiLineConnector.setOutputY(circleRight.getCenterCoordinate().getY());
			circleRight.getCenterCoordinate().getX_Property().addListener(new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineConnector.setOutputX(newValue.doubleValue());
					
				}
				
			});
			
			circleRight.getCenterCoordinate().getY_Property().addListener(new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineConnector.setOutputY(newValue.doubleValue());
				}
			});
			
			//init of the outputconnector
			
		}
		else
		{
			//TODO listener as member variable to delete them
			//circleRight.getCenterCoordinate().getX_Property().dele
			
		}
		
		
		
		
		
	}


}
