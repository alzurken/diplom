package ru.mipt.sign.neurons.functions;

public class UnitFunction implements ActivationFunction
{

    @Override
    public double getValue(double x)
    {
        return x;
    }

    @Override
    public double getInverseValue(double y)
    {
        return y;
    }

    @Override
    public double getDerivative(double x)
    {
        return 1;
    }

}
