package eu.matfx.gui.util;

import eu.matfx.gui.interfaces.ISelection;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UtilFx 
{
	/**
	 * Prüfung ob der übergebene Punkt sich innerhalb des Bereiches befindet
	 * <br>Voraussetzung für die Überprüfung ist, dass die Werte aus der gleichen komponente kommen.
	 * @param point
	 * @param shapeBounds
	 * @return
	 */
	public static boolean isPointInShape(Point2D point, Bounds shapeBounds)
	{
		if(point.getX() >= shapeBounds.getMinX() 
				&& point.getX() <= shapeBounds.getMaxX()
				&& point.getY() >= shapeBounds.getMinY()
				&& point.getY() <= shapeBounds.getMaxY())
			return true;
		return false;
	}
	
	public static boolean isUIElementInShape(Bounds uiElement, Bounds shapeBounds)
	{
		//only complete uiElements in shape delivering true
		if(shapeBounds.contains(uiElement))
		{
			return true;
		}
		return false;
	}

	public static Point2D getPointFromEvent(MouseEvent mouseEvent)
	{
		if(mouseEvent == null)
			return new Point2D(Double.MIN_VALUE, Double.MIN_VALUE);
		return new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
	}
	
	public static double getCoordinate(double minValue, double maxValue)
	{
		double ergebnis = minValue + ((maxValue - minValue) / 2);
		return ergebnis;
	}

	/**
	 * place holder 
	 * @return
	 */
	public static Node createHGrowSpacer() 
	{
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}

	/**
	 * place holder 
	 * @return
	 */
	public static Node createVGrowSpacer() 
	{
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	
	/**
	 * set selection at the node
	 * @param node
	 */
	public static void setSelected(Node node)
	{
		if(node instanceof ISelection)
			((ISelection)node).setSelected(true);
		//Es kann sein, dass der Parent das Interface ISelection enthält.
		else
			UtilFx.setSelected(node.getParent());
	}
	
	
}
