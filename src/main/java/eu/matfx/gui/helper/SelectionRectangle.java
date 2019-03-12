package eu.matfx.gui.helper;

import javafx.scene.shape.Rectangle;

public class SelectionRectangle extends Rectangle
{
	private double start_x;
	
	private double start_y;

	public SelectionRectangle(double start_x, double start_y)
	{
		this.start_x = start_x;
		this.start_y = start_y;
		this.setLayoutX(start_x);
		this.setLayoutY(start_y);
	
	}

	public double getStartX() 
	{
		return start_x;
	}
	
	public double getStartY()
	{
		return start_y;
	}

}
