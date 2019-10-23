package eu.matfx.gui;


import java.util.Optional;

import eu.matfx.gui.main.ContentPane;
import eu.matfx.gui.main.HeightSceneChangeListener;
import eu.matfx.gui.main.RightPane;
import eu.matfx.gui.main.ScrollPaneExtended;
import eu.matfx.gui.main.StatusPane;
import eu.matfx.gui.main.TopPane;
import eu.matfx.gui.main.WidthSceneChangeListener;
import eu.matfx.gui.util.ECommand;
import eu.matfx.logic.database.SchemeDataStorage;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Editor extends Application 
{
	private ObjectProperty<ECommand> command = new SimpleObjectProperty<ECommand>();
	
	/**
	 * flag to see the state of the schemelist container
	 */
	private SimpleBooleanProperty notSaved = new SimpleBooleanProperty();
	
	private StringProperty statusText = new SimpleStringProperty("Start");
	
	private DoubleProperty xCoords = new SimpleDoubleProperty(0D);
	
	private DoubleProperty yCoords = new SimpleDoubleProperty(0D);
	
	private DoubleProperty wCoords = new SimpleDoubleProperty(0D);
	
	private DoubleProperty hCoords = new SimpleDoubleProperty(0D);
	
	private ContentPane panelsPane;
	
	
	//TODO command missing

	@Override
	public void start(Stage primaryStage) 
	{
		try
		{
			//init of storage and other singleton objects
			SchemeDataStorage.initSchemeDataStorage();
			//aktuelle xml Konfiguration des Schemas in einen tmp Ordner sichern
			SchemeDataStorage.getInstance().saveCurrentFile();
			
			
			command.set(ECommand.NO_COMMAND);
			
			
			
			final BorderPane sceneRoot = new BorderPane();
			Scene scene = new Scene(sceneRoot, 1400, 900);
			primaryStage.setScene(scene);
			
			TopPane tbp = new TopPane(primaryStage, command, notSaved);
			sceneRoot.setTop(tbp);
			
			panelsPane = new ContentPane(primaryStage, statusText, xCoords, yCoords, command, notSaved);
			//panelsPane.initSnapShotContainer();
			
			ScrollPaneExtended scrollPane = new ScrollPaneExtended();
		 
			scrollPane.setContent(panelsPane);
			sceneRoot.setCenter(scrollPane);
			sceneRoot.setId("sceneroot");
		
			
			BorderPane.setAlignment(panelsPane, Pos.TOP_LEFT);
		    
		    StatusPane statusPane = new StatusPane(primaryStage, statusText, xCoords, yCoords);
		    sceneRoot.setBottom(statusPane);
		    
		    RightPane rightPane = new RightPane(primaryStage);
		    sceneRoot.setRight(rightPane);
		    
		   // scrollPane.viewportBoundsProperty().addListener(rightPane.getBoundsListener());
		   // scrollPane.vvalueProperty().addListener(rightPane.getVScrollListener());
		   // scrollPane.hvalueProperty().addListener(rightPane.getHScrollListener());
		   
			primaryStage.show();
			//Ein Snapshot funktioniert es dann wie gewünscht wenn einmalig gerendert worden ist.
			//css und co kommt erst mit show in Spiel
			//panelsPane.makeNewSnapshot();
			//Ich vermute die Anbindung darf erst nach dem Snapshot erfolgen...wegen Breite-/Höhenermittlung in der
			//Miniaturansicht.
			
			//rightPane.getH_scroll().addListener(scrollPane.getHScrollListener());
			//rightPane.getV_scroll().addListener(scrollPane.getVScrollListener());
			
			//Zuständig um den resize der komplette Szene "abzufangen" bzw. der ScrollPane weiterzugeben.
			scene.widthProperty().addListener(new WidthSceneChangeListener(scrollPane,panelsPane, panelsPane.getWidth()));
			scene.heightProperty().addListener(new HeightSceneChangeListener(scrollPane, panelsPane, panelsPane.getHeight()));
			
			
			primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> closeRequest());
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void closeRequest() 
	{
		if(notSaved.get())
		{
			//TODO evtl. Nachfrage ob noch nicht gespeicherte Einstellungen verworfen werden sollen.
			Alert alert = new Alert(AlertType.CONFIRMATION);
			//TODO language
			alert.setTitle("Hinweis");
			alert.setHeaderText("Aktuelle Einstellungen sind noch nicht gespeichert.");
			alert.setContentText("Solle eine Speicherung durchgeführt werden?");
			alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.YES)
			{
				//sicherstellen, dass die aktuelle Sicht mit allen veränderten Koordinaten auch in der Map landet
				panelsPane.saveActiveScheme();
			} 
			else 
			{
				//Copy tmp to file to normale store folder
				SchemeDataStorage.getInstance().resetFile();
			}
		}
		SchemeDataStorage.getInstance().cleanTmpFilesOrFolder();
	
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
