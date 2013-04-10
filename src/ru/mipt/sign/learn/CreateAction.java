package ru.mipt.sign.learn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;

public class CreateAction extends LearningAction
{

	public CreateAction(NeuroNet nn, List rightValue)
	{
		super(nn, rightValue);
	}

	@Override
	public void perform() throws NeuronNotFound
	{  
		BigInteger newNeuron = nn.addNeuron();
		List<BigInteger> neurons = nn.getNeurons();
		neurons.remove(nn.getNeuron(NeuronConst.LAST_NEURON_ID).getID());
		neurons.remove(nn.getNeuron(newNeuron).getID());
		Double rnd = Math.floor(Math.random() * neurons.size());
		Integer index =  rnd.intValue();// random index
		nn.connectNeuron(neurons.get(index), newNeuron, NeuronConst.DEFAULT_FIBER_NUMBER);
		nn.connectNeuron(newNeuron, NeuronConst.LAST_NEURON_ID, NeuronConst.DEFAULT_FIBER_NUMBER);
	}

}
