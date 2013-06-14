package ru.mipt.sign.neurons.data.impl;

import java.util.ArrayList;
import java.util.List;

import ru.mipt.sign.neurons.data.InputDataProvider;

public class InputDataProviderByData implements InputDataProvider
{
    private List<Double> input;
    private int inNumber;
    private int currentIn;

    public InputDataProviderByData(List<Double> input, int inNumber)
    {
        this.inNumber = inNumber;
        this.input = input;
        currentIn = 0;
    }

    @Override
    public List<Double> getNextInput()
    {
        List<Double> result = new ArrayList<Double>();
        for (int i = currentIn; i < currentIn + inNumber; i++)
        {
            result.add(input.get(i));
        }
        currentIn += inNumber;
        return result;
    }

}
