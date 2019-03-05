package eu.matfx.gui.component.impl;

import eu.matfx.gui.component.AUIElement;
import eu.matfx.gui.helper.Coordinate;
import eu.matfx.logic.data.impl.LineConnector;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class UILineConnector extends AUIElement<LineConnector>
{
	
	private DoubleProperty outX = new SimpleDoubleProperty(5), outY = new SimpleDoubleProperty(5);
	
	private DoubleProperty inX = new SimpleDoubleProperty(10), inY = new SimpleDoubleProperty(10);
	
	

	public UILineConnector(LineConnector logicElement) 
	{
		super(logicElement);
		//TODO take the coords from logicElement
		
		Line line = new Line();
		line.setStrokeWidth(2);
		line.setStroke(Color.BLUE);
	
		outX.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				line.setStartX(newValue.doubleValue());
				
			}
			
		});
		
		outY.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				line.setStartY(newValue.doubleValue());
				
			}
			
		});
		
		inX.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				line.setEndX(newValue.doubleValue());
				
			}
			
		});
		
		inY.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				line.setEndY(newValue.doubleValue());
			}
			
		});
		
		
		this.getChildren().add(line);
		
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

	public void setOutputX(double outX) 
	{
		
		this.outX.set(outX);
		
	}

	public void setOutputY(double outY) {
		this.outY.set(outY);
		
	}

	public void setInputX(double inX) {
		System.out.println("inX ist " + inX);
		this.inX.set(inX);
		
	}
	
	public void setInputY(double inY) {
		this.inY.set(inY);
		
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


}
