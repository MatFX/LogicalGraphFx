package eu.matfx.gui.util;

/**
 * some enums for the possible base logic templates
 * @author m.goerlich
 *
 */
public enum EBaseTemplate
{
	//TODO language
	SENSOR("Sensor Container"),
	
	FUNCTION("Function Container"),
	
	AND("AND Container"),
	
	OR("OR Container");
	
	private String description;
	
	private EBaseTemplate(String description)
	{
		this.description = description;
	}
	
	public String toString()
	{
		return description;
	}
	
	
	
	

}
