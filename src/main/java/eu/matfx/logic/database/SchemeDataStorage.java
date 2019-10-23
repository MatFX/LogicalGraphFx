package eu.matfx.logic.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.matfx.logic.Scheme;
import eu.matfx.logic.SchemeListContainer;


public class SchemeDataStorage extends ASaveCurrentData
{
	
	private static SchemeDataStorage instance = null;
	
	/**
	 * XML container for the scheme list
	 */
	private SchemeListContainer schemeList = null;
	
	private SchemeDataStorage()
	{
		schemeList = new SchemeListContainer();
		
	}

	public static void initSchemeDataStorage()
	{
		instance = new SchemeDataStorage();
		//import and fill the scheme list
		instance.schemeList = (SchemeListContainer) XMLAccess.readObjectFromFile(instance.schemeList);
	}
	
	
	public static SchemeListContainer getSchemeList()
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
		
		newScheme.setId(SchemeListContainer.calculatedNextFreeId());
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

	@Override
	public void saveCurrentFolder() {
		//not used
		
	}

	@Override
	public void resetFolder() {
		//not used
		
	}

	@Override
	public void saveCurrentFile() {
		String filename = instance.schemeList.getFileName();
		String pfadZurDatei = instance.schemeList.getCompletePath().replace(filename, "");
		
		
		//Sicherheitshalber schauen ob 
		File file = new File(pfadZurDatei +  filename);
		if(!file.exists())
		{
			XMLAccess.writeObjectToFile(instance.schemeList);
		}
		this.saveCurrentFile(pfadZurDatei, filename);
		
	}

	@Override
	public void resetFile() {
		String filename = instance.schemeList.getFileName();
		String pfadZurDatei = instance.schemeList.getCompletePath().replace(filename, "");
		this.resetFile(pfadZurDatei, filename);
		
	}

	@Override
	public void cleanTmpFilesOrFolder() {
		String filename = instance.schemeList.getFileName();
		String pfadZurDatei = instance.schemeList.getCompletePath().replace(filename, "");
		//es Bedarf hier nur einen Löschung 
		deleteTMPFiles(pfadZurDatei);
		deleteTMPFolder(pfadZurDatei);
		
	}

	public static SchemeDataStorage getInstance() {
		return instance;
	}

	public void reloadFile() {
		initSchemeDataStorage();
	}

	
	
	
	
	
}
