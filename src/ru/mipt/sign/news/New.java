package ru.mipt.sign.news;

import java.util.List;
import java.util.Vector;

public class New implements INew
{

	private String text; // original text
	private Vector<Double> vector; // result vector
	private Double amplitude; // amplitude

	public Double getInfluence(Double t)
	{
		return amplitude * Math.exp(-t / amplitude);
	}

	public New(String text)
	{
		super();
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public Double getAmplitude()
	{
		return amplitude;
	}

	public Vector<Double> getVector()
	{
		return vector;
	}

}
