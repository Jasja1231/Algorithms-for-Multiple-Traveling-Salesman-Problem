/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author Krzysztof
 */
public class OptionsPanel extends javax.swing.JPanel {

    MainView parentView; 
    AlgorithmChoicesPanel algPanel;
    InputDataPanel inputDataPanel;
    JScrollPane algOptionsScrollPane;
    JLabel algorithmChooseLabel;
    
    /**
     * Creates new form OptionsPanel
     */
    public OptionsPanel(MainView pV) {
        initComponents();
         this.parentView = pV; 
        init();
        
      
        this.setPreferredSize(new Dimension(260,150));
    }

    private void init(){
        algorithmChooseLabel = new JLabel("Choose algorithms : ");
        algPanel = new AlgorithmChoicesPanel(parentView);
        inputDataPanel = new InputDataPanel(parentView);
        this.contentjPanel.add(inputDataPanel);
        this.contentjPanel.add(algorithmChooseLabel);
        this.contentjPanel.add(algPanel); 
    }
    
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        contentjPanel = new javax.swing.JPanel();
        ButtonjPanel = new javax.swing.JPanel();
        computeButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 204, 204)));
        setLayout(new java.awt.BorderLayout());

        contentjPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentjPanel.setLayout(new javax.swing.BoxLayout(contentjPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(contentjPanel);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        ButtonjPanel.setBackground(new java.awt.Color(0, 204, 204));
        ButtonjPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 10, 10, 10, new java.awt.Color(0, 204, 204)));
        ButtonjPanel.setLayout(new java.awt.GridLayout(0, 2, 20, 40));

        computeButton.setText("Compute");
        computeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computeButtonActionPerformed(evt);
            }
        });
        ButtonjPanel.add(computeButton);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        ButtonjPanel.add(jButton2);

        add(ButtonjPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      //Cancel Button
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void computeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_computeButtonActionPerformed

        //Clear previous algorithms
        this.parentView.clearAlgorithms();
       //Read selected algorithms
        this.readSelectedAlgorithms();
       //Check if algorithm is selected 
        //Read salesman
        this.parentView.setSalesmenCount(this.getSalesmanCount());
       //if yes 
        this.parentView.startComputation();
    }//GEN-LAST:event_computeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonjPanel;
    private javax.swing.JButton computeButton;
    private javax.swing.JPanel contentjPanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    void readSelectedAlgorithms() {
        this.algPanel.readSelectedAlgorithms();
    }
    
     public int  getSalesmanCount(){
       return this.inputDataPanel.getSalesmanCount();
   }
}