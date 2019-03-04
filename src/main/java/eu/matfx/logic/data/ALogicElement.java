package eu.matfx.logic.data;

/**
 * 
 * @author m.goerlich
 *
 */
public abstract class ALogicElement 
{
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
