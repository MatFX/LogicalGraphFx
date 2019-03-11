package eu.matfx.gui.helper;

import java.io.Serializable;

public class GenericPair<LEFT, RIGHT> implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4917205497682520568L;
	
	private LEFT left;
	
	private RIGHT right;

	public GenericPair(LEFT left, RIGHT right)
	{
		this.left = left;
		this.right = right;
	}

	public LEFT getLeft() {
		return left;
	}

	public RIGHT getRight() {
		return right;
	}

	public void setLeft(LEFT left) {
		this.left = left;
	}

	public void setRight(RIGHT right) {
		this.right = right;
	}
	
	//TODO hashcode compare?

}
