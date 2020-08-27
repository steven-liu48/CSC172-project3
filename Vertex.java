import java.util.LinkedList;

public class Vertex implements Comparable<Vertex>{
    String ID;
    double latitude;
    double longitude;
    LinkedList<Vertex> adj = new LinkedList<>();
    double weight = Integer.MAX_VALUE;
    Vertex parent = null;
    Boolean visited = false;

    public Vertex(String ID, double latitude, double longitude){
        this.ID = ID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getID(){
        return ID;
    }

    public String toString(){
        return "ID is " + getID() + ", Latitude is " + getLatitude() + ", Longitude is " + getLongitude() + " Weight: " + getWeight() + " Parent: " + getParent();
    }

    public double getWeight(){
        return weight;
    }
    public void setWeight(double w){
        this.weight = w;
    }
    public Vertex getParent(){
        return parent;
    }
    public void setParent(Vertex p){
        this.parent = p;
    }
    @Override
    public int compareTo(Vertex o) {

        if(weight - o.weight > 0){
            return 1;
        }
        else if(weight - o.weight < 0){
            return -1;
        }
        else{
            return 0;
        }
    }
}
