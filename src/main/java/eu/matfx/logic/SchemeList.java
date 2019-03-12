package eu.matfx.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import eu.matfx.logic.database.XMLAccess;
import eu.matfx.logic.interfaces.IFileName;

@XmlRootElement(name = "Schemelist")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"activeSchemeOnScreen", "schemeList"})
public class SchemeList implements IFileName
{
	
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
	private int activeSchemeOnScreen = Integer.MIN_VALUE;
	
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

	public int getActiveSchemeOnScreen() {
		return activeSchemeOnScreen;
	}

	public void setActiveSchemeOnScreen(int activeSchemeOnScreen) {
		this.activeSchemeOnScreen = activeSchemeOnScreen;
	}

}
