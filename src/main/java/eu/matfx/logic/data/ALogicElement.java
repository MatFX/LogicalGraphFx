package eu.matfx.logic.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import eu.matfx.logic.data.impl.CircleLineConnector;
import eu.matfx.logic.data.impl.FunctionElement;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.data.impl.SensorElement;
import eu.matfx.logic.data.impl.container.AndContainer;
import eu.matfx.logic.data.impl.container.OrContainer;
import eu.matfx.logic.data.impl.container.RSFlipFlopContainer;
import eu.matfx.logic.data.impl.container.XorContainer;
import eu.matfx.logic.helper.DimensionView;
import eu.matfx.logic.helper.LocationView;




/**
 * Abstract logic element with member variable of all extended classes
 * @author m.goerlich
 *
 */
@XmlSeeAlso({AndContainer.class, OrContainer.class, RSFlipFlopContainer.class, XorContainer.class, FunctionElement.class, 
	LineConnector.class, SensorElement.class, CircleLineConnector.class})
@XmlType(propOrder={"classDescription", "index", "locationView", "dimensionView"})
public abstract class ALogicElement implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6177940045926437821L;
	
	/**
	 * id from the object; it must be filled when object added to list
	 */
	private int index = Integer.MIN_VALUE;
	
	/**
	 * class description it's only a helper describe the xml context.
	 */
	@XmlElement
	private String classDescription = this.getClass().getSimpleName();

	private DimensionView dimensionView = new DimensionView(0, 0);
	
	private LocationView locationView = new LocationView(0,0);
	
	/**
	 * return the dimension of the ui component on the screen.
	 * @return
	 */
	public DimensionView getDimensionView() {
		return dimensionView;
	}

	public void setDimensionView(DimensionView dimensionView) {
		this.dimensionView = dimensionView;
	}

	public void setLocationView(LocationView locationView) {
		this.locationView = locationView;
	}

	/**
	 * return the location of the ui component on the screen.
	 * @return
	 */
	public LocationView getLocationView() {
		return locationView;
	}

	/**
	 * return the id of the logic element. 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
}
