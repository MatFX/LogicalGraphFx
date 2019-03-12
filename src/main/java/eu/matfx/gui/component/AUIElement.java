package eu.matfx.gui.component;

import eu.matfx.gui.interfaces.IConnectorArea;
import eu.matfx.gui.interfaces.IMoveComponent;
import eu.matfx.gui.util.Tools;
import eu.matfx.logic.data.ALogicElement;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public abstract class AUIElement<T extends ALogicElement> extends Group implements IMoveComponent, IConnectorArea
{
	private T aLogicElement;
	
	//needed at all ui elemnts?
	protected boolean isSelected;
	
	protected AUIElement(T logicElement)
	{
		this.aLogicElement = logicElement;
	}
	

	@Override
	public boolean isMovePossible(MouseEvent mouseEvent) {
		// TODO die Anfasser dürfen nicht als Bewegungseinstiegspunkte erhalten
		return true;
	}

	@Override
	public void moveComponent(double newTranslateX, double newTranslateY) 
	{
	
		//Neue Koordinate für die komplette GroupComponent
		this.setTranslateX(newTranslateX);
		this.setTranslateY(newTranslateY);
		//informe the connected ui lines
		recalcualteCenterPoint();
	}
	
	/**
	 * help method to find the concrete connectivity between ui and logic object
	 * @return
	 */
	public Class<? extends ALogicElement> getLogicClass()
	{
		return aLogicElement.getClass();
	}
	
	public T getLogicElement()
	{
		return this.aLogicElement;
	}
	
	//TODO move it to the implementation classes
	@Override
	public void setSelected(boolean isSelected) 
	{
		this.isSelected = isSelected;
	}

	@Override
	public boolean isSelected() {
		return this.isSelected;
	}
	
	
	public static AUIElement getInstance(ALogicElement aLogicElement)
	{
		
		
		Class<?>[] clazz = Tools.getClasses(AUIElement.class.getPackage().getName(), true);
		Class[] argument = new Class[1]; 
		argument[0] = aLogicElement.getClass();
		for(int i = 0; i < clazz.length; i++)
		{
			AUIElement returnValue = null;
			try
			{
				
				returnValue = (AUIElement) clazz[i].getDeclaredConstructor(argument).newInstance(aLogicElement);
				return returnValue;
				
			}
			catch(NoSuchMethodException nm)
			{
				//nm.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}
			
			
		}
		return null;
	}
	
	
	
	
	


}
