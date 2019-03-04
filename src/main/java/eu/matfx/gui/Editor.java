package eu.matfx.gui;

import eu.matfx.logic.database.SchemeDataStorage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Editor extends Application 
{

	@Override
	public void start(Stage primaryStage) 
	{
		try
		{
			//init of storage and other singleton objects
			SchemeDataStorage.initSchemeDataStorage();
			
			
			
			
			
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
