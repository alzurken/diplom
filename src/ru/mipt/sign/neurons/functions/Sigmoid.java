package ru.mipt.sign.neurons.functions;

import ru.mipt.sign.core.exceptions.CalculationException;

public class Sigmoid implements ActivationFunction
{
    private double alpha = 0.1;

    @Override
    public double getValue(double x)
    {
        return x/(Math.abs(x) + alpha);//x/(|x|+a)
    }

    @Override
    public double getInverseValue(double y)
    {
    	if (Math.abs(y) >= 1)
    	{
    		throw new CalculationException();
    	}
    	if (y > 0)
    	{
    		return alpha*y/(1-y);
    	}
    	else
    	{
    		return alpha*y/(1+y);
    	}
    }

	@Override
	public double getDerivative(double x) {
		return alpha/(Math.abs(x) + alpha)/(Math.abs(x) + alpha); //a/(|x|+a)^2
	    
	}
}
