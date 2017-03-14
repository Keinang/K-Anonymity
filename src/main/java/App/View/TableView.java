package App.View;

import App.Common.Utils.DemoDataCreator;
import App.Model.Graph;
import App.Model.Vertex;
import com.intellij.vcs.log.ui.frame.WrappedFlowLayout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.util.Precision;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import static App.Datasets.DataSetController.getDegreeFreq;

/**
 * Created by Keinan.Gilad on 9/19/2016.
 */
public class TableView extends JPanel {
    public static final String VERTICES = "Vertices";
    public static final String DEGREE = "Degree";
    public static final String VERTEX = "Vertex";
    public static final String TOTAL_EDGES = "Total Edges";
    public static final String TOTAL_VERTICES = "Total Vertices";
    public static final String SIZE = "Size";
    public static final String PARTITIONS = "Partitions";
    public static final String PROBABILITY_AFTER = "Degree similarity";

    // view params
    private String title;

    // runtime params
    private float duration = 0;
    private int verticesAdded;
    private int edgeAdded;
    private List<List<Vertex>> partitions;

    public TableView(Graph anonymizedData, Graph originalData, long before, String algorithm, String k) {
        // diff
        this.edgeAdded = anonymizedData.getEdges().size() - originalData.getEdges().size();
        this.verticesAdded = anonymizedData.getVertices().size() - originalData.getVertices().size();
        this.partitions = anonymizedData.getPartitions();

        // time calculation
        long after = System.currentTimeMillis();
        this.duration = before < 1 ? 0 : ((after - before) / 1000f);

        // setting title
        if (StringUtils.isNotEmpty(algorithm) && StringUtils.isNotEmpty(k)) {
            this.title = String.format("%s anonymized with %s", k, algorithm);
        }

        // init UI components
        setUI(anonymizedData);
    }

    private void setUI(Graph dataSetModel) {
        // create container
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        // add tables
        Map<Integer, Integer> degreeToCount = calculateDegreeToCount(dataSetModel);
        JScrollPane degreeToVerticesPane = addDegreeToVerticesTable(degreeToCount);

        JScrollPane vertexToVerticesPane = addVertexToVerticesTable(dataSetModel, degreeToCount);

        JScrollPane partitionsPane = new JScrollPane();
        if (this.partitions != null) {
            partitionsPane = addParititonsTable();
        }

        JPanel indicationsPanel = addIndicationPanel(dataSetModel);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(degreeToVerticesPane)
                .addComponent(vertexToVerticesPane)
                .addComponent(partitionsPane)
                .addComponent(indicationsPanel)
        );

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(degreeToVerticesPane)
                .addComponent(vertexToVerticesPane)
                .addComponent(partitionsPane)
                .addComponent(indicationsPanel)
        );

        repaint();
        revalidate();
    }

    private Map<Integer, Integer> calculateDegreeToCount(Graph dataSetModel) {
        // get data set model:
        Map<Vertex, Set<Vertex>> vertexToNeighbors = dataSetModel.getVertexToNeighbors();
        Collection<Set<Vertex>> vertexDegreesValues = vertexToNeighbors.values();
        ArrayList<Integer> allDegreesWithDuplicates = new ArrayList<>();
        Iterator<Set<Vertex>> vertexDegreesValuesIter = vertexDegreesValues.iterator();
        while (vertexDegreesValuesIter.hasNext()) {
            allDegreesWithDuplicates.add(vertexDegreesValuesIter.next().size());
        }
        return getDegreeFreq(allDegreesWithDuplicates);
    }

    private JPanel addIndicationPanel(Graph dataSetModel) {
        JPanel indicationPanel = new JPanel();
        GroupLayout layout = new GroupLayout(indicationPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        indicationPanel.setLayout(layout);

        // Total values:
        JLabel totalVerticesLabel = new JLabel(TOTAL_VERTICES);
        List<Vertex> vertices = dataSetModel.getVertices();
        JLabel totalVerticesLabelValue = new JLabel(String.valueOf(vertices.size()));

        Integer verticesAdded = this.verticesAdded;
        JLabel verticesAddedLabel = new JLabel("Vertices added (%)");
        JLabel verticesAddedLabelValue = new JLabel(String.format("%s (%.2f%s)", verticesAdded, ((verticesAdded * 100.0f / (vertices.size() - verticesAdded))), "%"));

        int totalEdges = dataSetModel.getEdges().size();
        JLabel totalEdgesLabel = new JLabel(TOTAL_EDGES);
        JLabel totalEdgesLabelValue = new JLabel(String.valueOf(totalEdges));

        Integer edgeAdded = this.edgeAdded;
        JLabel edgesAddedLabel = new JLabel("Edges added (%)");
        JLabel edgesAddedLabelValue = new JLabel(String.format("%s (%.2f%s)", edgeAdded, ((edgeAdded * 100.0f / (totalEdges - edgeAdded))), "%"));

        // Values after running the algorithm
        JLabel durationLabel = new JLabel("Duration");
        JLabel durationLabelValue = new JLabel(String.format("%.2fsec", this.duration));

        // set components in layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalVerticesLabel)
                        .addComponent(verticesAddedLabel)
                        .addComponent(totalEdgesLabel)
                        .addComponent(edgesAddedLabel)
                        .addComponent(durationLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalVerticesLabelValue)
                        .addComponent(verticesAddedLabelValue)
                        .addComponent(totalEdgesLabelValue)
                        .addComponent(edgesAddedLabelValue)
                        .addComponent(durationLabelValue)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalVerticesLabel)
                        .addComponent(totalVerticesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(verticesAddedLabel)
                        .addComponent(verticesAddedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalEdgesLabel)
                        .addComponent(totalEdgesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(edgesAddedLabel)
                        .addComponent(edgesAddedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(durationLabel)
                        .addComponent(durationLabelValue)
                )
        );
        return indicationPanel;
    }

    @NotNull
    private JScrollPane addVertexToVerticesTable(Graph dataSetModel, Map<Integer, Integer> degreeToCount) {
        Object[][] rowData = prepareVertexToVerticesModel(dataSetModel, degreeToCount);
        Object columnNames[] = {VERTEX, DEGREE, PROBABILITY_AFTER, VERTICES};
        return createTable(this.title, rowData, columnNames);
    }

    @NotNull
    private JScrollPane addDegreeToVerticesTable(Map<Integer, Integer> degreeToCount) {
        Object[][] rowData = prepareDegreeToVerticesModel(degreeToCount);
        Object columnNames[] = {DEGREE, VERTICES};
        return createTable(this.title, rowData, columnNames);
    }

    private JScrollPane addParititonsTable() {
        Object[][] rowData = preparePartitionsModel(this.partitions);
        Object columnNames[] = {SIZE, PARTITIONS};
        return createTable(this.title, rowData, columnNames);
    }

    private Object[][] preparePartitionsModel(List<List<Vertex>> partitions) {
        Object rowData[][] = new Object[partitions.size()][1];

        int i = 0;
        for (List<Vertex> partition : partitions) {
            rowData[i] = new Object[]{partition.size(), partition};
            i++;
        }
        return rowData;
    }

    @NotNull
    private JScrollPane createTable(String title, final Object[][] rowData, Object[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
            @Override
            public Class getColumnClass(int column) {
                if (column < 3) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        JTable table = new JTable(model);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title, TitledBorder.CENTER, TitledBorder.TOP));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        // add the scroll pane wrapper:
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        return scrollPane;
    }

    private Object[][] prepareVertexToVerticesModel(Graph dataSetToModel, Map<Integer, Integer> degreeToCount) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = dataSetToModel.getVertexToNeighbors();
        Set<Vertex> vertices = vertexToNeighbors.keySet();
        Iterator<Vertex> iterator = vertices.iterator();
        Object rowData[][] = new Object[vertices.size()][3];

        int i = 0;
        while (iterator.hasNext()) {
            Vertex vertex = iterator.next();
            Set<Vertex> verticesNeighborsSet = vertexToNeighbors.get(vertex);
            int degree = verticesNeighborsSet.size();
            Double afterProp = getObfuscationValue(degree, degreeToCount);
            rowData[i] = new Object[]{vertex.getName(), degree, afterProp, Arrays.toString(verticesNeighborsSet.toArray())};
            i++;
        }
        return rowData;
    }

    private Double getObfuscationValue(int degree, Map<Integer, Integer> degreeToCount) {
        Double d =  (1.0 / degreeToCount.get(degree) * 100.0f);
        return Precision.round(d, 2);
    }

    private Object[][] prepareDegreeToVerticesModel(Map<Integer, Integer> degreeToCount) {
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

    public static void main(String[] args) {
        final DemoDataCreator demoDataCreatorLocal = new DemoDataCreator();
        //create a window to display...
        JFrame jf = new JFrame("Demo Table");
        Graph dataSetToModel = demoDataCreatorLocal.generateRandomGraph();
        TableView chart = new TableView(dataSetToModel, dataSetToModel, 0, "algo1", "5");
        jf.add(chart);

        //do something when click on x
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make sure everything fits...
        jf.pack();
        //make it show up...
        jf.setVisible(true);
    }
}