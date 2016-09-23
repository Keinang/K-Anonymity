package App.View;

import App.Model.DataSetModel;
import App.Model.Vertex;
import App.Utils.DemoDataCreator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

import static App.Controller.DataSetController.getDegreeFreq;

/**
 * Created by Keinan.Gilad on 9/19/2016.
 */
public class TableView extends JPanel {
    public static final String VERTICES = "Vertices";
    public static final String DEGREE = "Degree";
    public static final String TOTAL_EDGES = "Total Edges:";
    public static final String TOTAL_VERTICES = "Total Vertices:";

    public TableView(DataSetModel dataSetToModel) {
        setUI(dataSetToModel);
    }

    private Object[][] prepareModel(DataSetModel dataSetToModel) {
        // get dataset model:
        Map<Vertex, Set<Vertex>> vertexToNeighbors = dataSetToModel.getVertexToNeighbors();
        Collection<Set<Vertex>> vertexDegreesValues = vertexToNeighbors.values();
        ArrayList<Integer> allDegreesWithDuplicates = new ArrayList<>();
        Iterator<Set<Vertex>> vertexDegreesValuesIter = vertexDegreesValues.iterator();
        while (vertexDegreesValuesIter.hasNext()){
            allDegreesWithDuplicates.add(vertexDegreesValuesIter.next().size());
        }

        // Count frequency of each degree
        Map<Integer, Integer> degreeToCount = getDegreeFreq(allDegreesWithDuplicates);

        // prepare the table model
        Set<Integer> degrees = degreeToCount.keySet();
        Iterator<Integer> degreeIterator = degrees.iterator();
        Object rowData[][] = new Object[degrees.size()][2];

        int i = 0;
        while (degreeIterator.hasNext()) {
            Integer degree = degreeIterator.next();
            Integer degreeCount = degreeToCount.get(degree);

            rowData[i] = new Object[]{degree, degreeCount};
            i++;
        }
        return rowData;
    }

    private void setUI(DataSetModel dataSetModel) {
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        this.setLayout(layout);
        Object[][] rowData = prepareModel(dataSetModel);

        // create table:
        Object columnNames[] = {DEGREE, VERTICES};

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
            @Override
            public Class getColumnClass(int column) {
                return Integer.class;
            }
        };
        JTable table = new JTable(model);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), dataSetModel.getTitle(), TitledBorder.CENTER, TitledBorder.TOP));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(0);

        // scroll:
        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setPreferredSize(new Dimension(320, 240));
        jScrollPane.setVisible(true);

        // labels:
        JLabel beforeVerticesLabel = new JLabel(TOTAL_VERTICES);
        JLabel beforeVerticesLabelValue = new JLabel(String.valueOf(dataSetModel.getVertices().size()));
        JLabel beforeEdgesLabel = new JLabel(TOTAL_EDGES);
        JLabel beforeEdgesLabelValue = new JLabel(String.valueOf(dataSetModel.getEdges().size()));

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(beforeVerticesLabel)
                                .addComponent(beforeEdgesLabel)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(beforeVerticesLabelValue)
                                .addComponent(beforeEdgesLabelValue)
                        )
                ));

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(beforeVerticesLabel)
                                .addComponent(beforeVerticesLabelValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(beforeEdgesLabel)
                                .addComponent(beforeEdgesLabelValue)
                        )
                )
        );
    }

    public static void main(String[] args) {
        final DemoDataCreator demoDataCreatorLocal = new DemoDataCreator();
        //create a window to display...
        JFrame jf = new JFrame("Demo Table");
        DataSetModel dataSetToModel = demoDataCreatorLocal.getDataSetToModel();
        TableView chart = new TableView(dataSetToModel);
        jf.add(chart);

        //do something when click on x
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make sure everything fits...
        jf.pack();
        //make it show up...
        jf.setVisible(true);
    }
}


/*
Labels for tableView after executing the algorithm
.addComponent(durationLabelValue)
.addComponent(afterVerticesLabelValue)
.addComponent(afterVerticesAddedLabelValue)
.addComponent(afterVerticesRemovedLabelValue)
.addComponent(afterEdgesLabelValue)
.addComponent(afterEdgesAddedLabelValue)
.addComponent(afterEdgesRemovedLabelValue)
.addComponent(afterObfuscationLeveldLabelValue)


.addComponent(durationLabel)
.addComponent(afterVerticesLabel)
.addComponent(afterVerticesAddedLabel)
.addComponent(afterVerticesRemovedLabel)
.addComponent(afterEdgesLabel)
.addComponent(afterEdgesAddedLabel)
.addComponent(afterEdgesRemovedLabel)
.addComponent(afterObfuscationLeveldLabel)*/