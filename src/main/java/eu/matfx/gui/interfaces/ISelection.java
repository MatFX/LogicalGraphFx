package eu.matfx.gui.interfaces;

import javafx.scene.Node;
import javafx.scene.effect.Glow;

/**
 * Bei allen Komponenten hinzufügen die auswählbar sind....evtl. später noch erweitern um Fokus
 * @author m.goerlich
 *
 */
public interface ISelection 
{
	public boolean isSelected();
	
	public void setSelected(boolean isSelected);
	
	public static void drawSelection(boolean isSelected, Node node)
	{
		if(isSelected)
			node.setEffect(new Glow(0.6));
		else
			node.setEffect(null);
	}
}
