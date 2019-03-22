package eu.matfx.gui.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.matfx.gui.component.AUIElement;
import eu.matfx.logic.data.ALogicElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * all selected commponents (user definied rectangle) listening on the coordinates of the rectangle
 * <br>This is the help container for listening on x or y coordinate.
 * @author m.goerlich
 *
 */
public class XYSelectRectangleChangeListener implements ChangeListener<Number>
{
	
	private List<AUIElement<? extends ALogicElement>> uiElementList;
	
	private boolean isX = true;
	
	public XYSelectRectangleChangeListener(boolean isX) 
	{
		this.isX = isX;
		this.uiElementList = new ArrayList<AUIElement<? extends ALogicElement>>();
	}
	
	public void addUIElementAUIElement(AUIElement<? extends ALogicElement> uiElement)
	{
		//only add when object not in list
		if(!uiElementList.contains(uiElement))
		{
			uiElementList.add(uiElement);
		}
	
	}


	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		
	
		
		if(isX)
		{
			Iterator<AUIElement<? extends ALogicElement>> iterator = uiElementList.iterator(); 
			while(iterator.hasNext())
			{
				iterator.next().setGroupedMovementX(newValue.doubleValue());
			}
			
		}
		else
		{
			Iterator<AUIElement<? extends ALogicElement>> iterator = uiElementList.iterator(); 
			while(iterator.hasNext())
			{
				iterator.next().setGroupedMovementY(newValue.doubleValue());
			}
		}
		
	}


	public void removeUIElement(AUIElement<? extends ALogicElement> node) 
	{
		uiElementList.remove(node);
	}


	public boolean isListEmpty() 
	{
		if(uiElementList == null || uiElementList.size() <= 0)
			return true;
		return false;
	}


}
