package ru.mipt.sign.core;

import java.math.BigInteger;

import org.jdom.Element;

public abstract class SObject
{
	protected BigInteger id;
	protected Element image;
	
	public BigInteger getID()
	{
		return id;
	}
	
	public SObject(BigInteger id)
	{
		this.id = id;
	}
	
	public  SObject(Element el)
	{
		this.image = el;
		this.id = new BigInteger(image.getAttribute("id").getValue());
	}
	
	public abstract Element getXML();
	
	public abstract String getType();
}
