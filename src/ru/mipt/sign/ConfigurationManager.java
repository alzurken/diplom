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
import ru.mipt.sign.facade.NeuroManager;
import ru.mipt.sign.neurons.HopfieldNeuroNet;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.util.ParserXml;
import ru.mipt.sign.util.XmlFormatter;

public class ConfigurationManager
{
    private static String DATA_PATH = "data/input.dat";

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
        ParserXml parser = new ParserXml(NeuronConst.DEFAULT_CONF_PATH + key + ".xml");

        ApplicationContext appCtx = ApplicationContext.getInstance();
        appCtx.setCurrentID(parser.getCurrentID());
        appCtx.setOut(System.out);
        NeuroNet nn = new NeuroNet(parser);
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
        init(3, 1);
    }

    public static void init(Integer inNumber, Integer outNumber)
    {// TODO megre init method
        ApplicationContext appCtx = ApplicationContext.getInstance();
        appCtx.setCurrentID(BigInteger.ZERO);
        appCtx.setOut(System.out);
        NeuroNet nn = new HopfieldNeuroNet(inNumber);
        
        appCtx.setNet(nn);
        appCtx.setManager(new NeuroManager());
    }

    public static void exit()
    {
        saveConfiguration("configuration");
    }

    public static void saveConfiguration(String name)
    {
        Element root = new Element("configuration");
        Document doc = new Document(root);
        root.addContent(ApplicationContext.getInstance().getNeuroNet().getXml());
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
        id.setAttribute("value", ApplicationContext.getInstance().getNextId().toString());
        conf.addContent(id);
    }

}
