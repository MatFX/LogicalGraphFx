package eu.matfx.logic.database;

import java.util.ArrayList;
import java.util.List;

import eu.matfx.logic.Scheme;
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

	/**
	 * add new scheme to list, calculate the id and select it as setActiveSchemeOnScreen
	 * @param newScheme
	 */
	public static void addNewScheme(Scheme newScheme) 
	{
		List<Scheme>schemeList = instance.schemeList.getSchemeList();
		if(schemeList == null)
			schemeList = new ArrayList<Scheme>();
		
		newScheme.setId(SchemeList.calculatedNextFreeId());
		schemeList.add(newScheme);
		instance.schemeList.setActiveSchemeOnScreen(newScheme.getId());
	}

	public static void removeScheme(Scheme selectedItem)
	{
		List<Scheme>schemeList = instance.schemeList.getSchemeList();
		if(schemeList != null)
		{
			schemeList.remove(selectedItem);
		}
		
		//Aenderung der Aktivierung; 
		if(schemeList != null && schemeList.size() > 0)
		{
			//einen Index sich holen, damit dieser angezeigt werden kann.
			int neuerAktivierterIndex = schemeList.get(0).getId();
			instance.schemeList.setActiveSchemeOnScreen(neuerAktivierterIndex);
		}
	}
	
	
	
	
	
}
