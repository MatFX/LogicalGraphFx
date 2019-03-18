package eu.matfx.logic.helper;

import java.io.Serializable;

public class PairedInteger implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2828162872315154756L;

	private int masterIndex = Integer.MIN_VALUE;
	
	private int subIndex = 0;
	
	public PairedInteger()
	{
	}
	
	public PairedInteger(int masterIndex, int subIndex)
	{
		this.masterIndex = masterIndex;
		this.subIndex = subIndex;
	}

	public int getMasterIndex() {
		return masterIndex;
	}

	public int getSubIndex() {
		return subIndex;
	}

	public void setMasterIndex(int masterIndex) {
		this.masterIndex = masterIndex;
	}

	public void setSubIndex(int subIndex) {
		this.subIndex = subIndex;
	}

}
