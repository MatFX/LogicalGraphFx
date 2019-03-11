package eu.matfx.gui.main;


import java.util.Map.Entry;


import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import eu.matfx.gui.component.AUIElement;
import eu.matfx.gui.component.AUIInputOutputElement;
import eu.matfx.gui.component.AUIOutputElement;
import eu.matfx.gui.component.impl.UILineConnector;
import eu.matfx.gui.interfaces.UILineInputConnector;
import eu.matfx.gui.interfaces.UILineOutputConnector;
import eu.matfx.gui.util.ECommand;
import eu.matfx.gui.util.UtilFx;
import eu.matfx.logic.Scheme;
import eu.matfx.logic.SchemeList;
import eu.matfx.logic.data.ALogicElement;
import eu.matfx.logic.database.SchemeDataStorage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class ContentPane extends Pane
{
	private Stage primaryStage;
	
	private ObjectProperty<ECommand> command;
	
	private Canvas canvas;
	
	private StringProperty statusText;
	private DoubleProperty xCoords;
	private DoubleProperty yCoords;
	
	private double orgSceneX, orgSceneY;
	private double orgTranslateX, orgTranslateY;
	
	private TreeMap<Integer, AUIElement> uiMap = new TreeMap<Integer, AUIElement>();

	//TODO rau test to show the points
	private Circle transCircle;
	
	private Circle mousePointCircle;

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
		
		transCircle = new Circle();
		transCircle.setRadius(4);
		transCircle.setFill(Color.BROWN);
		transCircle.setLayoutX(0);
		transCircle.setLayoutY(0);
		
		mousePointCircle = new Circle();
		mousePointCircle.setRadius(4);
		mousePointCircle.setFill(Color.DARKSALMON);
		mousePointCircle.setLayoutX(0);
		mousePointCircle.setLayoutY(0);
		
		this.getChildren().addAll(transCircle, mousePointCircle);
		
		
		if(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen() >= 0)
		{
			//put scheme on screen
			rebuildView();
		}
	}

	private void rebuildView() 
	{
		//with the rebuild the map must be deleted
		uiMap = new TreeMap<Integer, AUIElement>();
		ContentPane.this.setStyle("-fx-background-color: #5691b0;");
		
		Scheme schemeObject  = SchemeDataStorage.getSchemeList().getSchemeList().get(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen());
		
		if(schemeObject != null)
		{
			SortedMap<Integer, ALogicElement> workflowMap = schemeObject.getWorkflowMap();
			
			for(Entry<Integer, ALogicElement> entry : workflowMap.entrySet())
			{
				ALogicElement aLogicElement = entry.getValue();
				
				AUIElement createdElement = AUIElement.getInstance(aLogicElement);
				if(createdElement != null)
				{
					//in the first draw no line connector!
					if(!(createdElement instanceof UILineConnector))
					{
					
						//TODO was ist mit größe und location?
						ContentPane.this.getChildren().add(createdElement);
						
						//TODO positioning 
						createdElement.moveComponent(1, 1);
						createdElement.recalcualteCenterPoint();
						
						addMouseListener(createdElement);
						
						
					}
					uiMap.put(entry.getKey(), createdElement);
				}
			}
			
			
			
			//with the second draw the line connector came on view
			for(Entry<Integer, AUIElement> entry : uiMap.entrySet())
			{
				if(entry.getValue() instanceof UILineConnector)
				{
					//we need two listener one for the output and one for the input
					//When any component will change the position the line connector will change the position
					
					UILineConnector connector = (UILineConnector)entry.getValue();
					//no question about the pickup of the value 
					if(uiMap.get(uiMap.lowerKey(entry.getKey())) instanceof UILineOutputConnector)
					{
						((UILineOutputConnector)uiMap.get(uiMap.lowerKey(entry.getKey()))).setUIOutputConnector(connector);
					}
					
					if(uiMap.get(uiMap.higherKey(entry.getKey())) instanceof UILineInputConnector)
					{
						((UILineInputConnector)uiMap.get(uiMap.higherKey(entry.getKey()))).setUIInputConnector(connector);
					}
					ContentPane.this.getChildren().add(connector);
					addMouseListener(connector);
					
				}
			}
		}
	}
	
	/**
	 * Alle Komponenten auf der Hauptoberfläche erhalten die gleiche Mausaktivitäten.
	 * <br>Muss ich diese auch wieder entfernen, wenn ich die Komponente aus der Oberfläche entfernen will?
	 * @param node
	 */
	private void addMouseListener(Node node)
	{
		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);
	}
	
	/**
	 * Ein Teil für das Verschieben. Hier das festhalten der aktuellen Coords
	 */
	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() 
	{
	 
	        @Override
	        public void handle(MouseEvent t) 
	        {
	        	
	        	switch(command.get())
	        	{
	        		//Liegt kein Kommando an, dann darf verschoben, skaliert oder neue verbunden werden
		        	default:
	        		case NO_COMMAND:
		        		//Nur GroupComponent kann hier bearbeitet werden
		            	if(!(t.getSource() instanceof AUIElement))
		            		return;
			        	//Eine Mausanfrage wird nur bearbeitet wenn kein Kommando anliegt
			        	if(command.get() == ECommand.NO_COMMAND)
			        	{
			        		//Move nur dann wenn wir uns mit dem Punkt auf der einer gültige Komponent uns befinden
			        		AUIElement node = (AUIElement) t.getSource();
			        		Point2D point2d = new Point2D(t.getSceneX(), t.getSceneY());
			        		
			        	
			        		//line connector selected? to delete the line the user must select the line
			        		if(node instanceof UILineConnector)
			        		{
			        			((UILineConnector)node).setSelected(true);
			        			
			        		}
			        		else if(node.isOutputArea(UtilFx.getPointFromEvent(t)))
			        		{
			        			Point2D point2D = node.getOutputCenterPoint();
			        			
			        			//Abmaße der ContentPane die bei der Startkoordinate berücksichtigt werden müssen.
				        		Bounds conBounds = ContentPane.this.localToScene(ContentPane.this.getLayoutBounds());
				        		ContentPane.this.getScene().setCursor(Cursor.HAND);
				        		statusText.set("OutputArea erkannt");
				        		
				        		//line = new ConnectorDrawLine(EConnector.OUTPUT_CONNECTOR, point2D.getX(), point2D.getY()-conBounds.getMinY(), point2D.getX(), point2D.getY()-conBounds.getMinY());
				        		//line.setStrokeWidth(2);
				        		//line.setStroke(Color.CORAL);
				        		//ContentPane.this.getChildren().add(line);
			        			
			        			
			        			
			        		}
			        		else if(node.isMovePossible(t))
			        		{
				        		//noch keine Ahnung wie ich das mit der Kollisionsabfrage mache
				        		ContentPane.this.getScene().setCursor(Cursor.MOVE);
					        	
				        		Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
				        		orgSceneX = transferCoord.getX();
					            orgSceneY = transferCoord.getY();
				        		
					            orgTranslateX = ((Node)(t.getSource())).getTranslateX();
					            orgTranslateY = ((Node)(t.getSource())).getTranslateY();
				                statusText.set("Bewegung erkannt");
			        		}
			        		
			        		
			        		
			        	}
		        		break;
		        	//Auswahl wurde in der Menüleiste ausgewählt
		        	/*
	        		case SELECT:
		        		//Bei einer Selektion muss festgestellt werden was selektiert worden ist
		        		PickResult pickResult = t.getPickResult();
		        		
		        		//jetzt einmal bitte highlgiht der Auswahl 
		        		Node node = pickResult.getIntersectedNode();
		        		node.setEffect(new Glow(0.8));
		        		UtilFx.setSelected(node);
		        		break;
		        		*/
	        	
	        		
	        	}
	        	//navigator view
	        	//makeNewSnapshot();
	        }

	};
	
	/**
	 * Dragged und somit die Verschiebung der Komponente auf der Oberfäche
	 */
	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() 
	{
 
        @Override
        public void handle(MouseEvent t) {
        	
        	
        	//TODO schauen was später besser past
        	if(!(t.getSource() instanceof AUIElement))
        		return;
        	
        	AUIElement node = (AUIElement) t.getSource();
        	
         	//if the line connector selected and the mouse is out of a range ...the line will be deleted
           	if(node instanceof UILineConnector)
    		{
           		
           		Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
           		//System.out.println(" transferCoord " + transferCoord);
           		
           		Point2D point2d = new Point2D(t.getSceneX(), t.getSceneY());
           		
           		//System.out.println(" test " + new Point2D(t.getSceneX(), t.getSceneY()));
           		
           		
           		transCircle.setLayoutX(transferCoord.getX());
           		transCircle.setLayoutY(transferCoord.getY());
           		
           		mousePointCircle.setLayoutX(point2d.getX());
           		mousePointCircle.setLayoutY(point2d.getY());
           		
           		if(((UILineConnector)node).isSelected())
           		{
           			
           			if(((UILineConnector)node).isOuterTolerance(transferCoord))
           			{
           				//show delete color
           				((UILineConnector)node).setDeleteColor();
           			}
           			else
           			{
           				//change to normal select color
           				((UILineConnector)node).removeDeleteColor();
           			}
           		}
           	}
           	else if(ContentPane.this.getScene().getCursor() == Cursor.MOVE)
        	{
        		//Habe noch keine Ahnung wie ich eine Kollisionsabfrage reinfummel
        		Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
        		
        		if(transferCoord.getX() < 0)
        			transferCoord = new Point2D(0, transferCoord.getY());
        		if(transferCoord.getY() < 0)
        			transferCoord = new Point2D(transferCoord.getX(), 0);
        	
        		double offsetX = transferCoord.getX() - orgSceneX;
                double offsetY = transferCoord.getY() - orgSceneY;
              
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;
                
                //jetzt noch die Korrektur für den Fall die Komponente wird außerhalb der möglichen darstellebaren Fläche verschoben
                if(newTranslateX < 0)
                	newTranslateX = 0;
            	if(newTranslateY < 0)
            		newTranslateY = 0;
            	
            	//nicht außerhalb setzen lassen
            	if((newTranslateX + node.getBoundsInLocal().getWidth()) > ContentPane.this.getWidth())
            		newTranslateX = ContentPane.this.getWidth() -  node.getBoundsInLocal().getWidth(); 
            	if((newTranslateY + node.getBoundsInLocal().getHeight()) > ContentPane.this.getHeight())
            		newTranslateY = ContentPane.this.getHeight() - node.getBoundsInLocal().getHeight();
                
                node.moveComponent(newTranslateX, newTranslateY);
        	}
        	/*
        	else if(ContentPane.this.getScene().getCursor() == Cursor.HAND)
        	{
        		EConnector eConnector = line.getStartPunkt();
        		//Bei Hand muss vom Ausgangspunkt etwas gezeichnet werden.
        		
        		Point2D mousePoint = new Point2D(t.getSceneX(), t.getSceneY());
        		//Umsetzung ist notwendig wegen der Menüleiste die evtl. noch in der Oberfläche abgelegt ist.
        		Point2D translated = ContentPane.this.sceneToLocal(mousePoint);
	        	
        		//Weitergabe als Info für die Statusleiste
        		xCoords.set(translated.getX());
	        	yCoords.set(translated.getY());
	        	
	        	ContentPane.this.line.setEndX(translated.getX());
	        	ContentPane.this.line.setEndY(translated.getY());
	        
	        	//Hier noch prüfen ob ablegbar ist
	        	//Dementsprechend soll die Farbe des Striches geändert werden
	        	//Bei Y wieder den Wert korrigieren mit den ConBounds
	        	Bounds conBounds = ContentPane.this.localToScene(ContentPane.this.getLayoutBounds());
	        	boolean isConnectable = isConnectable(ContentPane.this.line.getEndX(), ContentPane.this.line.getEndY()+conBounds.getMinY(), node, eConnector);
	        	
	        	if(isConnectable)
	        		ContentPane.this.line.setStroke(Color.BLUE);
	        	else
	        		ContentPane.this.line.setStroke(Color.CORAL);
	        
        	}*/
        	/*
        	else if(ContentPane.this.getScene().getCursor() == Cursor.SE_RESIZE)
        	{
        		//Transfer auf die parent Koordinate
        		Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
        		Bounds boundsInParent = node.getBoundsInParent();
        		double newWidth = transferCoord.getX() - boundsInParent.getMinX();
        		double newHeight = transferCoord.getY() - boundsInParent.getMinY();
        		
        		double scaleFactorWidth = newWidth / node.getMinWidth();
        		double scaleFactorHeight = newHeight / node.getMinHeight();
        		
        		if(scaleFactorWidth < 1D)
        			scaleFactorWidth = 1D;
        		if(scaleFactorHeight < 1D)
        			scaleFactorHeight = 1D;
        		
        		node.setScale(scaleFactorWidth, scaleFactorHeight);
         	}*/
        	//makeNewSnapshot();
        }

    };
    
    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() 
   	{
    
           @Override
           public void handle(MouseEvent t) 
           {
           	//Nur GroupComponent kann hier bearbeitet werden
           	//TODO schauen was am besten später passt
           	if(!(t.getSource() instanceof AUIElement) )
           		return;
           	
           	
           	AUIElement node = (AUIElement) t.getSource();
           	if(node instanceof UILineConnector)
    		{
           		UILineConnector uiNode = ((UILineConnector)node);
           		if(uiNode.isDeletedDesignated())
           		{
           			//TODO remove from map
           			ContentPane.this.getChildren().remove(uiNode);
           			
           			
           			
           		}
           		else
           		{
           			uiNode.setSelected(false);
           		}
           		
    			
    			
    		}
           
           	/*
           	if(ContentPane.this.getScene().getCursor() == Cursor.HAND)
           	{
           		//Feststellen wo man zum stehen gekommen ist
           		EConnector eConnector = line.getStartPunkt();
           	
           		SchemeElement ausloeser = (SchemeElement) t.getSource();
           		
           		//Wird über das MouseEvent ermittelt..die Endpunkte der bisherig gezeichneten Linie sollten nicht verwendet werden.
           		Point2D endpunktLinie = new Point2D(t.getSceneX(), t.getSceneY());
           		
           		List<Node> nodeList = ContentPane.this.getChildren();
           		endpunktLinie = ContentPane.this.localToParent(endpunktLinie);
           		
           		Bounds conBounds = ContentPane.this.localToScene(ContentPane.this.getLayoutBounds());
           		
           		GenericPairVO<GenericPairVO<Coordinate, Coordinate>, SchemeElement> targetComponent = getTargetComponent(ContentPane.this.line.getEndX(), ContentPane.this.line.getEndY()+conBounds.getMinY(), ausloeser, eConnector);
   	        	 
           		//ungleich null, dann ist eine Verbindung möglich
           		if(targetComponent != null && targetComponent.getRight() != null)
           		{
           			ContentPane.this.line.setStroke(Color.RED);
           			
           			//Gefunden jetzt linie nehmen und "zementieren".
           			//ContentPane.this.getChildren().remove(line);
           		
           			//TODO weiß ich noch nicht wie die Verbindung zustande kommt.
           			/*Object createdObject = targetComponent.getRight().addObjectToEntry(targetComponent.getRight().getEntryObject());
           			//ListCellElement listCellElement = (ListCellElement) createdObject;
           			
           			
           			//Ist empty zu dem Zeitpunkt!
           			CircleComponent circleComponent = listCellElement.getCircleComponent();
           			
           			
           			//Sollte der inhalt nicht sichtbar sein, dann jetzt öffnen
           			if(!targetComponent.getRight().isInhaltSichtbar())
           			{
           				
           				targetComponent.getRight().openContentView();
           				//Nach dem Öffnen ist sichergestellt, dass die Komponete gezeichnet ist
           				
           			}
           			
           		}
           		else
           		{
           			//Linie löschen
           			ContentPane.this.getChildren().remove(line);
           		}
           	}*/
           	//makeNewSnapshot();
           	//Cursor auf normal zurücksetzen
            ContentPane.this.getScene().setCursor(Cursor.DEFAULT);
           }
       };

}
