package ru.mipt.sign.learn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ru.mipt.sign.connect.Connection;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;

public class ConnectAction extends LearningAction
{

    public ConnectAction(NeuroNet nn, List result, List rightValue)
    {
        super(nn, result, rightValue);
    }

    @Override
    public void perform() throws NeuronNotFound
    {
        List<BigInteger> neurons = nn.getNeurons();
        neurons.remove(new BigInteger("1"));
        Double rnd = Math.floor(Math.random() * neurons.size());
        Integer index = rnd.intValue();// random index
        BigInteger id2 = neurons.get(index);
        neurons.remove(neurons.get(index));

        neurons.clear();
        List<Connection> conns = nn.getConnectionsForZNeuron(id2);
        Set<BigInteger> neuroTemp = new HashSet<BigInteger>();
        while (!conns.isEmpty())
        {
            Iterator<Connection> it = conns.iterator();
            List<Connection> temp = new ArrayList<Connection>();
            while (it.hasNext())
            {
                BigInteger curr = it.next().getAID();
                neuroTemp.add(curr);
                temp.addAll(nn.getConnectionsForZNeuron(curr));
            }
            conns = temp;
        }

        neurons = new ArrayList<BigInteger>(neuroTemp);
        rnd = Math.floor(Math.random() * neurons.size());
        index = rnd.intValue();// random index
        BigInteger id1 = neurons.get(index);
        nn.connectNeuron(id1, id2, NeuronConst.DEFAULT_FIBER_NUMBER);
        this.message = "Connected from " + id1 + " to " + id2;
    }

}
