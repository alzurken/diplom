package ru.mipt.sign.core.exceptions;

import java.math.BigInteger;

public class NeuronNotFound extends NeuronException
{
    private BigInteger id;

    public BigInteger getId()
    {
        return id;
    }

    public NeuronNotFound(BigInteger id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "There is no neuron with id=" + id;
    }
}
