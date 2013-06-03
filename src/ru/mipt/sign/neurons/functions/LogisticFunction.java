package ru.mipt.sign.neurons.functions;

public class LogisticFunction implements ActivationFunction
{

    @Override
    public double getValue(double x)
    {
        return 1/(2 + Math.expm1(-x));
    }

    @Override
    public double getInverseValue(double y)
    {
        
        return 0.0;
    }

    @Override
    public double getDerivative(double x)
    {
        Double result = getValue(x)*(1-getValue(x));
        return result;
    }

}
