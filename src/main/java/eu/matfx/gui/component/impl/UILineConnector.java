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
	
	private DoubleProperty outX = new SimpleDoubleProperty(5), outY = new SimpleDoubleProperty(5);
	
	private DoubleProperty inX = new SimpleDoubleProperty(10), inY = new SimpleDoubleProperty(10);
	
	private Line line;
	
	private DropShadow ds;



	public UILineConnector(LineConnector logicElement) 
	{
		super(logicElement);
		//TODO take the coords from logicElement
		
		line = new Line();
		line.setStrokeWidth(3);
		line.setStroke(Color.BLUE);
		
		//line.setFill(Color.web("#74aa7400"));
		ds = new DropShadow();
		ds.setOffsetY(0.1f);
		ds.setColor(Color.web("#0000e6AA"));
		line.setEffect(ds);
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
	
	@Override
	public void setSelected(boolean isSelected) 
	{
		super.setSelected(isSelected);
		if(this.isSelected)
		{
			line.setStroke(Color.CORAL);
			ds.setColor(Color.web("#992900AA"));
		}
		else
		{
			line.setStroke(Color.BLUE);
			ds.setColor(Color.web("#0000e6AA"));
		}
	}
	
	
	public static void main (String[] args)
	{
		
		Point2D mousePoint = new Point2D(4, 4);
		
		
		Point2D startPoint = new Point2D(2, 3);
		Point2D endPoint = new Point2D(5, 6);
		
		double m1 = (startPoint.getY() - endPoint.getY()) / (startPoint.getX() - endPoint.getX());
		System.out.println("m " + m1);
		
		double b = startPoint.getY() - (startPoint.getX() * m1);
		
		System.out.println("b " + b);
		
		double m2 = -1d / m1;
		System.out.println("m2 " + m2);
		
		//yO = m2 * x + b
		
		//mousePointY = m2 * mousePointX + b
		
		double bO = mousePoint.getY() - (m2 * mousePoint.getY());
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
		
		
		
		Point2D startPoint = new Point2D(outX.get(), outY.get());
		Point2D endPoint = new Point2D(inX.get(), inY.get());
		
		System.out.println("startPoint " +  startPoint.getX() + " " + startPoint.getY());
		System.out.println("endPoint " +    endPoint.getX() + " " + endPoint.getY());
		System.out.println("Mouse point " + mousePoint.getX() + " " + mousePoint.getY());
		
		//need result y = mx + b
		
		//p1 => outY = m * outX + b
		
		//p2 => inY = m * outY + b
		
		//b1 => outY - (m * outX) = b
		
		//b2 => inY - (m * inX) = b
		
		//m ==> (inY - outY) / (inX - OutX)
		
		double m1 = (startPoint.getY() - endPoint.getY()) / (startPoint.getX() - endPoint.getX());
		
		System.out.println("m1 " + m1);
		
		//b ==> outY - (outX * m)
		
		double b = startPoint.getY() - (startPoint.getX() * m1);
		
		/* Orthogonale Gerade */
		
		System.out.println("b " + b);
		//m1 * m2 = -1
		
		double m2 = -1d / m1;
		
		
		//yO = m2 * x + b
		
		//mousePointY = m2 * mousePointX + b
		
		double bO = mousePoint.getY() - (m2 * mousePoint.getY());
		
		//jetzt gleichsetzen mit Funktionsgleichung
		// m2*x + b0 = m1*x + b
		
		double x = (b - bO) / (m2 - m1);
		
		double y = m1 * x + b;
		
		System.out.println("Punkt auf gerade " + x + " " + y);
		
		//abstand zwischen den beiden punkten
		
		//double rangeBetweenPoints = Math.sqrt((y - mousePoint.getY()) + (x - mousePoint.getX()));
		
		double rangeBetweenPoints = Math.sqrt( Math.pow((mousePoint.getY() - y), 2) + Math.pow(( mousePoint.getX() - x), 2));
		
		
		System.out.println("rangeBetweenPoints " + rangeBetweenPoints);
		
		
		if(rangeBetweenPoints > 50)
			return false;
		
		
		
		
		
		// TODO Auto-generated method stub
		return true;
	}


}
