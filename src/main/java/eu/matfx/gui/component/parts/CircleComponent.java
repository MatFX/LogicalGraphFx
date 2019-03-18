package eu.matfx.gui.component.parts;



import eu.matfx.gui.helper.Coordinate;
import eu.matfx.gui.interfaces.IArea;
import eu.matfx.gui.util.UtilFx;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * tiny circle component for the input or output connector from a ui component
 * @author m.goerlich
 *
 */
public class CircleComponent extends Group implements IArea 
{
	/**
	 * Grundfläche
	 */
	private Circle circleBase;
	
	/**
	 * Rand
	 */
	//private Circle circleBorder;
	

	private Coordinate centerCoordinate = new Coordinate();
	
	
	public CircleComponent(double r, double centerX, double centerY, Color color)
	{
		circleBase = new Circle();
		circleBase.setRadius(r);
		circleBase.setCenterX(centerX);
		circleBase.setCenterY(centerY);
		//TODO
		//circleBase.getStyleClass().add("circleBase");
		circleBase.setFill(color);
//		
//		circleBorder = new Circle();
//		circleBorder.setRadius(r);
//		circleBorder.setCenterX(centerX);
//		circleBorder.setCenterY(centerY);	
//		circleBorder.getStyleClass().add("circleBorder");
		
		this.getChildren().addAll(circleBase);
		
	}


	@Override
	public boolean isPointInArea(Point2D point) 
	{
		Bounds localBounds = circleBase.localToScene(circleBase.getBoundsInLocal());
		return UtilFx.isPointInShape(point, localBounds);
	}

	@Override
	public Point2D getCenterPointFromArea() {
		Point2D point2D = null;
		Bounds localBounds = circleBase.localToScene(circleBase.getBoundsInLocal());
		point2D = new Point2D(UtilFx.getCoordinate(localBounds.getMinX(), localBounds.getMaxX()), 
				UtilFx.getCoordinate(localBounds.getMinY(), localBounds.getMaxY()));
		return point2D;
	}
	
	public Coordinate getCenterCoordinate()
	{
		return centerCoordinate;
	}
	
	@Override
	public void recalcualteCenterPoint() 
	{
		Point2D point2D = null;
		//schaut komisch ist aus, geht aber nicht anders...hängt mit der Menüleiste zusammen
		Bounds localBounds = circleBase.getParent().getParent().localToParent(circleBase.getBoundsInLocal());
		point2D = new Point2D(UtilFx.getCoordinate(localBounds.getMinX(), localBounds.getMaxX()),
				UtilFx.getCoordinate(localBounds.getMinY(), localBounds.getMaxY()));
		centerCoordinate.setX(point2D.getX());
		centerCoordinate.setY(point2D.getY());
	}
	
	
	public Point2D getScenePoint()
	{
		
		try
		{
			Scene scene = CircleComponent.this.getScene();
			final Point2D windowCoord = new Point2D(scene.getWindow().getX(), scene.getWindow().getY());
			final Point2D sceneCoord = new Point2D(scene.getX(), scene.getY());
			final Point2D nodeCoord = CircleComponent.this.localToScene(0.0, 0.0);
			final double clickX = Math.round(windowCoord.getX() + sceneCoord.getX() + nodeCoord.getX());
			final double clickY = Math.round(windowCoord.getY() + sceneCoord.getY() + nodeCoord.getY());
			return new Point2D(clickX, clickY);
			
		}
		catch(Exception e)
		{
			return null;
		}
		
	}
	
	

}