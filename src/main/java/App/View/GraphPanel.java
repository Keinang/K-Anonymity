package App.View;

import App.Model.Edge;
import App.Model.Vertice;
import App.Utils.DemoDataCreator;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by Keinan.Gilad on 9/10/2016.
 */
public class GraphPanel extends JPanel {
    private static Logger logger = Logger.getLogger(GraphPanel.class);
    private static int POINT_SIZE = 5;
    private static Color VERTICES_COLOR = Color.blue;
    private static Color EDGE_COLOR = Color.black;

    private HashMap<String, Vertice> vertices = null;
    private List<Edge> edges = null;

    @Autowired
    private DemoDataCreator demoDataCreator;

    protected GraphPanel() {
    }

    protected GraphPanel(HashMap<String, Vertice> vertices, List<Edge> edges) {
        super();
        // set model
        this.vertices = vertices;
        this.edges = edges;

        // general properties
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new BorderLayout());

        // set table
        if (this.edges != null && this.vertices != null) {
            addTable();
        }

        this.setMinimumSize(new Dimension(1200, 1200));
    }

    private void addTable() {
        Object columnNames[] = {"Vertice", "Degree"};
        Object rowData[][] = initTableMatrix();
        JXTable table = new JXTable(rowData, columnNames);
        table.setVisibleColumnCount(2);
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        JScrollPane tableContainer = new JScrollPane(table);
        this.add(tableContainer, BorderLayout.LINE_END);
    }

    private Object[][] initTableMatrix() {
        Object[][] rows = new Object[1][];

        if (vertices != null) {
            rows = new Object[this.vertices.size()][2];
            Set<String> verticesKeys = this.vertices.keySet();
            Iterator<String> verticesKeysIter = verticesKeys.iterator();
            int i = 0;
            while (verticesKeysIter.hasNext()) {
                String verticeKey = verticesKeysIter.next();
                Vertice vertice = vertices.get(verticeKey);

                rows[i][0] = String.format("%s (%s, %s)", vertice.getName(), vertice.x, vertice.y);
                rows[i][1] = countVerticeInEdge(vertice.x, vertice.y);
                i++;
            }
        }
        return rows;
    }

    private int countVerticeInEdge(int x, int y) {
        int count = 0;
        if (edges != null) {
            for (Edge e : edges) {
                if ((e.getX().x == x && e.getX().y == y) || (e.getY().x == x && e.getY().y == y)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (vertices != null) {
            g.setColor(VERTICES_COLOR);

            for (Vertice p : vertices.values()) {
                g.drawOval(p.x, p.y, POINT_SIZE, POINT_SIZE);
                g.drawString(p.getName(), p.x, p.y);
            }
        }

        if (edges != null) {
            g.setColor(EDGE_COLOR);
            for (Edge e : edges) {
                g.drawLine(e.getX().x, e.getX().y, e.getY().x, e.getY().y);
            }
        }
    }

    public static void main(String[] args) {
        final DemoDataCreator demoDataCreatorLocal = new DemoDataCreator();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                HashMap<String, Vertice> vertices = demoDataCreatorLocal.createDemoVertices();
                List<Edge> edges = demoDataCreatorLocal.createDemoEdges(vertices);
                GraphPanel grpahPanel = new GraphPanel(vertices, edges);
                grpahPanel.setVisible(true);
                frame.add(grpahPanel);
                frame.setVisible(true);
                frame.setSize(new Dimension(750, 650));
            }
        });
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(750, 650);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(750, 650);
    }
}
