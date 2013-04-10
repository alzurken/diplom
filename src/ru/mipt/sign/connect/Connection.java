package ru.mipt.sign.connect;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.DataConversionException;
import org.jdom.Element;

import ru.mipt.sign.core.SObject;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.Neuron;

public class Connection extends SObject
{
    private Neuron aNeuron;
    private Neuron zNeuron;
    private Map<Integer, Integer> a2zMapping;
    private NeuroNet parent;

    public Neuron getANeuron()
    {
        return aNeuron;
    }

    public Neuron getZNeuron()
    {
        return zNeuron;
    }

    public BigInteger getAID()
    {
        return aNeuron.getID();
    }

    public List<Integer> getOutputNumbers()
    {
        return new ArrayList<Integer>(a2zMapping.keySet());
    }

    public BigInteger getZID()
    {
        return zNeuron.getID();
    }

    public boolean chechASide(Integer start)
    {
        Integer min = Collections.min(a2zMapping.keySet());
        return min >= start;
    }

    public boolean chechZSide(Integer start)
    {
        Integer min = Collections.min(a2zMapping.values());
        return min >= start;
    }

    public void moveASide(Integer delta)
    {
        HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
        for (Integer i : a2zMapping.keySet())
        {
            temp.put(i - delta, a2zMapping.get(i));
        }
        a2zMapping = temp;
    }

    public void moveZSide(Integer delta)
    {
        HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
        for (Integer i : a2zMapping.keySet())
        {
            temp.put(i, a2zMapping.get(i) - delta);
        }
        a2zMapping = temp;
    }

    public void disconnect()
    {
        aNeuron.removeConnection(this);
        aNeuron.removeOutputs(new ArrayList<Integer>(a2zMapping.keySet()));
        Integer aStart = Collections.max(a2zMapping.keySet()) + 1;
        Integer delta = a2zMapping.keySet().size();
        List<Connection> forMove = parent.getConnectionsForRefactor(aNeuron, aStart, true);
        for (Connection c : forMove)
        {
            c.moveASide(delta);
        }
        zNeuron.removeInputs(new ArrayList<Integer>(a2zMapping.values()));
        Integer zStart = Collections.max(a2zMapping.values()) + 1;
        forMove = parent.getConnectionsForRefactor(zNeuron, zStart, false);
        for (Connection c : forMove)
        {
            c.moveZSide(delta);
        }
        System.out.println("Disconnected neurons " + aNeuron.getID() + " and " + zNeuron.getID());
    }

    public Connection(Element el, NeuroNet nn) throws NeuronNotFound
    {
        super(el);
        parent = nn;
        Element connection = el;
        init();
        try
        {
            this.aNeuron = parent.getNeuron(new BigInteger(connection.getAttribute("aNeuron").getValue()));
            aNeuron.addConnection(this);
            this.zNeuron = parent.getNeuron(new BigInteger(connection.getAttribute("zNeuron").getValue()));

            Element mapping = connection.getChild("mapping");
            @SuppressWarnings("unchecked")
            List<Element> items = mapping.getChildren();
            for (Iterator<Element> it = items.iterator(); it.hasNext();)
            {
                Element item = it.next();
                Integer aIndex = item.getAttribute("a").getIntValue();
                Integer zIndex = item.getAttribute("z").getIntValue();
                a2zMapping.put(aIndex, zIndex);
            }
        } catch (DataConversionException e)
        {
            e.printStackTrace();
        }
    }

    public Connection(BigInteger id, NeuroNet nn)
    {
        super(id);
        init();
        parent = nn;
    }

    private void init()
    {
        a2zMapping = new HashMap<Integer, Integer>();
    }

    @Override
    public Element getXML()
    {
        Element connection = new Element("connection");
        connection.setAttribute("id", id.toString());
        connection.setAttribute("aNeuron", aNeuron.getID().toString());
        connection.setAttribute("zNeuron", zNeuron.getID().toString());

        Element mapping = new Element("mapping");
        for (Integer i : a2zMapping.keySet())
        {
            Element item = new Element("item");
            item.setAttribute("a", i.toString());
            item.setAttribute("z", a2zMapping.get(i).toString());
            mapping.addContent(item);
        }
        connection.addContent(mapping);
        return connection;
    }

    public void connect(BigInteger aNeuronID, BigInteger zNeuronID, Integer fiber) throws NeuronNotFound
    {
        aNeuron = parent.getNeuron(aNeuronID);
        aNeuron.addConnection(this);
        zNeuron = parent.getNeuron(zNeuronID);
        List<Integer> aSide = aNeuron.getUnboundOutputs(fiber); // set output number resize neuron
        aNeuron.addOutputs(aSide);
        List<Integer> zSide = zNeuron.getUnboundInputs(fiber);
        zNeuron.addInputs(zSide);
        for (Integer i = 0; i < aSide.size(); i++)
        {
            a2zMapping.put(aSide.get(i), zSide.get(i));
        }

    }

    public void emit()
    {
        Map<Integer, Double> output = aNeuron.getOutput();
        Integer zIndex;
        for (Integer i : a2zMapping.keySet())
        {
            zIndex = a2zMapping.get(i);
            zNeuron.addInputValue(zIndex, output.get(i));
        }
    }

    @Override
    public String getType()
    {
        return "connection";
    }
}
