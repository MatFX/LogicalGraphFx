package eu.matfx.gui.component.impl;

import eu.matfx.gui.component.AUIElement;
import eu.matfx.gui.helper.Coordinate;
import eu.matfx.gui.interfaces.UILineInputConnector;
import eu.matfx.gui.interfaces.UILineOutputConnector;
import eu.matfx.logic.data.impl.CircleLineConnector;
import eu.matfx.logic.helper.DimensionView;
import eu.matfx.logic.helper.LocationView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class UICircleLineConnector extends AUIElement<CircleLineConnector> implements UILineOutputConnector, UILineInputConnector
{
	
	private Circle circle;
	
	/**
	 * uiLineConnector; when component is moving give coords to the connector.
	 */
	private UILineConnector uiLineInputConnector;
	
	protected ChangeListener<Number> changeListenerInputX, changeListenerInputY;
	
	/**
	 * uiLineConnector; when component is moving give coords to the connector.
	 */
	protected UILineConnector uiLineOutputConnector;
	
	protected ChangeListener<Number> changeListenerOutputX, changeListenerOutputY;
	
	private Coordinate centerCoordinate = new Coordinate();
	
	private DropShadow ds;

	public UICircleLineConnector(CircleLineConnector logicElement) 
	{
		super(logicElement);
	
		circle = new Circle();
		//TODO radius
		circle.setRadius(10);
		circle.setFill(Color.BURLYWOOD);
		
		ds = new DropShadow();
		ds.setOffsetY(0.1f);
		ds.setColor(Color.web("#304f30"));
		circle.setEffect(ds);
		
		this.getChildren().add(circle);
	}

	@Override
	public boolean isInputArea(Point2D point) {
		//no allocation from user possible
		return false;
	}

	@Override
	public boolean isOutputArea(Point2D point) {
		//no allocation from user possible
		return false;
	}

	@Override
	public boolean isSecondInputArea(Point2D point) {
		//no allocation from user possible
		return false;
	}

	@Override
	public Point2D getInputCenterPoint() {
		return new Point2D(this.getLayoutX(), this.getLayoutY());
	}

	@Override
	public Point2D getOutputCenterPoint() {
		return new Point2D(this.getLayoutX(), this.getLayoutY());
	}

	@Override
	public Coordinate getInputCenterCoordinate() {
		return centerCoordinate;
	}

	@Override
	public Coordinate getOutputCenterCoordinate() {
		return centerCoordinate;
	}

	@Override
	public Coordinate getSecondInputCenterCoordinate() {
		return centerCoordinate;
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
		centerCoordinate.setX(this.getTranslateX());
		centerCoordinate.setY(this.getTranslateY());
	}

	@Override
	public Coordinate getCenterCoordinate() {
		return centerCoordinate;
	}

	@Override
	public void setUIOutputConnector(UILineConnector uiLineConnector) 
	{
		if(uiLineConnector != null)
		{	
			this.uiLineOutputConnector = uiLineConnector;
			
			uiLineOutputConnector.setOutputX(this.getTranslateX());
			uiLineOutputConnector.setOutputY(this.getTranslateY());
			
			changeListenerOutputX = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineOutputConnector.setOutputX(newValue.doubleValue());
					
				}
				
			};
			
			this.translateXProperty().addListener(changeListenerOutputX);
			changeListenerOutputY = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineOutputConnector.setOutputY(newValue.doubleValue());
				}
			};
			this.translateYProperty().addListener(changeListenerOutputY);
		}
	}

	@Override
	public boolean isUIOutputOccupied() {
		//is on screen then alway occupied
		return true;
	}

	@Override
	public void removeUIOutputConnector() {
		if(changeListenerOutputY != null)
			this.translateYProperty().removeListener(changeListenerOutputY);
		if(changeListenerOutputX != null)
			this.translateXProperty().removeListener(changeListenerOutputX);
		this.uiLineOutputConnector = null;
	}

	@Override
	public void changeCollectionColor() 
	{
		if(this.isCollected)
			ds.setColor(Color.web("#ff3333"));
		else
			ds.setColor(Color.web("#304f30"));
	}

	@Override
	public void saveVariables() 
	{
		this.getLogicElement().setLocationView(new LocationView(this.getTranslateX(), this.getTranslateY()));
		//TODO w und h?
		this.getLogicElement().setDimensionView(new DimensionView(w, h));
	}

	@Override
	public void setUIInputConnector(UILineConnector uiLineConnector) 
	{
		if(uiLineConnector != null)
		{	
			this.uiLineInputConnector = uiLineConnector;
			

			uiLineInputConnector.setInputX(this.getTranslateX());
			uiLineInputConnector.setInputY(this.getTranslateY());
			
			changeListenerInputX = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineInputConnector.setInputX(newValue.doubleValue());
				}
			};
			
			this.translateXProperty().addListener(changeListenerInputX);
			changeListenerInputY = new ChangeListener<Number>(){

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					uiLineInputConnector.setInputY(newValue.doubleValue());
				}
			};
			this.translateYProperty().addListener(changeListenerInputY);
		}
		
	}

	@Override
	public boolean isUIInputOccupied() {
		//is on screen then alway occupied
		return true;
	}

	@Override
	public void removeUIInputConnector() 
	{
		if(changeListenerInputX != null)
			this.translateXProperty().removeListener(changeListenerInputX);
		if(changeListenerInputY != null)
			this.translateYProperty().removeListener(changeListenerInputY);
		this.uiLineInputConnector = null;
	
	}
	
	public UILineConnector getUILineOutputConnector()
	{
		return uiLineOutputConnector;
	}
	
	public UILineConnector getUILineInputConnector()
	{
		return uiLineInputConnector;
	}
	
	public void setSelected(boolean isSelected) 
	{
		super.setSelected(isSelected);
		if(this.isSelected || this.isCollected)
			ds.setColor(Color.web("#ff3333"));
		else if(!this.isCollected)
			ds.setColor(Color.web("#304f30"));
	}

}
