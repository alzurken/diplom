package ru.mipt.sign.ui.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import ru.mipt.sign.ui.gui.listeners.ButtonListener;
import ru.mipt.sign.ui.gui.menu.WeightsTable;
import ru.mipt.sign.ui.gui.plugins.MousePlugin;
import ru.mipt.sign.ui.gui.plugins.VertexMenu;
import ru.mipt.sign.ui.gui.transformers.LabelTransformer;
import ru.mipt.sign.ui.gui.transformers.NeuronTransformer;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

public class MainWindow
{
    private JFrame window;
    private JPanel graphPanel;
    private JPanel tablePanel;
    private WeightsTable table;
    private EditingModalGraphMouse gm;
    private VisualizationViewer<GNeuron, GConnection> vv;

    public MainWindow()
    {
        window = new JFrame("Sign");
        window.setSize(500, 500);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initTable();
        initGraphPanel();

    }

    private void initTable()
    {
        tablePanel = new JPanel();
        Object[] columnNames = { "Input", "O 1" , "O 2"};
        table = new WeightsTable(new DefaultTableModel(columnNames, 3));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        table.setFillsViewportHeight(true);
        BoxLayout layout = new BoxLayout(tablePanel, BoxLayout.Y_AXIS);
        tablePanel.setLayout(layout);
        tablePanel.add(scrollPane);
        JButton button = new JButton("Next step");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new ButtonListener());
        tablePanel.add(button);
        window.add(tablePanel, BorderLayout.LINE_START);
    }

    private void initGraphPanel()
    {
        graphPanel = new JPanel();
        graphPanel.setSize(new Dimension(400, 400));
        graphPanel.setPreferredSize(new Dimension(400, 400));
        graphPanel.setLayout(new BorderLayout());
        window.add(graphPanel, BorderLayout.CENTER);
        window.setVisible(true);
    }

    public void display()
    {
        GNeuronNet graph = new GNeuronNet();

        Layout<GNeuron, GConnection> layout = new ISOMLayout<GNeuron, GConnection>(graph.getGraph());
        vv = new VisualizationViewer<GNeuron, GConnection>(layout);

        Dimension size = new Dimension(graphPanel.getWidth(), graphPanel.getHeight());
        vv.setPreferredSize(size);
        vv.getRenderContext().setVertexFillPaintTransformer(new NeuronTransformer());
        vv.getRenderContext().setVertexLabelTransformer(new LabelTransformer());
        EditingModalGraphMouse gm = new EditingModalGraphMouse(vv.getRenderContext(), null, null);
        MousePlugin myPlugin = new MousePlugin();
        // Add some popup menus for the edges and vertices to our mouse plugin.
        JPopupMenu vertexMenu = new VertexMenu();
        myPlugin.setVertexPopup(vertexMenu);
        gm.remove(gm.getPopupEditingPlugin()); // Removes the existing popup
                                               // editing plugin

        gm.add(myPlugin);
        gm.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(gm);
        graphPanel.removeAll();
        graphPanel.add(vv, BorderLayout.CENTER);
        table.updateWeights();
        window.validate();
    }
}
