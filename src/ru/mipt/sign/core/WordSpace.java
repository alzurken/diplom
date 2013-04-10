package ru.mipt.sign.core;

import java.util.HashMap;
import java.util.Vector;

public class WordSpace
{
    private HashMap<Vector<Double>, Double> space = new HashMap<Vector<Double>, Double>();

    public void setAmplitude(Vector<Double> vector, Double amplitude)
    {
        space.put(vector, amplitude);
    }

    public Double getAmplitude(Vector<Double> vector)
    {
        return null;
    }
}
