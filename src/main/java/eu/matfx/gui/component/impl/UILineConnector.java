package eu.matfx.gui.component.impl;

import eu.matfx.gui.component.AUIElement;
import eu.matfx.gui.helper.Coordinate;
import eu.matfx.logic.data.impl.LineConnector;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class UILineConnector extends AUIElement<LineConnector>
{
	/**
	 * connected with the output channel, start coordinate 
	 */
	private DoubleProperty outX = new SimpleDoubleProperty(5), outY = new SimpleDoubleProperty(5);
	
	/**
	 * connected with the input channel, end coordinate
	 */
	private DoubleProperty inX = new SimpleDoubleProperty(10), inY = new SimpleDoubleProperty(10);
	
	/**
	 * always on screen when connection established
	 */
	private Line masterLine;
	
	private DropShadow ds;

	private boolean isDeletedDesignated = false;


	public UILineConnector(LineConnector logicElement) 
	{
		super(logicElement);
		//TODO take the coords from logicElement
		
		masterLine = new Line();
		masterLine.setStrokeWidth(3);
		masterLine.setStroke(Color.BLUE);
		
		//line.setFill(Color.web("#74aa7400"));
		ds = new DropShadow();
		ds.setOffsetY(0.1f);
		ds.setColor(Color.web("#0000e6AA"));
		masterLine.setEffect(ds);
		
		
		//TODO change?
		outX.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				masterLine.setStartX(newValue.doubleValue());
				
			}
			
		});
		
		outY.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				masterLine.setStartY(newValue.doubleValue());
				
			}
			
		});
		
		inX.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				masterLine.setEndX(newValue.doubleValue());
				
			}
			
		});
		
		inY.addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				masterLine.setEndY(newValue.doubleValue());
			}
			
		});
		
		
		this.getChildren().add(masterLine);
		
	}

	@Override
	public boolean isInputArea(Point2D point) {
		//not needed
		return false;
	}

	@Override
	public boolean isOutputArea(Point2D point) {
		//not needed
		return false;
	}



	@Override
	public Coordinate getInputCenterCoordinate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate getOutputCenterCoordinate() {
		//not needed
		return null;
	}

	@Override
	public boolean isPointInArea(Point2D point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point2D getCenterPointFromArea() {
		//not needed
		return null;
	}

	@Override
	public void recalcualteCenterPoint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Coordinate getCenterCoordinate() {
		//not needed
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
		this.inX.set(inX);
		
	}
	
	public void setInputY(double inY) {
		this.inY.set(inY);
		
	}

	@Override
	public Point2D getInputCenterPoint() {
		//not needed
		return null;
	}

	@Override
	public Point2D getOutputCenterPoint() {
		//not needed
		return null;
	}
	
	@Override
	public boolean isSecondInputArea(Point2D point) {
		//not needed
		return false;
	}

	@Override
	public Coordinate getSecondInputCenterCoordinate() {
		//not needed
		return null;
	}

	
	@Override
	public void setSelected(boolean isSelected) 
	{
		super.setSelected(isSelected);
		if(this.isSelected)
		{
			masterLine.setStroke(Color.CORAL);
			ds.setColor(Color.web("#992900AA"));
		}
		else
		{
			masterLine.setStroke(Color.BLUE);
			ds.setColor(Color.web("#0000e6AA"));
		}
	}
	
	
	public static void main (String[] args)
	{
		//
		//Point2D mousePoint = new Point2D(4, 4);
		//Point2D startPoint = new Point2D(2, 3);
		//Point2D endPoint = new Point2D(5, 6);
		
		
		Point2D mousePoint = new Point2D(199, 95);
		
		Point2D startPoint = new Point2D(140, 126);
		Point2D endPoint = new Point2D(265, 61);
		
		double m1 = (startPoint.getY() - endPoint.getY()) / (startPoint.getX() - endPoint.getX());
		System.out.println("m " + m1);
		
		double b = startPoint.getY() - (startPoint.getX() * m1);
		
		System.out.println("b " + b);
		
		double m2 = -1d / m1;
		System.out.println("m2 " + m2);
		
		//yO = m2 * x + b
		
		//mousePointY = m2 * mousePointX + b
		
		double bO = mousePoint.getY() - (m2 * mousePoint.getX());
		System.out.println("bO " + bO);
		
		//jetzt gleichsetzen mit Funktionsgleichung
		// m2*x + b0 = m1*x + b
		
		double x = (b - bO) / (m2 - m1) ;
		
		double y = m1 * x + b;
		
		System.out.println("Punkt auf gerade " + x + " " + y);
				
		//abstand zwischen den beiden punkten
		
		//TODO scheint anscheinend noch falsch zu sein irgendwie kommt da meiner Meinung nach ein murks raus
		double rangeBetweenPoints = Math.sqrt( Math.pow((mousePoint.getY() - y), 2) + Math.pow(( mousePoint.getX() - x), 2));
		System.out.println("rangeBetweenPoints " + rangeBetweenPoints);
		
		
	}

	/**
	 * mouse pointer check in which area the point is
	 * @param point2d
	 * @return
	 */
	public boolean isOuterTolerance(Point2D mousePoint) 
	{
		
		Point2D orthPoint = getOrthogonalPointOnLine(mousePoint);
		double rangeBetweenPoints = Math.sqrt( Math.pow((mousePoint.getY() - orthPoint.getY()), 2) + Math.pow(( mousePoint.getX() - orthPoint.getX()), 2));
		if(rangeBetweenPoints > 10)
			return true;
		return false;
	}

	public void setDeleteColor() {
		masterLine.setStroke(Color.RED);
		ds.setColor(Color.web("#990000AA"));
		isDeletedDesignated = true;
	}
	
	public boolean isDeletedDesignated()
	{
		return this.isDeletedDesignated;
	}

	public void removeDeleteColor() {
		masterLine.setStroke(Color.CORAL);
		ds.setColor(Color.web("#992900AA"));
		isDeletedDesignated = false;
	}
	
	@Override
	public void collected(boolean isCollected)
	{
		//lines need no collect flag; they have the obserable connector to other uiElements
	}

	@Override
	public void changeCollectionColor() {
		//not to use
	}

	@Override
	public void saveVariables()
	{
		// a line need not a save.
	}


	public Point2D getOrthogonalPointOnLine(Point2D mousePoint) 
	{
		//need the end and start of the line and build a fucntion
		Point2D startPoint = new Point2D(outX.get(), outY.get());
		Point2D endPoint = new Point2D(inX.get(), inY.get());
		
		//need result y = mx + b
		
		//p1 => outY = m * outX + b
		
		//p2 => inY = m * outY + b
		
		//b1 => outY - (m * outX) = b
		
		//b2 => inY - (m * inX) = b
		
		//m ==> (inY - outY) / (inX - OutX)
		
		double m1 = (startPoint.getY() - endPoint.getY()) / (startPoint.getX() - endPoint.getX());
		
		//System.out.println("m1 " + m1);
		
		//b ==> outY - (outX * m)
		
		double b = startPoint.getY() - (startPoint.getX() * m1);
		
		/* Orthogonale Gerade */
		
		//System.out.println("b " + b);
		//m1 * m2 = -1
		
		double m2 = -1d / m1;
		
		
		//yO = m2 * x + b
		
		//mousePointY = m2 * mousePointX + b
		
		double bO = mousePoint.getY() - (m2 * mousePoint.getX());
		
		//jetzt gleichsetzen mit Funktionsgleichung
		// m2*x + b0 = m1*x + b
		
		double x = (b - bO) / (m2 - m1);
		
		double y = m1 * x + b;
		
		return new Point2D(x, y);
	}

}
