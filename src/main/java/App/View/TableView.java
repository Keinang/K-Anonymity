package App.View;

import App.Model.Graph;
import App.Model.Vertex;
import App.Utils.DemoDataCreator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
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

    public TableView(Graph dataSetToModel) {
        setUI(dataSetToModel);
    }

    private void setUI(Graph dataSetModel) {
        // create container
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        // add tables
        JScrollPane degreeToVerticesPane = addDegreeToVerticesTable(dataSetModel);
        JScrollPane vertexToVerticesPane = addVertexToVerticesTable(dataSetModel);
        JPanel indicationsPanel = addIndicationPanel(dataSetModel);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(degreeToVerticesPane)
                .addComponent(vertexToVerticesPane)
                .addComponent(indicationsPanel)
        );

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(degreeToVerticesPane)
                .addComponent(vertexToVerticesPane)
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
        JLabel totalVerticesLabelValue = new JLabel(String.valueOf(dataSetModel.getVertices().size()));

        int totalEdges = dataSetModel.getEdges().size();
        JLabel totalEdgesLabel = new JLabel(TOTAL_EDGES);
        JLabel totalEdgesLabelValue = new JLabel(String.valueOf(totalEdges));

        // Values after running the algorithm
        JLabel durationLabel = new JLabel("Duration");
        JLabel durationLabelValue = new JLabel(String.format("%ssec", dataSetModel.getDuration() / 1000.0f));

        Integer edgeAdded = dataSetModel.getEdgeAdded();
        JLabel edgesAddedLabel = new JLabel("Edges added (%)");
        JLabel edgesAddedLabelValue = new JLabel(String.format("%s (%.2f%s)", edgeAdded, ((edgeAdded * 100.0f / (totalEdges - edgeAdded))), "%"));

        JLabel obfuscationLeveldLabel = new JLabel("Obfuscation Level");
        JLabel obfuscationLeveldLabelValue = new JLabel();

        // set components in layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalVerticesLabel)
                        .addComponent(totalEdgesLabel)
                        .addComponent(durationLabel)
                        .addComponent(edgesAddedLabel)
                        .addComponent(obfuscationLeveldLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalVerticesLabelValue)
                        .addComponent(totalEdgesLabelValue)
                        .addComponent(durationLabelValue)
                        .addComponent(edgesAddedLabelValue)
                        .addComponent(obfuscationLeveldLabelValue)
                )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalVerticesLabel)
                        .addComponent(totalVerticesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(totalEdgesLabel)
                        .addComponent(totalEdgesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(durationLabel)
                        .addComponent(durationLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(edgesAddedLabel)
                        .addComponent(edgesAddedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(obfuscationLeveldLabel)
                        .addComponent(obfuscationLeveldLabelValue)
                )

        );
        return indicationPanel;
    }

    private JScrollPane addVertexToVerticesTable(Graph dataSetModel) {
        Object[][] rowData = prepareVertexToVerticesModel(dataSetModel);
        Object columnNames[] = {VERTEX, VERTICES};
        return createTable(dataSetModel.getTitle(), rowData, columnNames);
    }

    @NotNull
    private JScrollPane addDegreeToVerticesTable(Graph dataSetModel) {
        Object[][] rowData = prepareDegreeToVerticesModel(dataSetModel);
        Object columnNames[] = {DEGREE, VERTICES};
        return createTable(dataSetModel.getTitle(), rowData, columnNames);
    }

    @NotNull
    private JScrollPane createTable(String title, final Object[][] rowData, Object[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
            @Override
            public Class getColumnClass(int column) {
                if (column == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        JTable table = new JTable(model) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title, TitledBorder.CENTER, TitledBorder.TOP));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().toggleSortOrder(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // add the scroll pane wrapper:
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVisible(true);
        return scrollPane;
    }

    private Object[][] prepareVertexToVerticesModel(Graph dataSetToModel) {
        Map<Vertex, Set<Vertex>> vertexToNeighbors = dataSetToModel.getVertexToNeighbors();
        Set<Vertex> vertices = vertexToNeighbors.keySet();
        Iterator<Vertex> iterator = vertices.iterator();
        Object rowData[][] = new Object[vertices.size()][2];

        int i = 0;
        while (iterator.hasNext()) {
            Vertex vertex = iterator.next();
            Set<Vertex> verticesNeighborsSet = vertexToNeighbors.get(vertex);
            rowData[i] = new Object[]{Integer.valueOf(vertex.getName()), Arrays.toString(verticesNeighborsSet.toArray())};
            i++;
        }
        return rowData;
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
        Graph dataSetToModel = demoDataCreatorLocal.getDataSetToModel();
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