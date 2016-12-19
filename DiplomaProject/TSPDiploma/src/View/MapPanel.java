/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import de.cm.osm2po.model.LatLon;
import de.cm.osm2po.routing.RoutingResultSegment;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
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
    private PopUpMenuCustom pp;
    private MapMarker mm; //TODO : fix it somehow!!
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
        
        ////
        pp = new PopUpMenuCustom(this);
        MapPanel.this.map.setComponentPopupMenu(pp);
    }
    
    
    private void constructMap(){
        try {
            //Ysrs psth     C:\\Users\\Yaryna\\Documents\gith\WUT 2015-16\\Diploma\\MTSP\\DiplomaProject\\TSPDiploma\\Tiles\\
            // map.setTileSource(new OfflineOsmTileSource("C:\\Users\\Krzysztof\\Desktop\\DiplomaProject\\DiplomaProject\\TSPDiploma\\Tiles\\",10,14));
            //corrected
            map.setDisplayPosition(new Coordinate(52.2297,21.0122), 10); //center in warsaw 
            map.getPosition();
            //System.err.println(new File(".").getCanonicalPath());
            map.setTileSource(new OfflineOsmTileSource((new File(".\\Tiles").toURI().toURL()).toString(), 10, 14)); 
        } catch (Exception ex) {
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
                                System.err.println("COO : " + coo.getLat() + "   " + coo.getLon() + " E : " + e.getX() + " " + e.getY());
                                
                                if(!SwingUtilities.isRightMouseButton(e) ){    //TO DO : Check WTF is going on
                                    int dotID = MapPanel.this.parentView.model.getCoordinates().size();
                                    MapPanel.this.parentView.addCoordinate(coo);
                                    MapMarkerDot marker = new MapMarkerDot(Color.RED,coo.getLat(),coo.getLon());
                                    marker.setMapMarkerID(dotID);
                                    MapPanel.this.map.addMapMarker(marker);  
                                }
                                if(!SwingUtilities.isLeftMouseButton(e) ){
                                        mm = MapPanel.this.map.markerExistsWithTolerance(0.0007, coo);//.markerExists(new MapMarkerDot(coo.getLat(),coo.getLon()))){ //TODO: create separate method in JMapViewer so not to create marker every time
                                        if(mm!=null){
                                            if(pp.addedListenerAlready == false){
                                                pp.item.addActionListener(new ActionListener() {
                                                   @Override
                                                   public void actionPerformed(ActionEvent exa) {
                                                      MapPanel.this.setStartingPoint(mm);
                                                   }
                                               });
                                                pp.addedListenerAlready  = true;
                                            }
                                           pp.showPopUp(e.getX(), e.getY());
                                        }
                                }
                            }
                        }    
                    });
                    mouseTimer.setRepeats(false);
                    mouseTimer.start();
                }
            }
        };
    }
   
    
    private void setStartingPoint(MapMarker mm){
        this.parentView.setNewStartingPoint(mm.getCoordinate());
        //swap elements in map markers list
        int previousMMID = mm.getMapMarkerId();
        this.map.swapMapDods(previousMMID);
    }
    
    
    public void drawLines(List<Coordinate> route){
        Color thisRouteColor = generateRandomColor();
        for(int i = 0; i < route.size()-1;i++){
             List<Coordinate> tempRouteBetweenTwoPoints = new ArrayList<>();
             tempRouteBetweenTwoPoints.add(route.get(i));
             tempRouteBetweenTwoPoints.add(route.get(i+1));
             tempRouteBetweenTwoPoints.add(route.get(i+1));
             
             MapPolygonImpl mapPoly = new MapPolygonImpl(tempRouteBetweenTwoPoints);
             mapPoly.setColor(thisRouteColor);
             //mapPoly.setBackColor(thisRouteColor);
             map.addMapPolygon(mapPoly);
        }
    }   
    
    
    int last_used_color = 0;
    public Color generateRandomColor(){
        if(last_used_color == 5)
            last_used_color = -1;
        
        ArrayList<Color> colors  = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLACK);
        colors.add(Color.GREEN);
        
        last_used_color++;
        return colors.get(last_used_color);
    }

    
    public void drawCycles (ArrayList<ArrayList<Integer>>cycles)
    {
        for(ArrayList<Integer> l : cycles)
        {
            ArrayList<Coordinate>ac = new ArrayList<>();
            for(int i=0; i < l.size()-1; i++){
               // ac.add(this.parentView.model.getCoordinates().get(i));
                try{
                    int [] path = this.parentView.model.getTimeMatrix()[l.get(i)][l.get(i+1)];
                    for (int x=0;x < path.length;x++)
                    {
                        RoutingResultSegment rrs = this.parentView.model.getGraph().lookupSegment(path[x]);
                        rrs.getSourceId();
                        rrs.getTargetId();
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
    
    
    public void drawCyclesEuclideanLines (ArrayList<ArrayList<Integer>>cycles)
    {
        for(ArrayList<Integer>singleRoute : cycles)
        {
          List<Coordinate>ac = new ArrayList<>();

            for (Integer singleRoute1 : singleRoute) {
                ac.add(new Coordinate(this.parentView.model.getCoordinates().get(singleRoute1).getLat(), this.parentView.model.getCoordinates().get(singleRoute1).getLon()));
            }
          drawLines(ac);
        } 
    }
    
    public void clearMap(){
        this.map.removeAllMapMarkers();
        this.map.removeAllMapPolygons();
        this.map.repaint();
    }
    
    /***
     * Clears all roads from previous calculations. 
     * Leaving only markers.
     */
    public void clearAllRoads() {
        this.map.removeAllMapPolygons();
    }

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

    void setNewMarkerDots(List<Coordinate> coordinates) {
        for(int count = 0 ; count < coordinates.size();count++){
            MapMarkerDot marker = new MapMarkerDot(Color.RED,coordinates.get(count).getLat(),coordinates.get(count).getLon());
            marker.setMapMarkerID(count);
            MapPanel.this.map.addMapMarker(marker);  
        }
    }
}
