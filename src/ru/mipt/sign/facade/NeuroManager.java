package ru.mipt.sign.facade;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.data.InputDataProvider;
import ru.mipt.sign.data.impl.InputDataProviderByData;
import ru.mipt.sign.data.impl.InputDataProviderByNeuron;
import ru.mipt.sign.learn.LearningCenter;
import ru.mipt.sign.neurons.NeuroNet;

public class NeuroManager
{
    private final Random random = new Random(System.currentTimeMillis());
    private static final double start = 10; 
    private static double b = start;
    private static double c = 1;
    private static double prevAccuracy = 0;

    public void start(Long numberOfSteps) throws NeuronNotFound
    {
        ApplicationContext appCtx = ApplicationContext.getInstance();
        NeuroNet nn = appCtx.getNeuroNet();
        List<Double> result = new ArrayList<Double>();
        List<Double> rightResult = new ArrayList<Double>();
        // System.out.println("Start processing bars: \n");

        FileOutputStream fos;
        try
        {
            final double a = b;
            nn.setInputProvider(new InputDataProviderByNeuron()
            {
                @Override
                public List<Double> nextValue()
                {
                    double x = a/100;
                    List<Double> input = new ArrayList<Double>();
                    input.add(x);
                    return input;
                }
            });
            for (long s = 0; s < numberOfSteps; s++)
            {
                nn.calc();
                result = nn.getResult();
                nn.setInputProvider(new InputDataProviderByData(result,1));
//                rightResult = testFunction(nn.getCurrentInput());
                Double accuracy = 0d;//getAccuracy(result, rightResult).get(0);
//                if (Math.abs(100 - accuracy) < 2)
//                {
//                    if (b == 40)
//                        c *= -1;
//                    b += c;
//                }
//                if (b == start)
//                {
//                    b = start;
//                    c = 1;
//                }
//                Double eta = 1d;
//                double error = Math.abs(prevAccuracy - accuracy);
//                if (error < 1)
//                {
//                    eta = 2d;
//                }
//                if (error > 50)
//                {
//                    eta = 0.5d;
//                }
//                prevAccuracy = accuracy;
//                learn(nn, result, rightResult, eta);

                if (numberOfSteps < 100l)
                    System.out.println("x: " + nn.getCurrentInput() + " right: " + rightResult + " result: " + result
                            + " accuracy: " + accuracy);
            }
            if (numberOfSteps > 100)
            {
                fos = new FileOutputStream("log.txt", false);
                PrintStream out = new PrintStream(fos);
                final long predict = 100;
                final double dx = 0.01;
                nn.setInputProvider(new InputDataProvider()
                {
                    private int s = 0;

                    @Override
                    public List<Double> getNextInput()
                    {
                        Double x = s * dx;
                        s++;
                        return Collections.singletonList(x);
                    }
                });
                for (long s = 0; s < predict; s++)
                {
                    Double x = s * dx;
                    nn.calc();
                    Double predictedResult = nn.getResult().get(0);
                    DecimalFormat df = new DecimalFormat("###.######");
                    out.println(df.format(x) + "\t" + df.format(predictedResult));
                }
                fos.close();
            }
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (numberOfSteps > 1l)
            System.out.println("\nStop processing bars.");
    }

    private List<Double> testFunction(List<Double> x)
    {
        List<Double> result = new ArrayList<Double>();
        for (Double d : x)
        {
            result.add(func(d));
        }
        return result;
    }

    private Double func(Double x)
    {
        return  x;
    }

    private List<Double> getAccuracy(List<Double> actual, List<Double> right)
    {
        List<Double> result = new ArrayList<Double>();
        for (int i = 0; i < actual.size(); i++)
        {
            result.add(right.get(i) / actual.get(i) * 100);
        }
        return result;
    }

    public void learn(NeuroNet nn, List<Double> result, List<Double> rightValue, Double eta) throws NeuronNotFound
    {
        if (result.size() == 0)
            return;
        LearningCenter lc = new LearningCenter(LearningCenter.TYPE_ALL);
        lc.learn(nn, result, rightValue, eta);
    }
}
