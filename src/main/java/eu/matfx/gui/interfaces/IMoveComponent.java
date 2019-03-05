package eu.matfx.gui.interfaces;

import javafx.scene.input.MouseEvent;

/**
 * some methods to move a ui component on screen
 * @author m.goerlich
 *
 */
public interface IMoveComponent 
{

	/**
	 * to signalize the component the selection
	 * @param isSelected
	 * @return
	 */
	public void setSelected(boolean isSelected);
	/**
	 * selection state of ui component
	 * @return
	 */
	public boolean isSelected();
	
	/**
	 * TODO weiß ich noch nicht ob es hier benötigt wird
	 * @return
	 */
	public boolean isMovePossible(MouseEvent mouseEvent);
	
	public void moveComponent(double newTranslateX, double newTranslateY);

}
