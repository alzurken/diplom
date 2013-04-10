package ru.mipt.sign.neurons.functions;

public class Sigmoid implements ActivationFunction
{
    private Double amp = 5.0;

    @Override
    public Double getValue(Double x)
    {
        return x;
    }

    @Override
    public Double getInverseValue(Double x)
    {
        return x;
    }
}
