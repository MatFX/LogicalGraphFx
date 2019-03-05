package eu.matfx.logic.helper;

import java.io.Serializable;

import eu.matfx.logic.interfaces.ILocationView;



/**
 * 
 * @author M.Goerlich
 *
 */
public class LocationView implements ILocationView, Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8838446539491289635L;
	private double x = 0;
	private double y = 0;
	
	public LocationView()
	{
	}
	
	
	public LocationView(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}


	@Override
	public double getY() {
		return y;
	}


	@Override
	public void setX(double x) {
		this.x = x;
		
	}


	@Override
	public void setY(double y) {
		this.y = y;
	}


	@Override
	public boolean isILocationViewFilled() {
		if(this.x > 0 && this.x > 0)
			return true;
		return false;
	}

}