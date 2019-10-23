package eu.matfx.gui.main;

import eu.matfx.gui.main.sub.CreationLogicSchemeBar;
import eu.matfx.gui.util.ECommand;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



public class TopPane extends HBox
{
private Stage primaryStage;
	
	private CreationLogicSchemeBar schemeBar;
	
	//private CreationTestField testFieldCreate;
	
	public TopPane(Stage stage, ObjectProperty<ECommand> command, SimpleBooleanProperty notSaved)
	{
		super();
		this.setStyle("-fx-background-color: #3f687e;");
		this.primaryStage = stage;
		this.getChildren().addAll(getCreationLogicSchemeBar(command, notSaved), new Separator());//, getCreationTestField(command));
	}

	/*
	private Node getCreationTestField(ObjectProperty<ECommand> command) 
	{
		testFieldCreate = new CreationTestField(command);
		return testFieldCreate;
	}
*/
	private Node getCreationLogicSchemeBar(ObjectProperty<ECommand> command, SimpleBooleanProperty notSaved) 
	{
		schemeBar = new CreationLogicSchemeBar(command, notSaved);
		return schemeBar;
	}
}
