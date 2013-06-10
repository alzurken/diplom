package ru.mipt.sign.neurons.inner;

import java.math.BigInteger;
import java.util.HashMap;

import ru.mipt.sign.neurons.Neuron;

public interface NeuroNetCalculator
{
    public void calc(HashMap<BigInteger, Neuron> neuroPool);
}
