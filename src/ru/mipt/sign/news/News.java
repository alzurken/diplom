package ru.mipt.sign.news;

import java.util.Date;

import org.jdom.Element;

public class News  implements INews
{

	private String text; // original text
	private Date date; // original date
	private String source; // original source
	
	//private Vector<Double> vector; // result vector
	//private Double amplitude; // amplitude

	//public Double getInfluence(Double t)
	//{
	//	return amplitude * Math.exp(-t / amplitude);
	//}

	public News(String text, Date date, String source)
	{
		super();
		this.text = text;
		this.date = date;
		this.source = source;
	}

	public String getText()
	{
		return text;
	}
	public Date getDate()
	{
		return date;
	}
	public String getSource()
	{
		return source;
	}
/*
	 public News(Element elem) throws ParseException 
	    {
	  //      super(elem);
	        Element news = elem;
	        //this.source = news.getAttribute("source").getValue();
			//SimpleDateFormat sdf = new SimpleDateFormat();
			//this.date = sdf.parse(news.getAttribute("date").getValue());
			this.text = news.getAttribute("text").toString();
	    }*/
	 

	public Element getXml()
	{
		Element newsXml = new Element("News");
		
			Element sourceXml = new Element("source");
			sourceXml.setAttribute("source", source);
			newsXml.addContent(sourceXml);
			Element dateXml = new Element("date");
			dateXml.setAttribute("date", date.toString());
			newsXml.addContent(dateXml);
			Element textXml = new Element("text");
			
			textXml.setAttribute("text", text);
			newsXml.addContent(textXml);
		
	return newsXml;
	}
	
	


}
