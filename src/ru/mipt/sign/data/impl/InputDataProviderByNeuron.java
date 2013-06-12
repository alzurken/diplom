package ru.mipt.sign.data.impl;

import java.util.List;

import ru.mipt.sign.data.InputDataProvider;

public abstract class InputDataProviderByNeuron implements InputDataProvider
{
    @Override
    public List<Double> getNextInput()
    {
        return nextValue();
    }

    public abstract List<Double> nextValue();
}
