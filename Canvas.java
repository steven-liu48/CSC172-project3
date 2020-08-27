import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Canvas extends JComponent {
    HashMap<String, Vertex> map;
    ArrayList<Edge> edges;
    ArrayList<Vertex> path;
    boolean isFound;
    double maxLat = -1 * Double.MAX_VALUE, minLat = Double.MAX_VALUE, maxLong = -1 * Double.MAX_VALUE, minLong = Double.MAX_VALUE;
    static double distanceValue;

    public Canvas(HashMap<String, Vertex> map, ArrayList<Edge> edges, ArrayList<Vertex> path, boolean isFound){
        this.map = map;
        this.edges = edges;
        this.path = path;
        this.isFound = isFound;
        this.findMax();
        this.setDistance();
    }

    public void findMax(){
        for(String key : map.keySet()){
            Vertex v = map.get(key);
            if(map.get(key).getLongitude() > maxLong){
                maxLong = map.get(key).getLongitude();
            }
            if(map.get(key).getLatitude() > maxLat){
                maxLat = map.get(key).getLatitude();
            }
            if(map.get(key).getLatitude() < minLat){
                minLat = map.get(key).getLatitude();
            }
            if(map.get(key).getLongitude() < minLong){
                minLong = map.get(key).getLongitude();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight() ;
        g.setColor(new Color(0,102,51));
        g.fillRect(0, 0, w, h);
        g.setColor(Color.WHITE);
        //finds the difference between min and max Longitude and Latitude
        double diffX = Math.abs(maxLat - minLat);
        double diffY = Math.abs(maxLong - minLong);

        for(Edge edge: edges) {
            double latitudeFirst = edge.getV1().getLatitude();
            double longitudeFirst = edge.getV1().getLongitude();

            double latitudeSecond = edge.getV2().getLatitude();
            double longitudeSecond = edge.getV2().getLongitude();

            //scaling algorithm
            int x1Scale = (int)(((longitudeFirst - minLong) / (diffY) * w));
            int x2Scale = (int)(((longitudeSecond - minLong) / (diffY) * w));

            double y1Scale = (((latitudeFirst - minLat) / (diffX) * h));
            double y2Scale = (((latitudeSecond - minLat) / (diffX) * h));

            g.drawLine(x1Scale, h - (int) y1Scale,  x2Scale,h - (int) y2Scale);
        }

        g.setColor(Color.RED);
        //if we are getting shortest path
        if(this.isFound) {
            ArrayList<Vertex> Nodes = this.path;//gets the list of nodes

            for(int i = 0; i < Nodes.size() - 1; i++) {
                double latitudeFirst = Nodes.get(i).latitude;
                double longitudeFirst = Nodes.get(i).longitude;

                double latitudeSecond = Nodes.get(i + 1).latitude;
                double longitudeSecond = Nodes.get(i + 1).longitude;

                //adds the distance
                distanceValue = distanceValue + Haversine.distance(latitudeFirst, longitudeFirst, latitudeSecond, longitudeSecond);
                int x1Scale = (int) (((longitudeFirst - minLong) / (diffY) * w));
                int x2Scale = (int) (((longitudeSecond - minLong) / (diffY) * w));

                double y1Scale = (((latitudeFirst - minLat) / (diffX) * h));
                double y2Scale = (((latitudeSecond - minLat) / (diffX) * h));

                //draws the line on the edge
                g.drawLine(x1Scale, h - (int) y1Scale, x2Scale, h - (int) y2Scale);

                //EXTRA CREDIT: puts markers on start and end point
                if (i == 0) {
                    try {
                        g.drawImage(loadImage(), x1Scale - 10, (h - (int)y1Scale) - 20, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (i == Nodes.size() - 2) {
                    try {
                        g.drawImage(loadImage(), x2Scale - 10, (h - (int)y2Scale) - 20, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            g.setColor(Color.WHITE);
            g.drawString("Distance: " + StreetMap.totalDistance + " mi.", 10, h - 5);
        }
    }

    //EXTRA CREDIT: Start and End pointers/markers
    private Image loadImage() throws IOException {
        Image imgs = ImageIO.read(getClass().getResourceAsStream("point.png"));
        return imgs.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
    }


    public void setDistance() {
        for(Edge edge:this.edges) {
            double latitudeFirst = edge.getV1().getLatitude();
            double longitudeFirst = edge.getV1().getLongitude();

            double latitudeSecond = edge.getV2().getLatitude();
            double longitudeSecond = edge.getV2().getLongitude();
            this.distanceValue = this.distanceValue + Haversine.distance(latitudeFirst, longitudeFirst, latitudeSecond, longitudeSecond);
        }
    }
}