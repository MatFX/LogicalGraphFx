package eu.matfx.logic.database;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import eu.matfx.logic.interfaces.IFileName;

/**
 * static methods to read and write xml files/objects
 * @author m.goerlich
 *
 */
public class XMLAccess 
{
	//TODO evtl. eigene Klasse wenn noch mehr hinzukommt
	public static final String TOP_LEVEL_CONFIG_FOLDER = "/config/";
	
	/**
	 * read xml file and return a instance of IFileName
	 * @param is an instance of IFileName
	 * @return
	 */
	public static IFileName readObjectFromFile(IFileName iFileName)
	{
		try 
		{
			File f = new File(iFileName.getCompletePath());
			if(f.exists())
			{
				JAXBContext jc = JAXBContext.newInstance(iFileName.getClass());
				Unmarshaller u = jc.createUnmarshaller();
				IFileName saveObject = (IFileName)u.unmarshal(f);
				return saveObject;
			}
		}
		catch(UnmarshalException e)
		{
			e.printStackTrace();
		}
		catch (JAXBException e) 
		{
			e.printStackTrace();
		}
		return iFileName;
	}
	
	/**
	 * Ein Objekt soll gespeichert werden
	 * @param iFileName Alle Objekte entgegennehmen die das Interface implementiert haben.
	 * @return
	 */
	public static boolean writeObjectToFile(IFileName iFileName)
	{
		try 
    	{
			JAXBContext jc = JAXBContext.newInstance(iFileName.getClass());
    		Marshaller m = jc.createMarshaller();
    		//Formatierung, damit es übersichtlich ist, falls mal jemand reinschaut
    		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
    		m.marshal(iFileName, new File(iFileName.getCompletePath()));
    		return true;
    		
    	} 
    	catch (JAXBException e) 
    	{
    		e.printStackTrace();
    	}
    	catch(NullPointerException e)
    	{
    		e.printStackTrace();
    	}
		return false;
	}
}
