package eu.matfx.gui.main;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;

public class HeightSceneChangeListener implements ChangeListener<Number>
{
	private ScrollPane scrollPane;
	
	private double maxHeightPane;
	
	private double schrittweiteFuerEinPixel; 
	
	private ContentPane contentPane;
	
	
	public HeightSceneChangeListener(ScrollPane scrollPane, ContentPane contentPane,  double maxHeightPane)
	{
		this.scrollPane = scrollPane;
		this.maxHeightPane = maxHeightPane;
		this.schrittweiteFuerEinPixel = 1D / maxHeightPane;
		this.contentPane = contentPane;
	}
	

	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
	{
		if(contentPane != null)
			contentPane.setPrefHeight(newValue.doubleValue());
		
		if(oldValue.doubleValue() > newValue.doubleValue())
		{
			scrollPane.setVvalue(scrollPane.getVvalue()-schrittweiteFuerEinPixel);
		}
		else if(oldValue.doubleValue() < newValue.doubleValue())
		{
			scrollPane.setVvalue(scrollPane.getVvalue()+schrittweiteFuerEinPixel);
		}
		
		
	}

}
