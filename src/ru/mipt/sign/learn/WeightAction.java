package ru.mipt.sign.learn;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.Connection;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.util.comparator.NeuronComparator;

public class WeightAction extends LearningAction
{
    double eta = 0.1;

    public WeightAction(NeuroNet nn, List result, List rightValue)
    {
        super(nn, result, rightValue);
    }

    @Override
    public void perform() throws NeuronNotFound
    {
        List<Neuron> lastNeurons = nn.getOutputNeurons();
        for (int i = 0; i < lastNeurons.size(); i++)
        {
            Neuron n = lastNeurons.get(i);
            double out = n.getOutput().get(0);
            double delta = (out - (Double) rightValue.get(i)) * n.getDerivative();
            n.setDelta(0, delta); // TODO for 1 output only, refactor
            Map<Integer, Double> input = n.getInput();
            for (int j = 0; j < n.getInNumber(); j++)
            {
                double deltaWeight = -eta * input.get(j) * delta;
                n.changeWeight(j, 0, deltaWeight);
            }
        }

        TreeSet<Neuron> neuronSet = new TreeSet<Neuron>(new NeuronComparator());
        neuronSet.addAll(lastNeurons);
        while (!neuronSet.isEmpty())
        {
            neuronSet = learnLayer(neuronSet);
        }
    }

    private TreeSet<Neuron> learnLayer(Set<Neuron> zNeurons)
    {
        TreeSet<Neuron> neuronSet = new TreeSet<Neuron>(new NeuronComparator());
        for (Neuron n : zNeurons)
        {
            List<Connection> connections = nn.getConnectionsForZNeuron(n.getID());
            for (Connection c : connections)
            {
                neuronSet.add(c.getANeuron());
            }
        }

        for (Neuron n : neuronSet)
        {
            List<Connection> connections = nn.getConnectionsForANeuron(n.getID());
            for (Connection c : connections)
            {
                Map<Integer, Integer> a2zmapping = c.getA2zMapping();
                Neuron next = c.getZNeuron();
                for (Integer j : a2zmapping.keySet())
                {
                    double deltaSum = 0d;

                    for (int k = 0; k < next.getOutNumber(); k++)
                    {
                        deltaSum += next.getWeight(a2zmapping.get(j), k) * next.getDelta(k);
                    }
                    double delta = n.getDerivative() * deltaSum;
                    n.setDelta(j, delta);
                    for (int i = 0; i < n.getInNumber(); i++)
                    {
                        double deltaWeight = -eta * n.getInput().get(i) * delta;
                        n.changeWeight(i, j, deltaWeight);
                    }
                }
            }
        }
        return neuronSet;
    }
    
}
