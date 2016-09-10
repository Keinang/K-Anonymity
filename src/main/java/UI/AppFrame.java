package UI;

import Model.Edge;
import Model.Vertice;

import javax.swing.*;
import java.util.List;

import static Service.DemoDataCreator.createDemoEdges;
import static Service.DemoDataCreator.createDemoVertices;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */

public class AppFrame extends JFrame {

    private static final String FRAME_TITLE = "K-Anonymity Algorithm Simulator";
    private static final String DEFAULT_VALUE = "N/A";

    public AppFrame() {
        initUIComponents();
    }

    @SuppressWarnings("ALL")
    private void initUIComponents() {
        // set layout properties
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        // init controls:
        JLabel algorithmsLabel = new JLabel("Algorithms");
        JLabel chooseKLabel = new JLabel("Choose K");
        JLabel chooseDataSetLabel = new JLabel("Data Sets");

        // radio buttons for algorithms
        ButtonGroup buttonGroupAlgorithms = new ButtonGroup();
        JRadioButton radioButtonAlgorithms1 = new JRadioButton("K-Degree");
        radioButtonAlgorithms1.setSelected(true);
        JRadioButton radioButtonAlgorithms2 = new JRadioButton("K-Neighborhood");
        buttonGroupAlgorithms.add(radioButtonAlgorithms1);
        buttonGroupAlgorithms.add(radioButtonAlgorithms2);

        // radio buttons for choosing K
        ButtonGroup buttonGroupForK = new ButtonGroup();
        JRadioButton radioButtonForK1 = new JRadioButton("5");
        radioButtonForK1.setSelected(true);
        JRadioButton radioButtonForK2 = new JRadioButton("10");
        JRadioButton radioButtonForK3 = new JRadioButton("15");
        JRadioButton radioButtonForK4 = new JRadioButton("20");
        buttonGroupForK.add(radioButtonForK1);
        buttonGroupForK.add(radioButtonForK2);
        buttonGroupForK.add(radioButtonForK3);
        buttonGroupForK.add(radioButtonForK4);

        // radio buttons for choosing Data Sets
        ButtonGroup buttonGroupForDataSets = new ButtonGroup();
        JRadioButton radioButtonForDataSets1 = new JRadioButton("DataSet-1");
        radioButtonForDataSets1.setSelected(true);
        JRadioButton radioButtonForDataSets2 = new JRadioButton("DataSet-2");
        buttonGroupForDataSets.add(radioButtonForDataSets1);
        buttonGroupForDataSets.add(radioButtonForDataSets2);

        // progress bar for loading the algorithm
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setBounds(0, 0, 100, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        // values for after running the algorithm
        JLabel durationLabel = new JLabel("Duration");
        JLabel durationLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel beforeVerticesLabel = new JLabel("Vertices total");
        JLabel beforeVerticesLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel beforeEdgesLabel = new JLabel("Edges total");
        JLabel beforeEdgesLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel afterVerticesLabel = new JLabel("Vertices total");
        JLabel afterVerticesLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel afterVerticesAddedLabel = new JLabel("Vertices added");
        JLabel afterVerticesAddedLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel afterVerticesRemovedLabel = new JLabel("Vertices removed");
        JLabel afterVerticesRemovedLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel afterEdgesLabel = new JLabel("Edges total");
        JLabel afterEdgesLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel afterEdgesAddedLabel = new JLabel("Edges added");
        JLabel afterEdgesAddedLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel afterEdgesRemovedLabel = new JLabel("Edges removed");
        JLabel afterEdgesRemovedLabelValue = new JLabel(DEFAULT_VALUE);

        JLabel afterObfuscationLeveldLabel = new JLabel("Obfuscation Level");
        JLabel afterObfuscationLeveldLabelValue = new JLabel(DEFAULT_VALUE);

        List<Vertice> vertices = createDemoVertices();
        List<Edge> edges = createDemoEdges(vertices);
        GraphPanel beforeGraph = new GraphPanel(vertices, edges);
        GraphPanel afterGraph = new GraphPanel(null, null);

        JSeparator sp1 = new JSeparator();
        JSeparator sp2 = new JSeparator();

        // setting the Layout
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(algorithmsLabel)
                                .addComponent(radioButtonAlgorithms1)
                                .addComponent(radioButtonAlgorithms2)
                                .addComponent(durationLabel)
                                .addComponent(beforeVerticesLabel)
                                .addComponent(beforeEdgesLabel)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(chooseKLabel)
                                .addComponent(radioButtonForK1)
                                .addComponent(radioButtonForK2)
                                .addComponent(radioButtonForK3)
                                .addComponent(radioButtonForK4)

                                .addComponent(durationLabelValue)
                                .addComponent(beforeVerticesLabelValue)
                                .addComponent(beforeEdgesLabelValue)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(chooseDataSetLabel)
                                .addComponent(radioButtonForDataSets1)
                                .addComponent(radioButtonForDataSets2)

                                .addComponent(afterVerticesLabel)
                                .addComponent(afterVerticesAddedLabel)
                                .addComponent(afterVerticesRemovedLabel)
                                .addComponent(afterEdgesLabel)
                                .addComponent(afterEdgesAddedLabel)
                                .addComponent(afterEdgesRemovedLabel)
                                .addComponent(afterObfuscationLeveldLabel)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(afterVerticesLabelValue)
                                .addComponent(afterVerticesAddedLabelValue)
                                .addComponent(afterVerticesRemovedLabelValue)
                                .addComponent(afterEdgesLabelValue)
                                .addComponent(afterEdgesAddedLabelValue)
                                .addComponent(afterEdgesRemovedLabelValue)
                                .addComponent(afterObfuscationLeveldLabelValue)
                        )
                )
                .addComponent(beforeGraph)
                .addComponent(progressBar)
                .addComponent(sp1)
                .addComponent(sp2)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(algorithmsLabel)
                        .addComponent(chooseKLabel)
                        .addComponent(chooseDataSetLabel)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(radioButtonAlgorithms1)
                        .addComponent(radioButtonForK1)
                        .addComponent(radioButtonForDataSets1)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(radioButtonAlgorithms2)
                        .addComponent(radioButtonForK2)
                        .addComponent(radioButtonForDataSets2)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(radioButtonForK3)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(radioButtonForK4)
                )
                .addComponent(sp1)
                // Panel 2:
                .addComponent(progressBar)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(durationLabel)
                        .addComponent(durationLabelValue)

                        .addComponent(afterVerticesLabel)
                        .addComponent(afterVerticesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(beforeVerticesLabel)
                        .addComponent(beforeVerticesLabelValue)

                        .addComponent(afterVerticesAddedLabel)
                        .addComponent(afterVerticesAddedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(beforeEdgesLabel)
                        .addComponent(beforeEdgesLabelValue)

                        .addComponent(afterVerticesRemovedLabel)
                        .addComponent(afterVerticesRemovedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(afterEdgesLabel)
                        .addComponent(afterEdgesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(afterEdgesAddedLabel)
                        .addComponent(afterEdgesAddedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(afterEdgesRemovedLabel)
                        .addComponent(afterEdgesRemovedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(afterObfuscationLeveldLabel)
                        .addComponent(afterObfuscationLeveldLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(afterObfuscationLeveldLabel)
                        .addComponent(afterObfuscationLeveldLabelValue)
                )
                .addComponent(sp2)
                // graphs
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(beforeGraph)
                )
        );

        pack();
        setSize(800, 800);
    }
}