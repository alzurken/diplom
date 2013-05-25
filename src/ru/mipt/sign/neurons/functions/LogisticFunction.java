package ru.mipt.sign.neurons.functions;

public class LogisticFunction implements ActivationFunction
{

    @Override
    public Double getValue(Double x)
    {
        return 1/(2 + Math.expm1(-x));
    }

    @Override
    public Double getInverseValue(Double y)
    {
        
        return null;
    }

    @Override
    public Double getDerivative(Double x)
    {
        Double result = getValue(x)*(1-getValue(x));
        return result;
    }

}
