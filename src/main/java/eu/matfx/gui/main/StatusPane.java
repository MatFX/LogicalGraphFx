package eu.matfx.gui.main;


import eu.matfx.gui.util.UtilFx;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StatusPane extends HBox
{
	private Stage primaryStage;
	private DoubleProperty xCoords;
	private DoubleProperty yCoords;
	
	public StatusPane(Stage stage, StringProperty statusInhalt, DoubleProperty xCoords, DoubleProperty yCoords)
	{
		super();
		this.primaryStage = stage;
		//top RIGHT bottom LEFT
		this.setPadding(new Insets(2, 10, 2, 10));
	
		
		Label label = new Label("");
		label.setFont(new Font("Cambria", 24));
		//Binding mit Prefix
		label.textProperty().bind(Bindings.concat("Status: ").concat(statusInhalt));
		
		Label xyCoords = new Label("");
		xyCoords.setFont(new Font("Cambria", 24));
		xyCoords.textProperty().bind(Bindings.concat(" x: ").concat(xCoords).concat(" y: ").concat(yCoords));
		
		this.getChildren().addAll(label, UtilFx.createHGrowSpacer(), xyCoords);
	}
	

}
