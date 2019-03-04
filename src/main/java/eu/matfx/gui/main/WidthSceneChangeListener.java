package eu.matfx.gui.main;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;

public class WidthSceneChangeListener implements ChangeListener<Number>
{
	
	private ScrollPane scrollPane;
	
	private double maxWidthPane;
	
	private double schrittweiteFuerEinPixel; 
	
	private ContentPane contentPane;
	
	public WidthSceneChangeListener(ScrollPane scrollPane, ContentPane contenPane, double maxWidthPane)
	{
		this.scrollPane = scrollPane;
		this.maxWidthPane = maxWidthPane;
		this.schrittweiteFuerEinPixel = 1D / maxWidthPane;
		this.contentPane = contenPane;
	}

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
	{
		if(contentPane != null)
			contentPane.setPrefWidth(newValue.doubleValue());
		
		if(oldValue.doubleValue() > newValue.doubleValue())
		{
			scrollPane.setHvalue(scrollPane.getHvalue()-schrittweiteFuerEinPixel);
		}
		else if(oldValue.doubleValue() < newValue.doubleValue())
		{
			scrollPane.setHvalue(scrollPane.getHvalue()+schrittweiteFuerEinPixel);
		}
		
		
	}

}
