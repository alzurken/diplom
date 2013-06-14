package ru.mipt.sign;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

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
        init(1, 1);
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
