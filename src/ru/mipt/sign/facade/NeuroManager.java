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
import ru.mipt.sign.data.impl.InputDataProviderByNeuron;
import ru.mipt.sign.learn.LearningCenter;
import ru.mipt.sign.neurons.NeuroNet;

public class NeuroManager
{
    private final Random random = new Random(System.currentTimeMillis());

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
            nn.setInputProvider(new InputDataProviderByNeuron()
            {
                
                @Override
                public List<Double> nextValue()
                {
                    double x = Double.valueOf(random.nextDouble() * 10).intValue();
                    List<Double> input = Collections.singletonList(x);
                    return input;
                }
            });
            for (long s = 0; s < numberOfSteps; s++)
            {
                nn.calc();
                result = nn.getResult();
                rightResult = nn.getCurrentInput();
                Double accuracy = getAccuracy(result, rightResult).get(0);
                if (numberOfSteps < 100l)
                    System.out.println("x: " + rightResult.get(0) + " right: " + testFunction(rightResult.get(0)) + " result: " + result + " accuracy: "
                            + accuracy);
                learn(nn, result, rightResult);
            }
            if (numberOfSteps > 100)
            {
                fos = new FileOutputStream("log.txt", false);
                PrintStream out = new PrintStream(fos);
                long predict = 1000;
                double dx = 0.01;
                for (long s = 0; s < predict; s++)
                {
                    Double x = s * dx;
                    List input = Collections.singletonList(x);
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

    private Double testFunction(Double x)
    {
        return 0.1 * x;
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

    public void learn(NeuroNet nn, List<Double> result, List<Double> rightValue) throws NeuronNotFound
    {
        if (result.size() == 0)
            return;
        LearningCenter lc = new LearningCenter(LearningCenter.TYPE_ALL);
        lc.learn(nn, result, rightValue);
    }
}
