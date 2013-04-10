package ru.mipt.sign.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import ru.mipt.sign.connect.Connection;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.Neuron;

public class ParserXml
{

	NeuroNet net = new NeuroNet();
	Document doc;

	public BigInteger getCurrentId()
	{
		XPath xpath;
		try
		{
			xpath = XPath.newInstance("//configuration//current_id");
			Element id = (Element) xpath.selectSingleNode(doc);
			return new BigInteger(id.getAttribute("value").getValue());
		} catch (JDOMException e)
		{
			e.printStackTrace();
		}
		return BigInteger.ZERO;
	}

	public ParserXml(String path) throws NextCommandException
	{
		if (!path.isEmpty())
		{
			SAXBuilder builder = new SAXBuilder();
			String data = "";

			File file = new File(path);
			int ch;
			StringBuffer strContent = new StringBuffer("");
			FileInputStream fin = null;
			try
			{
				fin = new FileInputStream(file);
				while ((ch = fin.read()) != -1)
					strContent.append((char) ch);
				fin.close();
			} catch (Exception e)
			{
				System.out.println("File '" + path + "' not found");
				throw new NextCommandException();
			}

			data = strContent.toString();

			try
			{
				doc = builder.build(new ByteArrayInputStream(data.getBytes()));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} else
		{
			System.out.println("Path for configuration is empty!");
		}
	}

	public List<Connection> getConnections(NeuroNet net) throws NeuronNotFound
	{
		List<Connection> connections = new ArrayList<Connection>();
		try
		{
			XPath xpath;
			xpath = XPath.newInstance("//configuration//neuronet");
			Element nn = (Element) xpath.selectSingleNode(doc);
			List<Element> results = nn.getChildren("connection");
			if (results.size() > 0)
			{
				for (Iterator<Element> it = results.iterator(); it.hasNext();)
				{
					connections.add(new Connection(it.next(), net));
				}
			}
		} catch (JDOMException e)
		{
			e.printStackTrace();
		}
		return connections;
	}
	
	public List<BigInteger> getNeuronsByKey(String key)
	{
		List<BigInteger> neurons = new ArrayList<BigInteger>();
		try
		{
			XPath xpath;
			xpath = XPath.newInstance("//configuration//neuronet//" + key);
			Element nn = (Element) xpath.selectSingleNode(doc);
			List<Element> results = nn.getChildren();
			if (results.size() > 0)
			{
				for (Iterator<Element> it = results.iterator(); it.hasNext();)
				{
					neurons.add(new BigInteger(it.next().getAttribute("id").getValue()));
				}
			}
		} catch (JDOMException e)
		{
			e.printStackTrace();
		}
		return neurons;
	}

	public List<Neuron> getNeurons()
	{
		List<Neuron> neurons = new ArrayList<Neuron>();
		try
		{
			XPath xpath;
			xpath = XPath.newInstance("//configuration//neuronet");
			Element nn = (Element) xpath.selectSingleNode(doc);
			List<Element> results = nn.getChildren("neuron");
			if (results.size() > 0)
			{
				for (Iterator<Element> it = results.iterator(); it.hasNext();)
				{
					neurons.add(new Neuron(it.next()));
				}
			}
		} catch (JDOMException e)
		{
			e.printStackTrace();
		}
		return neurons;
	}
}