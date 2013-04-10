package ru.mipt.sign.facade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.forex.Bar;
import ru.mipt.sign.learn.LearningCenter;
import ru.mipt.sign.neurons.NeuroNet;

public class NeuroManager
{

    public void start(ApplicationContext appCtx) throws NeuronNotFound
    {
        NeuroNet nn = appCtx.getNet();
        List<Bar> in = appCtx.getData();
        List<Double> prevResult = new ArrayList<Double>();
        List<Double> forCompare = new ArrayList<Double>();
        System.out.println("Start processing bars: \n");
        for (Iterator<Bar> it = in.iterator(); it.hasNext();)
        {
            Bar curr = it.next();
            if (!forCompare.isEmpty())
                System.out.println(curr.toString() + "  Result: " + getAccuracy(forCompare, curr.getList()));
            nn.nextInput(curr.getList());
            nn.calc();
            forCompare = nn.getResult();
            learn(nn, prevResult, curr.getList());
            prevResult = nn.getResult();
            // System.out.println(curr.toString() + "  Result: " + nn.getResult().toString());
        }
        System.out.println("\nStop processing bars.");
    }

    private List<Double> getAccuracy(List<Double> actual, List<Double> right)
    {
        List<Double> result = new ArrayList<Double>();
        for (int i = 0; i < actual.size(); i++)
        {
            result.add(right.get(i) / actual.get(i));
        }
        return result;
    }

    public void learn(NeuroNet nn, List<Double> result, List<Double> rightValue) throws NeuronNotFound
    {
        if (result.size() == 0)
            return;
        LearningCenter lc = new LearningCenter();
        Random rnd = new Random();
        Integer type = rnd.nextInt(100);
        if (type < 50)
        {
            Integer flag = 4;
            while (flag != 0)
            {
                flag = 4;
                lc.learn(nn, result, rightValue, LearningCenter.TYPE_WEIGHT);
                nn.calc();
                result = nn.getResult();
                List<Double> accuracy = getAccuracy(result, rightValue);
                // System.out.println("Current: " + accuracy);
                for (int i = 0; i < 4; i++)
                {
                    if (Math.abs(1 - accuracy.get(i)) < 0.1)
                        flag--;
                }
            }
            System.out.println(lc.getMessage());
        } else if (type < 60)
        {
            lc.learn(nn, result, rightValue, LearningCenter.TYPE_DISCONNECT);
            System.out.println(lc.getMessage());
        } else if (type < 85)
        {
            lc.learn(nn, result, rightValue, LearningCenter.TYPE_CONNECT);
            System.out.println(lc.getMessage());
        } else
        {
            lc.learn(nn, result, rightValue, LearningCenter.TYPE_CREATE);
            System.out.println(lc.getMessage());
        }
    }
}
