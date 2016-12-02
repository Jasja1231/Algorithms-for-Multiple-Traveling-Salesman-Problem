/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import Model.Model;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JMenu;

/**
 *
 * @author K
 */
public class MainView extends javax.swing.JFrame implements Observer , ActionListener{

    //elements
    private MapPanel mapPanel;
    private JMenu fileMenu;
    private OptionsPanel optionsPanel;
    
    //MVC
    public  final Model model;
    private final Controller controller;
    
    /**
     * Creates new form MainView
     */
    public MainView(Model model, Controller controller) {
        initComponents();
        init();
        
        //set window parameters
        this.setSize(new Dimension(500,500));
        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(500,500));
        
         //connect to model and controller 
        this.model = model;
        this.controller = controller; 
        this.model.addObserver(this);
        controller.setMainView(this);

        this.constructMenu();
        
        this.pack();
    }


    /**
     * Initiates elements of MainView Class 
     */
    private void init(){
        mapPanel = new MapPanel(this);
        optionsPanel = new OptionsPanel(this);
        
        
   
        //Add Map in the centre
        this.add(mapPanel,BorderLayout.CENTER);
        this.add(optionsPanel,BorderLayout.WEST);
    }
    
    
    private void constructMenu(){
      fileMenu = new JMenu("File");
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        loadFilejMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenuBar1.setBackground(new java.awt.Color(0, 204, 204));

        FileMenu.setText("File");
        FileMenu.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FileMenuStateChanged(evt);
            }
        });

        loadFilejMenuItem.setText("Load input file");
        loadFilejMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFilejMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(loadFilejMenuItem);

        jMenuItem1.setText("Generate multiple random files");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        FileMenu.add(jMenuItem1);

        jMenuBar1.add(FileMenu);

        jMenu3.setText("Edit");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadFilejMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFilejMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loadFilejMenuItemActionPerformed

    private void FileMenuStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FileMenuStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_FileMenuStateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param args the command line arguments
     */
   



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem loadFilejMenuItem;
    // End of variables declaration//GEN-END:variables
}
