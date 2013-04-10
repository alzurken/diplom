package ru.mipt.sign.learn;

import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;

public class WeightAction extends LearningAction
{

	public WeightAction(NeuroNet nn, List rightValue)
	{
		super(nn, rightValue);
	}

	@Override
	public void perform() throws NeuronNotFound
	{
		// TODO Auto-generated method stub

	}

}
