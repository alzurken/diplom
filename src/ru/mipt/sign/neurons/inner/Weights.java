package ru.mipt.sign.neurons.inner;

import java.util.*;

public class Weights
{
    private Map<Integer, Map<Integer, Double>> weights;
    private int inputNumber;
    private int outputNumber;
    private Random random;

    public Weights(Integer inputNumber, Integer outputNumber)
    {
        this.inputNumber = inputNumber;
        this.outputNumber = outputNumber;
        weights = new HashMap<Integer, Map<Integer, Double>>();
        random = new Random(System.currentTimeMillis());
        for (int i = 0; i < inputNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            for (int j = 0; j < outputNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
    }

    public double[][] getWeightsForCalc()
    {
        double[][] result = new double[inputNumber][outputNumber];
        for (int i = 0; i < inputNumber; i++)
        {
            for (int j = 0; j < outputNumber; j++)
            {
                result[i][j] = weights.get(i).get(j);
            }
        }
        return result;
    }
    
    public Double[][] getWeightsForShow()
    {
        Double[][] result = new Double[inputNumber][outputNumber + 1];
        for (int i = 0; i < inputNumber; i++)
        {
            result[i][0] = Double.valueOf(i);
            for (int j = 0; j < outputNumber; j++)
            {
                result[i][j+1] = weights.get(i).get(j);
            }
        }
        return result;
    }

    public void addInputs(Integer extraNumber)
    {
        for (int i = inputNumber; i < inputNumber + extraNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            for (int j = 0; j < outputNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
        inputNumber += extraNumber;
    }

    public void addOutputs(Integer extraNumber)
    {
        for (int i = 0; i < inputNumber; i++)
        {
            for (int j = outputNumber; j < outputNumber + extraNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
        outputNumber += extraNumber;
    }

    public void changeWeight(int input, int output, double value)
    {
        weights.get(input).put(output, weights.get(input).get(output) + value);
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
        }
        for (int i = min + delta; i < inputNumber; i++)
        {
            weights.put(i - delta, weights.get(i));
            weights.remove(i);
        }
        inputNumber -= delta;
    }

    public void removeOutputs(List<Integer> outputs)
    {
        int delta = outputs.size();
        int min = Collections.min(outputs);

        for (int i = 0; i < inputNumber; i++)
        {
            for (int j = min; j < min + delta; j++)
            {
                weights.get(i).remove(j);
            }
            for (int j = min + delta; j < outputNumber; j++)
            {
                weights.get(i).put(j - delta, weights.get(i).get(j));
                weights.get(i).remove(j);
            }
        }
        outputNumber -= delta;
    }

    public void setInNumber(int inNumber)
    {
        this.inputNumber = inNumber;
        weights = new HashMap<Integer, Map<Integer, Double>>();
        for (int i = 0; i < inputNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            for (int j = 0; j < outputNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
    }

    public Double initValue()
    {
        return random.nextDouble();
    }

}
