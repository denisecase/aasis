/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ksu.cis.macr.aasis.agent.views;

/**
 * @author gregm
 */
public class SmartMeterSensorsFrame extends javax.swing.JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static double PALoad = 0;
    private static double PBLoad = 0;
    private static double PCLoad = 0;
    private static double PGen = 0;
    private static double QALoad = 0;
    private static double QBLoad = 0;
    private static double QCLoad = 0;
    private static double QGen = 0;
    private static String title;
    //final static SmartMeterCapabilityPanel smart = new SmartMeterCapabilityPanel();
    //   	return smart;
    //   }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;


    /**
     * Creates new form SmartMeterSensorsFrame
     */
    public SmartMeterSensorsFrame() {
        //    initComponents();
    }

    public static void UpdateView() {
  /*
      smart.setPGen(PGen);
    	smart.setQGen(QGen);
    	smart.setPALoad(PALoad);
    	smart.setQALoad(QALoad);
    	smart.setPBLoad(PBLoad);
    	smart.setQBLoad(QBLoad);
    	smart.setPCLoad(PCLoad);
    	smart.setQCLoad(QCLoad);
    	*/
    }

    public static void setPALoad(double paLoad) {
        PALoad = paLoad;
    }

    public static void setPBLoad(double pbLoad) {
        PBLoad = pbLoad;
    }

    public static void setPCLoad(double pcLoad) {
        PCLoad = pcLoad;
    }

    public static void setPGen(double genKW) {
        PGen = genKW;
    }

    public static void setQALoad(double qaLoad) {
        QALoad = qaLoad;
    }

    public static void setQBLoad(double qbLoad) {
        QBLoad = qbLoad;
    }

    public static void setQCLoad(double qcLoad) {
        QCLoad = qcLoad;
    }

    public static void setQGen(double qGen) {
        QGen = qGen;
    }

    //  public SmartMeterCapabilityPanel setPanel(){

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
     /*   try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SmartMeterSensorsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SmartMeterSensorsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SmartMeterSensorsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SmartMeterSensorsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        // java.awt.EventQueue.invokeLater(new Runnable() {
        //   public void run() {
        //     new SmartMeterSensorsFrame().setVisible(true);
        // }
        //});
    }

    @Override
    public void setTitle(String name) {
        title = name;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
/*
        jScrollPane1 = new javax.swing.JScrollPane();
        panel1 = new java.awt.Panel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();*/
    }// </editor-fold>//GEN-END:initComponents

    public void startGrapher() {
        /*
        EventQueue.invokeLater(new Runnable() {

	        @Override
	        public void run(){
	        	smart.setTitle(title);
	            smart.pack();
	            RefineryUtilities.centerFrameOnScreen(smart);
	            smart.setVisible(true);
	            smart.start();
	            JPanel panel = smart.getPanel();
	            panel.updateUI();
	            panel.setVisible(true);
	        }
	    });
    */
    }

    // End of variables declaration//GEN-END:variables
}
