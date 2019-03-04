package eu.matfx.gui.main.sub;

import java.util.Optional;

import eu.matfx.gui.util.ECommand;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;

public class CreationLogicSchemeBar extends HBox
{
	
	private ObjectProperty<ECommand> command;
	
	private ComboBox<String> schemeComboBox = null;
	
	public CreationLogicSchemeBar(ObjectProperty<ECommand> command)
	{
		super(5);
		this.setPadding(new Insets(3,3,3,3));
		this.command = command;
		
		this.command.addListener(new ChangeListener<ECommand>()
		{

			@Override
			public void changed(ObservableValue<? extends ECommand> observable, ECommand oldValue, ECommand newValue) 
			{
				switch(newValue)
				{
					case DELETED_SCHEME:
						//TODO Neuaufbau der comboBox mit den schemas
						//nicht so wie jetzt
						
						schemeComboBox.getItems().remove(0);
						
						schemeComboBox.setDisable(false);
						command.set(ECommand.NO_COMMAND);
						break;
				}
				
			}
			
		});
		
		
		
		
		
		Button newButton = new Button("new scheme");
		newButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				createNewScheme();
				
			}
			
		});
		
		
		Button deleteButton = new Button("delete scheme");
		deleteButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				deleteActiveScheme();
				
			}
			
		});
		
		//TODO zu beginn noch nicht befüllt
		schemeComboBox = new ComboBox<String>();
		schemeComboBox.setMinWidth(150);
		
		
		TextField textField = new TextField();
		
		Button save = new Button("save scheme");
		save.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				saveScheme();
				
			}
			
		});
		
		Button reset = new Button("reset scheme");
		reset.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				resetScheme();
				
			}

			
		});
		
		
		this.getChildren().addAll(newButton, deleteButton, schemeComboBox, textField, save, reset);
		
	}

	protected void createNewScheme() {
		
		//bei einem neuem Schema als erstes nach dem Namen fragen
		/*
		TextInputDialog dialog = new TextInputDialog("Bezeichnung");
		
		dialog.setTitle("Erzeuge Schema");
		dialog.setHeaderText("Vergabe der Schema Bezeichnung:");
		dialog.setContentText("Name:");
		Optional<String> result = dialog.showAndWait();
		String bezeichnung = "";
		if (result.isPresent()) 
		{
			bezeichnung = result.get();
		} 
	
	//	SchemeRootContainer schemaRootContainer = new SchemeRootContainer(bezeichnung);
	//	TempDataStorage.setSchemeRootContainer(schemaRootContainer);
		
		schemeComboBox.getItems().add(schemaRootContainer.getBezeichnung());
		schemeComboBox.getSelectionModel().select(schemaRootContainer.getBezeichnung());
		schemeComboBox.setDisable(true);
		//hinweis an GUI, dass ein neuer Container erzeugt wurde
		*/
		command.set(ECommand.CREATED_NEW_SCHEME);
	}

	protected void deleteActiveScheme() 
	{
		//hinweis an GUI, dass ein neuer Container erzeugt wurde
		command.set(ECommand.DELETE_ACTIVE_SCHEME);
	}

	protected void saveScheme() {
		// TODO Auto-generated method stub
		
	}

	protected void resetScheme() {
		command.set(ECommand.RESET_ACTIVE_SCHEME);
		schemeComboBox.setDisable(false);
	}

}