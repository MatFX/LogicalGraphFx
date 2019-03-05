package eu.matfx.gui.interfaces;

import eu.matfx.gui.helper.Coordinate;
import javafx.geometry.Point2D;

/**
 * Erweiterung der IArea um zwei weitere Methoden die benötigt werden ums festzustellen,
 * <br>wo der Ein und Ausgang ist
 * @author m.goerlich
 *
 */
public interface IConnectorArea extends IArea
{
	/**
	 * Liegt der Punkt über den Eingangsverbinder?
	 * @param point
	 * @return
	 */
	public boolean isInputArea(Point2D point);
	
	/**
	 * Liegt der Punkt innerhalb des Ausgangsverbinders
	 * @param point
	 * @return
	 */
	public boolean isOutputArea(Point2D point);
	
	/**
	 * Gibt den Mittelpunkt des Eingangs zurück. Koordinate bezieht sich auf die Szene
	 * @return
	 */
	@Deprecated
	public Point2D getInputCenterPoint();
	
	/**
	 * Gibt den Mittelpunkt des Ausgangs zurück. Koordinate bezieht sich auf die Szene
	 * @return
	 */
	@Deprecated
	public Point2D getOutputCenterPoint();
	
	/**
	 * Gibt den Mittelpunkt des Eingangs zurück. Koordinate bezieht sich auf die Szene
	 * @return
	 */
	public Coordinate getInputCenterCoordinate();
	
	/**
	 * Gibt den Mittelpunkt des Eingangs zurück. Koordinate bezieht sich auf die Szene
	 * @return
	 */
	public Coordinate getOutputCenterCoordinate();
	
}