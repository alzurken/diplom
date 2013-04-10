package ru.mipt.sign.learn;

import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;

public abstract class LearningAction
{
	protected NeuroNet nn;
	protected List rightValue;

	public LearningAction(NeuroNet nn, List rightValue)
	{
		this.nn = nn;
		this.rightValue = rightValue;
	}

	public abstract void perform() throws NeuronNotFound;
}
