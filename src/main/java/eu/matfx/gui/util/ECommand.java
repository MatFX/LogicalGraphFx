package eu.matfx.gui.util;

public enum ECommand 
{
	
	/**
	 * to "reset" the object property; der changelistener will be only triggered, when command would changed
	 */
	NO_COMMAND,
	
	/**
	 * Command to create a new scheme on the main pane
	 */
	CREATED_NEW_SCHEME,
	
	/**
	 * delete the active scheme from the main pane (optional save ack)
	 */
	RESET_ACTIVE_SCHEME, 
	
	/**
	 * delete the active scheme from storage
	 */
	DELETE_ACTIVE_SCHEME,
	
	/**
	 * remark that the user deleted the active scheme
	 */
	DELETED_SCHEME,
	
	/**
	 * Save active scheme
	 */
	SAVE_ACTIVE_SCHEME,
	
	/**
	 * remark, that a active scheme on the main pane.
	 */
	ACTIVATED_SCHEME,
	
	//TODO raus ist nur übergangsweise
	CREATE_NEW_INDEX,
	
	//TODO raus ist nur testweise
	CREATE_NEW_CONTAINER;

}