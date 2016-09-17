package App.View;

import App.Utils.DemoDataCreator;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.DefaultToolTipFunction;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by Keinan.Gilad on 9/17/2016.
 */
public class jungGraphPanel extends JPanel {
    public static final String VERTICE = "Vertice";
    public static final String DEGREE = "Degree";
    private Graph m_graph;
    private Layout mVisualizer;
    private Renderer mRenderer;
    private VisualizationViewer mVizViewer;
    private DefaultModalGraphMouse m_graphmouse;

    public jungGraphPanel() {
        m_graph = new SparseGraph();//UndirectedSparseGraph/SparseGraph
        mVisualizer = new FRLayout(m_graph); // CircleLayout/FRLayout
        mRenderer = new PluggableRenderer();
        mVizViewer = new VisualizationViewer(mVisualizer, mRenderer);
        mVizViewer.setBackground(Color.WHITE);
        //m_graphmouse = new DefaultModalGraphMouse();
        //mVizViewer.setGraphMouse(m_graphmouse);
        /*mVizViewer.setToolTipFunction(new DefaultToolTipFunction() {
            public String getToolTipText(Vertex v) {
                return v.toString();
            }

            public String getToolTipText(Edge edge) {
                Set incidentVertices = edge.getIncidentVertices();
                Iterator<Vertex> incidentVerticesIterator = incidentVertices.iterator();
                StringBuilder tooltip = new StringBuilder();
                while (incidentVerticesIterator.hasNext()) {
                    tooltip.append(incidentVerticesIterator.next());
                    tooltip.append(" -- ");
                }

                return tooltip.toString();
            }
        });*/

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1000, 1000));
        this.setSize(new Dimension(1000, 1000));
        add(mVizViewer);
    }

    public void addTable() {
        Object columnNames[] = {VERTICE, DEGREE};
        Object rowData[][] = initTableMatrix();
        JXTable table = new JXTable(rowData, columnNames);
        table.setVisibleColumnCount(2);
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        JScrollPane tableContainer = new JScrollPane(table);
        this.add(tableContainer, BorderLayout.LINE_END);
        table.packAll();
    }

    private Object[][] initTableMatrix() {
        Object[][] rows = new Object[1][];
        Set vertices = m_graph.getVertices();
        if (vertices != null) {
            rows = new Object[vertices.size()][2];
            Iterator<Vertex> verticesKeysIter = vertices.iterator();
            int i = 0;
            while (verticesKeysIter.hasNext()) {
                Vertex verticeKey = verticesKeysIter.next();

                rows[i][0] = String.format("%s", verticeKey);
                rows[i][1] = verticeKey.getIncidentEdges().size();
                i++;
            }
        }
        return rows;
    }

    public void addVertexes(Map<String, Vertex> vertices) {
        Collection<Vertex> verticesValues = vertices.values();
        Iterator<Vertex> verticesIter = verticesValues.iterator();
        while (verticesIter.hasNext()) {
            m_graph.addVertex(verticesIter.next());
        }
    }

    public void addEdges(List<Edge> edges) {
        for (int i = 0; i < edges.size(); i++) {
            m_graph.addEdge(edges.get(i));
        }

        mVisualizer.restart();
        mVizViewer.revalidate();
        mVizViewer.repaint();
    }

    public static void main(String[] args) {
        final DemoDataCreator demoDataCreatorLocal = new DemoDataCreator();

        //create a window to display...
        JFrame jf = new JFrame("basic graph");

        // create the graph:
        jungGraphPanel jungGraphPanel = new jungGraphPanel();
        Map<String, Vertex> vertices = demoDataCreatorLocal.createDemoVertices();
        jungGraphPanel.addVertexes(vertices);
        List<Edge> edges = demoDataCreatorLocal.createDemoEdges(vertices);
        jungGraphPanel.addEdges(edges);
        jungGraphPanel.addTable();
        jf.getContentPane().add(jungGraphPanel);

        //set some size
        jf.setSize(700, 500);

        //do something when click on x
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //make sure everything fits...
        jf.pack();
        //make it show up...
        jf.setVisible(true);
    }

}
