package eu.matfx.logic.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.matfx.gui.helper.GenericPair;

public class GenericPairAdapter extends XmlAdapter<PairedInteger, GenericPair<Integer, Integer>>
{

	@Override
	public GenericPair<Integer, Integer> unmarshal(PairedInteger v) throws Exception 
	{
		if(v != null)
			return new GenericPair<Integer, Integer>(v.getMasterIndex(), v.getSubIndex());
		return new GenericPair<Integer, Integer>(Integer.MIN_VALUE, 0);
	}

	@Override
	public PairedInteger marshal(GenericPair<Integer, Integer> v) throws Exception 
	{
		if(v != null)
			return new PairedInteger(v.getLeft(), v.getRight());
		return new PairedInteger(Integer.MIN_VALUE, 0);
	}

}
