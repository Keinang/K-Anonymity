package App.View;

import App.Common.Utils.DemoDataCreator;
import App.Model.Graph;
import App.Model.Vertex;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.util.Precision;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.util.*;

import static App.Controller.DataSetController.getDegreeFreq;

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
    public static final String PROBABILITY_AFTER = "after";
    public static final String PROBABILITY_BEFORE = "before";

    // view params
    private String title;

    // runtime params
    private float duration = 0;
    private int verticesAdded;
    private int edgeAdded;
    private List<List<Vertex>> partitions;
    private Map<Vertex, Double> vertexBeforeToSameDegree;
    private Map<Vertex, Double> vertexAfterToSameDegree;

    public TableView(Graph anonymizedData, Graph originalData, long before, String algorithm, String k, String dataSet) {
        // diff
        this.edgeAdded = anonymizedData.getEdges().size() - originalData.getEdges().size();
        this.verticesAdded = anonymizedData.getVertices().size() - originalData.getVertices().size();
        this.partitions = anonymizedData.getPartitions();
        // calculate obfuscation
        initObfuscation(anonymizedData, originalData);

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

    private void initObfuscation(Graph anonymizedData, Graph originalData) {
        this.vertexBeforeToSameDegree = new HashMap<>();
        this.vertexAfterToSameDegree = new HashMap<>();

        Map<Vertex, Set<Vertex>> vertexBeforeToNeighbors = originalData.getVertexToNeighbors();
        Map<Vertex, Set<Vertex>> vertexAfterToNeighbors = anonymizedData.getVertexToNeighbors();

        // iterating to count all same degrees
        for (Vertex vBefore : originalData.getVertices()) {
            int vBeforeDeg = vertexBeforeToNeighbors.get(vBefore).size();

            for (Vertex vAfter : anonymizedData.getVertices()) {
                int vAfterDeg = vertexAfterToNeighbors.get(vAfter).size();

                if (vBeforeDeg == vAfterDeg) {
                    // updating both maps
                    updateMap(vBefore, vertexBeforeToSameDegree);
                    updateMap(vAfter, vertexAfterToSameDegree);
                }
            }
        }

        // iterating to calculate frequencies
        updateTotalFrequncy(originalData.getVertices(), vertexBeforeToSameDegree);
        updateTotalFrequncy(anonymizedData.getVertices(), vertexAfterToSameDegree);
    }

    private void updateTotalFrequncy(List<Vertex> vertices, Map<Vertex, Double> map) {
        int total = vertices.size();
        for (Vertex v : vertices) {
            Double sum = map.get(v);
            Double totalValue = 0.0;
            if (sum != null) {
                totalValue = Precision.round(sum / total, 4);
            }
            map.put(v, totalValue);
        }
    }

    private void updateMap(Vertex v, Map<Vertex, Double> map) {
        Double val = map.get(v);
        if (val == null) {
            val = 1.0;
        } else {
            val++;
        }
        map.put(v, val);
    }

    private void setUI(Graph dataSetModel) {
        // create container
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        // add tables
        JScrollPane degreeToVerticesPane = addDegreeToVerticesTable(dataSetModel);
        JScrollPane vertexToVerticesPane = addVertexToVerticesTable(dataSetModel);
        JScrollPane partitionsPane = addParititonsTable();
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
    private JScrollPane addVertexToVerticesTable(Graph dataSetModel) {
        Object[][] rowData = prepareVertexToVerticesModel(dataSetModel);
        Object columnNames[] = {VERTEX, PROBABILITY_BEFORE, PROBABILITY_AFTER, VERTICES};
        return createTable(this.title, rowData, columnNames);
    }

    @NotNull
    private JScrollPane addDegreeToVerticesTable(Graph dataSetModel) {
        Object[][] rowData = prepareDegreeToVerticesModel(dataSetModel);
        Object columnNames[] = {DEGREE, VERTICES};
        return createTable(this.title, rowData, columnNames);
    }

    private JScrollPane addParititonsTable() {
        if (this.partitions != null) {
            Object[][] rowData = preparePartitionsModel(this.partitions);
            Object columnNames[] = {SIZE, PARTITIONS};
            return createTable(this.title, rowData, columnNames);
        } else {
            return new JScrollPane();
        }
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
                /*if (column == 0) {
                    return Integer.class;
                }*/
                return String.class;
            }
        };

        JTable table = new JTable(model);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title, TitledBorder.CENTER, TitledBorder.TOP));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        //table.setAutoCreateRowSorter(true);
        //table.getRowSorter().toggleSortOrder(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // add the scroll pane wrapper:
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        return scrollPane;
    }

    private Object[][] prepareVertexToVerticesModel(Graph dataSetToModel) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = dataSetToModel.getVertexToNeighbors();
        Set<Vertex> vertices = vertexToNeighbors.keySet();
        Iterator<Vertex> iterator = vertices.iterator();
        Object rowData[][] = new Object[vertices.size()][3];

        int i = 0;
        while (iterator.hasNext()) {
            Vertex vertex = iterator.next();
            Set<Vertex> verticesNeighborsSet = vertexToNeighbors.get(vertex);
            Double beforeProp = getObfuscationValue(vertex, vertexBeforeToSameDegree);
            Double afterProp = getObfuscationValue(vertex, vertexAfterToSameDegree);
            rowData[i] = new Object[]{vertex.getName(), beforeProp, afterProp, Arrays.toString(verticesNeighborsSet.toArray())};
            i++;
        }
        return rowData;
    }

    private Double getObfuscationValue(Vertex vertex, Map<Vertex, Double> map) {
        Double val = map.get(vertex);
        if (val != null) {
            return val;
        } else {
            return 0.0;
        }
    }

    private Object[][] prepareDegreeToVerticesModel(Graph dataSetToModel) {
        // get data set model:
        Map<Vertex, Set<Vertex>> vertexToNeighbors = dataSetToModel.getVertexToNeighbors();
        Collection<Set<Vertex>> vertexDegreesValues = vertexToNeighbors.values();
        ArrayList<Integer> allDegreesWithDuplicates = new ArrayList<>();
        Iterator<Set<Vertex>> vertexDegreesValuesIter = vertexDegreesValues.iterator();
        while (vertexDegreesValuesIter.hasNext()) {
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

    public static void main(String[] args) {
        final DemoDataCreator demoDataCreatorLocal = new DemoDataCreator();
        //create a window to display...
        JFrame jf = new JFrame("Demo Table");
        Graph dataSetToModel = demoDataCreatorLocal.generateRandomGraph();
        TableView chart = new TableView(dataSetToModel, dataSetToModel, 0, "algo1", "5", "dataset1");
        jf.add(chart);

        //do something when click on x
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make sure everything fits...
        jf.pack();
        //make it show up...
        jf.setVisible(true);
    }
}