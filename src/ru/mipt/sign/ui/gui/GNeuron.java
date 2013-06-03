package ru.mipt.sign.ui.gui;

import java.math.BigInteger;

import ru.mipt.sign.neurons.Neuron;

public class GNeuron
{
    private Neuron neuron;
    private boolean connected;

    public Neuron getNeuron()
    {
        return neuron;
    }

    public boolean isConnected()
    {
        return connected;
    }

    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    public GNeuron(Neuron neuron)
    {
        this.neuron = neuron;
        this.connected = false;
    }

    public BigInteger getID()
    {
        return neuron.getID();
    }

    public Integer getRole()
    {
        return neuron.getRole();
    }

    @Override
    public String toString()
    {
        return neuron.toString();
    }
}
