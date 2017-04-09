/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tspdiploma;

import Algorithms.Parser;
import Controller.Controller;
import Model.Model;
import View.MainView;

/**
 *
 * @author K
 */
public class TSPDiploma {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        Model model  = new Model();
        Controller controller = new Controller(model);
        MainView mainView = new MainView(model, controller);
        
        mainView.setVisible(true);
        //todo: delete
        int salesman = 20;
        int[] numOfPoints = {3,5}; 
         
        //GENERATE FILES 
        for(int i = 25 ; i < 501; i+=25){
           
        }
         for(int j = 1; j<11 ; j++)
            {
                for (int x : numOfPoints)
                {
                   // Parser.writeRandomFile("C:\\Users\\Yaryna\\Documents\\3 salesman 25 point heuristic approxim\\"+x+"_"+25+"_"+j+"_",x,25);
                }
            }
    }
    
}
