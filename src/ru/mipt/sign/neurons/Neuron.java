package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.*;

import org.jdom.DataConversionException;
import org.jdom.Element;

import ru.mipt.sign.core.SObject;
import ru.mipt.sign.neurons.functions.ActivationFunction;
import ru.mipt.sign.neurons.functions.LogisticFunction;
import ru.mipt.sign.neurons.inner.Weights;

public class Neuron extends SObject
{
    private Set<Connection> connections;
    private Weights weights;
    private HashMap<Integer, Double> input;
    private HashMap<Integer, Double> output;
    private int inNumber = 0;
    private int currentIn = 0;
    private int outNumber = 0;
    private int state = NeuronConst.STATE_INIT;
    private int role = NeuronConst.NORMAL_ROLE;
    private ActivationFunction function;
    private double sum = 0d;
    private HashMap<Integer, Double> delta;

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role = role;
    }

    public double getDelta(int index)
    {
        return delta.get(index);
    }

    public void setDelta(int index, double delta)
    {
        this.delta.put(index, delta);
    }

    public double getWeight(int i, int j)
    {
        return weights.getWeight(i, j);
    }

    public double getSum()
    {
        return sum;
    }

    public ActivationFunction getFunction()
    {
        return function;
    }

    public HashMap<Integer, Double> getInput()
    {
        return input;
    }

    public HashMap<Integer, Double> getOutput()
    {
        return output;
    }

    public void setInput(List<Double> inputList)
    {
        for (int i = 0; i < inputList.size(); i++)
        {
            addInputValue(i, inputList.get(i));
        }
    }

    public void addConnection(Connection conn)
    {
        connections.add(conn);
    }

    public void removeConnection(Connection conn)
    {
        connections.remove(conn);
    }

    public int getInNumber()
    {
        return inNumber;
    }

    public void randomize()
    {
        weights.randomize();
    }

    public Neuron(Element elem)
    {
        super(elem);
        Element neuron = elem;
        try
        {
            this.inNumber = neuron.getAttribute("inNumber").getIntValue();
            this.currentIn = neuron.getAttribute("currentIn").getIntValue();
            this.outNumber = neuron.getAttribute("outNumber").getIntValue();
            init();

            Element weight = neuron.getChild("weight");
            @SuppressWarnings("unchecked")
            List<Element> layers = weight.getChildren();
            for (Iterator<Element> it = layers.iterator(); it.hasNext();)
            {
                Element layer = it.next();
                int input = layer.getAttribute("number").getIntValue();
                @SuppressWarnings("unchecked")
                List<Element> items = layer.getChildren();
                for (Iterator<Element> jt = items.iterator(); jt.hasNext();)
                {
                    Element item = jt.next();
                    int output = item.getAttribute("number").getIntValue();
                    double value = item.getAttribute("value").getDoubleValue();
                    weights.setWeight(input, output, value);
                }
            }
        } catch (DataConversionException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Element getXML()
    {
        Element neuron = new Element("neuron");
        neuron.setAttribute("id", id.toString());
        neuron.setAttribute("inNumber", Integer.toString(inNumber));
        neuron.setAttribute("currentIn", Integer.toString(currentIn));
        neuron.setAttribute("outNumber", Integer.toString(outNumber));

        Element weight = new Element("weight");
        for (int i = 0; i < inNumber; i++)
        {
            Element layer = new Element("layer");
            layer.setAttribute("number", Integer.toString(i));
            for (int j = 0; j < outNumber; j++)
            {
                Element item = new Element("item");
                item.setAttribute("number", Integer.toString(j));
                item.setAttribute("value", weights.getWeight(i, j).toString());
                layer.addContent(item);
            }
            weight.addContent(layer);
        }
        neuron.addContent(weight);
        return neuron;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public Neuron(BigInteger id)
    {
        super(id);

        init();
    }

    private void init()
    {
        input = new HashMap<Integer, Double>(inNumber);
        output = new HashMap<Integer, Double>(outNumber);
        weights = new Weights(inNumber, outNumber);
        connections = new HashSet<Connection>();
        delta = new HashMap<Integer, Double>(outNumber);
        state = NeuronConst.STATE_INIT;
    }

    public Weights getWeights()
    {
        return weights;
    }

    public List<Integer> getUnboundOutputs(Integer fiber)
    {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = outNumber; i < outNumber + fiber; i++)
        {
            result.add(i);
        }
        return result;
    }

    public List<Integer> getUnboundInputs(int fiber)
    {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = inNumber; i < inNumber + fiber; i++)
        {
            result.add(i);
        }
        return result;
    }

    public void calc()
    {
        double sum = 0.0;
        double[][] matrix = weights.getWeightsForCalc();
        if (function == null)
        {
            function = new LogisticFunction();
        }
        for (int i = 0; i < outNumber; i++)
        {
            sum = 0.0;
            for (int j = 0; j < inNumber; j++)
            {
                sum += matrix[j][i] * input.get(j);
            }
            this.sum = sum;
            sum = function.getValue(sum);
            output.put(i, sum);
        }
        state = NeuronConst.STATE_CALCULATED;
    }

    public void changeWeight(int i, int j, double changeValue)
    {
        weights.changeWeight(i, j, changeValue);
    }

    public void removeInputs(List<Integer> inputs)
    {
        int delta = inputs.size();
        weights.removeInputs(inputs);
        inNumber -= delta;
        input = new HashMap<Integer, Double>();
    }

    public void removeOutputs(List<Integer> outputs)
    {
        int delta = outputs.size();
        weights.removeOutputs(outputs);
        outNumber -= delta;
        output = new HashMap<Integer, Double>();
    }

    public int getState()
    {
        return state;
    }

    // Set input with 'index' id
    public void addInputValue(int index, double value)
    {
        input.put(index, value);
        currentIn++;
        if ((currentIn == inNumber))
        {
            state = NeuronConst.STATE_READY;
            currentIn = 0;
        }
    }

    public void setInNumber(int inNumber)
    {
        if (this.inNumber != inNumber)
        {
            input = new HashMap<Integer, Double>(inNumber);
            this.inNumber = inNumber;
            weights.setInNumber(inNumber);
        }
    }

    @Override
    public String getType()
    {
        return "neuron";
    }

    public void emit()
    {
        for (Connection conn : connections)
        {
            conn.emit();
        }
    }

    public void addOutputs(int extraNumber)
    {
        weights.addOutputs(extraNumber);
        outNumber += extraNumber;
    }

    public int getOutNumber()
    {
        return outNumber;
    }

    public void addInputs(int extraNumber)
    {
        weights.addInputs(extraNumber);
        inNumber += extraNumber;
    }

    public double getDerivative()
    {
        return function.getDerivative(sum);
    }

    public boolean connectedTo(BigInteger id)
    {
        for (Connection c : connections)
        {
            if (c.getZID().equals(id))
                return true;
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("Neuron:");
        sb.append(" ID = " + id);
        sb.append("]");
        return sb.toString();
    }

}
