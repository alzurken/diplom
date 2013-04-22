package ru.mipt.sign.neurons.functions;

import ru.mipt.sign.core.exceptions.CalculationException;

public class Sigmoid implements ActivationFunction
{
    private Double alpha = 0.1;

    @Override
    public Double getValue(Double x)
    {
        return x/(Math.abs(x) + alpha);//x/(|x|+a)
    }

    @Override
    public Double getInverseValue(Double y)
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
	public Double getDerivative(Double x) {
		return alpha/(Math.abs(x) + alpha)/(Math.abs(x) + alpha); //a/(|x|+a)^2
	    
	}
}
