package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.*;

import org.jdom.Element;

import ru.mipt.sign.neurons.functions.ActivationFunction;
import ru.mipt.sign.neurons.functions.Sigmoid;
import ru.mipt.sign.neurons.inner.Weights;

public class Neuron implements Comparable<Neuron>
{
    private BigInteger id;
    private Element image;

    private Set<Connection> connections;
    private Weights weights;
    private HashMap<Integer, Double> input;
    private HashMap<Integer, Double> output;
    private int currentIn = 0;
    private int state = NeuronConst.STATE_INIT;
    private int role = NeuronConst.NORMAL_ROLE;
    private int order;
    private ActivationFunction function;
    private double sum = 0d;
    private HashMap<Integer, Double> delta;

    public void setOrder(int order)
    {
        this.order = order;
    }

    public int getOrder()
    {
        return order;
    }

    public Element getImage()
    {
        return image;
    }

    public void setImage(Element image)
    {
        this.image = image;
    }

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

    public void setFunction(ActivationFunction function)
    {
        this.function = function;
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
        return weights.getInNumber();
    }

    public void setOutNumber(int outNumber)
    {
        if (weights.getOutNumber() != outNumber)
        {
            output = new HashMap<Integer, Double>(outNumber);
            weights.setOutNumber(outNumber);
        }
    }

    public void randomize()
    {
        weights.randomize();
    }

    public Neuron(BigInteger id, Element elem, Weights weights)
    {
        this.id = id;
        this.image = elem;
        this.weights = weights;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public Neuron(BigInteger id)
    {
        this.id = id;
    }

    {
        input = new HashMap<Integer, Double>();
        output = new HashMap<Integer, Double>();
        weights = new Weights(0, 0);
        weights.setParent(this);
        connections = new HashSet<Connection>();
        delta = new HashMap<Integer, Double>();
        state = NeuronConst.STATE_INIT;
    }

    public void setWeights(Weights weights)
    {
        this.weights = weights;
        this.weights.setParent(this);
    }

    public Weights getWeights()
    {
        return weights;
    }

    public List<Integer> getUnboundOutputs(Integer fiber)
    {
        List<Integer> result = new ArrayList<Integer>();
        int outNumber = weights.getOutNumber();
        for (int i = outNumber; i < outNumber + fiber; i++)
        {
            result.add(i);
        }
        return result;
    }

    public List<Integer> getUnboundInputs(int fiber)
    {
        List<Integer> result = new ArrayList<Integer>();
        int inNumber = weights.getInNumber();
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
            function = new Sigmoid();
        }
        int outNumber = weights.getOutNumber();
        int inNumber = weights.getInNumber();
        for (int i = 0; i < outNumber; i++)
        {
            sum = 0.0;
            for (int j = 0; j < inNumber; j++)
            {
                sum += matrix[j][i] * input.get(j);
            }
            this.sum = sum;
//            Log.debug("Neuron " + id + " calc sum: " + sum);
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
        weights.removeInputs(inputs);
        input = new HashMap<Integer, Double>();
    }

    public void removeOutputs(List<Integer> outputs)
    {
        weights.removeOutputs(outputs);
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
        if ((currentIn == weights.getInNumber()))
        {
            state = NeuronConst.STATE_READY;
            currentIn = 0;
        }
    }

    public void setInNumber(int inNumber)
    {
        if (weights.getInNumber() != inNumber)
        {
            input = new HashMap<Integer, Double>(inNumber);
            weights.setInNumber(inNumber);
        }
    }

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
    }

    public int getOutNumber()
    {
        return weights.getOutNumber();
    }

    public void addInputs(int extraNumber)
    {
        weights.addInputs(extraNumber);
    }

    public double getDerivative()
    {
//        Log.debug("Sum: " + sum + " Derivative: " + function.getDerivative(sum));
        return function.getDerivative(function.getValue(sum));
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

    public BigInteger getID()
    {
        return id;
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

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Neuron other = (Neuron) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Neuron neuron)
    {
        return -id.compareTo(neuron.getID());
    }
}
