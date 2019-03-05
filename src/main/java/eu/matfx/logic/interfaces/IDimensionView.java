package eu.matfx.logic.interfaces;

/**
 * Einheitliche Ansprechstation für alle Speicherobjekte bei denen die letzte Dimension gesichert wird.
 * @author M.Goerlich
 *
 */
public interface IDimensionView 
{
	
	public double getWidth(); 
	
	public double getHeight();
	
	public void setWidth(double width); 
	
	public void setHeight(double height); 
	
	/**
	 * are values at the object stored
	 * @return
	 */
	public boolean isIDimensionViewFilled();

}
