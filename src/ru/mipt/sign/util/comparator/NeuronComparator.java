package ru.mipt.sign.util.comparator;

import java.util.Comparator;

import ru.mipt.sign.neurons.Neuron;

public class NeuronComparator implements Comparator<Neuron>
{
    @Override
    public int compare(Neuron n1, Neuron n2)
    {
        return n1.compareTo(n2);
    }
};