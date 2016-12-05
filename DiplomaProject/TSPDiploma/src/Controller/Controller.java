/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Model;
import View.MainView;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author K
 */
public class Controller {

    private MainView view;
    private Model model;

    public Controller(Model model) {
       this.model = model;
               
    }

    public void setMainView(MainView mv) {
      this.view = mv;
    }

    public void addCoordinate(Coordinate coo) {
       this.model.addCoordinate(coo);
    }

    public void startComputation() {
        this.model.startComputation();
    }

    public void addAlgorithm(int i) {
        this.model.addAlgorithm(i);
    }

    public void clearAlgorithms() {
      this.model.clearAlgorithms();
    }

    public void setSalesmen(int salesmanCount) {
       this.model.setSalesmenCount(salesmanCount);
    }

    public void setSelectedMetric(int selectedMetric) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
