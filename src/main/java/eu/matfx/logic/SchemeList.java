package eu.matfx.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import eu.matfx.logic.database.SchemeDataStorage;
import eu.matfx.logic.database.XMLAccess;
import eu.matfx.logic.interfaces.IFileName;

@XmlRootElement(name = "Schemelist")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"activeSchemeOnScreen", "schemeList"})
public class SchemeList implements IFileName
{
	
	public static int NO_ACTIVE_SCHEME_ON_SCREEN = Integer.MIN_VALUE;
	
	public static int MIN_ACTIVE_SCHEME_ID = 1;
	
	/**
	 * Dieses ist ein Teil von dem Settingspfad. Es fehlt noch der Profil Ordner
	 */
	private static final String PATH_VIEW_FOLDER = "/view/";
	
	/**
	 * different defined schemes are in a array list
	 */
	@XmlElement(name="Scheme")
	private List<Scheme> schemeList = null;
	
	/**
	 * index from the active scheme on screen; Integer.MinValue = no active scheme on screen
	 */
	private int activeSchemeOnScreen = 0;
	
	public SchemeList()
	{
		schemeList = new ArrayList<Scheme>();
	}

	public List<Scheme> getSchemeList() {
		return schemeList;
	}

	public void setSchemeList(List<Scheme> schemeList) {
		this.schemeList = schemeList;
	}

	@Override
	public String getFileName() {
		return this.getClass().getSimpleName().toString()+".xml";
	}

	@Override
	public void setFileName(String fileName) {
		//no use
		
	}
	
	private String getPathToFile()
	{
		File file = new File("");
		file = new File(file.getAbsolutePath() + XMLAccess.TOP_LEVEL_CONFIG_FOLDER + PATH_VIEW_FOLDER);
		return file.getAbsolutePath();
	}

	@Override
	public boolean isSubDirectoryAvailable() {
		return true;
	}

	/**
	 * complete path to file.
	 */
	@Override
	public String getCompletePath() {
		
		File temp = new File("");
		temp = new File(getPathToFile() + File.separator);
		if(!temp.exists())
			temp.mkdirs();
		
		temp = new File(temp.getAbsolutePath() + "/" + getFileName());
		return temp.getAbsolutePath();
	}

	public int getActiveSchemeOnScreenIndex() {
		return activeSchemeOnScreen;
	}

	public void setActiveSchemeOnScreen(int activeSchemeOnScreen) {
		this.activeSchemeOnScreen = activeSchemeOnScreen;
	}

	public void setActiveSchemeOnScreen(Scheme newSelectedScheme)
	{
		System.out.println("newSelectedScheme " + newSelectedScheme.getId());
		SchemeDataStorage.getSchemeList().setActiveSchemeOnScreen(newSelectedScheme.getId());
	}
	
	/**
	 * every scheme needs a id
	 * @return
	 */
	public static int calculatedNextFreeId() 
	{
		//kleinste mögliche Wert initialisieren.
		int nextId = SchemeList.MIN_ACTIVE_SCHEME_ID;
		
		List<Scheme> tempList = SchemeDataStorage.getSchemeList().getSchemeList();
		if(tempList == null || tempList.size() <= 0)
		{
			return nextId;
		}
		else
		{
			Collections.sort(tempList, new Comparator<Scheme>() 
			{

				@Override
				public int compare(Scheme o1, Scheme o2) 
				{
					Integer objektidO1 = o1.getId();
					Integer objektidO2 = o2.getId();
					return objektidO1.compareTo(objektidO2);
				}
			});
			
			Iterator<Scheme> it = tempList.iterator();
			while(it.hasNext())
			{
				Scheme temp = it.next();
				if(nextId != temp.getId())
				{
					return nextId;
				}
				else
					nextId++;
			}
		}
		return nextId;
	}

	/**
	 * Search for the element with the parameter id
	 * @param activeSchemeOnScreen2
	 * @return
	 */
	public Scheme getSchemeElement(int id) 
	{
		for(int i = 0; i < schemeList.size(); i++)
		{
			if(schemeList.get(i).getId() == id)
				return schemeList.get(i);
		}
		return null;
	}

	public Scheme getActiveSchemeOnScreen() 
	{
		return SchemeDataStorage.getSchemeList().getSchemeElement(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreenIndex());
	}

}
