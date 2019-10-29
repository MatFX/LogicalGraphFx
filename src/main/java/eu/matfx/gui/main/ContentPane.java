package eu.matfx.gui.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import eu.matfx.gui.component.AUIDoubleInputOneOutputElement;
import eu.matfx.gui.component.AUIElement;
import eu.matfx.gui.component.AUIInputOutputElement;
import eu.matfx.gui.component.AUIOutputElement;
import eu.matfx.gui.component.impl.UICircleLineConnector;
import eu.matfx.gui.component.impl.UILineConnector;
import eu.matfx.gui.component.impl.container.UIRSFlipFlopContainer;
import eu.matfx.gui.helper.GenericPair;
import eu.matfx.gui.helper.SelectionRectangle;
import eu.matfx.gui.helper.TempLine;
import eu.matfx.gui.helper.XYSelectRectangleChangeListener;
import eu.matfx.gui.interfaces.IConnectorArea;
import eu.matfx.gui.interfaces.UILineInputConnector;
import eu.matfx.gui.interfaces.UILineOutputConnector;
import eu.matfx.gui.interfaces.UILineSecondInputConnector;
import eu.matfx.gui.util.ECommand;
import eu.matfx.gui.util.UtilFx;
import eu.matfx.logic.Scheme;
import eu.matfx.logic.data.ADoubleInputOneOutputElement;
import eu.matfx.logic.data.AInputOutputElement;
import eu.matfx.logic.data.ALogicElement;
import eu.matfx.logic.data.AOutputElement;
import eu.matfx.logic.data.impl.CircleLineConnector;
import eu.matfx.logic.data.impl.LineConnector;
import eu.matfx.logic.database.SchemeDataStorage;
import eu.matfx.logic.database.XMLAccess;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ContentPane extends Pane {
	private Stage primaryStage;

	private ObjectProperty<ECommand> command;

	private Canvas canvas;

	private StringProperty statusText;
	private DoubleProperty xCoords;
	private DoubleProperty yCoords;

	private double orgSceneX, orgSceneY;
	private double orgTranslateX, orgTranslateY;

	private TreeMap<Integer, AUIElement<? extends ALogicElement>> uiMap = new TreeMap<Integer, AUIElement<? extends ALogicElement>>();

	private TempLine tempLine;

	private SelectionRectangle selectionRect;

	
	/**
	 * is filled when with the selectionRect some uiElements grouped and moved.
	 * <br>
	 * Changelistener are connected with x and y from selectionRect
	 */
	private XYSelectRectangleChangeListener changeListenerRectangleX, changeListenerRectangleY;
	
	private Circle startRectangle = new Circle();

	private Point2D lastReceivedLineMousePoint = null;
	
	private SimpleBooleanProperty notSaved;

	public ContentPane(Stage primaryStage, StringProperty statusText, DoubleProperty xCoords, DoubleProperty yCoords, ObjectProperty<ECommand> command, SimpleBooleanProperty notSaved) 
	{
		super();
		this.notSaved = notSaved;
		this.command = command;

		/*
		 * navigator view? changedComponentView.addListener(new
		 * ChangeListener<Boolean>() {
		 * 
		 * @Override public void changed(ObservableValue<? extends Boolean>
		 * observable, Boolean oldValue, Boolean newValue) {
		 * 
		 * if(newValue) { makeNewSnapshot(); changedComponentView.set(false); }
		 * 
		 * 
		 * }
		 * 
		 * });
		 */
		
		

		this.command.addListener(new ChangeListener<ECommand>() {

			@Override
			public void changed(ObservableValue<? extends ECommand> observable, ECommand oldValue, ECommand newValue) {
				
				switch (newValue) {
				case SAVE_CONFIGURATION:
					saveCoordsFromActiveScheme();
					XMLAccess.writeObjectToFile(SchemeDataStorage.getSchemeList());
					//create new tmp file from the existing xml
					SchemeDataStorage.getInstance().saveCurrentFile();
					command.set(ECommand.NO_COMMAND);
					break;
				case CREATED_NEW_SCHEME:
					//Der Schemecontainer ist bereits erzeugt und in der Liste abgelegt
					//combobox wurde bereits bei der Schemebar angepasst

					//save last active scheme
					saveCoordsFromActiveScheme();
					//remove last active scheme
					removeContentAndCleanMap();
					command.set(ECommand.NO_COMMAND);
					break;
				case ACTIVATED_SCHEME:
					//first save the last view
					saveCoordsFromActiveScheme();
					//cleanmap
					removeContentAndCleanMap();
					rebuildView();
					command.set(ECommand.NO_COMMAND);
					break;
				case RESET_CONFIGURATION:
					
					//clean view
					removeContentAndCleanMap();
					//rebuild the view
					rebuildView();
					command.set(ECommand.NO_COMMAND);
					break;
				case NO_COMMAND:
					break;
				case ADD_NEW_BASE_TEMPLATE:
					//save the current coords from the active scheme
					saveCoordsFromActiveScheme();
					//cleanmap
					removeContentAndCleanMap();
					rebuildView();
					command.set(ECommand.NO_COMMAND);
					break;
				default:
					System.out.println("COMMAND NOT IMPLEMENTED: " + newValue);
					break;
				}
			}

		});

		// Canvas über die komplette Oberfläche; weiß noch nicht wie ich das
		// benötige für die Striche
		// zum zeichnen
		canvas = new Canvas(1400, 900);
		canvas.relocate(0, 0);

		this.primaryStage = primaryStage;
		// I think not needed
		this.primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

			}

		});
		this.primaryStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				switch (event.getCode()) {
				// two possible keys to delete the selected ui components
				case BACK_SPACE:
				case DELETE:
					deleteSelectedUIElements();
					notSaved.set(true);
					//Cursor wieder auf normal
					ContentPane.this.getScene().setCursor(Cursor.DEFAULT);
					break;
				case PLUS:

					// TODO das geht hier nicht

					// globale boolean für selektion oder doch jedesmal die
					// Suche anstossen?
					for (int i = 0; i < getChildren().size(); i++) 
					{
						// searching for selected line connector
						if (getChildren().get(i) instanceof UILineConnector
								&& ((UILineConnector) getChildren().get(i)).isSelected()) {
							UILineConnector lineConnector = (UILineConnector) getChildren().get(i);

							// find the point on the line
							if (lastReceivedLineMousePoint != null) 
							{
								// calculate the point on the line (orthogonale
								// function)
								Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(
										lastReceivedLineMousePoint.getX(), lastReceivedLineMousePoint.getY()));
								Point2D nearesPoint = lineConnector.getOrthogonalPointOnLine(transferCoord);

								Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
								CircleLineConnector circleLineConnector = new CircleLineConnector();
								schemeObject.addElementAtList(circleLineConnector);

								UICircleLineConnector uiCircle = new UICircleLineConnector(
										(CircleLineConnector) schemeObject.getElementWithId(circleLineConnector.getIndex()));
										
								uiCircle.moveComponent(nearesPoint.getX(), nearesPoint.getY());
								// release from the to observe components

								// here is the problem with the second input
								UILineInputConnector endConnector = (UILineInputConnector) getConnector(
										lineConnector.getLogicElement().getInputId());

								// der wird weiterhin bei der linie verbleiben
								// startConnector.removeUIOutputConnector();
								// remove the ending point...this point will be
								// establish at the new line
								if (lineConnector.getLogicElement().getInputId().getRight() == 1) {
									((UILineSecondInputConnector) endConnector).removeUISecondInputConnector();
								} else
									endConnector.removeUIInputConnector();

								// divide the selected line
								// => need a new line
								LineConnector newLineConnector = new LineConnector();
								schemeObject.addElementAtList(newLineConnector);
								// that line get the end container
								UILineConnector uiNewLineConnector = new UILineConnector(
										(LineConnector) schemeObject.getElementWithId(newLineConnector.getIndex()));
								//TODO circle remove
								// add new ending point to the old line
								//uiCircle.getLogicElement().setMasteridInput(lineConnector.getLogicElement().getIndex());
								// add new output id from the new line
								//uiCircle.getLogicElement()
								//		.setMasteridOutput(uiNewLineConnector.getLogicElement().getIndex());

								//use the values from the old line connection
								
								uiNewLineConnector.getLogicElement()
										.setMasteridInputWithSubindex(lineConnector.getLogicElement().getInputId().getLeft(), 
												lineConnector.getLogicElement().getInputId().getRight());
								
								uiNewLineConnector.getLogicElement()
										.setMasteridOutput(uiCircle.getLogicElement().getIndex());

								// add the ending point to the new line
								if (lineConnector.getLogicElement().getInputId().getRight() == 1)
									((UILineSecondInputConnector) endConnector)
											.setUISecondInputConnector(uiNewLineConnector);
								// normal
								else {
									endConnector.setUIInputConnector(uiNewLineConnector);
								}
								
								//change the old inputConector of the logicElement
								lineConnector.getLogicElement().setMasteridInput(uiCircle.getLogicElement().getIndex());
								

								// add new observe object to the circle
								// component
								// output to new line
								uiCircle.setUIOutputConnector(uiNewLineConnector);
								// input from old line
								uiCircle.setUIInputConnector(lineConnector);

								// previous: Start-Component -> Line ->
								// End-Component

								// after change: Start-Component -> Line1 ->
								// Circle -> Line2 -> End-Component

								addMouseListener(uiCircle);
								addMouseListener(uiNewLineConnector);

								// add to content
								ContentPane.this.getChildren().add(uiCircle);
								ContentPane.this.getChildren().add(uiNewLineConnector);

								uiMap.put(uiCircle.getLogicElement().getIndex(), uiCircle);
								uiMap.put(uiNewLineConnector.getLogicElement().getIndex(), uiNewLineConnector);
							}
						}
					
					}
					notSaved.set(true);
					break;

				case MINUS:
					
					for (int i = 0; i < getChildren().size(); i++) 
					{
						if(getChildren().get(i) instanceof UICircleLineConnector
								&& ((UICircleLineConnector)getChildren().get(i)).isSelected())
						{
							
							UICircleLineConnector circleToDelete = (UICircleLineConnector)getChildren().get(i);
							
							//neue endepunkte finden wenn dieser entfernt wird
							
							//inuptconnector need new ending point
							UILineConnector inputConnector = circleToDelete.getUILineInputConnector();
							
							//output to delete, but I need the output cooordinate for the inputconnector
							UILineConnector outputConnector = circleToDelete.getUILineOutputConnector();
							
							//the ending point it's called INPUT! name from the perspective of the logic container!
							inputConnector.setInputX(outputConnector.getInputX());
							inputConnector.setInputY(outputConnector.getInputY());
							//now change the logic line element 
							//with subindex (rs flipflop)
							inputConnector.getLogicElement().setMasteridInputWithSubindex(outputConnector.getLogicElement().getInputId().getLeft(),
									outputConnector.getLogicElement().getInputId().getRight());
							
							//remove from the contentpane
							ContentPane.this.getChildren().remove(circleToDelete);
							ContentPane.this.getChildren().remove(outputConnector);
							
							uiMap.remove(circleToDelete.getLogicElement().getIndex());
							uiMap.remove(outputConnector.getLogicElement().getIndex());
							Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
							
							schemeObject.removeElementAtMap(circleToDelete.getLogicElement());
							schemeObject.removeElementAtMap(outputConnector.getLogicElement());
							//TODO weiß noch nicht wie das löschen erfolgt und ob die Indizes neu sortiert werden
							//schemeObject.removeElementAtMap(circleToDelete.getLogicElement());
							//schemeObject.removeElementAtMap(outputConnector.getLogicElement());
							
							//wenn endepunkte gefunden dann den selektierten entfernen mit verbindungslinien
							
							//connect the inputConnector with the UI from the logic or circle container
							
							
							AUIElement newEndingInput = getEndingInputWithId(inputConnector.getLogicElement().getInputId());
							
							
							
							
							if(newEndingInput instanceof AUIInputOutputElement)
							{
								if(inputConnector.getLogicElement().getInputId().getRight() >= 1 &&
										newEndingInput instanceof UIRSFlipFlopContainer)
								{
									UIRSFlipFlopContainer flipFlopContainer = (UIRSFlipFlopContainer)newEndingInput;
									flipFlopContainer.setUISecondInputConnector(inputConnector);
									
								}
								else
								{
									AUIInputOutputElement outElement = (AUIInputOutputElement)newEndingInput;
									//TODO rsFlipFlop
									outElement.setUIInputConnector(inputConnector);
								}
								
							}
							else if(newEndingInput instanceof UICircleLineConnector)
							{
								UICircleLineConnector circleElement = (UICircleLineConnector)newEndingInput;
								circleElement.setUIInputConnector(inputConnector);
								
							}
							break;
						}
					}
					
					//Cursor wieder auf normal
					ContentPane.this.getScene().setCursor(Cursor.DEFAULT);
					notSaved.set(true);
					break;
				default:
					break;
				}

			}
		});

		this.statusText = statusText;
		this.xCoords = xCoords;
		this.yCoords = yCoords;

		// mal testweise einen container hier ablegen?

		// Canvas muss mit wegen der Bemalung des Hintergrunds
		this.getChildren().addAll(canvas);

		// canvas needs listener for the selection mechanism
		addMouseListener(canvas);
		// TODO raus
		canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				statusText.set("x: " + event.getSceneX() + " y: " + event.getSceneY());

			}

		});

		
		
		if (SchemeDataStorage.getSchemeList().getActiveSchemeOnScreenIndex() >= 0) {
			// put scheme on screen
			rebuildView();
		}
	}
	
	/**
	 * find the list to delete the connection way
	 * @param inputElement 
	 * @param index
	 * @return
	 */
	protected List<AUIElement<? extends ALogicElement>> findInputElementWay(List<AUIElement<? extends ALogicElement>> nodeToDeleteList, AUIElement<? extends ALogicElement> node) 
	{
		
		if(node instanceof UILineConnector)
		{
			if(!nodeToDeleteList.contains(node))
			{
				nodeToDeleteList.add(node);
			}
			int outputid = ((UILineConnector)node).getLogicElement().getOutputId().getLeft();
			
			AUIElement<?> element = this.findElement(outputid);
			if(element != null)
			{
				
				return findInputElementWay(nodeToDeleteList, element);
			}
		}
		else if(node instanceof UICircleLineConnector)
		{
			if(!nodeToDeleteList.contains(node))
				nodeToDeleteList.add(node);
			
			//backward searching to the next output
			AUIElement<?> element = this.findInputElement(((UICircleLineConnector)node).getLogicElement().getIndex());
			if(element != null)
			{
				return findInputElementWay(nodeToDeleteList, element);
			}
		}
		return nodeToDeleteList;
	}

	/**
	 * find the list to delete the connection way from output to the first base template with input
	 * @param index
	 * @return
	 */
	protected List<AUIElement<? extends ALogicElement>> findOutputElementWay(List<AUIElement<? extends ALogicElement>> nodeToDeleteList, AUIElement<? extends ALogicElement> node) 
	{
		if(node instanceof UILineConnector)
		{
			if(!nodeToDeleteList.contains(node))
				nodeToDeleteList.add(node);
			int outputId = ((UILineConnector)node).getLogicElement().getInputId().getLeft();
	
			AUIElement<?> element = this.findElement(outputId);
			if(element != null)
			{
				
				return findOutputElementWay(nodeToDeleteList, element);
			}
		}
		else if(node instanceof UICircleLineConnector)
		{
			if(!nodeToDeleteList.contains(node))
				nodeToDeleteList.add(node);
			
			//backward searching to the next output
			AUIElement<?> element = this.findOutputElement(((UICircleLineConnector)node).getLogicElement().getIndex());
			if(element != null)
			{
				return findOutputElementWay(nodeToDeleteList, element);
			}
		}
		return nodeToDeleteList;
	}

	protected AUIElement getEndingInputWithId(GenericPair<Integer, Integer> inputId) 
	{
		//TODO RS FliFlop
		for(int x = 0; x < this.getChildren().size(); x++)
		{
			if(this.getChildren().get(x) instanceof AUIElement)
			{
				AUIElement uiElement = (AUIElement) this.getChildren().get(x);
				if(uiElement.getLogicElement().getIndex() == inputId.getLeft())
				{
					return uiElement;
				}
			}
			
		}
		return null;
	}

	protected void removeContentAndCleanMap() {
		//remove ui elements from the screen
		for(Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet())
		{
			ContentPane.this.getChildren().remove(entry.getValue());
		}
		//clear the uiMap
		uiMap.clear();
	}

	/**
	 * save the coordinates of the different UI elements; it's not the saving to the xml file!
	 */
	public void saveCoordsFromActiveScheme() {
		// iterate through the ui map and update the logicelements with coords
		for (Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet()) {
			entry.getValue().saveVariables();
		}
	}

	/**
	 * delete all selecte ui elements from the view.
	 */
	protected void deleteSelectedUIElements() {
		LinkedHashSet<AUIElement<? extends ALogicElement>> toDeleteList = new LinkedHashSet<AUIElement<? extends ALogicElement>>();
		// find alle ui element without the line connectors
		for (Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet()) {
			//no delete at the Line (deletable over mouse move) and circle (only delete when is collected; deletable over MINUS)
			if ((entry.getValue() instanceof UICircleLineConnector && entry.getValue().isCollected()) || !(entry.getValue() instanceof UILineConnector)) 
			{
				if (entry.getValue().isSelected() || entry.getValue().isCollected()) 
				{
						
					List<AUIElement<? extends ALogicElement>> tempList = new ArrayList<AUIElement<? extends ALogicElement>>();
					//wir müssen uns komplett den Weg durchhaneln bis zu einer richtigen Komponente
					AUIElement toDeleteObject = entry.getValue();
					//von dem Start toDeleteObject den kompletten Weg durchnudeln
					//und der deleteListe anhängen
					
					if(entry.getValue() instanceof AUIDoubleInputOneOutputElement)
					{
						AUIDoubleInputOneOutputElement doubleInputElement = (AUIDoubleInputOneOutputElement)toDeleteObject;
					}
					
					if(entry.getValue() instanceof AUIInputOutputElement)
					{
						tempList = new ArrayList<AUIElement<? extends ALogicElement>>();
						AUIInputOutputElement inputElement = (AUIInputOutputElement)toDeleteObject;
						//Line finden die an Input hängt
						UILineConnector uiLine = null;
						for (Entry<Integer, AUIElement<? extends ALogicElement>> secondCheck : uiMap.entrySet()) 
						{
							if (secondCheck.getValue() instanceof UILineConnector)
							{
								UILineConnector tempConnector = (UILineConnector)secondCheck.getValue();
								if(tempConnector.getLogicElement().isMasterIdInput(inputElement.getLogicElement().getIndex()))
								{
									uiLine = tempConnector;
									break;
									
								}
								
								
							}
						}
						if(uiLine != null)
						{
							findInputElementWay(tempList, uiLine);
						
							for(int z = 0; z < tempList.size(); z++)
								toDeleteList.add(tempList.get(z));
						}
						
						
					}
					
					
					if(entry.getValue() instanceof AUIOutputElement)
					{
						tempList = new ArrayList<AUIElement<? extends ALogicElement>>();
						AUIOutputElement outputElement = (AUIOutputElement)toDeleteObject;
						UILineConnector uiLine = null;
						
						for (Entry<Integer, AUIElement<? extends ALogicElement>> secondCheck : uiMap.entrySet()) 
						{
							if (secondCheck.getValue() instanceof UILineConnector)
							{
								UILineConnector tempConnector = (UILineConnector)secondCheck.getValue();
								if(tempConnector.getLogicElement().isMasterIdOutput(outputElement.getLogicElement().getIndex()))
								{
									uiLine = tempConnector;
									break;
								}
							}
						}
						if(uiLine != null)
						{
							findOutputElementWay(tempList, uiLine);
			
							for(int z = 0; z < tempList.size(); z++)
								toDeleteList.add(tempList.get(z));
						}
					}
					
					if(entry.getValue() instanceof UICircleLineConnector)
					{
						UICircleLineConnector uiCircle = (UICircleLineConnector)entry.getValue();
						int myIndex = uiCircle.getLogicElement().getIndex();
						
						UILineConnector uiLine = null;
						for (Entry<Integer, AUIElement<? extends ALogicElement>> secondCheck : uiMap.entrySet()) 
						{
							if (secondCheck.getValue() instanceof UILineConnector)
							{
								UILineConnector tempConnector = (UILineConnector)secondCheck.getValue();
								if(tempConnector.getLogicElement().isMasterIdInput(myIndex))
								{
									uiLine = tempConnector;
									break;
									
								}
								
								
							}
						}
						if(uiLine != null)
						{
							findInputElementWay(tempList, uiLine);
						
							for(int z = 0; z < tempList.size(); z++)
								toDeleteList.add(tempList.get(z));
						}
						
						uiLine = null;
						
						for (Entry<Integer, AUIElement<? extends ALogicElement>> secondCheck : uiMap.entrySet()) 
						{
							if (secondCheck.getValue() instanceof UILineConnector)
							{
								UILineConnector tempConnector = (UILineConnector)secondCheck.getValue();
								if(tempConnector.getLogicElement().isMasterIdOutput(myIndex))
								{
									uiLine = tempConnector;
									break;
								}
							}
						}
						if(uiLine != null)
						{
							findOutputElementWay(tempList, uiLine);
					
							for(int z = 0; z < tempList.size(); z++)
								toDeleteList.add(tempList.get(z));
						}
						
						
						
					}
					// Now the founded element
					toDeleteList.add(entry.getValue());
				}
			}
		}

		//Delete the list with the collected elements from the ui.
		if (toDeleteList.size() > 0) {

			Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();

			Iterator<AUIElement<? extends ALogicElement>> it = toDeleteList.iterator();
			while (it.hasNext())
			{
				AUIElement element = it.next();
				schemeObject.removeElementAtMap(element.getLogicElement());
				// now the ui side
				deleteUINodeFromView(element);
				
			}
			//reorganize the ui map and release the connection
			it = toDeleteList.iterator();
			while (it.hasNext())
			{
				AUIElement aUIElement = it.next();
				//dieser Index darf nirgendwo mehr vorkommen
				int indexKomponent = aUIElement.getLogicElement().getIndex();
				releaseConnection(indexKomponent);
			}
			
		}

	}

	/**
	 * searching through the ui map and release the ui connection to the deleted LogicElement
	 * @param indexKomponent
	 */
	private void releaseConnection(int indexKomponent) 
	{
		for(Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet())
		{
			AUIElement element = entry.getValue();
			if(element instanceof AUIDoubleInputOneOutputElement)
			{
				AUIDoubleInputOneOutputElement doubleInput = (AUIDoubleInputOneOutputElement)element;
				
				if(doubleInput.isUISecondInputOccupied(indexKomponent))
				{
					doubleInput.removeUISecondInputConnector();
				}
			}
			
			if(element instanceof AUIInputOutputElement)
			{
				AUIInputOutputElement inputElement = (AUIInputOutputElement)element;
				
				if(inputElement.isUIInputOccupied(indexKomponent))
				{
					inputElement.removeUIInputConnector();
				}
			}
			
			if(element instanceof AUIOutputElement)
			{
				AUIOutputElement outputElement = (AUIOutputElement)element;
				if(outputElement.isUIOutputOccupied(indexKomponent))
				{
					outputElement.removeUIOutputConnector();
					
				}
			}
		}
	}

	private void rebuildView() {
		// with the rebuild the map must be deleted
		uiMap = new TreeMap<Integer, AUIElement<? extends ALogicElement>>();
		ContentPane.this.setStyle("-fx-background-color: #5691b0;");

		Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
		
		
		if (schemeObject != null) {
			
			List<ALogicElement> sortedList = schemeObject.getSortedList();
			Iterator<ALogicElement> it = sortedList.iterator();
			while(it.hasNext())
			{
				ALogicElement aLogicElement = it.next();
				
				AUIElement<? extends ALogicElement> createdElement = AUIElement.getInstance(aLogicElement);
				if (createdElement != null) {
					// in the first draw no line connector!
					if (!(createdElement instanceof UILineConnector)) 
					{
						
						
						
						// TODO was ist mit größe und location?
						ContentPane.this.getChildren().add(createdElement);

						// TODO positioning
						createdElement.moveComponent(aLogicElement.getLocationView().getX(), aLogicElement.getLocationView().getY());
						createdElement.recalcualteCenterPoint();

						addMouseListener(createdElement);

					}
					uiMap.put(aLogicElement.getIndex(), createdElement);
				}
			}
			
			// with the second draw the line connector came on view
			for (Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet()) 
			{
				if (entry.getValue() instanceof UILineConnector) {
					// we need two listener one for the output and one for the
					// input
					// When any component will change the position the line
					// connector will change the position

					UILineConnector connector = (UILineConnector) entry.getValue();

					LineConnector lineConnector = connector.getLogicElement();
					// one entry empty, dont draw the line
					if (!lineConnector.isOutputEmpty() && !lineConnector.isInputEmpty()) {
						// find the output connector
						UILineOutputConnector outputConnector = (UILineOutputConnector) getConnector(
								lineConnector.getOutputId());
						UILineInputConnector inputConnector = (UILineInputConnector) getConnector(
								lineConnector.getInputId());
						
						outputConnector.setUIOutputConnector(connector);
						// TODO not beautiful
						if (lineConnector.getInputId().getRight() == 1) 
						{
							//unterscheidung für Komponenten mit zweiten Eingang!
							if(inputConnector instanceof UILineSecondInputConnector)
							{
								((UILineSecondInputConnector) inputConnector).setUISecondInputConnector(connector);
							}
							else
								inputConnector.setUIInputConnector(connector);
						} 
						else 
						{
							inputConnector.setUIInputConnector(connector);
						}
					}
					ContentPane.this.getChildren().add(connector);
					addMouseListener(connector);

				}
			}
		}
	}

	private AUIElement<? extends ALogicElement> getConnector(GenericPair<Integer, Integer> inputId) {
		for (Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet()) {
			if (entry.getValue().getLogicElement().getIndex() == inputId.getLeft()
					&& (entry.getValue() instanceof UILineOutputConnector
							|| entry.getValue() instanceof UILineInputConnector)) {
				return entry.getValue();
			}

		}
		return null;
	}

	/**
	 * Alle Komponenten auf der Hauptoberfläche erhalten die gleiche
	 * Mausaktivitäten. <br>
	 * Muss ich diese auch wieder entfernen, wenn ich die Komponente aus der
	 * Oberfläche entfernen will?
	 * 
	 * @param node
	 */
	private void addMouseListener(Node node) {
		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);
	}

	/**
	 * Ein Teil für das Verschieben. Hier das festhalten der aktuellen Coords
	 */
	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {

			switch (command.get()) {
			// Liegt kein Kommando an, dann darf verschoben, skaliert oder neue
			// verbunden werden
			default:
			case NO_COMMAND:

				Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
				if (t.getSource() instanceof Canvas) 
				{
					// user definied rectangle on screen?
					boolean drawNew = true;
					if (selectionRect != null && ContentPane.this.getChildren().contains(selectionRect)) 
					{
						// is the mouse click inside the rect values or outside
						// outside => drawNew

						// inside => moveable
						Bounds boundsSelectionRect = new BoundingBox(selectionRect.getLayoutX(),
								selectionRect.getLayoutY(), selectionRect.getWidth(), selectionRect.getHeight());
						Bounds localBounds = selectionRect.localToScene(selectionRect.getBoundsInLocal());

						if (UtilFx.isPointInShape(transferCoord, localBounds)) 
						{
							selectionRect.setStroke(Color.BLUE);
							selectionRect.setCatchedUIElements(true);
							selectionRect.setStartCoordsMovement(transferCoord);

							
							//sicherstellen, dass es keine doppelten listener gibt...no contains check at the addListener method
							if(changeListenerRectangleX != null || changeListenerRectangleY != null)
							{
								selectionRect.getGroupedMovementProperties().getLeft().removeListener(changeListenerRectangleX);
								selectionRect.getGroupedMovementProperties().getRight().removeListener(changeListenerRectangleY);
							}
							
							// need empty list to store the connected
							// changelistener
							changeListenerRectangleX = new XYSelectRectangleChangeListener(true);
							changeListenerRectangleY = new XYSelectRectangleChangeListener(false);

							selectionRect.getGroupedMovementProperties().getLeft().addListener(changeListenerRectangleX);
							selectionRect.getGroupedMovementProperties().getRight().addListener(changeListenerRectangleY);
							
							
							// find the components and connect the visulisation

							for (int i = 0; i < ContentPane.this.getChildren().size(); i++) {
								Node node = ContentPane.this.getChildren().get(i);
								// only uielements
								if (node instanceof AUIElement) {
									@SuppressWarnings("unchecked")
									AUIElement<? extends ALogicElement> uiElement = (AUIElement<? extends ALogicElement>) node;
									// ausschlaggebend sind nur die Elemente die
									// nicht Line sind
									if (!(uiElement instanceof UILineConnector)) 
									{
										Bounds uiBounds = null;
										// TODO widht and heigt from component ...it is ugly
										if(uiElement instanceof UICircleLineConnector)
										{
											uiBounds = new BoundingBox(uiElement.getTranslateX(), uiElement.getTranslateY(),
													10D, 10D);
										}
										else
										{
											uiBounds = new BoundingBox(uiElement.getTranslateX(), uiElement.getTranslateY(),
													150D, 150D);
											
										}
										if (UtilFx.isUIElementInShape(uiBounds, boundsSelectionRect)) 
										{
											//it is possible, when user move with rectangle over the uielement and click again to move the rectangle
											//mode to add a Component for the next movement.
											if(!uiElement.isCollected())
												uiElement.collected(true);
											addChangeListenerToCollectRect(uiElement);
										}
										
									}
								}
							}
							drawNew = false;
							ContentPane.this.getScene().setCursor(Cursor.MOVE);
						} else {

							// sicherheitshalber die uiMap zurücksetzen
							for (Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet()) {
								if (!(entry.getValue() instanceof UILineConnector)) {
									entry.getValue().collected(false);
								}
							}
							selectionRect.setStroke(Color.RED);
							selectionRect.setCatchedUIElements(false);
							drawNew = true;
							ContentPane.this.getChildren().remove(selectionRect);
						}
					}

					if (drawNew) {
						// no selection rect on screen draw new rectangle

						selectionRect = new SelectionRectangle(transferCoord.getX(), transferCoord.getY());

						selectionRect.setWidth(1);
						selectionRect.setHeight(1);
						selectionRect.setStrokeWidth(0.5);
						selectionRect.getStrokeDashArray().addAll(2.0, 5.0, 2.0, 5.0);
						selectionRect.setStroke(Color.ANTIQUEWHITE);
						selectionRect.setFill(Color.web("#00000000"));

						ContentPane.this.getChildren().add(selectionRect);
					}
				} else if (t.getSource() instanceof AUIElement) {

					// Move nur dann wenn wir uns mit dem Punkt auf der einer
					// gültige Komponent uns befinden
					@SuppressWarnings("unchecked")
					AUIElement<? extends ALogicElement> node = (AUIElement<? extends ALogicElement>) t.getSource();

					// line connector selected? to delete the line the user must
					// select the line
					if (node instanceof UILineConnector) 
					{
						lastReceivedLineMousePoint = new Point2D(t.getSceneX(), t.getSceneY());
						((UILineConnector) node).setSelected(true);
						
					} else if (node.isOutputArea(UtilFx.getPointFromEvent(t))) {
						statusText.set("OutputArea erkannt");
						Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
						// is the output channel occupied
						if (node instanceof UILineOutputConnector) {
							// check the output connection
							UILineOutputConnector outputConnector = (UILineOutputConnector) node;
							if (!outputConnector.isUIOutputOccupied()) {
								int startIndex = node.getLogicElement().getIndex();
								
								if (startIndex >= 0) {
									// build a temp line
									tempLine = new TempLine(startIndex);
									tempLine.setStrokeWidth(3);
									tempLine.setStroke(Color.BLUE);
									tempLine.setStartX(node.getOutputCenterCoordinate().getX());
									tempLine.setStartY(node.getOutputCenterCoordinate().getY());
									tempLine.setEndX(node.getOutputCenterCoordinate().getX());
									tempLine.setEndY(node.getOutputCenterCoordinate().getY());
									ContentPane.this.getChildren().add(tempLine);
									// ok signal flag for the other listener
									ContentPane.this.getScene().setCursor(Cursor.HAND);
								}
							}
						}

					} else if (node.isMovePossible(t)) {
						((AUIElement) node).setSelected(true);
						// noch keine Ahnung wie ich das mit der
						// Kollisionsabfrage mache
						ContentPane.this.getScene().setCursor(Cursor.MOVE);

						orgSceneX = transferCoord.getX();
						orgSceneY = transferCoord.getY();

						orgTranslateX = ((Node) (t.getSource())).getTranslateX();
						orgTranslateY = ((Node) (t.getSource())).getTranslateY();
						statusText.set("Bewegung erkannt");
					}

				}
				notSaved.set(true);
				break;
			}
			// navigator view
			// makeNewSnapshot();
		}

	};

	/**
	 * movement from components on the view
	 */
	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			Point2D test = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
			statusText.set("x: " + test.getX() + " y " + test.getY());

			if (t.getSource() instanceof Canvas) {
				Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
				if (ContentPane.this.getScene().getCursor() == Cursor.MOVE) {
					// change the position of all components
					selectionRect.setMovementRectangle(transferCoord, ContentPane.this.getWidth(),
							ContentPane.this.getHeight());

					// TODO inner componet must be moved

				} else {
					// check ne new coordinate from the mouseevent und change
					// the orientation or size

					double w = 1;
					double h = 1;
					if (selectionRect.getStartX() > transferCoord.getX()) {
						// obacht hier startx anpassen
						w = selectionRect.getStartX() - transferCoord.getX();
						selectionRect.setLayoutX(transferCoord.getX());
					} else {
						w = transferCoord.getX() - selectionRect.getStartX();
						selectionRect.setLayoutX(selectionRect.getStartX());
					}

					if (selectionRect.getStartY() > transferCoord.getY()) {
						h = selectionRect.getStartY() - transferCoord.getY();
						selectionRect.setLayoutY(transferCoord.getY());

					} else {
						h = transferCoord.getY() - selectionRect.getStartY();
						selectionRect.setLayoutY(selectionRect.getStartY());
					}

					selectionRect.setWidth(w);
					selectionRect.setHeight(h);

				}

			} 
			else if (t.getSource() instanceof AUIElement) 
			{
				@SuppressWarnings("unchecked")
				AUIElement<? extends ALogicElement> node = (AUIElement<? extends ALogicElement>) t.getSource();

				// if the line connector selected and the mouse is out of a
				// range ...the line will be deleted
				if (node instanceof UILineConnector) 
				{

					Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));

					if (((UILineConnector) node).isSelected()) {

						if (((UILineConnector) node).isOuterTolerance(transferCoord)) {
							// outer tolerance no saving of mouse Point => PLUS
							// Key event
							lastReceivedLineMousePoint = null;
							// show delete color
							((UILineConnector) node).setDeleteColor();
						} else {
							lastReceivedLineMousePoint = new Point2D(t.getSceneX(), t.getSceneY());
							// change to normal select color
							((UILineConnector) node).removeDeleteColor();
						}
					}
				} 
				else if (ContentPane.this.getScene().getCursor() == Cursor.MOVE) 
				{
					// Habe noch keine Ahnung wie ich eine Kollisionsabfrage
					// reinfummel
					Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));

					if (transferCoord.getX() < 0)
						transferCoord = new Point2D(0, transferCoord.getY());
					if (transferCoord.getY() < 0)
						transferCoord = new Point2D(transferCoord.getX(), 0);

					double offsetX = transferCoord.getX() - orgSceneX;
					double offsetY = transferCoord.getY() - orgSceneY;

					double newTranslateX = orgTranslateX + offsetX;
					double newTranslateY = orgTranslateY + offsetY;

					// jetzt noch die Korrektur für den Fall die Komponente wird
					// außerhalb der möglichen darstellebaren Fläche verschoben
					if (newTranslateX < 0)
						newTranslateX = 0;
					if (newTranslateY < 0)
						newTranslateY = 0;

					// nicht außerhalb setzen lassen
					if ((newTranslateX + node.getBoundsInLocal().getWidth()) > ContentPane.this.getWidth())
						newTranslateX = ContentPane.this.getWidth() - node.getBoundsInLocal().getWidth();
					if ((newTranslateY + node.getBoundsInLocal().getHeight()) > ContentPane.this.getHeight())
						newTranslateY = ContentPane.this.getHeight() - node.getBoundsInLocal().getHeight();

					node.moveComponent(newTranslateX, newTranslateY);
				
					
					

				} else if (ContentPane.this.getScene().getCursor() == Cursor.HAND) {
					Point2D transferCoord = ContentPane.this.sceneToLocal(new Point2D(t.getSceneX(), t.getSceneY()));
					tempLine.setEndX(transferCoord.getX());
					tempLine.setEndY(transferCoord.getY());
				}
			}
			notSaved.set(true);
		}

	};

	private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			
			
			if (t.getSource() instanceof Canvas) 
			{

				if (selectionRect.isCatchedUIElements()) 
				{
					// I think nothing to do
				} 
				else 
				{
					// no component selected, search for it

					// beim loslassen muss geprüft werden ob in dem gezeicneten
					// Rechteck eine Komponente liegt

					boolean isComponentsInRect = false;

					Bounds boundsSelectionRect = new BoundingBox(selectionRect.getLayoutX(), selectionRect.getLayoutY(),
							selectionRect.getWidth(), selectionRect.getHeight());

					// TODO iterate through ui map?
					for (int i = 0; i < ContentPane.this.getChildren().size(); i++) 
					{
						Node node = ContentPane.this.getChildren().get(i);
						// only uielements
						if (node instanceof AUIElement) 
						{
							@SuppressWarnings("unchecked")
							AUIElement<? extends ALogicElement> uiElement = (AUIElement<? extends ALogicElement>) node;
							// ausschlaggebend sind nur die Elemente die nicht
							// Line sind
							if (!(uiElement instanceof UILineConnector)) 
							{
								Bounds uiBounds = null;
							
								// TODO widht and heigt from component ...it is ugly
								if(uiElement instanceof UICircleLineConnector)
								{
									uiBounds = new BoundingBox(uiElement.getTranslateX(), uiElement.getTranslateY(),
											10D, 10D);
								}
								else
								{
									uiBounds = new BoundingBox(uiElement.getTranslateX(), uiElement.getTranslateY(),
											150D, 150D);
									
								}
								
								if (UtilFx.isUIElementInShape(uiBounds, boundsSelectionRect)) {
									// change visulization from the element
									uiElement.collected(true);
									isComponentsInRect = true;
								}
							}
						}
					}
					if (!isComponentsInRect) 
					{
						
						if(changeListenerRectangleX != null)
						{
							selectionRect.getGroupedMovementProperties().getLeft().removeListener(changeListenerRectangleX);
						}
						
						if(changeListenerRectangleY != null)
						{
							selectionRect.getGroupedMovementProperties().getRight().removeListener(changeListenerRectangleY);
						}
						// sicherheitshalber die uiMap zurücksetzen
						for (Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet()) 
						{
							if (!(entry.getValue() instanceof UILineConnector)) 
							{
								entry.getValue().collected(false);
							}
						}
						// no components in rectangle found, delete dotted
						// rectangle
						ContentPane.this.getChildren().remove(selectionRect);
					}
					notSaved.set(true);
				}
			} 
			else if (t.getSource() instanceof AUIElement)
			{
				
				@SuppressWarnings("unchecked")
				AUIElement<? extends ALogicElement> node = (AUIElement<? extends ALogicElement>) t.getSource();
				if (node instanceof UILineConnector) 
				{
					UILineConnector uiNode = ((UILineConnector) node);
					if(uiNode.isDeletedDesignated())
              		{
						
						List<AUIElement<? extends ALogicElement>> tempList = new ArrayList<AUIElement<? extends ALogicElement>>();
						
						//first get the complete path from the line (all circle and all lines)
						
						int firstOutputIndex = getFirstOutputIndex(node, tempList);
						
						
						//System.exit(0);
						//now to the last element
						int lastInputIndex = getLastInputIndex(node, tempList);
						//first delete all circleConnector
						
						Iterator<AUIElement<? extends ALogicElement>> iterator = tempList.iterator();
						while(iterator.hasNext())
						{
							AUIElement<? extends ALogicElement> uiElement = iterator.next();
							if(uiElement instanceof UILineConnector)
							{
								UILineConnector lineToDelete = ((UILineConnector) uiElement);
								
								UILineOutputConnector outputConnector = (UILineOutputConnector) getConnector(lineToDelete.getLogicElement().getOutputId());
		              			UILineInputConnector inputConnector = (UILineInputConnector) getConnector(lineToDelete.getLogicElement().getInputId());
		                 		
		              			outputConnector.removeUIOutputConnector();
		              			//TODO not the best idea
		              			if(uiNode.getLogicElement().getInputId().getRight() == 1 && inputConnector instanceof UILineSecondInputConnector)
		              			{
		              				((UILineSecondInputConnector)inputConnector).removeUISecondInputConnector();
		              			}
		              			else
		              				inputConnector.removeUIInputConnector();
		              			
		              			Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
		              			//int indexFromMap = schemeObject.getIndexFromLogicElement(lineToDelete.getLogicElement());
		              		
		              			//if(indexFromMap >= 0)
		              				schemeObject.removeElementAtMap(lineToDelete.getLogicElement());
		              			
		              			deleteUINodeFromView(lineToDelete);
								
								
								
							}
						}
						
						//second run, delete all the circle connector
						iterator = tempList.iterator();
						while(iterator.hasNext())
						{
							AUIElement<? extends ALogicElement> uiElement = iterator.next();
							if(uiElement instanceof UICircleLineConnector)
							{
								UICircleLineConnector circleConnector = (UICircleLineConnector)uiElement;
								//sicherheitshalber
								circleConnector.removeUIInputConnector();
								circleConnector.removeUIOutputConnector();
								
								Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
		              			//int indexFromMap = schemeObject.getIndexFromLogicElement(circleConnector.getLogicElement());
		              			//if(indexFromMap >= 0)
		              				schemeObject.removeElementAtMap(circleConnector.getLogicElement());
		              			
		              			deleteUINodeFromView(circleConnector);
								
							}
							
							
							
						}
					}
              		else
              		{
              			uiNode.setSelected(false);
              		}
					notSaved.set(true);
				}
				// release the colored frame and the select flag
				else if (ContentPane.this.getScene().getCursor() == Cursor.MOVE)
				{
					System.out.println("move cursor");
					// it must be checked that the movement goes outside the
					// definied frame
					
					Bounds uiBounds = null;
					// TODO w und h flexible!
					if(node instanceof UICircleLineConnector)
						uiBounds = new BoundingBox(node.getTranslateX(), node.getTranslateY(), 10D, 10D);
					else
						uiBounds = new BoundingBox(node.getTranslateX(), node.getTranslateY(), 150D, 150D);

					if (selectionRect != null && ContentPane.this.getChildren().contains(selectionRect)) 
					{
						Bounds boundsSelectionRect = new BoundingBox(selectionRect.getLayoutX(),
								selectionRect.getLayoutY(), selectionRect.getWidth(), selectionRect.getHeight());
						if (!UtilFx.isUIElementInShape(uiBounds, boundsSelectionRect)) {
							// out of the shape, than reset flag value
							node.collected(false);
							removeChangeListenerFromCollectRect(node);
						} 
						else 
						{
							node.collected(true);
							addChangeListenerToCollectRect(node);
						}
					}
					// no selection rect ...reset flag
					else 
					{
						node.collected(false);
						removeChangeListenerFromCollectRect(node);
					}
					if (node.isSelected()) {
						node.setSelected(false);
					}
				} 
				else if (ContentPane.this.getScene().getCursor() == Cursor.HAND) 
				{
					// draw the line from started point to the cursor point.
					// decision to draw the real line or remove the temp line

					Point2D sceneCoords = new Point2D(t.getSceneX(), t.getSceneY());

					boolean found = false;
					for (int i = 0; i < ContentPane.this.getChildren().size(); i++) {
						// ich suche nach einer instanz die einen Eingang
						// besitzt

						Node uiNode = ContentPane.this.getChildren().get(i);

						// TODO second entry connector is missing
						if (uiNode instanceof UILineInputConnector) {
							UILineInputConnector uiLIneInputConnector = (UILineInputConnector) uiNode;
							IConnectorArea iConnectorArea = (IConnectorArea) uiNode;

							if (iConnectorArea.isInputArea(sceneCoords) && !uiLIneInputConnector.isUIInputOccupied()) {
								Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
								tempLine.setInputIndex(((AUIElement) uiNode).getLogicElement().getIndex());
								found = true;
								break;
							}
						}
						// when not found check the second input channel...not
						// the best solution
						if (!found) {

							if (uiNode instanceof UILineSecondInputConnector) {
								UILineSecondInputConnector uiLineSecondInputConnector = (UILineSecondInputConnector) uiNode;
								IConnectorArea iConnectorArea = (IConnectorArea) uiNode;

								if (iConnectorArea.isSecondInputArea(sceneCoords)
										&& !uiLineSecondInputConnector.isUISecondInputOccupied()) {
									Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
									tempLine.setInputIndex(((AUIElement) uiNode).getLogicElement().getIndex());
									tempLine.setSubIndexInput(1);
									found = true;
									break;
								}
							}
						}
					}

					
					if (found) {

						// build new uiLine/logicline
						Scheme schemeObject = SchemeDataStorage.getSchemeList().getActiveSchemeOnScreen();
						LineConnector lineConnector = new LineConnector();
						// add a new connector to the logical map; with the add
						// the line will get the new index
						schemeObject.addElementAtList(lineConnector);

						UILineConnector newLine = new UILineConnector(
								(LineConnector) schemeObject.getElementWithId(lineConnector.getIndex()));

						newLine.setOutputX(tempLine.getStartX());
						newLine.setOutputY(tempLine.getStartY());

						newLine.setInputX(tempLine.getEndX());
						newLine.setInputY(tempLine.getEndY());

						putUINodeAtMap(lineConnector.getIndex(), newLine);

						// connect the coordinates from the new line with the
						// input and output ui component
						// no question about the pickup of the value

						
						// TODO subindex must be in tempLine
						UILineOutputConnector outputConnector = (UILineOutputConnector) getConnector(
								new GenericPair<Integer, Integer>(tempLine.getOutputIndex(), 0));


					
						// here is the problem with the second input
						UILineInputConnector inputConnector = (UILineInputConnector) getConnector(
								(new GenericPair<Integer, Integer>(tempLine.getInputIndex(),
										tempLine.getSubIndexInput())));
						if (outputConnector != null && inputConnector != null) {

							// fill now the id to the logic elements
							AOutputElement outputElement = (AOutputElement) ((AUIElement) outputConnector)
									.getLogicElement();
							lineConnector.setMasteridOutput(outputElement.getIndex());

							if (tempLine.getSubIndexInput() == 0) {
								AInputOutputElement inAndOutElement = (AInputOutputElement) ((AUIElement) inputConnector)
										.getLogicElement();
								lineConnector.setMasteridInput(inAndOutElement.getIndex());
							} else if (tempLine.getSubIndexInput() == 1) {
								ADoubleInputOneOutputElement in2ndOutElement = (ADoubleInputOneOutputElement) ((AUIElement) inputConnector)
										.getLogicElement();
								lineConnector.setMasteridInput(in2ndOutElement.getIndex());
								lineConnector.setMasteridInputWithSubindex(tempLine.getInputIndex(),
										tempLine.getSubIndexInput());
							}
						}

						if (!lineConnector.isOutputEmpty() && !lineConnector.isInputEmpty()) {

							outputConnector.setUIOutputConnector(newLine);

							if (tempLine.getSubIndexInput() == 1)
								((UILineSecondInputConnector) inputConnector).setUISecondInputConnector(newLine);
							// normal
							else
								inputConnector.setUIInputConnector(newLine);
						}

						addMouseListener(newLine);
						// add to content
						ContentPane.this.getChildren().add(newLine);

					}
					// remove from view
					ContentPane.this.getChildren().remove(tempLine);
					notSaved.set(true);
				}
			}
			ContentPane.this.getScene().setCursor(Cursor.DEFAULT);
		}

	};

	/**
	 * needed, when a component removed from a user definied frame on screeen
	 * 
	 * @param node
	 */
	private void removeChangeListenerFromCollectRect(AUIElement<? extends ALogicElement> node) {
		
		if(changeListenerRectangleX == null || changeListenerRectangleY == null)
			return;
		changeListenerRectangleX.removeUIElement(node);
		changeListenerRectangleY.removeUIElement(node);
		
		if(changeListenerRectangleX.isListEmpty() || changeListenerRectangleY.isListEmpty())
		{
			selectionRect.getGroupedMovementProperties().getLeft().removeListener(changeListenerRectangleX);
			selectionRect.getGroupedMovementProperties().getRight().removeListener(changeListenerRectangleY);
		}
	}


	protected int getLastInputIndex(AUIElement<? extends ALogicElement> node, List<AUIElement<? extends ALogicElement>> nodeToDeleteList) 
	{
		if(node instanceof UILineConnector)
		{
			if(!nodeToDeleteList.contains(node))
				nodeToDeleteList.add(node);
			
			int inputid = ((UILineConnector)node).getLogicElement().getInputId().getLeft();
			AUIElement<?> element = this.findElement(inputid);
			if(element != null)
			{
				
				return getLastInputIndex(element, nodeToDeleteList);
			}
			else
				return inputid;
		}
		else if(node instanceof UICircleLineConnector)
		{
			//es wurde ein Kreis mit dem Index gefunden
			if(!nodeToDeleteList.contains(node))
				nodeToDeleteList.add(node);
			
			//der Kreis enthält aber keine Verbindungsinformationen mehr
			//sondern wir benötigen die nächste Komponente 
			
			//find InputIndex Element with ID of the Circle
			//von dem Circle den nächsten Input suchen mit der circle ID
			AUIElement<?> element = this.findOutputElement(((UICircleLineConnector)node).getLogicElement().getIndex());
			if(element != null)
			{
				return getLastInputIndex(element, nodeToDeleteList);
			}
			else
				return ((UICircleLineConnector)node).getLogicElement().getIndex();
		}
		return node.getLogicElement().getIndex();
	}


	/**
	 * Find the first outputindex from a "real" logic element
	 * @param node
	 * @return
	 */
	protected int getFirstOutputIndex(AUIElement<? extends ALogicElement> node, List<AUIElement<? extends ALogicElement>> nodeToDeleteList)
	{
		if(node instanceof UILineConnector)
		{
			if(!nodeToDeleteList.contains(node))
				nodeToDeleteList.add(node);
			int outputid = ((UILineConnector)node).getLogicElement().getOutputId().getLeft();
			
			AUIElement<?> element = this.findElement(outputid);
			if(element != null)
			{
				
				return getFirstOutputIndex(element, nodeToDeleteList);
			}
			else
				return outputid;
		}
		else if(node instanceof UICircleLineConnector)
		{
			if(!nodeToDeleteList.contains(node))
				nodeToDeleteList.add(node);
			
			//backward searching to the next output
			AUIElement<?> element = this.findInputElement(((UICircleLineConnector)node).getLogicElement().getIndex());
			if(element != null)
			{
				return getFirstOutputIndex(element, nodeToDeleteList);
			}
			else
				return ((UICircleLineConnector)node).getLogicElement().getIndex();
		}
		return node.getLogicElement().getIndex();
	}

	private AUIElement<?> findElement(int masterIndexOutput)
	{
		for(Entry<Integer, AUIElement<?>> entry : uiMap.entrySet())
		{
			if(entry.getValue().getLogicElement().getIndex() == masterIndexOutput)
				return entry.getValue();
		}
		
		return null;
	}
	
	/**
	 * sucht nach einem Element, dass den Parameter im Input beinhaltet
	 * @param idOfInput
	 * @return
	 */
	private AUIElement<?> findInputElement(int idOfInput)
	{
		for(Entry<Integer, AUIElement<?>> entry : uiMap.entrySet())
		{
			if(entry.getValue() instanceof UILineConnector)
			{
				UILineConnector uiLineConnector = (UILineConnector)entry.getValue();
				if(uiLineConnector.getLogicElement().isMasterIdInput(idOfInput))
					return uiLineConnector;
			}
			
			
		}
		return null;
	}
	
	/**
	 * sucht nach einem Element, dass den Parameter im Input beinhaltet
	 * @param idOfInput
	 * @return
	 */
	private AUIElement<?> findOutputElement(int idOfOutput)
	{
		for(Entry<Integer, AUIElement<?>> entry : uiMap.entrySet())
		{
			if(entry.getValue() instanceof UILineConnector)
			{
				UILineConnector uiLineConnector = (UILineConnector)entry.getValue();
				if(uiLineConnector.getLogicElement().isMasterIdOutput(idOfOutput))
					return uiLineConnector;
			}
		}
		return null;
	}
	

	/**
	 * a ui component is in the area of a user definied frame
	 * 
	 * @param node
	 */
	protected void addChangeListenerToCollectRect(AUIElement<? extends ALogicElement> uiElement) 
	{
		if (selectionRect != null && ContentPane.this.getChildren().contains(selectionRect)) 
		{

			Point2D movementInitCoords = selectionRect.getMovementStartCoords();
			uiElement.setGroupedMovementStartCoords(movementInitCoords);
			
			if(changeListenerRectangleX == null || changeListenerRectangleY == null)
			{
				changeListenerRectangleX = new XYSelectRectangleChangeListener(true);
				changeListenerRectangleY = new XYSelectRectangleChangeListener(false);
				
				
				selectionRect.getGroupedMovementProperties().getLeft().addListener(changeListenerRectangleX);
				selectionRect.getGroupedMovementProperties().getRight().addListener(changeListenerRectangleY);
			}
			changeListenerRectangleX.addUIElementAUIElement(uiElement);
			changeListenerRectangleY.addUIElementAUIElement(uiElement);
		}
	}

	private TreeMap<Integer, AUIElement<? extends ALogicElement>> restructureMap(TreeMap<Integer, AUIElement<? extends ALogicElement>> restructMap) {
		
		TreeMap<Integer, AUIElement<? extends ALogicElement>> newMap = new TreeMap<Integer, AUIElement<? extends ALogicElement>>();

		int startIndex = 0;

		// TODO können Lücken vorkommen? Eigentlich nicht weil es in schemedata
		// schon restrukturiert wird.
		for (Entry<Integer, AUIElement<? extends ALogicElement>> entry : restructMap.entrySet()) {
			newMap.put(startIndex, entry.getValue());
			startIndex++;
		}
		return newMap;
	}

	/**
	 * add the ui node with a exact position to the map
	 * 
	 * @param inputIndex
	 * @param newLine
	 */
	protected void putUINodeAtMap(int inputIndex, AUIElement<? extends ALogicElement> auiElement) {
		if (uiMap.get(inputIndex) != null) {
			int newIndex = inputIndex + 1;
			AUIElement<? extends ALogicElement> valueToMove = uiMap.get(inputIndex);
			// set the param from method
			uiMap.put(inputIndex, auiElement);
			// hop to the new index with the old value
			this.putUINodeAtMap(newIndex, valueToMove);
		} else {
			uiMap.put(inputIndex, auiElement);
		}
	}

	protected void deleteUINodeFromView(int indexFromMap) {
		AUIElement uiNode = uiMap.remove(indexFromMap);
		// TODO delete the connections from combined objects?
		uiMap = restructureMap(uiMap);
		ContentPane.this.getChildren().remove(uiNode);
	}
	

	private void deleteUINodeFromView(AUIElement element) 
	{
		for(Entry<Integer, AUIElement<? extends ALogicElement>> entry : uiMap.entrySet())
		{
			if(entry.getValue().equals(element))
			{
				uiMap.remove(entry.getKey());
				break;
			}
			
		}
		ContentPane.this.getChildren().remove(element);
		uiMap = restructureMap(uiMap);
		
	}



}
