package ru.mipt.sign.facade;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
        List<Double> forCompare = new ArrayList<Double>();
        List<Double> rightResult = new ArrayList<Double>();
        System.out.println("Start processing bars: \n");

        FileOutputStream fos;
        try
        {
            fos = new FileOutputStream("log.txt", false);
            PrintStream out = new PrintStream(fos);
            long maxSteps = 10000;
            long step = 0;
            long lostSteps = 0;
            for (long s = 0; s < maxSteps; s++)
            {
                step++;
                Double x = Math.random()*2*Math.PI;
                List input = Collections.singletonList(x);
                nn.nextInput(input);
                nn.calc();
                forCompare = nn.getResult();
                rightResult = Collections.singletonList(testFunction(x));
                Double accuracy = getAccuracy(forCompare, rightResult).get(0);
                if (Math.abs(100 - accuracy) < 10)
                {
                //out.println("Step: " + step + " Lost: " + lostSteps  + " Accuracy: "  + accuracy);
                }
                else
                {
                    lostSteps++;
                }
              //  learn(nn, forCompare, rightResult);         
            }
            long predict = 1000;
            double dx = 0.01;
            for (long s = 0; s< predict; s++)
            {
                Double x = s*dx;
                List input = Collections.singletonList(x);
                nn.nextInput(input);
                nn.calc();
                Double result = nn.getResult().get(0);
                DecimalFormat df = new DecimalFormat("###.######");
                out.println(df.format(x) + "\t" + df.format(result));
            }
            fos.close();
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("\nStop processing bars.");
    }

    private Double testFunction(Double x)
    {
        return 0.5*(Math.sin(x) + 1);
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
        LearningCenter lc = new LearningCenter();
        Random rnd = new Random();
        Integer type = rnd.nextInt(30);
        if (type < 50)
        {

            /*lc.learn(nn, result, rightValue, LearningCenter.TYPE_WEIGHT);
            nn.nextInput(new ArrayList(nn.getNeuron(new BigInteger("1")).getInput().values()));
            nn.calc();
            List<Double> newResult = nn.getResult();
            System.out.println("Previous: " + result + " Current: " + newResult + " Right: " + rightValue);*/

            // System.out.println(lc.getMessage());
        }
        else if (type < 60)
        {
            lc.learn(nn, result, rightValue, LearningCenter.TYPE_DISCONNECT);
            System.out.println(lc.getMessage());
        }
        else if (type < 85)
        {
            lc.learn(nn, result, rightValue, LearningCenter.TYPE_CONNECT);
            System.out.println(lc.getMessage());
        }
        else
        {
            lc.learn(nn, result, rightValue, LearningCenter.TYPE_CREATE);
            System.out.println(lc.getMessage());
        }
    }
}
