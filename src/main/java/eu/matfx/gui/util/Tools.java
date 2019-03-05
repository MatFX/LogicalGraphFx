package eu.matfx.gui.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import eu.matfx.gui.component.AUIElement;


public class Tools 
{
	//test the getClasses
	public static void main(String[] args)
	{
		System.out.println(" " + AUIElement.class.getPackage().getName());
		
	
		Class<?>[] test = Tools.getClasses(AUIElement.class.getPackage().getName(), true);
		
		for(int i = 0; i < test.length; i++)
		{
			System.out.println("test " + test[i]);
		}
		
	}
	
	
	
	public static Class<?>[] getClasses(String packageName, boolean subPackages)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
	
		URL resource;
		resource = classLoader.getResource(path);
		
		List<File> dirs = new ArrayList<File>();
		List<URL> urls = new ArrayList<URL>();
		
		if (!resource.toString().contains("!"))
		{
			dirs.add(new File(resource.getFile()));
		}
		else
		{
			urls.add(resource);
		}
		
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
				
		for (File directory : dirs)
		{
			try
			{
				classes.addAll(findClasses(directory, packageName, subPackages));
			}
			catch (ClassNotFoundException e)
			{
				continue;
			}
		}
		
		for (URL url : urls)
		{
			classes.addAll(findClasses(url, packageName, subPackages));
		}
		
		return classes.toArray(new Class[classes.size()]);
	}
	
	private static List<Class<?>> findClasses(File directory, String packageName, boolean subPackages) throws ClassNotFoundException
	{
		List<Class<?>> classes = new ArrayList<Class<?>>();
		
		if (!directory.exists())
		{
			return classes;
		}
		
		File[] files = directory.listFiles();
		
		for (File file : files)
		{
			if (file.isDirectory() && subPackages)
			{
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName(), subPackages));
			}
			else if (file.getName().contains(".class"))
			{
				String name = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
				
				if (name.startsWith("."))
				{
					name = name.substring(1);
				}

				try
				{
					classes.add(Class.forName(name));
				}
				catch (Throwable e)
				{
					System.err.println("Exception: " + e.getClass().getName() + ", Message: " + e.getMessage());
				}
			}
		}
		
		return classes;
	}
	
	private static List<Class<?>> findClasses(URL url, String packageName, boolean subPackages)
	{
		packageName = packageName.replace('.', '/');
		java.net.JarURLConnection connection = null;
		List<Class<?>> classes = new ArrayList<Class<?>>();

		try
		{
			connection = (java.net.JarURLConnection)url.openConnection();
			JarFile jarFile = connection.getJarFile();
			Enumeration<JarEntry> entries = jarFile.entries();
			
			while (entries.hasMoreElements())
			{
				JarEntry entry = entries.nextElement();
				
				String name = entry.getName();

				if (name.startsWith(packageName) && name.contains(".class"))
				{
					// Prüfen, ob Eintrag übersprungen werden muss
					if (!subPackages)
					{
						int index = name.lastIndexOf("/");
						
						if (!packageName.equals(name.substring(0, index)))
							continue;
					}
					
					name = name.substring(0, name.indexOf('.'));
					name = name.replace('/', '.');
					
					try
					{
						classes.add(Class.forName(name));
					}
					catch (Throwable e)
					{
						System.err.println("Exception: " + e.getClass().getName() + ", Message: " + e.getMessage());
					}
				}
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			return null;
		} 	

     	return classes;
	}
	

}
