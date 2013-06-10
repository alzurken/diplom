package ru.mipt.sign.data.impl;

import java.util.List;

import ru.mipt.sign.data.InputDataProvider;

public abstract class InputDataProviderByNeuron implements InputDataProvider
{
    private List<Double> current;
    
    @Override
    public List<Double> getNextInput()
    {
        current = nextValue();
        return current;
    }

    public List<Double> getCurrentInput()
    {
        return current;
    }

    public abstract List<Double> nextValue();
}
