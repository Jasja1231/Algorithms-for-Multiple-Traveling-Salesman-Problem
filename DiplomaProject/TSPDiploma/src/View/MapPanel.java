/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.tilesources.OfflineOsmTileSource;

/**
 *
 * @author K
 */
public class MapPanel extends javax.swing.JPanel {

    private JMapViewer map ;
    private MainView parentView;
    private Timer mouseTimer;
    private boolean wasDoubleClick;
    /**
     * Creates new form MapPanel
     */
    public MapPanel(MainView mv) {
        initComponents();
        parentView = mv;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.red);
        init();
    }
    
    private void init(){
        map = new JMapViewer(); 
        //add map to fill the parent;
        constructMap();
        this.add(map,BorderLayout.CENTER);
    }
    
    
    private void constructMap(){
        try {
            // map.setTileSource(new OfflineOsmTileSource("file:///C:/Users/K/Desktop/DiplomaProject/DiplomaProject/TSPDiploma/Tiles",10,14));
            //corrected
            map.setDisplayPosition(new Coordinate(52.2297,21.0122), 10); //center in warsaw 
            map.getPosition();
           
            map.setTileSource(new OfflineOsmTileSource((new File("C:\\Users\\Yaryna\\Documents\\WUT 2015-16\\Diploma\\MTSP\\DiplomaProject\\TSPDiploma\\Tiles").toURI().toURL()).toString(), 10, 14)); 
        } catch (MalformedURLException ex) {
            Logger.
                    getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        map.setVisible(true);
        map.setSize(new Dimension(500,500));
        
        
        //override mouse clicked method
        new DefaultMapController(map) {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount()== 2 ) {
                       System.out.println("double click");
                       wasDoubleClick = true;
                }
                else {
                    Integer timerinterval = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
                  
                    mouseTimer = new Timer(timerinterval, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            if (wasDoubleClick) {
                                wasDoubleClick = false; // reset flag
                            } else {
                                Coordinate coo = (Coordinate) map.getPosition(e.getPoint());
                                
                                System.out.println( "  and it's a simple click!");
                                System.out.println(e.getPoint());
                                System.out.println(coo);

                                MapPanel.this.parentView.addCoordinate(coo);
                                MapMarkerDot marker = new MapMarkerDot(Color.RED,coo.getLat(),coo.getLon());
                                MapPanel.this.map.addMapMarker(marker);                                                                                   
                            }
                        }    
                    });
                    mouseTimer.setRepeats(false);
                    mouseTimer.start();
                }
            }
        };
    }
   
    
    
    public void drawLines(List<Coordinate> route){
        if(route.size() == 2){
            route.add(route.get(1));
        }
        MapPolygonImpl mapPoly = new MapPolygonImpl(route);
        mapPoly.setColor(generateRandomColor());
         map.addMapPolygon(mapPoly);
    }
    
    public Color generateRandomColor(){
        Random rand = new Random();
        int r = rand.nextInt(120);
        int g =  rand.nextInt(120);
        int b =  rand.nextInt(120);
       
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

    public void drawCycles (ArrayList<ArrayList<Integer>>cycles)
    {
        for(ArrayList<Integer>l : cycles)
        {
            List<Coordinate>ac = new ArrayList<>();
            for(int i : l)
                ac.add(this.parentView.model.getCoordinates().get(i));
            drawLines(ac);
        }
    }
    
    /*
       public void drawCycles (ArrayList<ArrayList<Integer>>cycles)
    {
        for(ArrayList<Integer>l : cycles)
        {
            List<Coordinate>ac = new ArrayList<>();
            for(int i=0; i<l.size()-1; i++){
               // ac.add(this.parentView.model.getCoordinates().get(i));
                try{
                    int [] path = this.parentView.model.getShortestPaths()[i][i+1];
                    for (int x=0;x<path.length;x++)
                    {

                        RoutingResultSegment rrs = this.parentView.model.getGraph().lookupSegment(x);
                        int from = rrs.getSourceId();
                        int to = rrs.getTargetId();
                        LatLon[] lons = rrs.getLatLons();     
                        for (LatLon lon : lons)
                        {
                            ac.add(new Coordinate(lon.getLat(),lon.getLon()));   
                        }
                    }
                   
                }
                  catch(Exception e){}

            }
            drawLines(ac);
        }
    }
    
    */
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
