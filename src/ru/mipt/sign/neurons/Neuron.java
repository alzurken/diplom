package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.*;

import org.jdom.DataConversionException;
import org.jdom.Element;

import ru.mipt.sign.connect.Connection;
import ru.mipt.sign.core.SObject;
import ru.mipt.sign.neurons.functions.ActivationFunction;
import ru.mipt.sign.neurons.functions.LogisticFunction;
import ru.mipt.sign.neurons.functions.Sigmoid;

public class Neuron extends SObject
{
    private List<Connection> connections;
    private HashMap<Integer, HashMap<Integer, Double>> weights;
    private HashMap<Integer, Double> input;
    private HashMap<Integer, Double> output;
    private Integer inNumber = 0;
    private Integer currentIn = 0;
    private Integer outNumber = 0;
    private Integer state = NeuronConst.STATE_INIT;
    private ActivationFunction function;
    private Double sum = 0d;
    private HashMap<Integer, Double> delta;

    public Double getDelta(Integer index)
    {
        return delta.get(index);
    }

    public void setDelta(Integer index, Double delta)
    {
        this.delta.put(index, delta);
    }

    public Double getWeight(Integer i, Integer j)
    {
        return weights.get(i).get(j);
    }

    public Double getSum()
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
        for (Integer i = 0; i < inputList.size(); i++)
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

    public Integer getInNumber()
    {
        return inNumber;
    }

    public void randomize()
    {
        for (Integer i : weights.keySet())
        {
            for (Integer j : weights.get(i).keySet())
            {
                Double random = (2 * Math.random() - 1);
                weights.get(i).put(j, random); // [-1,1]
            }
        }
    }

    public Neuron(Element elem) {
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
                HashMap<Integer, Double> buf = new HashMap<Integer, Double>();
                Element layer = it.next();
                Integer index = layer.getAttribute("number").getIntValue();
                @SuppressWarnings("unchecked")
                List<Element> items = layer.getChildren();
                for (Iterator<Element> jt = items.iterator(); jt.hasNext();)
                {
                    Element item = jt.next();
                    Integer i = item.getAttribute("number").getIntValue();
                    Double value = item.getAttribute("value").getDoubleValue();
                    buf.put(i, value);
                }
                this.weights.put(index, buf);
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
        neuron.setAttribute("inNumber", inNumber.toString());
        neuron.setAttribute("currentIn", currentIn.toString());
        neuron.setAttribute("outNumber", outNumber.toString());

        Element weight = new Element("weight");
        for (Integer i = 0; i < this.weights.size(); i++)
        {
            HashMap<Integer, Double> buf = weights.get(i);
            Element layer = new Element("layer");
            layer.setAttribute("number", i.toString());
            for (Integer j = 0; j < buf.size(); j++)
            {
                Element item = new Element("item");
                item.setAttribute("number", j.toString());
                item.setAttribute("value", weights.get(i).get(j).toString());
                layer.addContent(item);
            }
            weight.addContent(layer);
        }
        neuron.addContent(weight);
        return neuron;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Neuron(BigInteger id) {
        super(id);

        init();
        for (Integer i = 0; i < inNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            for (Integer j = 0; j < outNumber; j++)
            {
                weights.get(i).put(j, Math.random());
            }
        }
    }

    private void init()
    {
        input = new HashMap<Integer, Double>(inNumber);
        output = new HashMap<Integer, Double>(outNumber);
        weights = new HashMap<Integer, HashMap<Integer, Double>>(inNumber * outNumber);
        connections = new ArrayList<Connection>();
        delta = new HashMap<Integer, Double>(outNumber);
        state = NeuronConst.STATE_INIT;
    }

    public List<Integer> getUnboundOutputs(Integer fiber)
    {
        List<Integer> result = new ArrayList<Integer>();
        for (Integer i = outNumber; i < outNumber + fiber; i++)
        {
            result.add(i);
        }
        return result;
    }

    public List<Integer> getUnboundInputs(Integer fiber)
    {
        List<Integer> result = new ArrayList<Integer>();
        for (Integer i = inNumber; i < inNumber + fiber; i++)
        {
            result.add(i);
        }
        return result;
    }

    public void calc()
    {
        Double sum = 0.0;
        if (function == null)
        {
            function = new LogisticFunction();
        }
        for (Integer i = 0; i < outNumber; i++)
        {
            sum = 0.0;
            for (Integer j = 0; j < inNumber; j++)
            {
                sum += weights.get(j).get(i) * input.get(j);
            }
            this.sum = sum;
            sum = function.getValue(sum);
            output.put(i, sum);
        }
        if (state != NeuronConst.STATE_OUTPUT)
            state = NeuronConst.STATE_CALCULATED;
    }

    public void changeWeight(Integer i, Integer j, Double changeValue)
    {
        weights.get(i).put(j, weights.get(i).get(j) + changeValue);
    }

    public void removeInputs(List<Integer> inputs)
    {
        Integer delta = inputs.size();
        Integer min = Collections.min(inputs);
        for (Integer i = min; i < min + delta; i++)
        {
            weights.remove(i);
        }
        for (Integer i = min + delta; i < inNumber; i++)
        {
            weights.put(i - delta, weights.get(i));
            weights.remove(i);
        }

        inNumber -= delta;
        input = new HashMap<Integer, Double>();
    }

    public void removeOutputs(List<Integer> outputs)
    {
        Integer delta = outputs.size();
        Integer min = Collections.min(outputs);

        for (Integer i = 0; i < inNumber; i++)
        {
            for (Integer j = min; j < min + delta; j++)
            {
                weights.get(i).remove(j);
            }
            for (Integer j = min + delta; j < outNumber; j++)
            {
                weights.get(i).put(j - delta, weights.get(i).get(j));
                weights.get(i).remove(j);
            }
        }
        outNumber -= delta;
        output = new HashMap<Integer, Double>();
    }

    public Integer getState()
    {
        return state;
    }

    // Set input with 'index' id
    public void addInputValue(Integer index, Double value)
    {
        input.put(index, value);
        currentIn++;
        if ((currentIn == inNumber) && (state != NeuronConst.STATE_OUTPUT))
        {
            state = NeuronConst.STATE_READY;
            currentIn = 0;
        }
    }

    public void setInNumber(Integer inNumber)
    {
        if (this.inNumber != inNumber)
            input = new HashMap<Integer, Double>(inNumber);
        {
            this.inNumber = inNumber;
            for (Integer i = 0; i < inNumber; i++)
            {
                weights.put(i, new HashMap<Integer, Double>(inNumber));
            }
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

    public void addOutputs(List<Integer> extra)
    {
        Integer extraNumber = extra.size();
        for (Integer i = 0; i < inNumber; i++)
        {
            for (Integer j = outNumber; j < outNumber + extraNumber; j++)
            {
                weights.get(i).put(j, Math.random());
            }
        }
        outNumber += extraNumber;
    }

    public Integer getOutNumber()
    {
        return outNumber;
    }

    public void addInputs(List<Integer> extra)
    {
        Integer extraNumber = extra.size();
        for (Integer i = inNumber; i < inNumber + extraNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            for (Integer j = 0; j < outNumber; j++)
            {
                weights.get(i).put(j, Math.random());
            }
        }
        inNumber += extraNumber;
    }

    public Double getDerivative()
    {
        return function.getDerivative(sum);
    }

}
