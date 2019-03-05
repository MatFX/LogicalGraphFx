package eu.matfx.logic.helper;

import java.io.Serializable;

import eu.matfx.logic.interfaces.IDimensionView;



/**
 * 
 * Storage object for the dimension of the ui component
 * @author M.Goerlich
 *
 */
public class DimensionView implements IDimensionView, Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8838446539491289635L;
	private double width = 0;
	private double height = 0;
	
	public DimensionView()
	{
	}
	
	
	public DimensionView(double width, double height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public void setHeight(double height) {
		this.height = height;
	}


	@Override
	public boolean isIDimensionViewFilled() 
	{
		if(this.height > 0 && this.width > 0)
			return true;
		return false;
	}

}
