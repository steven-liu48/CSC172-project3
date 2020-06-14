/*
Name: Winnie Wan
Email: wwan5@u.rochester.edu
Name: Xiaoxiang "Steven" Liu
Email: xliu102
Project 3
Date: 04/16/19
 */
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class StreetMap {

    private boolean isFound = false;
    private boolean show = false;
    private boolean directions = false;
    private String loc1 = "";
    private String loc2 = "";
    static double totalDistance = 0;
    static HashMap<String, Vertex> map = new HashMap<>();
    static ArrayList<Vertex> path = new ArrayList<>();
    public static void main(String[] args) {

        StreetMap streetMap = new StreetMap();
        File file = new File(args[0]);
        for(int i = 0; i < args.length; i++){
            if(args[i].equals("--show"))
                streetMap.show = true;
            if(args[i].equals("--directions")) {
                streetMap.directions = true;
                if(args[i+1].equals("--show")){
                    streetMap.loc1 = args[i + 2];
                    streetMap.loc2 = args[i + 3];
                }
                else {
                    streetMap.loc1 = args[i + 1];
                    streetMap.loc2 = args[i + 2];
                }
            }
        }

        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(file);
        } catch (FileNotFoundException | NullPointerException e) {
            System.out.println("File does not exist.");
        }

        ArrayList<Edge> edges = new ArrayList<>();

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] splitLine = line.split("\t");
            //Add Vertex
            if (splitLine[0].equals("i")) {
                map.put(splitLine[1], new Vertex(splitLine[1], Double.parseDouble(splitLine[2]), Double.parseDouble(splitLine[3])));
            }

            if (splitLine[0].equals("r")) {
                String ID = splitLine[1];
                Vertex v1 = map.get(splitLine[2]);
                Vertex v2 = map.get(splitLine[3]);
                double weight = Haversine.distance(v1.getLatitude(),
                        v1.getLongitude(), v2.getLatitude(), v2.getLongitude());
                edges.add(new Edge(ID,v1,v2,weight));
                v1.adj.add(v2);
                v2.adj.add(v1);
            }
        }

        try {
            streetMap.dijkstra(streetMap.loc1, streetMap.loc2, path);
        }catch(NullPointerException e){

        }
        if(streetMap.show) {
            Canvas canvas = new Canvas(map, edges, path, streetMap.isFound);
            JFrame frame1 = new JFrame("Map");
            frame1.setSize(600, 600);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.add(canvas);
            frame1.setVisible(true);
        }
    }

    public void dijkstra(String s, String t, ArrayList<Vertex> path) {
        PriorityQueue<Vertex> pq = new PriorityQueue<>();
        map.get(s).weight = 0;

        pq.add(map.get(s));


        while (!pq.isEmpty() ) {
            Vertex u = pq.poll();
            u.visited = true;

            for(int i = 0; i < u.adj.size(); i++) {
                Vertex temp = u.adj.get(i);

                double UV_dis = Haversine.distance(map.get(u.ID).getLatitude(),
                        map.get(u.ID).getLongitude(), map.get(temp.ID).getLatitude(), map.get(temp.ID).getLongitude());
                if (temp.weight > u.weight + UV_dis && !temp.visited) {
                    temp.weight = u.weight + UV_dis;
                    map.get(temp.ID).parent = map.get(u.ID);
                }
                if (!pq.contains(temp)&& !temp.visited) {
                    pq.add(temp);
                }
            }

        }

        if(map.get(t).parent != null) {
            isFound = true;
            totalDistance = map.get(t).weight;
            System.out.println("Total Distance: " + totalDistance);

            while (!map.get(t).parent.ID.equals(s)) {
                path.add(map.get(map.get(t).ID));
                t = map.get(t).parent.ID;
            }
            path.add(map.get(map.get(t).ID));
            path.add(map.get(map.get(t).parent.ID));

            if(this.directions){
                for(int i = path.size() - 1; i >= 0; i--){
                    System.out.println(path.get(i).ID);
                }
            }
        }
        else{
            isFound = false;
            System.out.println("No Path Found");
        }
    }
}