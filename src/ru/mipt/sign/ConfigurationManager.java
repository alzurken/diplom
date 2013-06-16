package ru.mipt.sign;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.facade.NeuroManager;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.util.ParserJSON;

import com.google.gson.JsonObject;

public class ConfigurationManager implements NeuronConst
{
    public static void connect(BigInteger id1, BigInteger id2, Integer fiber) throws NeuronNotFound
    {
        NeuroNet nn = ApplicationContext.getInstance().getNeuroNet();
        nn.connectNeuron(id1, id2, fiber);
    }

    public static void removeNeuron(BigInteger id) throws NeuronNotFound
    {

        NeuroNet nn = ApplicationContext.getInstance().getNeuroNet();

        if (nn.removeNeuron(id) != 0)
        {
            throw new NeuronNotFound(id);
        }
        ApplicationContext.getInstance().setLast_removed_id(id);
    }

    public static void addNeuron()
    {
        NeuroNet nn = ApplicationContext.getInstance().getNeuroNet();

        ApplicationContext.getInstance().setLast_added_id(nn.addNeuron());
    }

    public static void init(String key) throws NeuronNotFound, NextCommandException
    {
        if ((key == null) || (key.isEmpty()))
        {
            key = "configuration";
        }
        JsonObject neuroNet = ParserJSON.getJsonObject(DEFAULT_CONF_PATH + key + ".jsn");

        ApplicationContext appCtx = ApplicationContext.getInstance();
        appCtx.setOut(System.out);
        NeuroNet nn = new NeuroNet(neuroNet);
        appCtx.setNet(nn);
        appCtx.setManager(new NeuroManager());

    }

    public static void randomize()
    {
        NeuroNet nn = ApplicationContext.getInstance().getNeuroNet();

        nn.randomize();
    }

    public static void init()
    {
        int inputNumber = INPUT_NUMBER;
        int firstNumber = 100;
        int secondNumber = 30;
        init(inputNumber, 1);
        ApplicationContext appCtx = ApplicationContext.getInstance();
        NeuroNet nn = appCtx.getNeuroNet();
        List<BigInteger> inputLayer = nn.getInputNeurons();
        List<BigInteger> firstLayer = new ArrayList<BigInteger>();
        List<BigInteger> secondLayer = new ArrayList<BigInteger>();
        
        Double average = 0d;
        for (int i = 0; i < firstNumber; i++)
        {
            if (i % 10 == 0)
            {
                System.out.println(i+"%");
            }
            long time = System.currentTimeMillis();
            BigInteger neuronID = nn.addNeuron();
            firstLayer.add(neuronID);
            for (int j = 0; j < inputNumber; j++)
            {
                try
                {
                    nn.connectNeuron(inputLayer.get(j), neuronID, 1);
                } catch (NeuronNotFound e)
                {
                    e.printStackTrace();
                }
            }
            average += System.currentTimeMillis() - time;
//            System.out.println("" + (i+1) + " neuron added. Time: " + average / (i+1));
        }
        System.out.println("100%");
        System.out.println("Average: " + (average/firstNumber));
        System.out.println("Time: " + (average));
        System.out.println("First layer created");
        average = 0d;
        for (int i = 0; i < secondNumber; i++)
        {
            if (i % 3 == 0)
            {
                System.out.println((i/3*10)+"%");
            }
            long time = System.currentTimeMillis();
            BigInteger neuronID = nn.addNeuron();
            secondLayer.add(neuronID);
            for (int j = 0; j < firstNumber; j++)
            {
                try
                {
                    nn.connectNeuron(firstLayer.get(j), neuronID, 1);
                } catch (NeuronNotFound e)
                {
                    e.printStackTrace();
                }
            }
            average += System.currentTimeMillis() - time;
//            System.out.println("" + (i+1) + " neuron added. Time: " + average / (i+1));
        }
        System.out.println("100%");
        System.out.println("Average: " + (average/secondNumber));
        System.out.println("Time: " + (average));
        System.out.println("Second layer created");
        for (int j = 0; j < secondNumber; j++)
        {
            try
            {
                nn.connectNeuron(secondLayer.get(j), LAST_NEURON_ID, 1);
            } catch (NeuronNotFound e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Ready");
    }

    public static void init(Integer inNumber, Integer outNumber)
    {// TODO megre init method
        ApplicationContext appCtx = ApplicationContext.getInstance();
        appCtx.setOut(System.out);
        NeuroNet nn = new NeuroNet(inNumber, outNumber);
        
        appCtx.setNet(nn);
        appCtx.setManager(new NeuroManager());
    }

    public static void exit()
    {
        saveConfiguration("configuration");
    }

    public static void saveConfiguration(String name)
    {
        JsonObject neuroNet = ApplicationContext.getInstance().getNeuroNet().getJSON();

        String out = neuroNet.toString();
        File f = new File(DEFAULT_CONF_PATH + name + ".jsn");
        f.setWritable(true);
        try
        {
            FileWriter fw = new FileWriter(f);
            fw.write(out);
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
