//the haversine distance method cite from: https://gist.github.com/vananth22/888ed9a22105670e7a4092bdcf0d72e4//
//import src.HaversineDistance;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JFrame;
//This file include algorithm on creating and drawing map by plugin node and edge file and
//can create shorted path from and to specific
//point, it will also draw out shortest path and print distance.

public class streetmapping extends JFrame {

    private ArrayList<Node> N_List;
    private ArrayList<Edge> E_List;
    boolean showdirection = false;
    Edge a;
    private double[] cost_map;
    private int[] pi_map;
    public static String start;
    public static String end;
    public static double maxLon = Integer.MIN_VALUE;
    public static double maxLat = Integer.MIN_VALUE;
    public static double minLon = Integer.MIN_VALUE;
    public static double minLat = Integer.MIN_VALUE;

    public streetmapping() {
        N_List = new ArrayList<>();
        E_List = new ArrayList<>();
        setTitle("Map");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
    }

    public void draw() {
        JPanel ar = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                for (Edge a : E_List) {
                    double Lat1 = LontoX(findNode1(a).Lat);
                    double Lon1 = LontoY(findNode1(a).Lon);
                    double Lat2 = LontoX(findNode2(a).Lat);
                    double Lon2 = LontoY(findNode2(a).Lon);
                    ((Graphics2D) g).draw(new Line2D.Double(Lat1, Lon1, Lat2, Lon2));
                }
                if (showdirection) {
                    int current_index = 0;
                    String current = "";

                    for (int i = 0; i < N_List.size(); i++) {
                        if (N_List.get(i).id.equals(end)) {
                            current_index = i;
                            current = N_List.get(i).id;
                            break;
                        }
                    }

                    boolean goon = true;

                    while (goon) {
                        if (current.equals(start)) {
                            goon = false;
                        }
                        else {
                            Graphics2D g2 = (Graphics2D)g;
                            g2.setColor(Color.RED);
                            g2.setStroke(new BasicStroke(3));
                            double Lat1 = LontoX(N_List.get(current_index).Lat);
                            double Lon1 = LontoY(N_List.get(current_index).Lon);
                            double Lat2 = LontoX(N_List.get(pi_map[current_index]).Lat);
                            double Lon2 = LontoY(N_List.get(pi_map[current_index]).Lon);
                            (g2).draw(new Line2D.Double(Lat1, Lon1, Lat2, Lon2));
                            
                            current_index = pi_map[current_index];
                            current = N_List.get(pi_map[current_index]).id;
                        }
                    }
                }
                repaint();
            };
        };
        getContentPane().add(ar);
    }

    public void drawLine(Graphics g) {
        Graphics2D g3 = (Graphics2D) g;
        g3.setColor(Color.BLACK);
    }

    public void getMaxDif(){
        maxLon = N_List.get(0).Lon;
        minLon = N_List.get(0).Lon;
        maxLat = N_List.get(0).Lat;
        minLat = N_List.get(0).Lat;

        for (int i = 0; i < N_List.size(); i++) {
            if (N_List.get(i).Lon > maxLon) {
                maxLon = N_List.get(i).Lon;
            }
            else if (N_List.get(i).Lon < minLon) {
                minLon = N_List.get(i).Lon;
            }
            if (N_List.get(i).Lat > maxLat) {
                maxLat = N_List.get(i).Lat;
            }
            else if (N_List.get(i).Lat < minLat) {
                minLat = N_List.get(i).Lat;
            }
        }

        //maxLon = Math.abs(maxLon);
        //minLon = Math.abs(minLon);
        //maxLat = Math.abs(maxLat);
        //minLat = Math.abs(minLat);

        //LonDif = 1200/(maxLon-minLon);
        //LatDif = 700/(maxLat-minLat);
    }

    public double LontoY (double Lon) {
        double dif = (getHeight()+20) / (maxLon - minLon);
        return getHeight() - (Lon - minLon) * dif;
    }

    public double LontoX (double Lat) {
        double dif = (getWidth()-50) / (maxLat - minLat);
        return (Lat - minLat) * dif;
    }

    public Node findNode1 (Edge a) {
        for (Node node : N_List) {
            if(node.id.equals(a.I1)) {
                return node;
            }
        }
        return null;
    }
    public Node findNode2 (Edge a) {
        for (Node node : N_List) {
            if(node.id.equals(a.I2)) {
                return node;
            }
        }
        return null;
    }

    public void cost_Initilization(){
        pi_map = new int[N_List.size()];
        for (int i = 0; i < pi_map.length; i++) {
            pi_map[i] = -1;
        }
        cost_map = new double[N_List.size()];
        for (int i = 0; i < cost_map.length; i++) {
            cost_map[i] = Double.MAX_VALUE;
        }

        for (int i = 0; i < E_List.size(); i++) {
            String n1 = E_List.get(i).I1;
            String n2 = E_List.get(i).I2;
            double Lat1 = 0.0;
            double Lon1 = 0.0;
            double Lat2 = 0.0;
            double Lon2 = 0.0;

            for (int j = 0; j < N_List.size(); j++) {
                if (N_List.get(j).id.equals(n1)) {
                    Lat1 = N_List.get(j).Lat;
                    Lon1 = N_List.get(j).Lon;
                }
                else if (N_List.get(j).id.equals(n2)) {
                    Lat2 = N_List.get(j).Lat;
                    Lon2 = N_List.get(j).Lon;
                }
                if (n1.equals(N_List.get(j).id)) {
                    N_List.get(j).neighbor_add(E_List.get(i));
                }
                else if (n2.equals(N_List.get(j).id)) {
                    N_List.get(j).neighbor_add(E_List.get(i));
                }
            }

            E_List.get(i).cost = getCost(Lat1,Lon1,Lat2,Lon2);
        }

        getMaxDif();
    }

    /*public void printall(){
        for (int i = 0; i < N_List.size(); i++) {
            System.out.println(N_List.get(i).Lon);
        }
    }*/

    public static void main(String[] args) throws Exception {
        streetmapping test = new streetmapping();
        String filename = args[0];

        test.setVisible(false);
        test.setResizable(true);

        String dir = System.getProperty("user.dir");
        File file = new File(dir + "/" + filename);
        BufferedReader input = new BufferedReader(new FileReader(file));

        String lines;
        while ((lines = input.readLine()) != null) {
            String a[] = lines.split("	");

            if (a[0].equals("i")) {
                String id = a[1];
                double Latitude = Double.parseDouble(a[2]);
                double Longitude = Double.parseDouble(a[3]);
                Node temp = new Node(id, Latitude, Longitude);
                test.N_List.add(temp);
            }
            else if (a[0].equals("r")){
                String id = a[1];
                String n1 = a[2];
                String n2 = a[3];
                Edge temp = new Edge(id, n1, n2);
                test.E_List.add(temp);
            }
        }

        test.cost_Initilization();

        //test.printall();

        for (int i = 1; i < args.length; i++) {
            String command = args[i];
            if (command.equals("--show")){
                //show();
                test.draw();
                test.showdirection = false;
                test.setVisible(true);
            }
            else if (command.equals("--directions")){
                start = args[i+1];
                end = args[i+2];
                test.directions(start, end);
                test.showdirection = true;
                test.draw();
                test.setVisible(true);
            }
        }

    }

    public static double getCost(double Lat1, double Lat2, double Lon1, double Lon2){
        HaversineDistance temp = new HaversineDistance();
        return temp.distancef(Lat1, Lon1, Lat2, Lon2);
    }

    /*public static void show(){
        return;
    }*/

    public void directions(String start, String end){
        int indexi = 0;
        int indexj = 0;

        for (int i = 0; i < N_List.size(); i++) {
            if (N_List.get(i).id.equals(start)) {
                indexi = i;
                break;
            }
        }

        for (int i = 0; i < N_List.size(); i++) {
            if (N_List.get(i).id.equals(end)) {
                indexj = i;
                break;
            }
        }

        cost_map[indexi] = 0;
        N_List.get(indexi).status = true;

        dijkstra(0.0, indexi);

        ArrayList<String> trace = new ArrayList<>();
        double total_cost = cost_map[indexj];

        System.out.print("moving through: ");
        while (indexj != -1) {
            trace.add(N_List.get(indexj).id);
            indexj = pi_map[indexj];
        }

        for (int i = trace.size()-1; i > -1; i--) {
            System.out.print(trace.get(i) + " ");
        }
        System.out.println();

        System.out.print("Total cost is: " + total_cost);
    }
    public int getNID(String id){
        int temp = 0;
        for (int i = 0; i < N_List.size(); i++) {
            if (N_List.get(i).id.equals(id)) {
                temp = i;
            }
        }
        return temp;
    }
    public void dijkstra(double cost, int n_index) {
        N_List.get(n_index).status = true;

        for (int i = 0; i < N_List.get(n_index).neighbor.size(); i++) {
            if (N_List.get(n_index).neighbor.get(i).I1.equals(N_List.get(n_index).id)){
                int temp = getNID(N_List.get(n_index).neighbor.get(i).I2);
                if (N_List.get(n_index).neighbor.get(i).cost + cost < cost_map[temp]) {
                    pi_map[temp] = n_index;
                    cost_map[temp] = N_List.get(n_index).neighbor.get(i).cost + cost;
                }
            }
            else if (N_List.get(n_index).neighbor.get(i).I2.equals(N_List.get(n_index).id)){
                int temp = getNID(N_List.get(n_index).neighbor.get(i).I1);
                if (N_List.get(n_index).neighbor.get(i).cost + cost < cost_map[temp]) {
                    pi_map[temp] = n_index;
                    cost_map[temp] = N_List.get(n_index).neighbor.get(i).cost + cost;
                }
            }
        }

        int index = -1;
        double temp = Double.MAX_VALUE;
        for (int i = 0; i < cost_map.length; i++) {
            if (temp > cost_map[i] && N_List.get(i).status == false) {
                temp = cost_map[i];
                index = i;
            }
        }

        if (index != -1) {
            if (N_List.get(index).status == false) {
                cost = cost_map[index];
                dijkstra(cost, index);
            }   
        }
    }

}

class Node {
    double Lat;
    double Lon;
    String id;
    ArrayList<Edge> neighbor;
    boolean status;
    public Node(String id, double Lat, double Lon) {
        this.id = id;
        this.Lon = Lat;
        this.Lat = Lon;
        neighbor = new ArrayList<>();
        status = false;
    }
    public void neighbor_add(Edge input){
        neighbor.add(input);
    }
}

class Edge {
    String id;
    String I1;
    String I2;
    double cost;
    public Edge(String id, String I1, String I2){
        this.id = id;
        this.I1 = I1;
        this.I2 = I2;
        cost = 0.0;
    }
}
