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
	 * Reset complete configuration
	 */
	RESET_CONFIGURATION,
	
	/**
	 * write the objects to the xml and make a new tmp copy file
	 */
	SAVE_CONFIGURATION,
	
	/**
	 * delete the active scheme from storage
	 */
	DELETE_ACTIVE_SCHEME,
	
	/**
	 * remark that the user deleted the active scheme
	 */
	DELETED_SCHEME,
	
	/**
	 * take the current xml file and create a new tmp backup
	 */
	SAVE_SCHEME_LIST,
	
	/**
	 * remark, that a active scheme on the main pane.
	 */
	ACTIVATED_SCHEME,
	
	/**
	 * when user add a new base template to the active scheme
	 */
	ADD_NEW_BASE_TEMPLATE,
	
	//TODO raus ist nur übergangsweise
	CREATE_NEW_INDEX,
	
	//TODO raus ist nur testweise
	CREATE_NEW_CONTAINER;

}