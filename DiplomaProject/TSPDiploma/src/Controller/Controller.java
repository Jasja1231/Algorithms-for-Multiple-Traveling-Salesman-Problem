/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Model;
import View.MainView;
import View.MapPanel;
import java.io.File;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 *
 * @author K old version
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
        this.model.setSelectedMetric(selectedMetric);
    }

    public void resetData() {
       this.model.resetData();
    }
    
       public void setNewStartingPoint(Coordinate co) {
        this.model.setNewStartingPoint(co);
    }

    public void saveSolutionScreenShot(MapPanel mapPanel, File selectedFile) {
        this.model.saveSolutionScreenShot(mapPanel,selectedFile);
    }

   
}
