package ru.mipt.sign.learn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;

public class ConnectAction extends LearningAction
{

	public ConnectAction(NeuroNet nn, List rightValue)
	{
		super(nn, rightValue);
	}

	@Override
	public void perform() throws NeuronNotFound
	{
		List<BigInteger> neurons = nn.getNeurons();
		Double rnd = Math.floor(Math.random() * neurons.size());
		Integer index =  rnd.intValue();// random index
		BigInteger id1 = neurons.get(index);
		neurons.remove(neurons.get(index));
		rnd = Math.floor(Math.random() * neurons.size());
		index =  rnd.intValue();// random index
		BigInteger id2 = neurons.get(index);
		if(id1.compareTo(id2) < 0)
			nn.connectNeuron(id1, id2, NeuronConst.DEFAULT_FIBER_NUMBER);
		else
			nn.connectNeuron(id2, id1, NeuronConst.DEFAULT_FIBER_NUMBER);
	}

}
