package eu.matfx.gui.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import eu.matfx.gui.component.AUIElement;
import eu.matfx.gui.component.AUIInputOutputElement;
import eu.matfx.gui.component.AUIOutputElement;
import eu.matfx.gui.component.impl.UILineConnector;
import eu.matfx.gui.helper.GenericPair;
import eu.matfx.gui.helper.SelectionRectangle;
import eu.matfx.gui.helper.TempLine;
import eu.matfx.gui.interfaces.IConnectorArea;
import eu.matfx.gui.interfaces.UILineInputConnector;
import eu.matfx.gui.interfaces.UILineOutputConnector;
import eu.matfx.gui.util.ECommand;
import eu.matfx.gui.util.UtilFx;
import eu.matfx.logic.Scheme;
import eu.matfx.logic.data.AInputOutputElement;
import eu.matfx.logic.data.ALogicElement;
import eu.matfx.logic.data.AOutputElement;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.database.SchemeDataStorage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
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
	
	private TempLine tempLine;
	
	private SelectionRectangle selectionRect;
	
	/**
	 * is filled when with the selectionRect some uiElements grouped and moved.
	 * <br>Changelistener are connected with x and y from selectionRect
	 */
	private HashMap<AUIElement, GenericPair<ChangeListener, ChangeListener>> changeListenerMap;
	
	private Circle startRectangle = new Circle();
	
	
	
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
				//TODO
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
		
		//canvas needs listener for the selection mechanism
		addMouseListener(canvas);
		//TODO raus 
		canvas.setOnMouseMoved(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) 
			{
				statusText.set("x: " + event.getSceneX() + " y: " + event.getSceneY());
				
			}
			
		});
		
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
					
					LineConnector lineConnector = connector.getLogicElement();
					//one entry empty, dont draw the line 
					if(!lineConnector.isOutputEmpty() && !lineConnector.isInputEmpty())
					{
						//find the output connector
						UILineOutputConnector outputConnector = (UILineOutputConnector) getConnector(lineConnector.getOutputId());
						UILineInputConnector inputConnector = (UILineInputConnector) getConnector(lineConnector.getInputId());
						outputConnector.setUIOutputConnector(connector);
						inputConnector.setUIInputConnector(connector);
					}
					ContentPane.this.getChildren().add(connector);
					addMouseListener(connector);
					
				}
			}
		}
	}
	
	private AUIElement getConnector(GenericPair<Integer, Integer> inputId) 
	{
		Scheme schemeObject  = SchemeDataStorage.getSchemeList().getSchemeList().get(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen());
    	
		for(Entry<Integer, AUIElement> entry : uiMap.entrySet())
		{
			if(entry.getValue().getLogicElement().getIndex() == inputId.getLeft()
					&& (entry.getValue() instanceof UILineOutputConnector || entry.getValue() instanceof UILineInputConnector))
			{
				return entry.getValue();
			}
			
		}
		return null;
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
	        			
	        			Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
	        			System.out.println("getSource " + t.getSource());
	        			if(t.getSource() instanceof Canvas)
	        			{
	        				//ich habe bereits ein rechteck gezeichnet
	        				//dieses liegt auch auf der oberfläche
	        				boolean drawNew = true;
	        				if(selectionRect != null && ContentPane.this.getChildren().contains(selectionRect))
	        				{
	        					//is the mouse click inside the rect values or outside
	        					//outside => drawNew
	        					
	        					//inside => moveable
	        					Bounds boundsSelectionRect = new BoundingBox(selectionRect.getLayoutX(), selectionRect.getLayoutY(), selectionRect.getWidth(), selectionRect.getHeight());
	        					Bounds localBounds = selectionRect.localToScene(selectionRect.getBoundsInLocal());
	        					
	        					if(UtilFx.isPointInShape(transferCoord, localBounds))
	        					{
	        						//TODO raus
	        						selectionRect.setStroke(Color.BLUE);
	        						selectionRect.setCatchedUIElements(true);
	        						selectionRect.setStartCoordsMovement(transferCoord);
	        						
	        						//need empty list to store the connected changelistener
	        						changeListenerMap = new HashMap<AUIElement, GenericPair<ChangeListener, ChangeListener>>();
	        						
	        						
	        						Point2D movementInitCoords = selectionRect.getMovementStartCoords();
	        					
	        						//statusText.set("MoveInitCoord: " + movementInitCoords.getX() + "/" + movementInitCoords.getY() + " transCoord " + transferCoord.getX() +  "/" + transferCoord.getY());
	        						
	        						//TODO raus
	        						startRectangle = new Circle();
	        						startRectangle.setRadius(3);
	        						startRectangle.setFill(Color.BROWN);
	        						startRectangle.setCenterX(	selectionRect.getLayoutX());
	        						startRectangle.setCenterY( 	selectionRect.getLayoutY());
	        					
	        						
	        						//find the components and connect the visulisation
	        						
	        						for(int i = 0; i < ContentPane.this.getChildren().size(); i++)
	        						{
	        							   Node node = ContentPane.this.getChildren().get(i);
	        							   //only uielements
	        							   if(node instanceof AUIElement)
	        							   {
	        								   AUIElement uiElement = (AUIElement)node;
	        								   //ausschlaggebend sind nur die Elemente die nicht Line sind
	        								   if(!(uiElement instanceof UILineConnector))
	        								   {
	        									  //TODO change width and height from component
	        									  Bounds uiBounds = new BoundingBox(uiElement.getTranslateX(), uiElement.getTranslateY(), 150D, 150D);
	        									 
	        									  if(UtilFx.isUIElementInShape(uiBounds,  boundsSelectionRect))
	        									  {
	        										  //calculate the x/y from uielement to x/y to rectangle
	        										  
	        										  
	        										  uiElement.setGroupedMovementStartCoords(movementInitCoords);
	        										  ChangeListener<Number> xListener = new ChangeListener<Number>(){

														@Override
														public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
															uiElement.setGroupedMovementX(selectionRect.getLayoutX());
														}
	        											  
	        										  };
	        										  ChangeListener<Number> yListener = new ChangeListener<Number>(){

															@Override
															public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
																uiElement.setGroupedMovementY(selectionRect.getLayoutY());
																
															}
		        											  
	        										  };
	        										  selectionRect.getGroupedMovementProperties().getLeft().addListener(xListener);
	        										  selectionRect.getGroupedMovementProperties().getRight().addListener(yListener);
		        										  
	        										  
		        									  changeListenerMap.put(uiElement, new GenericPair<ChangeListener, ChangeListener>(xListener, yListener));
		        									  
	        										  //change visulization from the element
	        										  //uiElement.collected(true);
	        										  
	        										 
	        									  }
	        									  else
	        										  statusText.set("UIElement liegt nicht im Rechteck");
	        								   }
	        							   }
	        						}
	        						drawNew = false;
	        						ContentPane.this.getScene().setCursor(Cursor.MOVE);
	        					}
	        					else
	        					{

	                 			   	//sicherheitshalber die uiMap zurücksetzen
	                 			   	for(Entry<Integer, AUIElement> entry : uiMap.entrySet())
	                 			   	{
	                 			   		if(!(entry.getValue() instanceof UILineConnector))
	                 			   		{
	                 			   			entry.getValue().collected(false);
	                 			   		}
	                 			   	}
	        						//TODO raus
	        						selectionRect.setStroke(Color.RED);
	        						selectionRect.setCatchedUIElements(false);
	        						drawNew = true;
	        						ContentPane.this.getChildren().remove(selectionRect);
	        					}
	        				}
	        			
	        				if(drawNew)
	        				{
	        					//no selection rect on screen draw new rectangle
		        				
		        				selectionRect = new SelectionRectangle(transferCoord.getX(), transferCoord.getY());
		        				
		        				selectionRect.setWidth(1);
		        				selectionRect.setHeight(1);
		        				selectionRect.setStrokeWidth(0.5);
		        				selectionRect.getStrokeDashArray().addAll(2.0,5.0,2.0,5.0);
		        				selectionRect.setStroke(Color.ANTIQUEWHITE);
		        				selectionRect.setFill(Color.web("#00000000"));
		        				
		        				ContentPane.this.getChildren().add(selectionRect);
	        				}
	        			}
	        			else if(t.getSource() instanceof AUIElement)
	        			{

			        		//Move nur dann wenn wir uns mit dem Punkt auf der einer gültige Komponent uns befinden
			        		AUIElement node = (AUIElement) t.getSource();
			        		Point2D point2d = new Point2D(t.getSceneX(), t.getSceneY());
			        		
			        		
			        		//line connector selected? to delete the line the user must select the line
			        		if(node instanceof UILineConnector)
			        		{
			        			((UILineConnector)node).setSelected(true);
			        		
			        		}
			        		else if(node instanceof AUIElement)
			        		{
			        			((AUIElement)node).setSelected(true);
			        		}
			        		//TODO else if?
			        		
			        		
			        		if(node.isOutputArea(UtilFx.getPointFromEvent(t)))
			        		{
			        			Point2D point2D = node.getOutputCenterPoint();
			        			
			        			//Abmaße der ContentPane die bei der Startkoordinate berücksichtigt werden müssen.
				        		Bounds conBounds = ContentPane.this.localToScene(ContentPane.this.getLayoutBounds());
				        		
				        		statusText.set("OutputArea erkannt");
				        		
				        		Scheme schemeObject  = SchemeDataStorage.getSchemeList().getSchemeList().get(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen());
				        		
				        		System.out.println("node " + node.getLogicElement());
				        		System.out.println("node " + node.getClass().toString());
				        		//is the output channel occupied
				        		
				        		if(node instanceof UILineOutputConnector)
				        		{
				        			//check the output connection
				        			UILineOutputConnector outputConnector = (UILineOutputConnector)node;
					        		if(!outputConnector.isUIOutputOccupied())
				        			{
						        		int startIndex = schemeObject.getIndexFromLogicElement(node.getLogicElement());
						        		if(startIndex >= 0)
						        		{
						        			//build a temp line
							        		tempLine = new TempLine(startIndex);
							        		tempLine.setStrokeWidth(3);
							        		tempLine.setStroke(Color.BLUE);
							        		tempLine.setStartX(node.getOutputCenterCoordinate().getX());
							        		tempLine.setStartY(node.getOutputCenterCoordinate().getY());
							        		tempLine.setEndX(node.getOutputCenterCoordinate().getX());
							        		tempLine.setEndY(node.getOutputCenterCoordinate().getY());
							        		ContentPane.this.getChildren().add(tempLine);
							        		//ok signal flag for the other listener
							        		ContentPane.this.getScene().setCursor(Cursor.HAND);
							        	}
				        			}
				        		}
				        		
			        		}
			        		else if(node.isMovePossible(t))
			        		{
			        			//noch keine Ahnung wie ich das mit der Kollisionsabfrage mache
				        		ContentPane.this.getScene().setCursor(Cursor.MOVE);
					        	
				        		orgSceneX = transferCoord.getX();
					            orgSceneY = transferCoord.getY();
				        		
					            orgTranslateX = ((Node)(t.getSource())).getTranslateX();
					            orgTranslateY = ((Node)(t.getSource())).getTranslateY();
				                statusText.set("Bewegung erkannt");
			        		}
			        		
	        			}
	        			break;
		        }
	        	//navigator view
	        	//makeNewSnapshot();
	        }

	};
	
	/**
	 * movement from components on the view
	 */
	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() 
	{
 
        @Override
        public void handle(MouseEvent t)
        {
        	Point2D test = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
        	statusText.set("x: " + test.getX() + " y " + test.getY());
        	
        	if(t.getSource() instanceof Canvas)
        	{
        		Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
        		if(ContentPane.this.getScene().getCursor() == Cursor.MOVE)
            	{
        			//change the position of all components
        			selectionRect.setMovementRectangle(transferCoord, ContentPane.this.getWidth(), ContentPane.this.getHeight());
        		
        			//TODO inner componet must be moved
        			
        			
        			
        			
            	}
        		else
        		{
            		//check ne new coordinate from the mouseevent und change  the orientation or size
            	
        			double w = 1;
            		double h = 1;
            		if(selectionRect.getStartX() > transferCoord.getX())
            		{
            			//obacht hier startx anpassen
            			w = selectionRect.getStartX() - transferCoord.getX();
            			selectionRect.setLayoutX(transferCoord.getX());
            		}
            		else
            		{
            			w = transferCoord.getX() - selectionRect.getStartX();
            			selectionRect.setLayoutX(selectionRect.getStartX());
            		}
            		
            		if(selectionRect.getStartY() > transferCoord.getY())
            		{
            			h = selectionRect.getStartY() - transferCoord.getY();
            			selectionRect.setLayoutY(transferCoord.getY());
            			
            		}
            		else
            		{
            			h = transferCoord.getY() - selectionRect.getStartY();
            			selectionRect.setLayoutY(selectionRect.getStartY());
            		}
            		
            		selectionRect.setWidth(w);
            		selectionRect.setHeight(h);
        			
        		}
        		
        	}
        	else if(t.getSource() instanceof AUIElement)
        	{
        		AUIElement node = (AUIElement) t.getSource();
            	
             	//if the line connector selected and the mouse is out of a range ...the line will be deleted
               	if(node instanceof UILineConnector)
        		{
               		
               		Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
               		
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
               	else if(ContentPane.this.getScene().getCursor() == Cursor.HAND)
               	{
               		Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
               		tempLine.setEndX(transferCoord.getX());
               		tempLine.setEndY(transferCoord.getY());
               	}
            }
        }

    };
    
    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() 
   	{
    
           @Override
           public void handle(MouseEvent t) 
           {
        	   if(t.getSource() instanceof Canvas)
        	   {
        		   
        		   if(selectionRect.isCatchedUIElements())
        		   {
        			   //I think nothing to do
        			   
        		   }
        		   else
        		   {
        			   //no component selected, search for it
        			   
            		   
            		   //beim loslassen muss geprüft werden ob in dem gezeicneten Rechteck eine Komponente liegt
            		   
            		   boolean isComponentsInRect = false;
            		   
            		   Bounds boundsSelectionRect = new BoundingBox(selectionRect.getLayoutX(), selectionRect.getLayoutY(), selectionRect.getWidth(), selectionRect.getHeight());
            		   
            		   
            		   //TODO iterate through ui map?
            		   for(int i = 0; i < ContentPane.this.getChildren().size(); i++)
            		   {
            			   Node node = ContentPane.this.getChildren().get(i);
            			   //only uielements
            			   if(node instanceof AUIElement)
            			   {
            				   AUIElement uiElement = (AUIElement)node;
            				   //ausschlaggebend sind nur die Elemente die nicht Line sind
            				   if(!(uiElement instanceof UILineConnector))
            				   {
            					  //TODO widht and heigt from component
            					  Bounds uiBounds = new BoundingBox(uiElement.getTranslateX(), uiElement.getTranslateY(), 150D, 150D);
            					  
            					  if(UtilFx.isUIElementInShape(uiBounds,  boundsSelectionRect))
            					  {
            						  //change visulization from the element
            						  uiElement.collected(true);
            						  
            						  isComponentsInRect = true;
            					  }
            				   }
            			   }
            		   }
            		   
            		   
            		   
            		   
            		   if(!isComponentsInRect)
            		   {
            			
            			   if(changeListenerMap != null)
            			   {
            				   
            				   for(Entry<AUIElement, GenericPair<ChangeListener, ChangeListener>>  entry : changeListenerMap.entrySet())
            				   {
            					   selectionRect.getGroupedMovementProperties().getLeft().removeListener(entry.getValue().getLeft());
                				   selectionRect.getGroupedMovementProperties().getRight().removeListener(entry.getValue().getRight());
            					   
            				   }
            				   changeListenerMap = new HashMap<AUIElement, GenericPair<ChangeListener, ChangeListener>>();
            			   }
            			   
            			   //sicherheitshalber die uiMap zurücksetzen
            			   for(Entry<Integer, AUIElement> entry : uiMap.entrySet())
            			   {
            				   if(!(entry.getValue() instanceof UILineConnector))
            				   {
            					   entry.getValue().collected(false);
            				   }
            			   }
            			   
            			   
            			   
            			   
            			   //no components in rectangle found, delete dotted rectangle
            			   ContentPane.this.getChildren().remove(selectionRect);
            		   }
        			   
        		   }
        		 
        		   
        		   
        	   }
        	   else if(t.getSource() instanceof AUIElement)
        	   {
        	      	AUIElement node = (AUIElement) t.getSource();
                  	if(node instanceof UILineConnector)
                  	{
                  		UILineConnector uiNode = ((UILineConnector)node);
                  		if(uiNode.isDeletedDesignated())
                  		{
                  			
                  			UILineOutputConnector outputConnector = (UILineOutputConnector) getConnector(uiNode.getLogicElement().getOutputId());
                  			//TODO second input?
                  			UILineInputConnector inputConnector = (UILineInputConnector) getConnector(uiNode.getLogicElement().getInputId());
                     		
                  			outputConnector.removeUIOutputConnector();
                  			inputConnector.removeUIInputConnector();
                  			
                  			Scheme schemeObject  = SchemeDataStorage.getSchemeList().getSchemeList().get(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen());
                  			int indexFromMap = schemeObject.getIndexFromLogicElement(node.getLogicElement());
                  			if(indexFromMap >= 0)
                  				schemeObject.deleteElementMap(indexFromMap);
                  			
                  			deleteUINodeFromView(indexFromMap);
                  		
                  			//TODO delete the connection to the ofter nodes
                  		}
                  		else
                  		{
                  			uiNode.setSelected(false);
                  		}
                  	}
                  	//release the colored frame and the select flag
                  	else if(ContentPane.this.getScene().getCursor() == Cursor.MOVE)
                  	{
                  		
                  		//when node is moved from a collected frame
                  		if(node.isCollected())
                  		{
                  			//it must be checked that the movement goes outside the definied frame
                  			//TODO w und h flexible?
                  			Bounds uiBounds = new BoundingBox(node.getTranslateX(), node.getTranslateY(), 150D, 150D);
                  			
                  			if(selectionRect != null && ContentPane.this.getChildren().contains(selectionRect))
                  			{
                  				Bounds boundsSelectionRect = new BoundingBox(selectionRect.getLayoutX(), selectionRect.getLayoutY(), selectionRect.getWidth(), selectionRect.getHeight());
    	        				if(!UtilFx.isUIElementInShape(uiBounds,  boundsSelectionRect))
                      			{
                      			  //out of the shape, than reset flag value
                      			  node.collected(false);
                      			  System.out.println("remove from map");
                      			  removeChangeListenerFromCollectRect(node);
                      			  
                      			}
    	        				else
    	        					node.collected(true);
                  			}
                  			//no selection rect ...reset flag
                  			else
                  			{
                  				node.collected(false);
                  				System.out.println("remove from map 2");
                  			 	removeChangeListenerFromCollectRect(node);
                  			}
                  		}
                  		
                  		
                  		
                  		
                  		System.out.println("move and released node selected " +node.isSelected());
                  		if(node.isSelected())
                  		{
                  			node.setSelected(false);
                  		}
                  		
                  	}
                  	else if(ContentPane.this.getScene().getCursor() == Cursor.HAND)
                  	{
                  		//draw the line from started point to the cursor point.
                  		//decision to draw the real line or remove the temp line           		
                  		PickResult pickResult = t.getPickResult();
                  		
                  		Point2D sceneCoords = new Point2D(t.getSceneX(), t.getSceneY());
                  		
                 		boolean found = false;
                 		GenericPair<Boolean, AUIElement> pair = new GenericPair<Boolean, AUIElement>(false, (AUIElement)null);
                 		for(int i = 0; i < ContentPane.this.getChildren().size(); i++)
                 		{
                 			//ich suche nach einer instanz die einen Eingang besitzt
                 			
                 			Node uiNode =  ContentPane.this.getChildren().get(i);
                 			
                 			//TODO second entry connector is missing
                 			if(uiNode instanceof UILineInputConnector)
                 			{
                 				UILineInputConnector uiLIneInputConnector = (UILineInputConnector)uiNode;
                 				IConnectorArea iConnectorArea = (IConnectorArea)uiNode;
                 			
                 				if(iConnectorArea.isInputArea(sceneCoords) && !uiLIneInputConnector.isUIInputOccupied())
                 				{
                 					Scheme schemeObject  = SchemeDataStorage.getSchemeList().getSchemeList().get(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen());
                 					int index = schemeObject.getIndexFromLogicElement(((AUIElement) uiNode).getLogicElement());
                 					if(index >= 0)
                 					{
                 						tempLine.setInputIndex(index);
                 						found = true;
                     				}
                 					break;
                 				}
                 			
                 			}
                 		}
                 		
                 		if(found)
                 		{
                 			//build new uiLine/logicline
                 			
                 			Scheme schemeObject  = SchemeDataStorage.getSchemeList().getSchemeList().get(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen());
                 			LineConnector lineConnector = new LineConnector();
                 			//add a new connector to the logical map; with the add the line will get the new index
                 			schemeObject.addElementAtMap(lineConnector);
                 			
                 			UILineConnector newLine = new UILineConnector((LineConnector) schemeObject.getWorkflowMap().get(lineConnector.getIndex()));
                 			
                 			newLine.setOutputX(tempLine.getStartX());
                 			newLine.setOutputY(tempLine.getStartY());
                 			
                 			newLine.setInputX(tempLine.getEndX());
                 			newLine.setInputY(tempLine.getEndY());
                 			
                 			putUINodeAtMap(lineConnector.getIndex(), newLine);
                 			
                 			//connect the coordinates from the new line with the input and output ui component
                 			//no question about the pickup of the value 
                 			
                 			//TODO subindex must be in tempLine
                 			UILineOutputConnector outputConnector = (UILineOutputConnector) getConnector(new GenericPair<Integer, Integer>(tempLine.getOutputIndex(), 0));
                 			
                 			UILineInputConnector inputConnector = (UILineInputConnector) getConnector((new GenericPair<Integer, Integer>(tempLine.getInputIndex(), 0)));
                 			if(outputConnector != null && inputConnector != null)
                 			{
                 				
                 				//fill now the id to the logic elements
                 				AOutputElement outputElement = (AOutputElement) ((AUIElement)outputConnector).getLogicElement();
                 				lineConnector.setMasteridOutput(outputElement.getIndex());
                 				
                 				AInputOutputElement inAndOutElement = (AInputOutputElement) ((AUIElement)inputConnector).getLogicElement();
                 				lineConnector.setMasteridInput(inAndOutElement.getIndex());
                 			}
                 			
                 			
                 			if(!lineConnector.isOutputEmpty() && !lineConnector.isInputEmpty())
                 			{
                 				outputConnector.setUIOutputConnector(newLine);
                 				inputConnector.setUIInputConnector(newLine);
                 			}
                 			
	       					addMouseListener(newLine);
	       					//add to content
	                 		ContentPane.this.getChildren().add(newLine);
                 			
                 			
                 		}
                 		//remove from view
                 		ContentPane.this.getChildren().remove(tempLine);
                 	}
        	   }
        	   ContentPane.this.getScene().setCursor(Cursor.DEFAULT);
           }

		

       };
       
    /**
     * needed, when a component removed from a user definied frame on screeen
     * @param node
     */
    private void removeChangeListenerFromCollectRect(AUIElement node) 
    {
    	System.out.println(" changeListenerList.get(node).getLeft() " + changeListenerMap.get(node).getLeft());
    	selectionRect.getGroupedMovementProperties().getLeft().removeListener(changeListenerMap.get(node).getLeft());
		selectionRect.getGroupedMovementProperties().getRight().removeListener(changeListenerMap.get(node).getRight());
		changeListenerMap.remove(node);
	} 

	private UILineOutputConnector getOutputConnector(Point2D point2d)
	{
		for(Entry<Integer, AUIElement> entry : uiMap.entrySet())
		{
			if(entry.getValue() instanceof UILineOutputConnector)
			{
				AUIOutputElement element = (AUIOutputElement)entry.getValue();
				
				
				//the coord and the saved values must be equals.
			//	System.out.println(" element.getOutputCenterCoordinate().getX() " + element.getOutputCenterCoordinate().getX() + " point " + point2d.getX());
			//	System.out.println(" element.getOutputCenterCoordinate().getY() " + element.getOutputCenterCoordinate().getY() + " point " + point2d.getY());
				if(element.isOutputArea(point2d))
				{
			//		System.out.println("return value out");
					return element;
				}
			}
		}
		return null;
	}
	
	private UILineInputConnector getInputConnector(Point2D point2d)
	{
		System.out.println("IN " + point2d);
		for(Entry<Integer, AUIElement> entry : uiMap.entrySet())
		{
			if(entry.getValue() instanceof UILineInputConnector)
			{
				AUIInputOutputElement element = (AUIInputOutputElement)entry.getValue();
				if(element.isInputArea(point2d))
				{
					return element;
				}
			}
		}
		return null;
	}
	
	
       
    private TreeMap<Integer, AUIElement> restructureMap(TreeMap<Integer, AUIElement> restructMap) 
   	{
    	
    	
    	Scheme schemeObject  = 
    			SchemeDataStorage.getSchemeList().getSchemeList().get(SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen());
    	
    	//TODO raus
    	SortedMap<Integer, ALogicElement> schemeMap = schemeObject.getWorkflowMap();
    	for(Entry<Integer, ALogicElement> entry : schemeMap.entrySet())
    	{
    		//System.out.println("entry " + entry.getValue().getIndex() +  " " + entry.getKey());
    	}
    	
    	
   		TreeMap<Integer, AUIElement> newMap = new TreeMap<Integer, AUIElement>();
   		
   		int startIndex = 0; 
   		
   		//TODO können Lücken vorkommen? Eigentlich nicht weil es in schemedata schon restrukturiert wird.
   		for(Entry<Integer, AUIElement> entry : restructMap.entrySet())
   		{
   			newMap.put(startIndex, entry.getValue());
   			startIndex++;
   		}
   		return newMap;
   	}

    /**
     * add the ui node with a exact position to the map
     * @param inputIndex
     * @param newLine
     */
	protected void putUINodeAtMap(int inputIndex, AUIElement auiElement) 
	{
		if(uiMap.get(inputIndex) != null)
		{
			int newIndex = inputIndex + 1;
			AUIElement valueToMove = uiMap.get(inputIndex);
			//set the param from method
			uiMap.put(inputIndex, auiElement);
			//hop to the new index with the old value
			this.putUINodeAtMap(newIndex, valueToMove);
		}
		else
		{
			uiMap.put(inputIndex, auiElement);
		}
	}

	protected void deleteUINodeFromView(int indexFromMap) 
	{
		AUIElement uiNode = uiMap.remove(indexFromMap);
		//TODO delete the connections from combined objects?
		uiMap = restructureMap(uiMap);
		ContentPane.this.getChildren().remove(uiNode);
	}

}
