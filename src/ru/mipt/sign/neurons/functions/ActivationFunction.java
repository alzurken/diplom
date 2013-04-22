package ru.mipt.sign.neurons.functions;

public interface ActivationFunction
{
    public Double getValue(Double x);

    public Double getInverseValue(Double y) ;
    
    public Double getDerivative(Double x);
}
