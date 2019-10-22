package eu.matfx.gui.main.sub;

import java.util.List;
import java.util.Optional;

import eu.matfx.gui.util.ECommand;
import eu.matfx.logic.Scheme;
import eu.matfx.logic.SchemeList;
import eu.matfx.logic.database.SchemeDataStorage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	
	
	private ComboBox<Scheme> schemeComboBox = null;
	
	/**
	 * add and remove the changelistener when the combobox get new content
	 */
	private ChangeListener<Scheme> changeListener = new ChangeListener<Scheme>(){

		@Override
		public void changed(ObservableValue<? extends Scheme> observable, Scheme oldValue, Scheme newValue) 
		{
			if(!(oldValue.equals(newValue)) && oldValue != null)
			{
				//index ermitteln und diesen setzen
				SchemeDataStorage.getSchemeList().setActiveSchemeOnScreen(newValue);
				activateScheme();
			}
		}
		
	};
	
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
					case DELETE_ACTIVE_SCHEME:
						//TODO Neuaufbau der comboBox mit den schemas
						//nicht so wie jetzt
						
						Scheme selectedItem = schemeComboBox.getSelectionModel().getSelectedItem();
						
						if(selectedItem != null)
						{
							SchemeDataStorage.removeScheme(selectedItem);
						}
						activateScheme();
						rebuildComboBoxContent();
						command.set(ECommand.NO_COMMAND);
						break;
					case CREATED_NEW_SCHEME:
						rebuildComboBoxContent();
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
		
		SchemeList schemeObject = SchemeDataStorage.getSchemeList();
		//TODO need sort ...alphabetical?
		List<Scheme> tempList = schemeObject.getSchemeList();
		
		
		ObservableList<Scheme> selectionComboBox = 
				FXCollections.observableArrayList(tempList);
		
		
		//TODO zu beginn noch nicht befüllt
		schemeComboBox = new ComboBox<Scheme>(selectionComboBox);
		if(schemeObject.getActiveSchemeOnScreen() >= 0)
		{
			for(int i = 0; i < schemeComboBox.getItems().size(); i++)
			{
				if(schemeComboBox.getItems().get(i).getId() == schemeObject.getActiveSchemeOnScreen())
				{
					schemeComboBox.getSelectionModel().select(i);
					break;
				}
			}
		}
		schemeComboBox.setMinWidth(150);
		schemeComboBox.valueProperty().addListener(changeListener);
		
		
		
		
		
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

	protected void rebuildComboBoxContent() {

		//first remove 
		schemeComboBox.valueProperty().removeListener(changeListener);
		//rebuild the combox and select the scheme on screen
		SchemeList schemeList = SchemeDataStorage.getSchemeList();
		//TODO need sort ...alphabetical?
		List<Scheme> tempList = schemeList.getSchemeList();
		
		ObservableList<Scheme> selectionComboBox = 
				FXCollections.observableArrayList(tempList);
	
		schemeComboBox.getItems().clear();
		schemeComboBox.getItems().addAll(selectionComboBox);
		for(int i = 0; i < schemeComboBox.getItems().size(); i++)
		{
			if(schemeComboBox.getItems().get(i).getId() == schemeList.getActiveSchemeOnScreen())
			{
				schemeComboBox.getSelectionModel().select(i);
				break;
			}
		}
		schemeComboBox.valueProperty().addListener(changeListener);
		
	}

	protected void createNewScheme() {
		
		//bei einem neuem Schema als erstes nach dem Namen fragen
		
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
		
		Scheme newScheme = new Scheme();
		newScheme.setDescriptionName(bezeichnung);
		
		//zugehörige Id wird in Methode ermittelt.
		SchemeDataStorage.addNewScheme(newScheme);
	
		command.set(ECommand.CREATED_NEW_SCHEME);
		
		
	}

	protected void deleteActiveScheme() 
	{
		//hinweis an GUI, dass ein neuer Container erzeugt wurde
		command.set(ECommand.DELETE_ACTIVE_SCHEME);
	}

	protected void saveScheme() 
	{
		command.set(ECommand.SAVE_ACTIVE_SCHEME);
		
	}

	protected void resetScheme() {
		command.set(ECommand.RESET_ACTIVE_SCHEME);
		schemeComboBox.setDisable(false);
	}
	

	/**
	 * Anderes Schema wurde ausgewählt.
	 */
	protected void activateScheme() {
		command.set(ECommand.ACTIVATED_SCHEME);
	}


}