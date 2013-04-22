package ru.mipt.sign;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.data.ImportData;
import ru.mipt.sign.data.impl.ImportDataInput;
import ru.mipt.sign.facade.NeuroManager;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.util.ParserXml;
import ru.mipt.sign.util.XmlFormatter;

public class ConfigurationManager
{
    private static String DATA_PATH = "data/input.dat";
    private static ApplicationContext appCtx;

    public static ApplicationContext connect(BigInteger id1, BigInteger id2, Integer fiber) throws NeuronNotFound
    {
        NeuroNet nn = appCtx.getNet();
        nn.connectNeuron(id1, id2, fiber);
        return appCtx;
    }

    public static ApplicationContext removeNeuron(BigInteger id) throws NeuronNotFound
    {

        NeuroNet nn = appCtx.getNet();

        if (nn.removeNeuron(id) != 0)
        {
            throw new NeuronNotFound(id);
        }
        appCtx.setLast_removed_id(id);
        return appCtx;
    }

    public static ApplicationContext addNeuron()
    {
        NeuroNet nn = appCtx.getNet();

        appCtx.setLast_added_id(nn.addNeuron());
        return appCtx;
    }

    public static ApplicationContext init(String key) throws NeuronNotFound, NextCommandException
    {
        if ((key == null) || (key.isEmpty()))
        {
            key = "configuration";
        }
        ParserXml parser = new ParserXml(NeuronConst.DEFAULT_CONF_PATH + key + ".xml");

        appCtx = new ApplicationContext(parser.getCurrentId(), System.out);
        ImportData id = new ImportDataInput();
        appCtx.setData(id.getData(DATA_PATH));
        NeuroNet nn = new NeuroNet(parser, appCtx);
        appCtx.setNet(nn);
        appCtx.setManager(new NeuroManager());
        return appCtx;

    }

    public static ApplicationContext randomize()
    {
        NeuroNet nn = appCtx.getNet();

        nn.randomize();

        return appCtx;
    }

    public static ApplicationContext init()
    {
        return init(1, 1);
    }

    public static ApplicationContext init(Integer inNumber, Integer outNumber)
    {// TODO megre init method
        appCtx = new ApplicationContext(BigInteger.ZERO, System.out);
        ImportData id = new ImportDataInput();
        appCtx.setData(id.getData(DATA_PATH));
        NeuroNet nn = new NeuroNet(inNumber, outNumber, appCtx);
        try
        {
            nn.setInputNeuron(BigInteger.ONE);
        } catch (NeuronNotFound e)
        {
            System.out.println("There is no neuron with id = " + e.getId());
        }
        appCtx.setNet(nn);
        appCtx.setManager(new NeuroManager());
        return appCtx;
    }

    public static void exit()
    {
        saveConfiguration("configuration");
    }

    public static void saveConfiguration(String name)
    {
        Element root = new Element("configuration");
        Document doc = new Document(root);
        root.addContent(appCtx.getNet().getXml());
        saveParams(root);

        String out = "";
        XMLOutputter outputter = new XMLOutputter();
        out = outputter.outputString(doc);
        File f = new File(NeuronConst.DEFAULT_CONF_PATH + name + ".xml");
        f.setWritable(true);
        try
        {
            FileWriter fw = new FileWriter(f);
            fw.write(XmlFormatter.format(out));
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void saveParams(Element conf)
    {
        Element id = new Element("current_id");
        id.setAttribute("value", appCtx.getNextId().toString());
        conf.addContent(id);
    }

}
