package eu.matfx.logic.database;

import eu.matfx.logic.SchemeList;

public class SchemeDataStorage 
{
	
	private static SchemeDataStorage instance = null;
	
	//TODO im Speicher halten? oder jedesmal neu auslesen
	private SchemeList schemeList = null;
	
	private SchemeDataStorage()
	{
		schemeList = new SchemeList();
		
	}

	public static void initSchemeDataStorage()
	{
		instance = new SchemeDataStorage();
		//import and fill the scheme list
		instance.schemeList = (SchemeList) XMLAccess.readObjectFromFile(instance.schemeList);
	}
	
	
	public static SchemeList getSchemeList()
	{
		return instance.schemeList;
	}
	
	
	
}
