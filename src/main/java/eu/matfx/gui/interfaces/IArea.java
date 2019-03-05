package eu.matfx.gui.interfaces;


import eu.matfx.gui.helper.Coordinate;
import javafx.geometry.Point2D;

public interface IArea
{
	/**
	 * Ist Dort zu implementieren wo ermittelt werden muss, ob ein Koordinatenpunkt innerhalb eines Bereiches liegt
	 * <br>Der Punkt ist von außerhalb zu übergeben, die Bereichsermittlung findet im eigentlichen Objekt statt.
	 * @param point
	 * @param bounds
	 * @return
	 */
	public boolean isPointInArea(Point2D point);
	
	/**
	 * Ermittelt die Mittelpunktkoordinate auf Basis der Szene.
	 * @return
	 */
	@Deprecated
	public Point2D getCenterPointFromArea();
	
	
	/**
	 * Hier wird die Mittelpunkt-Koordinate x und y auf Basis der Szene neu ermittelt
	 */
	public void recalcualteCenterPoint();

	/**
	 * Gibt die Center Coordinate zurück. Diese wird beispielsweise für das binding benötigt
	 * @return
	 */
	public Coordinate getCenterCoordinate();
	

}
