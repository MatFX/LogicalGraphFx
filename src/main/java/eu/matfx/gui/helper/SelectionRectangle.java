package eu.matfx.gui.helper;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class SelectionRectangle extends Rectangle
{
	private double start_x;
	
	private double start_y;
	
	private boolean catchedObjects = false;

	private Point2D movementCoords;
	
	private DoubleProperty groupedMovement_X = new SimpleDoubleProperty();
	
	private DoubleProperty groupedMovement_Y = new SimpleDoubleProperty();
	
	public SelectionRectangle(double start_x, double start_y)
	{
		this.start_x = start_x;
		this.start_y = start_y;
		this.setLayoutX(start_x);
		this.setLayoutY(start_y);
		//mouse transparency needed to get the click from the canvas.
		this.setMouseTransparent(true);
	
	}

	public double getStartX() 
	{
		return start_x;
	}
	
	public double getStartY()
	{
		return start_y;
	}

	public void setCatchedUIElements(boolean catchedObjects)
	{
		this.catchedObjects = catchedObjects;
	}
	
	public boolean isCatchedUIElements()
	{
		return this.catchedObjects;
	}

	public void setStartCoordsMovement(Point2D transferCoord) 
	{
		//calculate the range between startx/y and transferCoord
		this.movementCoords = new Point2D(transferCoord.getX() - this.getLayoutX(), transferCoord.getY() - this.getLayoutY());
	}
	
	/* TODO raus
	public Point2D getMovementStartCoords()
	{
		return movementCoords;
	}
	*/
	
	public void setMovementRectangle(Point2D transferCoord, double maxW, double maxH)
	{
		double newX = transferCoord.getX() - movementCoords.getX();
		double newY = transferCoord.getY() - movementCoords.getY();
		if(newX < 0)
			newX = 0;
		
		if(newY < 0)
			newY = 0;
		
		if(newX + this.getWidth() > maxW)
			newX = maxW - this.getWidth();
	
		if(newY + this.getHeight() > maxH)
			newY = maxH - this.getHeight();
		

		//needed for the components
		groupedMovement_X.set(newX);
		groupedMovement_Y.set(newY);
		
		this.setLayoutX(newX);
		this.setLayoutY(newY);
		
	
	}
	
	public GenericPair<DoubleProperty, DoubleProperty> getGroupedMovementProperties()
	{
		return new GenericPair<DoubleProperty, DoubleProperty>(groupedMovement_X, groupedMovement_Y);
		
	}

	public Point2D getMovementStartCoords() 
	{
		return new Point2D(this.getLayoutX(), this.getLayoutY());
	}

}
