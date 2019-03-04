package eu.matfx.logic.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;




/**
 * 
 * @author m.goerlich
 *
 */
public abstract class ALogicElement implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6177940045926437821L;
	
	@XmlElement
	private String classDescription = this.getClass().getSimpleName();
	
	
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
