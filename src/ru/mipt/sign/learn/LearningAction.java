package ru.mipt.sign.learn;

import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;

public abstract class LearningAction
{
    protected NeuroNet nn;
    protected List rightValue;
    protected List result;
    protected String message;

    public LearningAction(NeuroNet nn, List result, List rightValue) {
        this.nn = nn;
        this.rightValue = rightValue;
        this.result = result;
    }

    public String getMessage()
    {
        return message;
    }

    public abstract void perform() throws NeuronNotFound;
}
