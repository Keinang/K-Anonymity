package View;

import Model.*;
import Controller.ExecuteController;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static Data.DemoDataCreator.createDemoEdges;
import static Data.DemoDataCreator.createDemoVertices;

/**
 * Created by Keinan.Gilad on 9/10/2016.
 */

public class AppFrame extends JFrame {
    private static Logger logger = Logger.getLogger(AppFrame.class);
    private static final String FRAME_TITLE = "K-Anonymity Algorithm Simulator";
    private static final String DEFAULT_VALUE = "N/A";
    private ExecuteController executeController;
    private ButtonGroup buttonGroupAlgorithms;
    private ButtonGroup buttonGroupForK;
    private ButtonGroup buttonGroupForDataSets;

    public AppFrame() {
        initUIComponents();
        executeController = new ExecuteController();
    }

    @SuppressWarnings("ALL")
    private void initUIComponents() {
        // set layout properties
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // init controls:
        JLabel algorithmsLabel = new JLabel("Algorithms");
        JLabel chooseKLabel = new JLabel("Choose K");
        JLabel chooseDataSetLabel = new JLabel("Data Sets");

        // radio buttons for algorithms
        buttonGroupAlgorithms = new ButtonGroup();
        JRadioButton radioButtonAlgorithms1 = new JRadioButton("K-Degree");
        radioButtonAlgorithms1.setActionCommand(AlgoType.KDegree.toString());
        radioButtonAlgorithms1.setSelected(true);
        JRadioButton radioButtonAlgorithms2 = new JRadioButton("K-Neighborhood");
        radioButtonAlgorithms2.setActionCommand(AlgoType.KNeighborhood.toString());
        buttonGroupAlgorithms.add(radioButtonAlgorithms1);
        buttonGroupAlgorithms.add(radioButtonAlgorithms2);

        // radio buttons for choosing K
        buttonGroupForK = new ButtonGroup();
        JRadioButton radioButtonForK1 = new JRadioButton("5");
        radioButtonForK1.setActionCommand("5");
        radioButtonForK1.setSelected(true);
        JRadioButton radioButtonForK2 = new JRadioButton("10");
        radioButtonForK2.setActionCommand("10");
        JRadioButton radioButtonForK3 = new JRadioButton("15");
        radioButtonForK3.setActionCommand("15");
        JRadioButton radioButtonForK4 = new JRadioButton("20");
        radioButtonForK4.setActionCommand("20");
        buttonGroupForK.add(radioButtonForK1);
        buttonGroupForK.add(radioButtonForK2);
        buttonGroupForK.add(radioButtonForK3);
        buttonGroupForK.add(radioButtonForK4);

        // radio buttons for choosing Data Sets
        buttonGroupForDataSets = new ButtonGroup();
        JRadioButton radioButtonForDataSets1 = new JRadioButton("DataSet-1");
        radioButtonForDataSets1.setActionCommand(DataSetType.DS1.toString());
        radioButtonForDataSets1.setSelected(true);
        JRadioButton radioButtonForDataSets2 = new JRadioButton("DataSet-2");
        radioButtonForDataSets2.setActionCommand(DataSetType.DS2.toString());
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
        List<Vertice> anonymizedVertices = createDemoVertices();
        List<Edge> anonymizedEdges = createDemoEdges(anonymizedVertices);
        GraphPanel afterGraph = new GraphPanel(anonymizedVertices, anonymizedEdges);

        JSeparator sp1 = new JSeparator();
        JSeparator sp2 = new JSeparator();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Original Graph", beforeGraph);
        tabbedPane.add("Anonymized Graph", afterGraph);

        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String algoTypeCommand = buttonGroupAlgorithms.getSelection().getActionCommand();
                String kTypeCommand = buttonGroupForK.getSelection().getActionCommand();
                String dataSetCommand = buttonGroupForDataSets.getSelection().getActionCommand();
                logger.info(String.format("executeButton called for {Algorithm=%s, K=%s, DataSet=%s}", algoTypeCommand,  kTypeCommand, dataSetCommand));
                InputContext inputContext = new InputContext();
                inputContext.setAlgoTypeCommand(algoTypeCommand);
                inputContext.setKTypeCommand(kTypeCommand);
                inputContext.setDataSetCommand(dataSetCommand);
                executeController.execute(inputContext);
            }
        });

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
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(executeButton)
                                .addComponent(progressBar)
                        )

                )
                .addComponent(tabbedPane)
                .addComponent(sp1)
                .addComponent(sp2)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(algorithmsLabel)
                        .addComponent(chooseKLabel)
                        .addComponent(chooseDataSetLabel)
                        .addComponent(executeButton)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(radioButtonAlgorithms1)
                        .addComponent(radioButtonForK1)
                        .addComponent(radioButtonForDataSets1)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(radioButtonAlgorithms2)
                        .addComponent(radioButtonForK2)
                        .addComponent(radioButtonForDataSets2)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(radioButtonForK3)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(radioButtonForK4)
                )
                .addComponent(sp1)
                // Panel 2:
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(progressBar)
                        .addComponent(durationLabel)
                        .addComponent(durationLabelValue)

                        .addComponent(afterVerticesLabel)
                        .addComponent(afterVerticesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(beforeVerticesLabel)
                        .addComponent(beforeVerticesLabelValue)

                        .addComponent(afterVerticesAddedLabel)
                        .addComponent(afterVerticesAddedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(beforeEdgesLabel)
                        .addComponent(beforeEdgesLabelValue)

                        .addComponent(afterVerticesRemovedLabel)
                        .addComponent(afterVerticesRemovedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(afterEdgesLabel)
                        .addComponent(afterEdgesLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(afterEdgesAddedLabel)
                        .addComponent(afterEdgesAddedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(afterEdgesRemovedLabel)
                        .addComponent(afterEdgesRemovedLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(afterObfuscationLeveldLabel)
                        .addComponent(afterObfuscationLeveldLabelValue)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(afterObfuscationLeveldLabel)
                        .addComponent(afterObfuscationLeveldLabelValue)
                )
                .addComponent(sp2)
                // graphs
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(tabbedPane)
                )
        );

        pack();
        //setSize(800, 800);
    }
}