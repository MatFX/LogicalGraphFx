package eu.matfx.logic.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import eu.matfx.logic.data.impl.FunctionElement;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.data.impl.SensorElement;
import eu.matfx.logic.data.impl.container.AndContainer;
import eu.matfx.logic.data.impl.container.OrContainer;
import eu.matfx.logic.data.impl.container.RSFlipFlopContainer;
import eu.matfx.logic.data.impl.container.XorContainer;




/**
 * 
 * @author m.goerlich
 *
 */
@XmlSeeAlso({AndContainer.class, OrContainer.class, RSFlipFlopContainer.class, XorContainer.class, FunctionElement.class,
	LineConnector.class, SensorElement.class})
public abstract class ALogicElement implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6177940045926437821L;
	
	@XmlElement
	private String classDescription = this.getClass().getSimpleName();
	
	/**
	 * it's not the index of the map. this is the identifier in a seperated Storage module.
	 * <br>//TODO evtl. noch keine Ahnung
	 */
	//private int logicElementIdentifier = 0;
	
	
	/**
	 * every logic element knows the index from the followed element
	 * TODO weiß ich noch nicht ob benötigt
	 */
	protected int nextFollowedElementIndex;
	
	public int getNextFollowedElementIndex()
	{
		return nextFollowedElementIndex;
	}

	
}
