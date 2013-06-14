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
    private Map<Key, Double> weights;
    private Map<Key, Double> dweights;
    private int inNumber;
    private int outNumber;
    private Neuron parent;
    private Random random = new Random(System.currentTimeMillis());
    private double alpha = 0.01;

    public void setParent(Neuron neuron)
    {
        this.parent = neuron;
    }
    
    {
        weights = new HashMap<Key, Double>();
        dweights = new HashMap<Key, Double>();
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
            weights.put(key, value);
        }
    }

    public Weights(Integer inNumber, Integer outNumber)
    {
        this.inNumber = inNumber;
        this.outNumber = outNumber;
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                weights.put(key(i, j), initValue());
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
                result[i][j] = weights.get(key(i, j));
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
                result[i][j + 1] = weights.get(key(i, j));
            }
        }
        return result;
    }

    public void addInputs(Integer extraNumber)
    {
        for (int i = inNumber; i < inNumber + extraNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                weights.put(key(i, j), initValue());
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
                weights.put(key(i, j), initValue());
            }
        }
        outNumber += extraNumber;
    }

    public void changeWeight(int input, int output, double value)
    {
        if (parent.getRole() != INPUT_ROLE)
        {
            Key k = key(input, output);
            Double previousW = dweights.get(k);
            if (previousW == null)
                previousW = 0d;
            double deltaW = value + previousW * alpha;
            weights.put(k, weights.get(k) + deltaW);
            dweights.put(k, deltaW);
        }
    }

    public Double getWeight(int input, int output)
    {
        return weights.get(key(input, output));
    }

    public void randomize()
    {
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = 0; j < inNumber; j++)
            {
                weights.put(key(i, j), initValue());
            }
        }
    }

    public void setWeight(int input, int output, double value)
    {
        weights.put(key(input, output), value);
    }

    public void removeInputs(List<Integer> inputs)
    {
        int delta = inputs.size();
        int min = Collections.min(inputs);
        for (int j = 0; j < outNumber; j++)
        {
            for (int i = min; i < min + delta; i++)
            {
                weights.remove(key(i, j));
                dweights.remove(key(i, j));
            }
            for (int i = min + delta; i < inNumber; i++)
            {
                weights.put(key(i, j - delta), weights.get(key(i, j)));
                weights.remove(key(i, j));
                dweights.put(key(i, j - delta), weights.get(key(i, j)));
                dweights.remove(key(i, j));
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
            for (int j = min; j < min + delta; j++)
            {
                weights.remove(key(i, j));
                dweights.remove(key(i, j));
            }
            for (int j = min + delta; j < outNumber; j++)
            {
                weights.put(key(i, j - delta), weights.get(key(i, j)));
                weights.remove(key(i, j));
                dweights.put(key(i, j - delta), weights.get(key(i, j)));
                dweights.remove(key(i, j));
            }
        }
        outNumber -= delta;
    }

    public void setInNumber(int inNumber)
    {
        this.inNumber = inNumber;
        weights = new HashMap<Key, Double>();
        dweights = new HashMap<Key, Double>();
        for (int i = 0; i < this.inNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                weights.put(key(i, j), initValue());
            }
        }
    }

    public void setOutNumber(int outNumber)
    {
        this.outNumber = outNumber;
        weights = new HashMap<Key, Double>();
        dweights = new HashMap<Key, Double>();
        for (int i = 0; i < this.inNumber; i++)
        {
            for (int j = 0; j < this.outNumber; j++)
            {
                weights.put(key(i, j), initValue());
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

    @Override
    public JsonObject getJSON()
    {
        JsonObject result = new JsonObject();
        result.addProperty("inNumber", inNumber);
        result.addProperty("outNumber", outNumber);
        result.add("weights", JSONHelper.getJSONArray(weights));
        return result;
    }

    private Key key(Integer i, Integer j)
    {
        return new Key(i, j);
    }
}
