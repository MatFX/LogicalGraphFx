package eu.matfx.gui.main;


import eu.matfx.gui.interfaces.IHScrollListener;
import eu.matfx.gui.interfaces.IVScrollListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;

/**
 * Eigene Klasse ist nur notwendig um die Informationen der Miniaturansicht zu erhalten 
 * <br>(Bewegung des roten Rechtecks durch Anwender)
 * @author m.goerlich
 *
 */
public class ScrollPaneExtended extends ScrollPane implements IVScrollListener, IHScrollListener
{
	public ScrollPaneExtended()
	{
		super();
	}

	@Override
	public ChangeListener<Number> getHScrollListener()
	{
		return new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
			{
				//Der Wert kann übernommen werden, dieser liegt bereits in "Scrollformat" vor.
				setHvalue((double) newValue);
			}
		};
	}

	@Override
	public ChangeListener<Number> getVScrollListener() 
	{
		return new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
			{
				//Der Wert kann übernommen werden, dieser liegt bereits in "Scrollformat" vor.
				setVvalue((double) newValue);
			}
		};
	}
}
