package eu.matfx.gui.main;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ContentPane extends Pane
{
	private Stage primaryStage;

	public ContentPane(Stage primaryStage, StringProperty statusView, DoubleProperty xCoords, DoubleProperty yCoords) 
	{
		super();
		this.primaryStage = primaryStage;
	
	}

}
