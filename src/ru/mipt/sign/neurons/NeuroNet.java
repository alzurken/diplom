package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.*;

import org.jdom.Element;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.CalculationException;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.data.InputDataProvider;
import ru.mipt.sign.neurons.factory.NeuronFactory;
import ru.mipt.sign.neurons.inner.DefaultCalculator;
import ru.mipt.sign.neurons.inner.NeuroNetCalculator;
import ru.mipt.sign.util.ParserXml;
import ru.mipt.sign.util.comparator.OrderComparator;

public class NeuroNet
{
    private HashMap<BigInteger, Neuron> neuroPool;
    private Set<Connection> connPool;
    private TreeSet<Neuron> inputNeurons; // id, number
    private int inputNumber;
    private int outputNumber;
    private TreeSet<Neuron> outputNeurons;
    private NeuroNetCalculator calculator;
    private InputDataProvider inputProvider;

    {
        neuroPool = new HashMap<BigInteger, Neuron>();
        connPool = new HashSet<Connection>();
        inputNeurons = new TreeSet<Neuron>(new OrderComparator());
        outputNeurons = new TreeSet<Neuron>(new OrderComparator());
    }

    public void setCalclulator(NeuroNetCalculator calclulator)
    {
        this.calculator = calclulator;
    }

    public int getOutputNumber()
    {
        return outputNumber;
    }

    public List<Neuron> getNeurons()
    {
        return new ArrayList<Neuron>(neuroPool.values());
    }

    public List<BigInteger> getNeuronIDs()
    {
        return new ArrayList<BigInteger>(neuroPool.keySet());
    }

    public List<Connection> getConnections()
    {
        return new ArrayList<Connection>(connPool);
    }

    public List<Connection> getConnectionsForZNeuron(BigInteger id)
    {
        List<Connection> result = new ArrayList<Connection>();
        for (Connection c : connPool)
        {
            if (c.getZID().equals(id))
                result.add(c);
        }
        return result;
    }

    public List<Connection> getConnectionsForANeuron(BigInteger id)
    {
        List<Connection> result = new ArrayList<Connection>();
        for (Connection c : connPool)
        {
            if (c.getAID().equals(id))
                result.add(c);
        }
        return result;
    }

    public Element getXml()
    {
        Element neuronet = new Element("neuronet");
        for (BigInteger i : neuroPool.keySet())
        {
            neuronet.addContent(NeuronFactory.getXML(neuroPool.get(i)));
        }
        for (Connection c : connPool)
        {
            neuronet.addContent(c.getXML());
        }
        Element in = new Element("input_neurons");
        for (Neuron n : inputNeurons)
        {
            Element item = new Element("item");
            item.setAttribute("id", n.getID().toString());
            in.addContent(item);
        }
        neuronet.addContent(in);

        Element out = new Element("output_neurons");
        out.setAttribute("out_number", "" + outputNeurons.size());
        for (Neuron n : outputNeurons)
        {
            Element item = new Element("item");
            item.setAttribute("id", n.getID().toString());
            out.addContent(item);
        }
        neuronet.addContent(out);
        return neuronet;
    }

    public List<Double> getResult()
    {
        List<Double> result = new ArrayList<Double>();
        for (Neuron n : outputNeurons)
        {
            Map<Integer, Double> out = n.getOutput();
            result.add(out.get(0));
        }
        return result;
    }

    public List<Neuron> getOutputNeurons()
    {
        List<Neuron> result = new ArrayList<Neuron>();
        for (int i = 0; i < outputNumber; i++)
        {
            result.add(neuroPool.get(NeuronConst.LAST_NEURON_ID.add(BigInteger.valueOf(i))));
        }
        return result;
    }

    public List<Connection> getConnectionsForRefactor(Neuron neuron, Integer start, boolean aSide)
    {
        List<Connection> result = new ArrayList<Connection>();
        for (Connection c : connPool)
        {
            if (aSide)
            {
                if (c.getAID().equals(neuron.getID()))
                {
                    if (c.checkASide(start))
                        result.add(c);
                }
            }
            else
            {
                if (c.getZID().equals(neuron.getID()))
                {
                    if (c.checkZSide(start))
                        result.add(c);
                }
            }
        }
        return result;
    }

    public void setInput(List<BigInteger> neurons) throws NeuronNotFound
    {
        if ((neurons != null) && (!neurons.isEmpty()))
        {
            for (Integer i = 0; i < neurons.size(); i++)
            {
                setInputNeuron(getNeuron(neurons.get(i)));
            }
        }
        inputNumber = inputNeurons.size();
    }

    public int removeNeuron(BigInteger id)
    {
        List<Connection> connections = this.getConnectionsForZNeuron(id);
        connections.addAll(this.getConnectionsForANeuron(id));
        if (connections.size() > 0)
        {
            for (Connection c : connections)
            {
                c.disconnect();
                this.removeConnection(c);
            }
        }
        return neuroPool.remove(id) != null ? 0 : 1;
    }

    public void removeConnection(Connection c)
    {
        c.disconnect();
        connPool.remove(c);
    }

    public void setInputNeuron(BigInteger id) throws NeuronNotFound
    {
        setInputNeuron(getNeuron(id));
    }

    public void setInputNeuron(Neuron n) throws NeuronNotFound
    {
        inputNeurons.add(n);
        n.setRole(NeuronConst.INPUT_ROLE);
        n.setOrder(inputNeurons.size() + 1);
        n.setInNumber(1);
    }

    public void connectNeuron(BigInteger id1, BigInteger id2, int fiber) throws NeuronNotFound
    {
        Neuron n1 = getNeuron(id1);
        Connection conn = null;
        if (!n1.connectedTo(id2))
        {
            conn = new Connection(this);
            connPool.add(conn);
        }
        else
        {
            for (Connection c : connPool)
            {
                if ((c.getAID().equals(id1)) && (c.getZID().equals(id2)))
                {
                    conn = c;
                    break;
                }
            }
        }
        if (conn != null)
            conn.connect(id1, id2, fiber);
    }

    public NeuroNet(Integer inNumber, Integer outNumber)
    {
        this.inputNumber = inNumber;
        this.outputNumber = outNumber;
        for (int i = 0; i < inputNumber; i++)
        {
            BigInteger id = ApplicationContext.getInstance().getNextId();
            neuroPool.put(id, new Neuron(id));
        }
        for (int i = 0; i < outputNumber; i++)
        {
            Neuron out = new Neuron(NeuronConst.LAST_NEURON_ID.add(BigInteger.valueOf(i)));
            neuroPool.put(out.getID(), out);
            try
            {
                setOutputNeuron(out.getID());
            } catch (NeuronNotFound e)
            {
                e.printStackTrace();
            }
        }
        randomize();
    }

    public void randomize()
    {
        for (BigInteger id : neuroPool.keySet())
        {
            Neuron neuron = neuroPool.get(id);
            neuron.randomize();
        }
    }

    public void setOutputNeuron(List<BigInteger> out) throws NeuronNotFound
    {
        for (BigInteger id : out)
        {
            setOutputNeuron(getNeuron(id), false);
        }
    }

    public void setOutputNeuron(BigInteger id) throws NeuronNotFound
    {
        setOutputNeuron(getNeuron(id), true);
    }

    public void setOutputNeuron(Neuron n, boolean addOutput) throws NeuronNotFound
    {
        n.setRole(NeuronConst.OUTPUT_ROLE);
        if (addOutput)
        {
            List<Integer> outputs = new ArrayList<Integer>();
            outputs.add(0);
            n.addOutputs(outputs.size());
        }
        outputNeurons.add(n);
        n.setOrder(outputNeurons.size() + 1);
        outputNumber = outputNeurons.size();
    }

    public NeuroNet(ParserXml parser) throws NeuronNotFound
    {
        List<Neuron> neurons = parser.getNeurons();
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
        if (connections != null && !neurons.isEmpty())
        {
            Iterator<Connection> it = connections.iterator();
            while (it.hasNext())
            {
                Connection curr = it.next();
                connPool.add(curr);
            }
        }
        setInput(parser.getNeuronsByKey("input_neurons"));
        setOutputNeuron(parser.getNeuronsByKey("output_neurons"));
    }

    public BigInteger addNeuron()
    {
        BigInteger id = ApplicationContext.getInstance().getNextId();
        neuroPool.put(id, new Neuron(id));
        return id;
    }

    public Neuron getNeuron(BigInteger id) throws NeuronNotFound
    {
        if (neuroPool.get(id) == null)
        {
            throw new NeuronNotFound(id);
        }
        return neuroPool.get(id);
    }

    public List<Double> getCurrentInput()
    {
        return inputProvider.getCurrentInput();
    }
    
    public void calc() throws NeuronNotFound
    {
        if (inputProvider == null)
            throw new CalculationException("InputDataProvider is null. Please, set InputDataProvider before any calculations");
        for (Iterator<Neuron> it = this.neuroPool.values().iterator(); it.hasNext();)
        {
            Neuron n = it.next();
            n.setState(NeuronConst.STATE_INIT);
        }
        for (Iterator<Neuron> it = this.inputNeurons.iterator(); it.hasNext();)
        {
            Neuron n = it.next();
            n.setInput(inputProvider.getNextInput());
        }
        if (calculator == null)
        {
            calculator = new DefaultCalculator();
        }
        calculator.calc(neuroPool);
    }

    public void setInputProvider(InputDataProvider inputProvider)
    {
        this.inputProvider = inputProvider;
    }

    public Set<Neuron> getPrevious(List<Neuron> layer)
    {
        Set<Neuron> result = new HashSet<Neuron>();
        for (Neuron n : layer)
        {
            List<Connection> connectionList = getConnectionsForZNeuron(n.getID());
            for (Connection connection : connectionList)
            {
                result.add(neuroPool.get(connection.getAID()));
            }
        }
        return result;
    }

    @Override
    public String toString()
    {
        StringBuffer out = new StringBuffer();
        Element net = getXml();
        out.append(net.toString());
        if (!net.getChildren().isEmpty())
        {
            out.append(net.getChildren());
        }
        return out.toString();
    }

}
