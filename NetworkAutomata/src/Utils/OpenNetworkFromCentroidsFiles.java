package Utils;

import CA.graph.JNode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import CA.graph.Model;

/**
 *
 * @author Jeaneth Machicao
 */
public class OpenNetworkFromCentroidsFiles {

    public static void Main2(String[] args) {

        final String[] paths = {"redesDB/DatasetEstomatos/ambiente/", "redesDB/DatasetEstomatos/umidade90/", "redesDB/DatasetEstomatos/umidade90CO2/"};

        double distThreshold = 200;

        for (int Threshold = 50; Threshold <= 200; Threshold = Threshold + 30) {
            Model old = open(paths[0] + "albicans1L_centroids.txt", Threshold * 1.0);//albicans1L_centroids
            Model cn = Model.newInstance(old);
            System.out.println("Threshold=" + Threshold + "\t" + cn.getN() + "\t" + cn.getNumberEdges() + "\t");
        }

    } 

    public static Model open(String fileName, double distThreshold) {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            ArrayList<GeographicNode> imgPixels = new ArrayList<>();

            String line =reader.readLine();  
            line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("\t");
                GeographicNode gNode = new GeographicNode(Integer.parseInt(tokens[0]) - 1, Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
                imgPixels.add(gNode);
                line = reader.readLine();
            }
            int n = CUtil.countLines(fileName) - 1;//ignorar 1 linha
            Model model = new Model(n);
            HashMap<Integer, JNode> adjList = model.getNodes();

            for (int i = 0; i < (imgPixels.size() - 1); i++) {

                int id1 = imgPixels.get(i).getNodeID();
                double x1 = imgPixels.get(i).getXcoord();
                double y1 = imgPixels.get(i).getYcoord();

                JNode nodei = adjList.get(id1);

                for (int j = (i + 1); j < imgPixels.size(); j++) {

                    int id2 = imgPixels.get(j).getNodeID();
                    double x2 = imgPixels.get(j).getXcoord();
                    double y2 = imgPixels.get(j).getYcoord();

                    double dist = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
                    if (dist <= distThreshold) {
                        nodei.addNeighbor(id2);

                        JNode nodej = adjList.get(id2);
                        nodej.addNeighbor(id1);
                    }
                }
                //adjList.put(id1, nodei);

            }
            return model;
        } catch (IOException | NumberFormatException ex) {
            System.err.println(ex);
            return null;
        }
    }
    
    public static Model openCthenante(String fileName, double distThreshold) {
        //arquivos de redes  ctenanthe estomatos
        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            ArrayList<GeographicNode> imgPixels = new ArrayList<>();

            String line = reader.readLine(); 
            int id=0;    
            while (line != null) {
                String[] tokens = line.split("\t");                
                GeographicNode gNode = new GeographicNode(id++, Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
                imgPixels.add(gNode);
                line = reader.readLine();
            }
            int n = CUtil.countLines(fileName);
            Model model = new Model(n);
            HashMap<Integer, JNode> adjList = model.getNodes();

            for (int i = 0; i < (imgPixels.size() - 1); i++) {

                int id1 = imgPixels.get(i).getNodeID();
                double x1 = imgPixels.get(i).getXcoord();
                double y1 = imgPixels.get(i).getYcoord();

                JNode nodei = adjList.get(id1);

                for (int j = (i + 1); j < imgPixels.size(); j++) {

                    int id2 = imgPixels.get(j).getNodeID();
                    double x2 = imgPixels.get(j).getXcoord();
                    double y2 = imgPixels.get(j).getYcoord();

                    double dist = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
                    if (dist <= distThreshold) {
                        nodei.addNeighbor(id2);

                        JNode nodej = adjList.get(id2);
                        nodej.addNeighbor(id1);
                    }
                }
                //adjList.put(id1, nodei);

            }
            return model;
        } catch (IOException | NumberFormatException ex) {
            System.err.println(ex);
            return null;
        }
    }
}
