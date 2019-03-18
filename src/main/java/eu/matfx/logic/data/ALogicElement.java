package eu.matfx.logic.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
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
 * 
 * @author m.goerlich
 *
 */
@XmlSeeAlso({AndContainer.class, OrContainer.class, RSFlipFlopContainer.class, XorContainer.class, FunctionElement.class, LineConnector.class, SensorElement.class})
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
	
	@XmlElement
	private String classDescription = this.getClass().getSimpleName();

	private DimensionView dimensionView = new DimensionView(0, 0);
	
	private LocationView locationView = new LocationView(0,0);
	
	public DimensionView getDimensionView() {
		return dimensionView;
	}

	public void setDimensionView(DimensionView dimensionView) {
		this.dimensionView = dimensionView;
	}

	public void setLocationView(LocationView locationView) {
		this.locationView = locationView;
	}

	public LocationView getLocationView() {
		return locationView;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	
}
