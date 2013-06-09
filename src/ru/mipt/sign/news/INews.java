package ru.mipt.sign.news;

import java.util.Date;
//import java.util.Vector;

import org.jdom.Element;

public interface INews
{

	public String getText();
	public Date getDate();
	public String getSource();
	public Element getXml();
	//public Double getAmplitude();

	//public Vector<Double> getVector();
}
