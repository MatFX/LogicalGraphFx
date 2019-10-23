package eu.matfx.logic.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Grundlegende Verarbeitugnen wenn Verzeichnisse oder Dateien temporär gespeichert werden sollen
 * @author m.goerlich
 *
 */
public abstract class ASaveCurrentData 
{
	/**
	 * 
	 */
	private static final String TMP = "/tmp/";
	
	/**
	 * Sicherung des übergebenen Verzeichnisses in das tmp Verzeichnis
	 * @param orginalVerzeichnisPfad
	 */
	protected void saveCurrentFolder(String orginalVerzeichnisPfad)
	{
		String zielVerzeichnis = orginalVerzeichnisPfad + TMP;
		File file = new File(zielVerzeichnis);
		if(!file.exists())
		{
			file.mkdirs();
		
		}
		else
		{
			//wenn vorhanden prüfen ob auch temp wirklich leer ist
			deleteFiles(zielVerzeichnis);
		}
	
		file = new File(orginalVerzeichnisPfad);
		File[] files = file.listFiles();
		if(files != null && files.length > 0)
		{
			for(int i = 0; i < files.length; i++)
			{
				File toCopy = files[i];
				if(toCopy.isFile())
				{
					String name = toCopy.getName();
					Path target = Paths.get(zielVerzeichnis+name);
					try 
					{
						Files.copy(toCopy.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		
	}
	/**
	 * Die parameterlosen methoden sind dann in der abgeleiteten Klasse zu implementiernen
	 */
	public abstract void saveCurrentFolder();
	
	/**
	 * Das gesicherte TMP Verzeichnis soll den Inhalt vom Orginalverzeichnis überschreiben
	 * @param orginalVerzeichnisPfad
	 */
	protected void resetFolder(String orginalVerzeichnisPfad )
	{
		//clear des Zielverzeichnisses? aber nur die Einträge nicht tmp
		deleteFiles(orginalVerzeichnisPfad);
		
		
		//eigentlich die gleiche Verfahrensweise wie beim speichern jedoch jetzt anders herum
		//sicherstellen ob zielVerzeichnis existiert
		
		String zielVerzeichnis = orginalVerzeichnisPfad;
		File file = new File(zielVerzeichnis);
		if(!file.exists())
			file.mkdirs();
		//Ausgangslage ist das Orginalverzeichnis mit dem TMP
		file = new File(orginalVerzeichnisPfad + TMP);
		File[] files = file.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			File toCopy = files[i];
			if(toCopy.isFile())
			{
				String name = toCopy.getName();
				Path target = Paths.get(zielVerzeichnis+name);
				try 
				{
					Files.copy(toCopy.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Hier wird das übergebene Verzeichnis von Inhalt befreit.
	 * <br>Es werden nur Dateien keine Unterverzeichnisse entfernt.
	 * @param verzeichnisInhaltLoeschen
	 */
	private void deleteFiles(String verzeichnisInhaltLoeschen)
	{
		 
		File file = new File(verzeichnisInhaltLoeschen);
		if(file.exists())
		{
			List<File> fileList = Arrays.asList(file.listFiles());
			Iterator<File> iterator = fileList.iterator();
			while(iterator.hasNext())
			{
				File temp = iterator.next();
				if(temp.isFile())
					temp.delete();
			}
			
		}
		
	}
	public abstract void resetFolder();
	
	/**
	 * save the file in the tmp folder (file in tmp is the backup)
	 * @param orginalVerzeichnisPfad
	 * @param fileName
	 */
	public void saveCurrentFile(String orginalVerzeichnisPfad, String fileName)
	{
		//prüfen ob tmp pfad bereits existiert.
		
		String zielVerzeichnis = orginalVerzeichnisPfad + TMP;
		File file = new File(zielVerzeichnis);
		if(!file.exists())
			file.mkdirs();
		//eine Löschung einer alten Datei wird nicht gemacht...die wird überkopiert.
		
		Path toCopy = Paths.get(orginalVerzeichnisPfad+fileName);
		Path target = Paths.get(zielVerzeichnis+fileName);
		try 
		{
			Files.copy(toCopy, target, StandardCopyOption.REPLACE_EXISTING);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public abstract void saveCurrentFile();
	
	
	/**
	 * Das gesicherte File soll in den Orignalordner wieder zurückverschoben werden.
	 * @param orginalVerzeichnisPfad
	 * @param fileName
	 */
	public void resetFile(String orginalVerzeichnisPfad, String fileName)
	{
		//hier das ganze wieder zurück
		String zielVerzeichnis = orginalVerzeichnisPfad;
		File file = new File(zielVerzeichnis);
		if(!file.exists())
			file.mkdirs();
		//eine Löschung einer alten Datei wird nicht gemacht...die wird überkopiert.
		
		Path toCopy = Paths.get(orginalVerzeichnisPfad+TMP+fileName);
		Path target = Paths.get(zielVerzeichnis+fileName);
		try 
		{
			Files.copy(toCopy, target, StandardCopyOption.REPLACE_EXISTING);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public abstract void resetFile();
	
	/**
	 * Diese Methode kann für die Verzeichnis Löschung oder für das einzelne File löschen eingesetzt werden.
	 * <br>Es wird immer dann aufgerufen, wenn die Bearbeitung beendet wurde und die TMP nicht mehr benötigt werden.
	 */
	public abstract void cleanTmpFilesOrFolder();
	
	
	public void deleteTMPFolder(String orginalPfad)
	{
		File file = new File(orginalPfad+TMP);
		file.delete();
	}
	
	public void deleteTMPFiles(String orginalPfad)
	{
		String weitergabe = orginalPfad + TMP;
		deleteFiles(weitergabe);
	}

	
}
