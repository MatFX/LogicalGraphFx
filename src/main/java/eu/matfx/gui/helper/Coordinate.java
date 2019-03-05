package eu.matfx.gui.helper;

import java.text.MessageFormat;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * 
 * Helper class for the x and y coordinate; they are bounding on a component
 * @author m.goerlich
 *
 */
public class Coordinate 
{

	private DoubleProperty x = new SimpleDoubleProperty(0D);
	private DoubleProperty y = new SimpleDoubleProperty(0D);
	
	public Coordinate()
	{
		
	}
	

	public DoubleProperty getX_Property() {
		return x;
	}

	public DoubleProperty getY_Property() {
		return y;
	}
	
	public double getX()
	{
		return x.get();
	}
	
	public double getY()
	{
		return y.get();
	}
	
	public void setX(double xValue)
	{
		x.set(xValue);
	}

	public void setY(double yValue)
	{
		y.set(yValue);
	}
	
	public String toString()
	{
		return MessageFormat.format("Coordinate x: {0} y: {1}", new Object[]{this.getX(), this.getY()});
	}

	

}