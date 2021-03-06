package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.connect.Connection;
import ru.mipt.sign.core.SObject;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.util.ParserXml;

public class NeuroNet
{
	private HashMap<BigInteger, Neuron> neuroPool;
	private HashMap<BigInteger, Connection> connPool;
	private HashMap<BigInteger, Neuron> cache;
	private List<Neuron> inputNeurons; // id, number
	private Integer inputNumber;
	private Neuron output;
	private ApplicationContext appCtx;

	public List<BigInteger> getNeurons()
	{
		return new ArrayList<BigInteger>(neuroPool.keySet());
	}
	
	public List<BigInteger> getConnections()
	{
		return new ArrayList<BigInteger>(connPool.keySet());
	}

	public List<Connection> getConnectionsForZNeuron(BigInteger id)
	{
		List<Connection> result = new ArrayList<Connection>();
		for (Connection c : connPool.values())
		{
			if(c.getZID().equals(id))
				result.add(c);
		}
		return result;
	}
	
	public Element getXml()
	{
		Element neuronet = new Element("neuronet");
		for (BigInteger i : neuroPool.keySet())
		{
			neuronet.addContent(neuroPool.get(i).getXML());
		}
		for (BigInteger i : connPool.keySet())
		{
			neuronet.addContent(connPool.get(i).getXML());
		}
		Element in = new Element("input_neurons");
		for (int i = 0; i < inputNeurons.size(); i++)
		{
			Element item = new Element("item");
			item.setAttribute("id", inputNeurons.get(i).getID().toString());
			in.addContent(item);
		}
		neuronet.addContent(in);

		Element out = new Element("output_neurons");
		Element item = new Element("item");
		item.setAttribute("id", output.getID().toString());
		out.addContent(item);
		neuronet.addContent(out);
		return neuronet;
	}

	public Integer getInputNumber()
	{
		return inputNumber;
	}

	public List<Double> getResult()
	{
		List<Double> result = new ArrayList<Double>();
		Map<Integer, Double> out = output.getInput();
		for (Integer i = 0; i < out.size(); i++)
		{
			result.add(out.get(i));
		}
		return result;
	}

	public List<Connection> getConnectionsForRefactor(Neuron neuron, Integer start, boolean aSide)
	{
		List<Connection> result = new ArrayList<Connection>();
		for (Connection c : connPool.values())
		{
			if(aSide)
			{
				if (c.getAID().equals(neuron.getID()))
				{
					if(c.chechASide(start))
						result.add(c);
				}
			}
			else
			{
				if (c.getZID().equals(neuron.getID()))
				{
					if (c.chechZSide(start))
						result.add(c);
				}
			}
		}
		return result;
	}
	
	public void setInputNumber(Integer inputNumber)
	{
		this.inputNumber = inputNumber;
	}

	public void setInput(List<BigInteger> neurons) throws NeuronNotFound
	{
		if ((neurons != null) && (!neurons.isEmpty()))
		{
			for (Integer i = 0; i < neurons.size(); i++)
			{
				inputNeurons.add((Neuron)getNeuron(neurons.get(i)));
			}
		}
	}

	public void nextInput(List<Double> input)
	{
		for (Iterator<Neuron> it = this.neuroPool.values().iterator(); it.hasNext();)
		{
			Neuron n = it.next();
			if (n.getState() != NeuronConst.STATE_OUTPUT)
				n.setState(NeuronConst.STATE_INIT);
		}
		for (Iterator<Neuron> it = this.inputNeurons.iterator(); it.hasNext();)
		{
			Neuron n = it.next();
			n.setInput(input);
		}
	}

	public Integer removeNeuron(BigInteger id)
	{
		return neuroPool.remove(id) != null ? 0 : 1;
	}
	
	public Integer removeConnection(BigInteger id)
	{
		return connPool.remove(id) != null ? 0 : 1;
	}

	public void setInputNeuron(BigInteger id) throws NeuronNotFound
	{
		inputNeurons.add(this.getNeuron(id));
		getNeuron(id).setInNumber(NeuronConst.DEFAULT_FIBER_NUMBER);
	}
	
	public void connectNeuron(BigInteger id1, BigInteger id2, Integer fiber) throws NeuronNotFound
	{
		Connection conn = new Connection(appCtx.getNextId(), this);
		connPool.put(conn.getID(), conn);
		conn.connect(id1, id2, fiber);
	}

	public NeuroNet()
	{
		neuroPool = new HashMap<BigInteger, Neuron>();
		connPool = new HashMap<BigInteger, Connection>();
		cache = new HashMap<BigInteger, Neuron>();
		inputNeurons = new ArrayList<Neuron>();
	}

	public NeuroNet(Integer number, ApplicationContext appCtx)
	{
		this();
		this.appCtx = appCtx;
		for (int i = 0; i < number; i++)
		{
			BigInteger id = appCtx.getNextId();
			neuroPool.put(id, new Neuron(id));
		}
		this.output = new Neuron(NeuronConst.LAST_NEURON_ID);
		output.setState(NeuronConst.STATE_OUTPUT);
		neuroPool.put(output.getID(), output);
		randomize();
	}
	
	public void randomize()
	{
		for (BigInteger id : neuroPool.keySet())
		{
			SObject so = neuroPool.get(id);
			if (so.getType().equalsIgnoreCase("neuron"))
			{
				((Neuron)so).randomize();
			}
		}
	}

	public void setOutput(List<BigInteger> out) throws NeuronNotFound
	{
		Neuron n = getNeuron(out.get(0));
		n.setState(NeuronConst.STATE_OUTPUT);
		output = n;
	}

	public NeuroNet(ParserXml parser, ApplicationContext appCtx) throws NeuronNotFound
	{
		this();
		List<Neuron> neurons = parser.getNeurons();
		this.appCtx = appCtx;
		if (neurons != null && !neurons.isEmpty())
		{
			Iterator<Neuron> it = neurons.iterator();
			while (it.hasNext())
			{
				Neuron curr = it.next();
				neuroPool.put(curr.getID(), curr);
			}
		}
		List<Connection> connections = parser.getConnections(this);
		if(connections != null && !neurons.isEmpty())
		{
			Iterator<Connection> it = connections.iterator();
			while (it.hasNext())
			{
				Connection curr = it.next();
				connPool.put(curr.getID(), curr);
			}
		}
		setInput(parser.getNeuronsByKey("input_neurons"));
		setOutput(parser.getNeuronsByKey("output_neurons"));
	}

	public BigInteger addNeuron()
	{
		BigInteger id = appCtx.getNextId();
		neuroPool.put(id, new Neuron(id));
		return id;
	}

	public void addNeuron(Neuron neuron)
	{
		neuroPool.put(neuron.getID(), neuron);
	}

	public Neuron getNeuron(BigInteger id) throws NeuronNotFound
	{
		if (neuroPool.get(id) == null)
		{
			throw new NeuronNotFound(id);
		}
		return (Neuron)neuroPool.get(id);
	}

	public void calc() throws NeuronNotFound
	{
		cache = new HashMap<BigInteger, Neuron>();
		for (BigInteger k : neuroPool.keySet())
		{
			SObject so = neuroPool.get(k);
			if (!so.getType().equalsIgnoreCase("neuron"))
				continue;
			Neuron n = (Neuron)so;
			if (n.getState() == NeuronConst.STATE_READY)
			{
				n.calc();
				n.emit();
			}
			if (n.getState() == NeuronConst.STATE_INIT)
			{
				cache.put(n.getID(), n);
			}
		}
		while (!cache.isEmpty())
		{
			HashMap<BigInteger, Neuron> bufferCache = new HashMap<BigInteger, Neuron>();
			Iterator<Neuron> it = cache.values().iterator();
			while (it.hasNext())
			{
				Neuron n = it.next();
				if (n.getState() == NeuronConst.STATE_READY)
				{
					n.calc();
					n.emit();
				}
				else
				{
					bufferCache.put(n.getID(), n);
				}
			}
			cache = bufferCache;
		}
	}

	@Override
	public String toString()
	{
		StringBuffer out = new StringBuffer();
		Element net = appCtx.getNet().getXml();
		out.append(net.toString());
		if (!net.getChildren().isEmpty())
		{
			out.append(net.getChildren());
		}
		return out.toString();
	}
}
