package ru.mipt.sign.ui.gui;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.neurons.Connection;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.Neuron;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class GNeuronNet
{
    private Graph<GNeuron, GConnection> graph;
    
    public GNeuronNet()
    {
        initialize();
    }
    
    public Graph<GNeuron, GConnection> getGraph()
    {
        return graph;
    }

    private void initialize()
    {
        NeuroNet nn = ApplicationContext.getInstance().getNeuroNet();
        graph = new DirectedSparseMultigraph<GNeuron, GConnection>();
        List<Connection> connections = nn.getConnections();
        List<Neuron> neurons = nn.getNeurons();
        Map<BigInteger, GNeuron> gNeurons = new HashMap<BigInteger, GNeuron>();
        
        for (Neuron n : neurons)
        {
            gNeurons.put(n.getID(), new GNeuron(n));
        }
        
        GNeuron gNeuronA = null;
        GNeuron gNeuronZ = null;
        for (Connection c : connections)
        {
            try
            {
                gNeuronA = gNeurons.get(c.getAID());
                gNeuronA.setConnected(true);
                gNeuronZ = gNeurons.get(c.getZID());
                gNeuronZ.setConnected(true);
                graph.addEdge(new GConnection(c), gNeuronA, gNeuronZ);
            } catch (Exception e)
            {
                System.out.println("A - " + gNeuronA);
                System.out.println("Z - " + gNeuronZ);
                System.out.println("C - " + c);
            }
        }
        for (GNeuron gn : gNeurons.values())
        {
            if (!gn.isConnected())
            {
                graph.addVertex(gn);
            }
        }
    }
}
