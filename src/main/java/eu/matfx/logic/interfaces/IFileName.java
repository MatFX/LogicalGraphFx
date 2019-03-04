package eu.matfx.logic.interfaces;

public interface IFileName 
{
	/**
	 * Get the pure file name for the corresponding xml object
	 * @return
	 */
	public String getFileName();
	
	/**
	 * Set the pure filename for the corresponding xml object.
	 * <br>In general the set method called in the constructor of the object.
	 * @param fileName
	 * @return
	 */
	public void setFileName(String fileName);
	
	/**
	 * 
	 * Is there a sub directory in the default directory?
	 * @return
	 */
	public boolean isSubDirectoryAvailable();

	/**
	 * Complete path to the file
	 * @return
	 */
	public String getCompletePath();
	
}
