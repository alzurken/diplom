package ru.mipt.sign.neurons.learn;

import java.math.BigInteger;
import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.Connection;
import ru.mipt.sign.neurons.NeuroNet;

public class DisconnectAction extends LearningAction
{

    public DisconnectAction(NeuroNet nn, List result, List rightValue) {
        super(nn, result, rightValue);
    }

    @Override
    public void perform() throws NeuronNotFound
    {
        List<BigInteger> neurons = nn.getNeuronIDs();

        Double rnd = Math.floor(Math.random() * neurons.size());
        Integer index = rnd.intValue();// random index
        BigInteger id = neurons.get(index);
        List<Connection> connections = nn.getConnectionsForZNeuron(id);
        if (connections.size() > 1)
        {
            nn.removeConnection(connections.get(0));
            this.message = "Disconnected from " + id + " to " + connections.get(0).getAID();
        }
        this.message = "Disconnect action. Nothing happend";
    }

}
