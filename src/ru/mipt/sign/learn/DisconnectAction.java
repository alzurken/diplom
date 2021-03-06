package ru.mipt.sign.learn;

import java.math.BigInteger;
import java.util.List;

import ru.mipt.sign.connect.Connection;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;

public class DisconnectAction extends LearningAction
{

	public DisconnectAction(NeuroNet nn, List rightValue)
	{
		super(nn, rightValue);
	}

	@Override
	public void perform() throws NeuronNotFound
	{
		List<BigInteger> neurons =  nn.getNeurons();
		
		Double rnd = Math.floor(Math.random() * neurons.size());
		Integer index =  rnd.intValue();// random index
		BigInteger id = neurons.get(index);
		List<Connection> connections = nn.getConnectionsForZNeuron(id);
		
		if (connections.size() > 1)
		{
			connections.get(0).disconnect();
			nn.removeConnection(connections.get(0).getID());
		}
	}

}
