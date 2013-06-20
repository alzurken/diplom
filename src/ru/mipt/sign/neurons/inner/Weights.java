package ru.mipt.sign.neurons.inner;

import java.util.*;

import ru.mipt.sign.core.JSONable;
import ru.mipt.sign.core.Key;
import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.util.JSONHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Weights implements JSONable, NeuronConst
{
    private double[][] weights;
    // private Map<Key, Double> dweights;
    protected int inNumber;
    protected int outNumber;
    private Neuron parent;
    private Random random = new Random(System.currentTimeMillis());
    private double alpha = 0.01;

    public void setParent(Neuron neuron)
    {
        this.parent = neuron;
    }

    {
        // weights = new HashMap<Key, Double>();
        // dweights = new HashMap<Key, Double>();
    }

    public Weights(JsonObject json)
    {
        inNumber = json.get("inNumber").getAsInt();
        outNumber = json.get("outNumber").getAsInt();
        JsonArray array = json.get("weights").getAsJsonArray();
        for (JsonElement elem : array)
        {
            JsonObject object = (JsonObject) elem;
            Key key = new Key(object.get("key").getAsJsonObject());
            Double value = object.get("value").getAsDouble();
            weights[key.getKey1()][key.getKey2()] = value;
        }
    }

    public Weights(Integer inNumber, Integer outNumber)
    {
        this.inNumber = inNumber;
        this.outNumber = outNumber;
        weights = new double[inNumber][outNumber];
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                weights[i][j] = initValue();
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
                result[i][j] = weights[i][j];
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
                result[i][j + 1] = weights[i][j];
            }
        }
        return result;
    }

    public void addInputs(Integer extraNumber)
    {
        Integer newIn = inNumber + extraNumber;
        double[][] temp = new double[newIn][outNumber];
        for (int i = 0; i < newIn; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                if (i < inNumber)
                {
                    temp[i][j] = weights[i][j];
                }
                else
                {
                    temp[i][j] = initValue();
                }
            }
        }
        weights = temp;
        inNumber += extraNumber;
    }

    public void addOutputs(Integer extraNumber)
    {
        Integer newOut = outNumber + extraNumber;
        double[][] temp = new double[inNumber][newOut];
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = 0; j < newOut; j++)
            {
                if (j < outNumber)
                {
                    temp[i][j] = weights[i][j];
                }
                else
                {
                    temp[i][j] = initValue();
                }
            }
        }
        weights = temp;
        outNumber += extraNumber;
    }

    public void changeWeight(int input, int output, double value)
    {

        // Key k = new Key(input, output);
        // Double previousW = dweights.get(k);
        // if (previousW == null)
        // Double previousW = 0d;
        // double deltaW = value + previousW * alpha;
        weights[input][output] += value;
        // dweights.put(k, deltaW);
    }

    public Double getWeight(int input, int output)
    {
        return weights[input][output];
    }

    public void randomize()
    {
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                weights[i][j] = initValue();
            }
        }
    }

    public void setWeight(int input, int output, double value)
    {
        weights[input][output] = value;
    }

    public void removeInputs(List<Integer> inputs)
    {
        int delta = inputs.size();
        int min = Collections.min(inputs);
        for (int j = 0; j < outNumber; j++)
        {
            for (int i = min + delta; i < inNumber; i++)
            {
                weights[i][j - delta] = weights[i][j];
                // dweights.put(new Key(i, j - delta), weights.get(new Key(i,
                // j)));
                // dweights.remove(new Key(i, j));
            }
        }
        inNumber -= delta;
    }

    public void removeOutputs(List<Integer> outputs)
    {
        int delta = outputs.size();
        int min = Collections.min(outputs);

        for (int i = 0; i < inNumber; i++)
        {
            for (int j = min + delta; j < outNumber; j++)
            {
                weights[i][j - delta] = weights[i][j];
                // dweights.put(new Key(i, j - delta), weights.get(new Key(i,
                // j)));
                // dweights.remove(new Key(i, j));
            }
        }
        outNumber -= delta;
    }

    public void setInNumber(int inNumber)
    {
        this.inNumber = inNumber;
        weights = new double[inNumber][outNumber];
        // dweights = new HashMap<Key, Double>();
        for (int i = 0; i < this.inNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                weights[i][j] = initValue();
            }
        }
    }

    public void setOutNumber(int outNumber)
    {
        this.outNumber = outNumber;
        weights = new double[inNumber][outNumber];
        // dweights = new HashMap<Key, Double>();
        for (int i = 0; i < this.inNumber; i++)
        {
            for (int j = 0; j < this.outNumber; j++)
            {
                weights[i][j] = initValue();
            }
        }
    }

    public double initValue()
    {
        return (2 * random.nextDouble() - 1);
    }

    public int getInNumber()
    {
        return inNumber;
    }

    public int getOutNumber()
    {
        return outNumber;
    }

    @Override
    public JsonObject getJSON()
    {
        JsonObject result = new JsonObject();
        result.addProperty("inNumber", inNumber);
        result.addProperty("outNumber", outNumber);
        result.add("weights", JSONHelper.getJSONArray(weights, inNumber, outNumber));
        return result;
    }
}
