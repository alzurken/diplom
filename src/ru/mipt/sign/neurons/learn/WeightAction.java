package ru.mipt.sign.neurons.learn;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.Connection;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.Neuron;

public class WeightAction extends LearningAction
{
    private double eta;
    
    public WeightAction(NeuroNet nn, List result, List rightValue, double eta)
    {
        super(nn, result, rightValue);
        this.eta = eta;
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
        HashSet<Neuron> neuronSet = new HashSet<Neuron>();
        neuronSet.addAll(lastNeurons);
        while (!neuronSet.isEmpty())
        {
            neuronSet = learnLayer(neuronSet);
        }
    }

    private HashSet<Neuron> learnLayer(Set<Neuron> zNeurons)
    {
        HashSet<Neuron> neuronSet = new HashSet<Neuron>();
        for (Neuron n : zNeurons)
        {
            if (n.getRole() == INPUT_ROLE)
                continue;
            List<Connection> connections = nn.getConnectionsForZNeuron(n.getID());
            for (Connection c : connections)
            {
                Neuron neuron = c.getANeuron();
                if (neuron.getRole() == INPUT_ROLE)
                    continue;
                neuronSet.add(neuron);
            }
        }

        for (Neuron n : neuronSet)
        {
            if (n.getRole() == INPUT_ROLE)
                continue;
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
