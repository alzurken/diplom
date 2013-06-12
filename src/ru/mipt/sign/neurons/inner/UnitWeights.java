package ru.mipt.sign.neurons.inner;

public class UnitWeights extends Weights
{
    public UnitWeights(Integer inNumber, Integer outNumber)
    {
        super(inNumber, outNumber);
    }

    public Double initValue()
    {
        return 1d;
    }
}
