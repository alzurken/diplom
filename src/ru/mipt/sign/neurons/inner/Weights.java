package ru.mipt.sign.neurons.inner;

import java.util.*;

import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.neurons.NeuronConst;

public class Weights
{
    private Map<Integer, Map<Integer, Double>> weights;
    private Map<Integer, Map<Integer, Double>> dweights;
    private int inNumber;
    private int outNumber;
    private Neuron parent;
    private Random random = new Random(System.currentTimeMillis());
    private double alpha = 0.001;

    public void setParent(Neuron neuron)
    {
        this.parent = neuron;
    }

    public Weights(Integer inNumber, Integer outNumber)
    {
        this.inNumber = inNumber;
        this.outNumber = outNumber;
        weights = new HashMap<Integer, Map<Integer, Double>>();
        dweights = new HashMap<Integer, Map<Integer, Double>>();

        for (int i = 0; i < inNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            dweights.put(i, new HashMap<Integer, Double>());
            for (int j = 0; j < outNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
    }

    public double[][] getWeightsForCalc()
    {
        double[][] result = new double[inNumber][outNumber];
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                result[i][j] = weights.get(i).get(j);
            }
        }
        return result;
    }

    public Double[][] getWeightsForShow()
    {
        Double[][] result = new Double[inNumber][outNumber + 1];
        for (int i = 0; i < inNumber; i++)
        {
            result[i][0] = Double.valueOf(i + 1);
            for (int j = 0; j < outNumber; j++)
            {
                result[i][j + 1] = weights.get(i).get(j);
            }
        }
        return result;
    }

    public void addInputs(Integer extraNumber)
    {
        for (int i = inNumber; i < inNumber + extraNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            dweights.put(i, new HashMap<Integer, Double>());
            for (int j = 0; j < outNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
        inNumber += extraNumber;
    }

    public void addOutputs(Integer extraNumber)
    {
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = outNumber; j < outNumber + extraNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
        outNumber += extraNumber;
    }

    public void changeWeight(int input, int output, double value)
    {
        if (parent.getRole() != NeuronConst.INPUT_ROLE)
        {
            Double previousW = dweights.get(input).get(output);
            if (previousW == null)
                previousW = 0d;
            double deltaW = value + previousW * alpha;
            weights.get(input).put(output, weights.get(input).get(output) + deltaW);
            dweights.get(input).put(output, deltaW);
        }
    }

    public Double getWeight(int input, int output)
    {
        return weights.get(input).get(output);
    }

    public void randomize()
    {
        for (int i : weights.keySet())
        {
            for (int j : weights.get(i).keySet())
            {
                weights.get(i).put(j, initValue());
            }
        }
    }

    public void setWeight(int input, int output, double value)
    {
        weights.get(input).put(output, value);
    }

    public void removeInputs(List<Integer> inputs)
    {
        int delta = inputs.size();
        int min = Collections.min(inputs);
        for (int i = min; i < min + delta; i++)
        {
            weights.remove(i);
            dweights.remove(i);
        }
        for (int i = min + delta; i < inNumber; i++)
        {
            weights.put(i - delta, weights.get(i));
            weights.remove(i);
            dweights.put(i - delta, weights.get(i));
            dweights.remove(i);
        }
        inNumber -= delta;
    }

    public void removeOutputs(List<Integer> outputs)
    {
        int delta = outputs.size();
        int min = Collections.min(outputs);

        for (int i = 0; i < inNumber; i++)
        {
            for (int j = min; j < min + delta; j++)
            {
                weights.get(i).remove(j);
                dweights.get(i).remove(j);
            }
            for (int j = min + delta; j < outNumber; j++)
            {
                weights.get(i).put(j - delta, weights.get(i).get(j));
                weights.get(i).remove(j);
                dweights.get(i).put(j - delta, weights.get(i).get(j));
                dweights.get(i).remove(j);
            }
        }
        outNumber -= delta;
    }

    public void setInNumber(int inNumber)
    {
        this.inNumber = inNumber;
        weights = new HashMap<Integer, Map<Integer, Double>>();
        dweights = new HashMap<Integer, Map<Integer, Double>>();
        for (int i = 0; i < this.inNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            dweights.put(i, new HashMap<Integer, Double>());
            for (int j = 0; j < outNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
    }

    public void setOutNumber(int outNumber)
    {
        this.outNumber = outNumber;
        weights = new HashMap<Integer, Map<Integer, Double>>();
        dweights = new HashMap<Integer, Map<Integer, Double>>();
        for (int i = 0; i < this.inNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            dweights.put(i, new HashMap<Integer, Double>());
            for (int j = 0; j < this.outNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
    }

    public Double initValue()
    {
        return 2 * random.nextDouble() - 1;
    }

    public int getInNumber()
    {
        return inNumber;
    }

    public int getOutNumber()
    {
        return outNumber;
    }

}
