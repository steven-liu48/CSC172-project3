public class Edge {
    String ID;
    Vertex v1, v2;
    double weight;

    public Edge(String ID, Vertex v1, Vertex v2, double weight){
        this.ID = ID;
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

    public Vertex getV1(){
        return v1;
    }

    public Vertex getV2(){
        return v2;
    }
}
