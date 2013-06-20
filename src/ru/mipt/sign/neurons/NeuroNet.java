package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.*;

import org.jdom.Element;

import ru.mipt.sign.core.JSONable;
import ru.mipt.sign.core.exceptions.CalculationException;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.data.InputDataProvider;
import ru.mipt.sign.neurons.factory.NeuronFactory;
import ru.mipt.sign.neurons.functions.UnitFunction;
import ru.mipt.sign.neurons.inner.UnitWeights;
import ru.mipt.sign.util.JSONHelper;
import ru.mipt.sign.util.Log;
import ru.mipt.sign.util.ParserXml;
import ru.mipt.sign.util.comparator.OrderComparator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NeuroNet implements JSONable, NeuronConst
{
    private BigInteger currentID;
    protected HashMap<BigInteger, Neuron> neuroPool;
    private List<Connection> connPool;
    protected TreeSet<Neuron> inputNeurons; // id, number
    protected int inputNumber;
    protected int outputNumber;
    protected TreeSet<Neuron> outputNeurons;
    protected InputDataProvider inputProvider;
    protected List<Double> currentInput;
    private Map<BigInteger, List<Connection>> cacheConnectionsForANeuron;
    private Map<BigInteger, List<Connection>> cacheConnectionsForZNeuron;

    {
        currentID = BigInteger.valueOf(0);
        neuroPool = new HashMap<BigInteger, Neuron>();
        connPool = new ArrayList<Connection>();
        inputNeurons = new TreeSet<Neuron>(new OrderComparator());
        outputNeurons = new TreeSet<Neuron>(new OrderComparator());
        currentInput = new ArrayList<Double>();
        cacheConnectionsForANeuron = new HashMap<BigInteger, List<Connection>>();
        cacheConnectionsForZNeuron = new HashMap<BigInteger, List<Connection>>();
    }
    
    private void resetCache()
    {
        cacheConnectionsForANeuron = new HashMap<BigInteger, List<Connection>>();
        cacheConnectionsForZNeuron = new HashMap<BigInteger, List<Connection>>();
    }

    public List<BigInteger> getInputNeurons()
    {
        List<BigInteger> result = new ArrayList<BigInteger>();
        for (Neuron n : inputNeurons)
        {
            result.add(n.getID());
        }
        return result;
    }

    public int getOutputNumber()
    {
        return outputNumber;
    }

    public List<Neuron> getNeurons()
    {
        return new ArrayList<Neuron>(neuroPool.values());
    }
    
    public List<Neuron> getNeurons(List<BigInteger> ids) throws NeuronNotFound
    {
        List<Neuron> result = new ArrayList<Neuron>();
        for (BigInteger id : ids)
        {
                result.add(getNeuron(id));
        }
        return result;
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
        if (cacheConnectionsForZNeuron.get(id) != null)
        {
            result = cacheConnectionsForZNeuron.get(id);
        }
        else
        {
            for (Connection c : connPool)
            {
                if (c.getZID().equals(id))
                    result.add(c);
            }
            cacheConnectionsForZNeuron.put(id, result);
        }
        return result;
    }

    public List<Connection> getConnectionsForANeuron(BigInteger id)
    {
        List<Connection> result = new ArrayList<Connection>();
        if (cacheConnectionsForANeuron.get(id) != null)
        {
            result = cacheConnectionsForANeuron.get(id);
        }
        else
        {
            for (Connection c : connPool)
            {
                if (c.getAID().equals(id))
                    result.add(c);
            }
            cacheConnectionsForANeuron.put(id, result);
        }
        return result;
    }

    @Deprecated
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
            Double value = out.get(0);
            result.add(value == null ? 0d : value);
        }
        return result;
    }

    public List<Neuron> getOutputNeurons()
    {
        List<Neuron> result = new ArrayList<Neuron>();
        for (int i = 0; i < outputNumber; i++)
        {
            result.add(neuroPool.get(LAST_NEURON_ID.add(BigInteger.valueOf(i))));
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
        resetCache();
        return neuroPool.remove(id) != null ? 0 : 1;
    }

    public void removeConnection(Connection c)
    {
        resetCache();
        c.disconnect();
        connPool.remove(c);
    }

    public void setInputNeuron(BigInteger id) throws NeuronNotFound
    {
        setInputNeuron(getNeuron(id));
    }

    public void setInputNeuron(Neuron n) throws NeuronNotFound
    {
        n.setOrder(inputNeurons.size());
        inputNeurons.add(n);
        n.setRole(INPUT_ROLE);
        n.setFunction(new UnitFunction());
        n.setInNumber(1);
        n.setWeights(new UnitWeights(1, n.getOutNumber()));
    }

    public void connectNeuron(BigInteger id1, BigInteger id2, int fiber) throws NeuronNotFound
    {
        resetCache();
        // Neuron n1 = getNeuron(id1);
        Connection conn = null;
        // if (!n1.connectedTo(id2))
        // {
        conn = new Connection(this);
        connPool.add(conn);
        // }
        // else
        // {
        // for (Connection c : connPool)
        // {
        // if ((c.getAID().equals(id1)) && (c.getZID().equals(id2)))
        // {
        // conn = c;
        // break;
        // }
        // }
        // }
        // if (conn != null)
        conn.connect(id1, id2, fiber);
    }

    protected NeuroNet()
    {

    }

    public NeuroNet(JsonObject json)
    {
        inputNumber = json.get("inputNumber").getAsInt();
        outputNumber = json.get("outputNumber").getAsInt();
        currentID = json.get("currentID").getAsBigInteger();
        JsonArray neurons = json.get("neuroPool").getAsJsonArray();
        for (JsonElement elem : neurons)
        {
            JsonObject obj = (JsonObject) elem;
            Neuron n = new Neuron(obj);
            neuroPool.put(n.getID(), n);
            if (n.getRole() == INPUT_ROLE)
                inputNeurons.add(n);
            if (n.getRole() == OUTPUT_ROLE)
                outputNeurons.add(n);
        }
        JsonArray connections = json.get("connPool").getAsJsonArray();
        for (JsonElement elem : connections)
        {
            JsonObject obj = (JsonObject) elem;
            Connection c = new Connection(obj, this);
            connPool.add(c);
        }
    }

    public NeuroNet(Integer inNumber, Integer outNumber)
    {
        this.inputNumber = inNumber;
        this.outputNumber = outNumber;
        for (int i = 0; i < inputNumber; i++)
        {
            BigInteger id = getNextId();
            Neuron inputNeuron = new Neuron(id);
            neuroPool.put(id, inputNeuron);
            try
            {
                setInputNeuron(inputNeuron);
            } catch (NeuronNotFound e)
            {
                System.out.println("There is no neuron with id = " + e.getId());
            }
        }
        for (int i = 0; i < outputNumber; i++)
        {
            Neuron out = new Neuron(LAST_NEURON_ID.add(BigInteger.valueOf(i)));
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
        n.setRole(OUTPUT_ROLE);
        if (addOutput)
        {
            List<Integer> outputs = new ArrayList<Integer>();
            outputs.add(0);
            n.addOutputs(outputs.size());
        }
        n.setOrder(outputNeurons.size());
        outputNeurons.add(n);
        outputNumber = outputNeurons.size();
    }

    @Deprecated
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
        BigInteger id = getNextId();
        neuroPool.put(id, new Neuron(id));
        return id;
    }

    protected BigInteger getNextId()
    {
        currentID = currentID.add(BigInteger.valueOf(1));
        return currentID;
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
        return currentInput;
    }

    public void calc() throws NeuronNotFound
    {
        if (inputProvider == null)
            throw new CalculationException(
                    "InputDataProvider is null. Please, set InputDataProvider before any calculations");
        for (Iterator<Neuron> it = this.neuroPool.values().iterator(); it.hasNext();)
        {
            Neuron n = it.next();
            n.setState(STATE_INIT);
        }
        currentInput = new ArrayList<Double>();
        for (Iterator<Neuron> it = this.inputNeurons.iterator(); it.hasNext();)
        {
            Neuron n = it.next();
            List<Double> temp = inputProvider.getNextInput();
            currentInput.addAll(temp);
            Log.debug("temp : " + temp);
            Log.debug("currentInput: " + currentInput);
            n.setInput(temp);
        }
        HashMap<BigInteger, Neuron> cache = new HashMap<BigInteger, Neuron>();
        for (BigInteger k : neuroPool.keySet())
        {
            Neuron so = neuroPool.get(k);
            Neuron n = (Neuron) so;
            if (n.getState() == STATE_READY)
            {
                n.calc();
                n.emit();
            }
            if (n.getState() == STATE_INIT)
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
                if (n.getState() == STATE_READY)
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

    @Override
    public JsonObject getJSON()
    {
        JsonObject result = new JsonObject();
        result.addProperty("inputNumber", inputNumber);
        result.addProperty("outputNumber", outputNumber);
        result.addProperty("currentID", currentID);
        result.add("neuroPool", JSONHelper.getJSONList(neuroPool));
        result.add("connPool", JSONHelper.getJSONList(connPool));
        return result;
    }

}
