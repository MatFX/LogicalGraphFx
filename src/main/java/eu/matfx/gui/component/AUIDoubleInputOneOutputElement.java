package eu.matfx.gui.component;

import eu.matfx.gui.component.impl.UILineConnector;
import eu.matfx.gui.component.parts.CircleComponent;
import eu.matfx.gui.helper.Coordinate;
import eu.matfx.gui.interfaces.UILineSecondInputConnector;
import eu.matfx.logic.data.ALogicElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class AUIDoubleInputOneOutputElement<T extends ALogicElement> extends AUIInputOutputElement<T> implements UILineSecondInputConnector  
{
	/**
	 * second input circle
	 */
	private CircleComponent circleLeftSecond;
	
	/**
	 * uiLineConnector; when component is moving give coords to the connector.
	 */
	private UILineConnector ulineConnectorSecondInput;
	
	protected ChangeListener<Number> changeListenerSecondInputX, changeListenerSecondInputY;

	protected AUIDoubleInputOneOutputElement(T logicElement) 
	{
		super(logicElement);
		circleLeftSecond = new CircleComponent(w * 0.033, w * 0.066, h - (h * 0.166), Color.web("#304f30"));
	  	this.getChildren().addAll(circleLeftSecond);
	}
	
	@Override
	public void setUISecondInputConnector(UILineConnector uiLineConnector) 
	{
		if(uiLineConnector != null)
		{	
			this.ulineConnectorSecondInput = uiLineConnector;
			

			ulineConnectorSecondInput.setInputX(circleLeftSecond.getCenterCoordinate().getX());
			ulineConnectorSecondInput.setInputY(circleLeftSecond.getCenterCoordinate().getY());
			
			changeListenerSecondInputX = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					ulineConnectorSecondInput.setInputX(newValue.doubleValue());
					
				}
				
			};
		
			circleLeftSecond.getCenterCoordinate().getX_Property().addListener(changeListenerSecondInputX);
			changeListenerSecondInputY = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					ulineConnectorSecondInput.setInputY(newValue.doubleValue());
					
				}
				
			};
			
			circleLeftSecond.getCenterCoordinate().getY_Property().addListener(changeListenerSecondInputY);
			
			System.out.println("centerCoordinate " + circleLeftSecond.getCenterCoordinate());
		}
	}

	@Override
	public boolean isUISecondInputOccupied() 
	{
		if(ulineConnectorSecondInput != null)
			return true;
		else
			return false;
	}

	@Override
	public void removeUISecondInputConnector() 
	{
		circleLeftSecond.getCenterCoordinate().getX_Property().removeListener(changeListenerSecondInputX);
		circleLeftSecond.getCenterCoordinate().getY_Property().removeListener(changeListenerSecondInputY);
		ulineConnectorSecondInput = null;
	}
	
	@Override
	public boolean isSecondInputArea(Point2D point) {
		return circleLeftSecond.isPointInArea(point);
	}

	@Override
	public Coordinate getSecondInputCenterCoordinate() {
		return circleLeftSecond.getCenterCoordinate();
	}
	
	@Override
	public void recalcualteCenterPoint() 
	{
		super.recalcualteCenterPoint();
		circleLeftSecond.recalcualteCenterPoint();
	}
}
