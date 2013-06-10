package ru.mipt.sign.ui.gui.menu;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ru.mipt.sign.neurons.Neuron;

public class WeightsTable extends JTable
{
    private Integer columnWidth = 100;
    private Neuron neuron;

    public WeightsTable(DefaultTableModel model)
    {
        super(model);
    }

    public void updateWeights()
    {
        updateWeights(neuron);
    }

    public void updateWeights(Neuron n)
    {
        if (n != null)
        {
            neuron = n;
        }
        if (neuron != null)
        {
            Double[][] weights = neuron.getWeights().getWeightsForShow();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Input");
            for (int i = 1; i < neuron.getOutNumber() + 1; i++)
            {
                model.addColumn("O " + i);
            }

            for (int i = 0; i < neuron.getInNumber(); i++)
            {
                model.addRow(weights[i]);
            }
            this.setModel(model);
            model.fireTableStructureChanged();
            for (int i = 0; i < this.getColumnCount(); i++)
            {
                TableColumn column = this.getColumnModel().getColumn(i);
                column.setPreferredWidth(columnWidth);
            }
        }
    }
}
