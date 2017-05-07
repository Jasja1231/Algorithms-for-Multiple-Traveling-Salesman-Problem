/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tspdiploma;

import Controller.Controller;
import Model.Model;
import View.MainView;

public class TSPDiploma {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        Model model  = new Model();
        Controller controller = new Controller(model);
        MainView mainView = new MainView(model, controller);
        mainView.setVisible(true);
    } 
}
