package ru.mipt.sign.facade;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextBarException;
import ru.mipt.sign.forex.Bar;
import ru.mipt.sign.forex.HistoryHolder;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.neurons.data.InputDataProvider;
import ru.mipt.sign.neurons.data.impl.InputDataProviderByData;
import ru.mipt.sign.neurons.learn.LearningCenter;

public class NeuroManager implements NeuronConst
{
    private static HistoryHolder historyHolder = new HistoryHolder();
    private DifferenceFinder difference = new DifferenceFinder();
    private LearningCenter lc = new LearningCenter(LearningCenter.TYPE_WEIGHT);

    public void start()
    {

    }

    public void start(Long l)
    {
        for (int j = 1; j <= l; j++)
        {
            for (int i = 0; i < j; i++)
            {
                System.out.println("Iteration " + (i + 1));
                NeuroNet nn = ApplicationContext.getInstance().getNeuroNet();
                Bar startBar = null;
                if (nn.getLastLearned() == null)
                {
                    try
                    {
                        startBar = historyHolder.getNextBar(historyHolder.getFirstBar(), LEARN_WINDOW);
                        nn.setLastLearned(startBar);
                    } catch (NextBarException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    startBar = nn.getLastLearned();
                }
                List<Double> input = getBarsAsList(startBar);
                List<Double> right = startBar.getList();
                lc.refresh();
                learning(new InputDataProviderByData(input, 1), right);
                try
                {
                    nn.setLastLearned(historyHolder.getNextBar(startBar));
                } catch (NextBarException e)
                {
                    e.printStackTrace();
                }
            }
            ApplicationContext.getInstance().getNeuroNet().setLastLearned(null);
        }
        predict(20);
    }

    private void predict(int forwardSteps)
    {
        NeuroNet nn = ApplicationContext.getInstance().getNeuroNet();
        Bar bar = nn.getLastLearned();
        List<Double> input = getBarsAsList(bar);
        for (int i = 0; i < forwardSteps; i++)
        {
            InputDataProvider inputProvider = new InputDataProviderByData(input, 1);
            nn.setInputProvider(inputProvider);
            try
            {
                nn.calc();
            } catch (NeuronNotFound e)
            {
                e.printStackTrace();
            }
            System.out.println("***");
            printList(bar.getList());
            printList(nn.getResult());

            bar = historyHolder.getNextBar(bar);
            List<Double> temp = new ArrayList<Double>();
            for (int j = 4; j < input.size(); j++)
            {
                temp.add(input.get(j));
            }
            temp.addAll(nn.getResult());
            input = temp;
        }
    }

    private void printList(List<Double> list)
    {
        String s = "[";
        DecimalFormat format = new DecimalFormat("#.#####");
        for (Double d : list)
        {
            s += format.format(d) + " ";
        }
        s += "]";
        System.out.println(s);
    }

    private List<Double> getBarsAsList(Bar startBar)
    {
        List<Double> result = new ArrayList<Double>();
        try
        {
            Bar temp = startBar;
            for (int i = 0; i < LEARN_WINDOW; i++)
            {
                temp = historyHolder.getPrevBar(temp);
                result.addAll(temp.getList());
            }
        } catch (NextBarException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    private void learning(InputDataProvider inputProvider, List<Double> rightResult)
    {
        ApplicationContext appCtx = ApplicationContext.getInstance();
        NeuroNet nn = appCtx.getNeuroNet();
        List<Double> result = new ArrayList<Double>();
        long time = System.currentTimeMillis();
        Double accuracy = 1d;
        Long step = 0l;
        while ((accuracy > ACCURACY) && (step < 100000))
        {
            step++;
            inputProvider.refresh();
            nn.setInputProvider(inputProvider);
            try
            {
                nn.calc();
            } catch (NeuronNotFound e)
            {
                e.printStackTrace();
            }
            result = nn.getResult();
            accuracy = getAccuracy(result, rightResult);
            double eta = 1d;
            if (difference.getDifference(result, rightResult) == -1)
            {
                eta = 0.5d;
            }
            try
            {
                learn(nn, result, rightResult, eta);
            } catch (NeuronNotFound e)
            {
                e.printStackTrace();
            }
        }
        Long timeLearn = System.currentTimeMillis() - time;
        System.out.println("Steps: " + step);
        // System.out.println(" right: " + rightResult + " result: " + result +
        // " accuracy: " + accuracy + " step: "
        // + step + " timeLearn: " + timeLearn);
        // System.out.println("timeCalc: " + timeCalc + " timeLearn: " +
        // timeLearn);
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
        return x;
    }

    private Double getAccuracy(List<Double> actual, List<Double> right)
    {
        Double result = 0d;
        for (int i = 0; i < actual.size(); i++)
        {
            result += Math.abs(right.get(i) - actual.get(i));
        }
        return result;
    }

    public void learn(NeuroNet nn, List<Double> result, List<Double> rightValue, Double eta) throws NeuronNotFound
    {
        if (result.size() == 0)
            return;
        lc.learn(nn, result, rightValue, eta);
    }
}
