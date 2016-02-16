package edu.ksu.cis.macr.aasis.simulators.view;

import edu.ksu.cis.macr.aasis.simulator.clock.Clock;
import edu.ksu.cis.macr.aasis.simulators.model.SimulatorTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * The SmartMeterSimulatorView shows the current sensor data for the SelfOrganization simulation.
 */
public class SimulatorView extends JFrame {
    /**
     * The default preferred size of the frame.
     */
    private static final Dimension DEFAULT_PREFERRED_SIZE = new Dimension(450,
            775);
    private static final Logger LOG = LoggerFactory.getLogger(SimulatorView.class);
//    static {
//        final LookAndFeelInfo[] installedLookAndFeels = UIManager
//                .getInstalledLookAndFeels();
//        try {
//            for (final LookAndFeelInfo info : installedLookAndFeels) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            /* should not occur */
//            LOG.debug("Error: sensor frame instantiation error. ");
//        } catch (final UnsupportedLookAndFeelException e) {
//            /* use default look and feel */
//            LOG.debug("Using default look and feel. ");
//        }
//    }
    /**
     * Default serial version ID.
     */
    private static final long serialVersionUID = 1L;
    private boolean stepMode = false;
    private String[] columnNames = {"RowID", "AgentAbbrev",
            "Phase A - P (load)",
            "Phase A - Q (load)",
            "Phase B - P (load)",
            "Phase B - Q (load)",
            "Phase C - P (load)",
            "Phase C - Q (load)",
            "P (generation)",
            "Q (generation)",
            "Voltage Phase A",
            "Voltage Phase B",
            "Voltage Phase C"};
    private long timeSliceNumber = 0;
    private HashMap<String, Component> componentMap;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JScrollPane jScrollPane3;
    private JTable jTableNow;
    private JLabel lblPurpose;
    private JLabel lblSimTime;
    private JPanel sidePanel;
    private JPanel topPanel;
    private JLabel txtSimTime;
    private JTextField txtSimulationTime;
    private JTextField txtTimeSliceNumber;


    /**
     * Creates new form
     */
    public SimulatorView(SimulatorTableModel model) {
        super("Sensor View");
        initComponents();
        jTableNow.setModel(model);
        createComponentMap();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        topPanel = new JPanel();
        txtSimTime = new JLabel();
        lblSimTime = new JLabel();
        lblPurpose = new JLabel();
        txtSimulationTime = new JTextField();
        txtTimeSliceNumber = new JTextField();
        sidePanel = new JPanel();
        jScrollPane3 = new JScrollPane();
        jTableNow = new JTable();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        topPanel.setBackground(new Color(204, 204, 255));

        txtSimTime.setLabelFor(lblSimTime);
        txtSimTime.setName("simTime"); // NOI18N

        lblSimTime.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        lblSimTime.setText("Simulation Time:");

        lblPurpose.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        lblPurpose.setText("Simulated Sensor Values");

        txtSimulationTime.setEditable(false);
        txtSimulationTime.setBackground(new Color(204, 204, 255));
        txtSimulationTime.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        txtSimulationTime.setText("jTextField2");
        txtSimulationTime.setBorder(null);
//      txtSimulationTime.addActionListener(new java.awt.event.ActionListener() {
//        @Override
//        public void actionPerformed(java.awt.event.ActionEvent evt) {
//          txtSimulationTimeActionPerformed(evt);
//        }
//      });
        // replace with method listener
        txtSimulationTime.addActionListener(this::txtSimulationTimeActionPerformed);

        txtTimeSliceNumber.setBackground(new Color(204, 204, 255));
        txtTimeSliceNumber.setHorizontalAlignment(JTextField.RIGHT);
        txtTimeSliceNumber.setText("jTextField1");
        txtTimeSliceNumber.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTimeSliceNumber.setMaximumSize(new Dimension(333, 333));
        txtTimeSliceNumber.setName(""); // NOI18N
        //  txtTimeSliceNumber.setNextFocusableComponent(txtSimulationTime); // deprecated
        txtTimeSliceNumber.setSelectionStart(0);

        GroupLayout topPanelLayout = new GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
                topPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(topPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblPurpose)
                                .addGap(18, 18, 18)
                                .addComponent(txtTimeSliceNumber, GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                                .addGap(31, 31, 31)
                                .addComponent(lblSimTime)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSimulationTime, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txtSimTime, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(39, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
                topPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(topPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(topPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(topPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblSimTime)
                                                .addComponent(txtSimulationTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(topPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblPurpose)
                                                .addComponent(txtTimeSliceNumber, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                        .addComponent(txtSimTime))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtSimTime.getAccessibleContext().setAccessibleDescription("simTime");

        sidePanel.setBorder(BorderFactory.createTitledBorder("Current Sensor Values"));
        sidePanel.setMinimumSize(new Dimension(45, 45));

        jTableNow.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13"
                }
        ));
        jScrollPane3.setViewportView(jTableNow);

        GroupLayout sidePanelLayout = new GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
                sidePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3)
        );
        sidePanelLayout.setVerticalGroup(
                sidePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(topPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sidePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(topPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sidePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //  AUTO CODE BELOW.................................................................
    private void createComponentMap() {
        componentMap = new HashMap<>();
        Component[] components = this.getContentPane().getComponents();
        for (Component component : components) {
            componentMap.put(component.getName(), component);
        }
    }

    public long getTimeSliceNumber() {
        return this.timeSliceNumber;
    }

    public void setTimeSliceNumber(long timeSlice) {
        this.timeSliceNumber = timeSlice;
        String strTimeSlice = Long.toString(timeSlice);
        // LOG.debug("timeSlice = {}, i.e. {}", timeSlice, strTimeSlice);
        this.txtTimeSliceNumber.setText(strTimeSlice);
    }

    public Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return componentMap.get(name);
        } else {
            return null;
        }
    }

    public void intializeAndDisplay(final int timeSlicesElapsedSinceStart, final GregorianCalendar simulationStartTime) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("SelfOrganization Simulation - System Sensor Values");
        setSimulationTime(simulationStartTime);
        setTimeSliceNumber(0);
        int pixelsFromTop = 600;
        int pixelsFromLeft = 0;
        setLocation(pixelsFromLeft, pixelsFromTop);
        pack();  // call pack right before setVisible
        setVisible(true);
        startListener();
    }

    public void setSimulationTime(GregorianCalendar cal) {
        Date creationDate = cal.getTime();
        SimpleDateFormat date_format = new SimpleDateFormat("EEE, MMM d yyyy h:mm:ss a");
        this.txtSimulationTime.setText(date_format.format(creationDate));
    }

    public void startListener() {
        Thread t = new Thread(() -> {
            while (true) {
                updateSensorsView();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error("Error: InterruptedException in ConnectionView listener. {}", e.getMessage());
                }
            }
        });

        t.start();
    }

    public void updateSensorsView() {
        //Objects.requireNonNull(ec, "ERROR - LocalMessageView.updateVisualizationPanel() requires EC");
        //if (ec.getCapability(IPowerCommunicationCapability.class) == null) {
        //    return;
        //}
        // TODO:  The integer we want isn't actually the elapsed milliseconds (from the run manager)
        // TODO:  We want to see the count of the number of turns (see each ipds's organization getTurns() method.

        //int numElapsed = (int) this.getAgent().createOrganization().getTurns();
        setTimeSliceNumber(Clock.getTimeSlicesElapsedSinceStart());
        this.setTimeSliceNumber(timeSliceNumber);
    }

    public void intializeAndDisplay(final int timeSlicesElapsedSinceStart, final GregorianCalendar simulationStartTime, int pixelsFromLeft, int pixelsFromTop) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("SelfOrganization Simulation - System Sensor Values");
        setSimulationTime(simulationStartTime);
        setTimeSliceNumber(0);
        setLocation(pixelsFromLeft, pixelsFromTop);
        pack();  // call pack right before setVisible
        setVisible(true);
        startListener();
    }

    private void txtSimulationTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSimulationTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSimulationTimeActionPerformed

    public void updateData(final SimulatorTableModel model) {
        jTableNow.setModel(model);
    }

    // End of variables declaration//GEN-END:variables
}
