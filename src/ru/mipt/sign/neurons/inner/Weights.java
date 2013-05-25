package ru.mipt.sign.neurons.inner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Weights
{
    private Map<Integer, Map<Integer, Double>> weights;
    private Integer inputNumber;
    private Integer outputNumber;

    public Weights(Integer inputNumber, Integer outputNumber)
    {
        this.inputNumber = inputNumber;
        this.outputNumber = outputNumber;
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

    @SuppressWarnings("unchecked")
    public Double[][] getWeightsForCalc()
    {
        Double[][] result = new Double[inputNumber][outputNumber];
        for (int i = 0; i < inputNumber; i++)
        {
            for (int j = 0; j < outputNumber; j++)
            {
                result[i][j] = weights.get(i).get(j);
            }
        }
        return result;
    }

    public void addInputs(Integer extraNumber)
    {
        for (Integer i = inputNumber; i < inputNumber + extraNumber; i++)
        {
            weights.put(i, new HashMap<Integer, Double>());
            for (Integer j = 0; j < outputNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
        inputNumber += extraNumber;
    }

    public void addOutputs(Integer extraNumber)
    {
        for (Integer i = 0; i < inputNumber; i++)
        {
            for (Integer j = outputNumber; j < outputNumber + extraNumber; j++)
            {
                weights.get(i).put(j, initValue());
            }
        }
        outputNumber += extraNumber;
    }

    public void changeWeight(Integer input, Integer output, Double value)
    {
        weights.get(input).put(output, weights.get(input).get(output) + value);
    }

    public Double getWeight(Integer input, Integer output)
    {
        return weights.get(input).get(output);
    }

    public void randomize()
    {
        for (Integer i : weights.keySet())
        {
            for (Integer j : weights.get(i).keySet())
            {
                weights.get(i).put(j, initValue());
            }
        }
    }

    public void setWeight(Integer input, Integer output, Double value)
    {
        weights.get(input).put(output, value);
    }

    public void removeInputs(List<Integer> inputs)
    {
        Integer delta = inputs.size();
        Integer min = Collections.min(inputs);
        for (Integer i = min; i < min + delta; i++)
        {
            weights.remove(i);
        }
        for (Integer i = min + delta; i < inputNumber; i++)
        {
            weights.put(i - delta, weights.get(i));
            weights.remove(i);
        }
    }

    public void removeOutputs(List<Integer> outputs)
    {
        Integer delta = outputs.size();
        Integer min = Collections.min(outputs);

        for (Integer i = 0; i < inputNumber; i++)
        {
            for (Integer j = min; j < min + delta; j++)
            {
                weights.get(i).remove(j);
            }
            for (Integer j = min + delta; j < outputNumber; j++)
            {
                weights.get(i).put(j - delta, weights.get(i).get(j));
                weights.get(i).remove(j);
            }
        }
    }

    public void setInNumber(Integer inNumber)
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

    public abstract Double initValue();

}
