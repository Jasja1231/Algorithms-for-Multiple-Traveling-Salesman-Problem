/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Model;
import View.MainView;

/**
 *
 * @author K
 */
public class Controller {

    private MainView view;
    private Model model;

    public Controller(Model model) {
       
    }

    public void setMainView(MainView mv) {
      this.view = mv;
    }
    
}
