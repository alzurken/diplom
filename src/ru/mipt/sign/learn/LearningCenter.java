package ru.mipt.sign.learn;

import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;

public class LearningCenter
{
    public static final int TYPE_CREATE = 0;
    public static final int TYPE_WEIGHT = 1;
    public static final int TYPE_CONNECT = 2;
    public static final int TYPE_DISCONNECT = 3;
    public static String lastMessage;

    public String getMessage()
    {
        return lastMessage;
    }

    public void learn(NeuroNet nn, List result, List rightValue, Integer type) throws NeuronNotFound
    {
        LearningAction action = null;
        switch (type)
        {
        case TYPE_CREATE:
            action = new CreateAction(nn, result, rightValue);
            break;
        case TYPE_WEIGHT:
            action = new WeightAction(nn, result, rightValue);
            break;
        case TYPE_CONNECT:
            action = new ConnectAction(nn, result, rightValue);
            break;
        case TYPE_DISCONNECT:
            action = new DisconnectAction(nn, result, rightValue);
            break;
        }
        action.perform();
        lastMessage = action.getMessage();
    }
}
