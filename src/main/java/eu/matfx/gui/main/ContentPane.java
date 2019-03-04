package eu.matfx.gui.main;

import java.util.List;

import eu.matfx.gui.util.ECommand;
import eu.matfx.logic.database.SchemeDataStorage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ContentPane extends Pane
{
	private Stage primaryStage;
	
	private ObjectProperty<ECommand> command;
	
	private Canvas canvas;
	
	private StringProperty statusText;
	private DoubleProperty xCoords;
	private DoubleProperty yCoords;

	public ContentPane(Stage primaryStage, StringProperty statusText, DoubleProperty xCoords, DoubleProperty yCoords, ObjectProperty<ECommand> command) 
	{
		super();
		
		this.command = command;
		
		/* navigator view?
		changedComponentView.addListener(new ChangeListener<Boolean>()
		{

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) 
			{
				
				if(newValue)
				{
					makeNewSnapshot();
					changedComponentView.set(false);
				}
				
				
			}
			
		});
		*/
		
		this.command.addListener(new ChangeListener<ECommand>()
		{

			@Override
			public void changed(ObservableValue<? extends ECommand> observable, ECommand oldValue, ECommand newValue) 
			{
				/* TODO
				SchemeRootContainer schema = TempDataStorage.getSchemeRootContainer();
				switch(newValue)
				{
					case CREATED_NEW_SCHEME:
						ContentPane.this.setStyle("-fx-background-color: #5691b0;");
						command.set(ECommand.NO_COMMAND);
						
						
						break;
						
					//TODO raus ist nur übergangsweise
					case CREATE_NEW_INDEX:
						
						if(schema.getSchemaMap().get(SchemeRootContainer.ROOT_INDEX) != null)
						{
							SensorValue sensorValue = (SensorValue) schema.getSchemaMap().get(SchemeRootContainer.ROOT_INDEX).get(0);
							SensorElement testElement = new SensorElement(sensorValue.getId());
							getChildren().add(testElement);
							testElement.relocate(0, 0);
							addMouseListener(testElement);
							
							
						}
						command.set(ECommand.NO_COMMAND);
						break;
				
					case CREATE_NEW_CONTAINER:
						System.out.println("create New dingsbums");
						if(schema.getSchemaMap().get(1) != null)
						{
							
							ANDContainer andContainer = (ANDContainer) schema.getSchemaMap().get(1).get(0);
							
							
							AndContainerElement testElement = new AndContainerElement("AndContainer");
							getChildren().add(testElement);
							testElement.relocate(0, 0);
							addMouseListener(testElement);
							
						}
						
						
						
						command.set(ECommand.NO_COMMAND);
						break;
				
				
				//TODO
				/*
					case ADDCOMPONENT:
						//Mit add eine neue GroupComponent hinzufügen
						
						//Finde nun einen Bereich wo das Objekt abgelegt werden kann.
					
						ContainerComponent cc = new ContainerComponent(changedComponentView);
						
						Comparator<Node> xValueNumber = (n1, n2) -> Double.compare(n1.getLayoutX(), n2.getLayoutX());
						
						//ausgehend von 0,0 nach rechts verschiebend
						List<Node> nodeList = getIConnectorArea().stream().sorted(xValueNumber).collect(Collectors.toList());
						
						//Abstand
						double X_VALUE = 15;
						double minXValue = 0;
						
						for(int i = 0; i < nodeList.size(); i++)
						{
							double nodeX = nodeList.get(i).getLayoutX();
							if(nodeX >= minXValue && nodeX < (minXValue + X_VALUE))
							{
								minXValue = minXValue + X_VALUE;
							}
							else
							{
		 						break;
							}
						}
						cc.relocate(minXValue, 0);
						addMouseListener(cc);
						ContentPane.this.getChildren().add(cc);
						command.set(E_old_Command.NO_COMMAND);
						break;
					case DELETE_COMPONENT:
						//Selektion sich holen und die Objekte entfernen
						List<Node> selectedNodes = getSelectedNodes();
						
						for(Node nodeToDelete : selectedNodes)
						{
							//TODO wenn verbindung besteht, muss diese aufgelöst werden
							
							ContentPane.this.getChildren().remove(nodeToDelete);
						}
						break;
						
				
				}
				//Sollte das neue Value No_Comamnd kommen so sind alle selektierungen aufzuheben
				List<Node> nodeList = ContentPane.this.getChildren();
				for(int i = 0; i < nodeList.size(); i++)
				{
					//Muss ISelection implementiert haben
					if(nodeList.get(i) instanceof ISelection)
					{
						((ISelection)nodeList.get(i)).setSelected(false);
					}
				}
				makeNewSnapshot();
			}
			*/
			}
			
		});
		
		//Canvas über die komplette Oberfläche; weiß noch nicht wie ich das benötige für die Striche
		//zum zeichnen
		canvas = new Canvas(1400, 900);
		canvas.relocate(0, 0);
		
		this.primaryStage = primaryStage;
		this.statusText = statusText;
		this.xCoords = xCoords;
		this.yCoords = yCoords;
		
		//mal testweise einen container hier ablegen?

		//Canvas muss mit wegen der Bemalung des Hintergrunds
		this.getChildren().addAll(canvas);
		//containerComponent.recalcualteCenterPoint();
		
		
		if(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen() >= 0)
		{
			//put scheme on screen
			rebuildView();
		}
		
		
		
		
		
		//TODO raus ist auch nur tesweise
		//if(TempDataStorage.getSchemeRootContainer() != null)
		//{
		//	ContentPane.this.setStyle("-fx-background-color: #5691b0;");
	//	}
	//
	}

	private void rebuildView() 
	{
		ContentPane.this.setStyle("-fx-background-color: #5691b0;");
		
		//TODO more content with positioning and draw of the scheme
		
		
	}

}
