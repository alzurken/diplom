package ru.mipt.sign.neurons.learn;

import java.math.BigInteger;
import java.util.*;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.Connection;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.neurons.NeuronConst;

public class CreateAction extends LearningAction
{

    public CreateAction(NeuroNet nn, List result, List rightValue) {
        super(nn, result, rightValue);
    }

    @Override
    public void perform() throws NeuronNotFound
    {
        BigInteger newNeuron = nn.addNeuron();
        List<BigInteger> neurons = nn.getNeuronIDs();
        neurons.remove(new BigInteger("1"));
        neurons.remove(nn.getNeuron(newNeuron).getID());
        Double rnd = Math.floor(Math.random() * neurons.size());
        Integer index = rnd.intValue();// random index
        Neuron zNeuron = nn.getNeuron(neurons.get(index));
        neurons.clear();
        List<Connection> conns = nn.getConnectionsForZNeuron(zNeuron.getID());
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
        nn.connectNeuron(neurons.get(index), newNeuron, DEFAULT_FIBER_NUMBER);
        nn.connectNeuron(newNeuron, zNeuron.getID(), DEFAULT_FIBER_NUMBER);
        this.message = "Create neuron # ID = " + newNeuron + ", connected from " + neurons.get(index) + " to "
                + zNeuron.getID();
    }

}
