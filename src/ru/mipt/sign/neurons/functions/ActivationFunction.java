package ru.mipt.sign.neurons.functions;

public interface ActivationFunction
{
    public double getValue(double x);

    public double getInverseValue(double y) ;
    
    public double getDerivative(double x);
}
