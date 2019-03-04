package eu.matfx.logic.data.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import eu.matfx.logic.data.ALogicElement;

/**
 * This object connect two different LogicElements. At the ui it will be represented with a line.
 * @author m.goerlich
 *
 */
@XmlType(name = "LineConnector")
@XmlAccessorType(XmlAccessType.FIELD)
public class LineConnector extends ALogicElement
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4985338600265951913L;

}
