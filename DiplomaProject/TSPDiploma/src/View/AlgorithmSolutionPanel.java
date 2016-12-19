/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Algorithms.AlgorithmData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Yaryna
 */
public class AlgorithmSolutionPanel  extends JPanel {
    //textArea
    JTextArea textArea;
    //JButton 
    JButton deleteRuleButton;
    private MainView parent;
    
    public AlgorithmSolutionPanel(MainView parent,ArrayList<ArrayList<Integer>> cycles){
            this.parent = parent;
            Dimension d = new Dimension(250,100);
        
        this.setPreferredSize(d);
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        this.setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.WHITE));
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        this.add(textArea);
        this.setLayout(new BorderLayout());
        String content = constructCyclesAsList(cycles);
        textArea.setText(content);
        this.add(textArea,BorderLayout.CENTER);
        
    }   

    private String constructCyclesAsList(ArrayList<ArrayList<Integer>> cycles) {
        StringBuilder sb = new StringBuilder();
        int salesmen = 0 ; 
        for(ArrayList<Integer> singleRoute : cycles){
            sb.append(" "+ Integer.toString(salesmen) + ": ");
            salesmen++;
            for(Integer i : singleRoute){
                sb.append("-->");
                sb.append(i.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
