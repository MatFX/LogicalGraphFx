package eu.matfx.logic.interfaces;

public interface ILocationView 
{
	public double getX(); 
	
	public double getY();
	
	public void setX(double x); 
	
	public void setY(double y); 

	/**
	 * are values at the object stored
	 * @return
	 */
	public boolean isILocationViewFilled();

}
