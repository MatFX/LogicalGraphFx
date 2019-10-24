package eu.matfx.gui.main.sub;

import java.util.List;
import java.util.Optional;

import eu.matfx.gui.util.EBaseTemplate;
import eu.matfx.gui.util.ECommand;
import eu.matfx.logic.Scheme;
import eu.matfx.logic.SchemeListContainer;
import eu.matfx.logic.data.impl.FunctionElement;
import eu.matfx.logic.data.impl.SensorElement;
import eu.matfx.logic.data.impl.container.AndContainer;
import eu.matfx.logic.data.impl.container.OrContainer;
import eu.matfx.logic.database.SchemeDataStorage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
	
	private SimpleBooleanProperty notSaved;
	
	private ComboBox<EBaseTemplate> templateBaseComboBox;
	
	/**
	 * add and remove the changelistener when the combobox get new content
	 */
	private ChangeListener<Scheme> changeListener = new ChangeListener<Scheme>(){

		@Override
		public void changed(ObservableValue<? extends Scheme> observable, Scheme oldValue, Scheme newValue) 
		{
			if(!(oldValue.equals(newValue)) && oldValue != null)
			{
				//vor Activate sicherung der aktuellen Darstellung auf Contentseite
				
				//index ermitteln und diesen setzen
				SchemeDataStorage.getSchemeList().setActiveSchemeOnScreen(newValue);
				activateScheme();
			}
		}
		
	};
	
	public CreationLogicSchemeBar(ObjectProperty<ECommand> command, SimpleBooleanProperty notSaved)
	{
		super(5);
		this.notSaved = notSaved;
		
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
		
		SchemeListContainer schemeObject = SchemeDataStorage.getSchemeList();
		//TODO need sort ...alphabetical?
		List<Scheme> tempList = schemeObject.getSchemeList();
		
		
		ObservableList<Scheme> selectionComboBox = 
				FXCollections.observableArrayList(tempList);
		
		
		//TODO zu beginn noch nicht befüllt
		schemeComboBox = new ComboBox<Scheme>(selectionComboBox);
		if(schemeObject.getActiveSchemeOnScreenIndex() >= 0)
		{
			for(int i = 0; i < schemeComboBox.getItems().size(); i++)
			{
				if(schemeComboBox.getItems().get(i).getId() == schemeObject.getActiveSchemeOnScreenIndex())
				{
					schemeComboBox.getSelectionModel().select(i);
					break;
				}
			}
		}
		schemeComboBox.setMinWidth(150);
		schemeComboBox.valueProperty().addListener(changeListener);
		
		
		
		
		
		TextField textField = new TextField();
		
		Button save = new Button("save configuration");
		save.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				saveScheme();
				
				
			}
			
		});
		
		Button reset = new Button("reset configuration");
		reset.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				//drop the current xml and copy the tmp file as configuration
				resetConfiguration();
			}

			
		});
		
	
		//erstmal nur die wichtigsten
		ObservableList<EBaseTemplate> tempObsList = 
				FXCollections.observableArrayList(EBaseTemplate.SENSOR,
						EBaseTemplate.FUNCTION,
						EBaseTemplate.AND,
						EBaseTemplate.OR);
		templateBaseComboBox = new ComboBox<EBaseTemplate>(tempObsList);
		
		
		Button addTemplate = new Button("Add template");
		addTemplate.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) 
			{
				//template add
				EBaseTemplate selectedValue = templateBaseComboBox.getSelectionModel().getSelectedItem();
				Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
				if(schemeObject != null)
				{
					boolean refreshView = true;
					switch(selectedValue)
					{
						case SENSOR:
							SensorElement sensorElement = new SensorElement();
							schemeObject.addElementAtList(sensorElement);
							//creae
							break;
						case FUNCTION:
							FunctionElement functionElement = new FunctionElement();
							schemeObject.addElementAtList(functionElement);
							break;
						case AND:
							AndContainer andContainer = new AndContainer();
							schemeObject.addElementAtList(andContainer);
							break;
						case OR:
							OrContainer orContainer = new OrContainer();
							schemeObject.addElementAtList(orContainer);
							break;
						default:
							refreshView = false;
							break;
					}
					if(refreshView)
					{
						command.set(ECommand.ADD_NEW_BASE_TEMPLATE);
						notSaved.set(true);
					}
				}
			}
			
		});
		
		this.getChildren().addAll(newButton, deleteButton, schemeComboBox, textField, save, reset, templateBaseComboBox, addTemplate);
		
	}

	protected void rebuildComboBoxContent() {

		//first remove 
		schemeComboBox.valueProperty().removeListener(changeListener);
		//rebuild the combox and select the scheme on screen
		SchemeListContainer schemeList = SchemeDataStorage.getSchemeList();
		//TODO need sort ...alphabetical?
		List<Scheme> tempList = schemeList.getSchemeList();
		
		ObservableList<Scheme> selectionComboBox = 
				FXCollections.observableArrayList(tempList);
	
		schemeComboBox.getItems().clear();
		schemeComboBox.getItems().addAll(selectionComboBox);
		for(int i = 0; i < schemeComboBox.getItems().size(); i++)
		{
			if(schemeComboBox.getItems().get(i).getId() == schemeList.getActiveSchemeOnScreenIndex())
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
		notSaved.set(true);
		
		
	}

	protected void deleteActiveScheme() 
	{
		//hinweis an GUI, dass ein neuer Container erzeugt wurde
		command.set(ECommand.DELETE_ACTIVE_SCHEME);
		notSaved.set(true);
		
	}

	protected void saveScheme() 
	{
		//TODO ist schmarrn
		command.set(ECommand.SAVE_CONFIGURATION);
		notSaved.set(false);
		
	}

	
	/**
	 * 
	 */
	protected void resetConfiguration()
	{
		//copy the tmp and drop the current xml
		SchemeDataStorage.getInstance().resetFile();
		//reload the xml
		SchemeDataStorage.getInstance().reloadFile();
		
		//rebuild combox with selection of the active view
		rebuildComboBoxContent();
		
		
		
		//inform the content view
		command.set(ECommand.RESET_CONFIGURATION);
		schemeComboBox.setDisable(false);
		notSaved.set(false);

	}
	

	/**
	 * Anderes Schema wurde ausgewählt.
	 */
	protected void activateScheme() {
		command.set(ECommand.ACTIVATED_SCHEME);
	}


}