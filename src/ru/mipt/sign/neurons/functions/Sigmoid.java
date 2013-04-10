package ru.mipt.sign.neurons.functions;

public class Sigmoid implements ActivationFunction
{

	@Override
	public Double getValue(Double x)
	{
		return 3/(2+Math.expm1(x));
	}

}
