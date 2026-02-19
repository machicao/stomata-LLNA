package Utils;

import CA.graph.Model; 
import CA.graph.JNode;
import CA.graph.MNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author Jeaneth Machicao
 */
public class OpenNetworkEdgeList {

    public Model open(String path) {
        try {

            if (!(new File(path)).exists()) {
                System.out.println("Não foi possível abrir arquivo: " + path);
                return null;
            }
            int cabecallo = 2;//2 linhas extras de cabecallo
            int n = CUtil.countLines(path) - cabecallo;
            Model model = new Model(n);
            HashMap<Integer, JNode> nodes = model.getNodes();
            FileReader file = new FileReader(path);
            BufferedReader leitor = new BufferedReader(file);
            StringTokenizer st;
            String properties = leitor.readLine();//#1 first line on header file: properties
            leitor.readLine();  //#2 second line: --------
            //put the already properties extracted from R     
            setupPropertiesNetwork(model, properties);

            String line = leitor.readLine();
            while (line != null) {
                st = new StringTokenizer(line, " ");//whitespace
                int v1 = Integer.parseInt(st.nextToken());

                while (st.hasMoreElements()) {
                    int v2 = Integer.parseInt(st.nextToken());
                    nodes.get(v1).addNeighbor(v2);
                    nodes.get(v2).addNeighbor(v1);// REDE JA É DIRECCIONADA

                    //System.out.println(v1 + "," + v2);
                }

                line = leitor.readLine();
            }

            file.close();
            leitor.close();

 
            return model;

        } catch (IOException | NumberFormatException ex) {
            System.err.println(ex);
            return null;
        }
    }

    public Model open(String path, int n, boolean isRandomWeight) {
        try {

            if (!(new File(path)).exists()) {
                System.out.println("Não foi possível abrir arquivo: " + path);
                return null;
            }
            Model model = new Model(n);
            HashMap<Integer, JNode> nodes = model.getNodes();
            FileReader file = new FileReader(path);
            BufferedReader leitor = new BufferedReader(file);
            StringTokenizer st;
            String properties = leitor.readLine();//#1 first line on header file: properties
            leitor.readLine();  //#2 second line: --------
            //put the already properties extracted from R     
            setupPropertiesNetwork(model, properties);

            Random rand = new Random();
            String line = leitor.readLine();
            while (line != null) {
                st = new StringTokenizer(line, " ");//whitespace
                int v1 = Integer.parseInt(st.nextToken());
                while (st.hasMoreElements()) {
                    int v2 = Integer.parseInt(st.nextToken());
                    nodes.get(v1).addNeighbor(v2);
                    nodes.get(v2).addNeighbor(v1);

                    //System.out.println(v1 + "," + v2);
                }
                line = leitor.readLine();
            }

            file.close();
            leitor.close();

            for (int i = 0; i < nodes.size(); i++) {
                ArrayList<Integer> neighbor = nodes.get(i).getNeighbor();
                for (int j = 0; j < nodes.get(i).getK(); j++) {
                    if (isRandomWeight) {
                        nodes.get(i).setWeight(j, rand.nextInt(256));
                    } else {
                        nodes.get(i).setWeight(j, (float) Math.abs((nodes.get(i).getK() - nodes.get(neighbor.get(j)).getK())));
                    }
                }
            }

            return model;

        } catch (IOException | NumberFormatException ex) {
            System.err.println(ex);
            return null;
        }
    }

    public Model open(String path, int n, boolean isRandomWeight, float f) {
        int numberToRemove = (int) (n * 0.2);
        Random r = new Random();
        int[] nodesToRemove = new int[n];
        boolean[] nodesToRemoveBoolean = new boolean[n];
        HashMap<Integer, Integer> indices = new HashMap<Integer, Integer>();

        for (int i = 0; i < n; i++) {
            nodesToRemove[i] = i;
        }

        for (int i = 0; i < n * n; i++) {
            int v1 = r.nextInt(n);
            int v2 = r.nextInt(n);
            int temp = nodesToRemove[v1];
            nodesToRemove[v1] = nodesToRemove[v2];
            nodesToRemove[v2] = temp;
        }

        for (int i = 0; i < numberToRemove; i++) {
            nodesToRemoveBoolean[nodesToRemove[i]] = true;
        }

        int previousN = n;
        Model model = new Model(n - numberToRemove);
        HashMap<Integer, JNode> nodes = model.getNodes();

        int index = 0;
        for (int i = 0; i < previousN; i++) {
            if (!nodesToRemoveBoolean[i]) {
                nodes.put(index, new JNode(index));
                indices.put(i, index);
                index++;
            }
        }

        try {

            if (!(new File(path)).exists()) {
                System.out.println("Não foi possível abrir arquivo: " + path);
                return null;
            }

            FileReader file = new FileReader(path);
            BufferedReader leitor = new BufferedReader(file);
            String line;
            StringTokenizer st;
            line = leitor.readLine();
            Random rand = new Random();

            while (line != null) {
                st = new StringTokenizer(line);
                while (st.hasMoreElements()) {
                    int v1 = Integer.parseInt(st.nextToken());
                    int v2 = Integer.parseInt(st.nextToken());

                    if (!nodesToRemoveBoolean[v1] && !nodesToRemoveBoolean[v2]) {
                        nodes.get(indices.get(v1)).addNeighbor(indices.get(v2));
                        nodes.get(indices.get(v2)).addNeighbor(indices.get(v1));
                    }

                    //System.out.println(v1 + "," + v2);
                }
                line = leitor.readLine();
            }

            file.close();
            leitor.close();

            for (int i = 0; i < nodes.size(); i++) {
                ArrayList<Integer> neighbor = nodes.get(i).getNeighbor();
                for (int j = 0; j < nodes.get(i).getK(); j++) {
                    if (isRandomWeight) {
                        nodes.get(i).setWeight(j, rand.nextInt(256));
                    } else {
                        nodes.get(i).setWeight(j, (float) Math.abs((nodes.get(i).getK() - nodes.get(neighbor.get(j)).getK())));
                    }
                }
            }

            return model;

        } catch (Exception ex) {
            System.err.println(ex);
            return null;
        }
    } 
     
 
    public static Model openIgnoringHeaders(String fileName, int numLinesHeader) {
        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            HashMap<String, Integer> nodes = new HashMap<>();
            HashMap<Integer, JNode> adjList = new HashMap<>();
            String line = "";
            for (int i = 0; i < numLinesHeader; i++) {

                line = reader.readLine(); // ignorar a  linha
            }

            line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split(" ");
                String[] neighbors = new String[tokens.length - 1];
                System.arraycopy(tokens, 1, neighbors, 0, tokens.length - 1);
                String A = tokens[0];
                //ADD NODE FROM 
                if (!nodes.containsKey(A)) {
                    //int id = nodes.size();                    
                    int id=Integer.parseInt(A);
                    nodes.put(A, id);
                    JNode nodeA = new MNode(id, A);
                    adjList.put(id, nodeA);
                    //System.out.println("adding node "+id);
                }

                //add neigbors
                for (String neighs : neighbors) {
                    if (!nodes.containsKey(neighs)) {
                        //int id = nodes.size();
                        int id=Integer.parseInt(neighs);
                        nodes.put(neighs, id);
                        JNode nodeA = new MNode(id, neighs);
                        adjList.put(id, nodeA);                        
                        //System.out.println("adding node "+ id);
                    }
                }

                //add edges
                for (String neighs : neighbors) {
                    adjList.get(nodes.get(A)).addNeighbor(nodes.get(neighs)); // if node was added then add en cgraph
                    adjList.get(nodes.get(neighs)).addNeighbor(nodes.get(A));// FORMANDO UMA REDE  DIRECCIONADA!!! ****
                    //System.out.println("adding edge "+ A+"->"+neighs);
                }
                line = reader.readLine();
            }
            reader.close();
            Model cn = new Model();
            cn.setNodes(nodes.size(), adjList);

            return cn;
            // ADD NEIGHS                
        } catch (IOException | NumberFormatException ex) {
            System.err.println(ex);
            return null;
        }

    }

    private void setupPropertiesNetwork(Model g, String properties) {
        //avgDegree=%f\tclusCoef=%f\tL=%f\tdiameter=%f
        //avgDegree=5.992000	clusCoef=0.013787	L=3.011519	diameter=5.000000	gamma=2.400948	KS=0.032786	pvalue=0.998663
        StringTokenizer tokens = new StringTokenizer(properties, "\t");//4 tokens
        for (int i = 0; i < tokens.countTokens(); i++) {

            StringTokenizer subtoken = new StringTokenizer(tokens.nextToken(), "=");
            String label = subtoken.nextToken();
            switch (label) {
                case "avgDegree":
                    g.setAvgDegree(Double.parseDouble(subtoken.nextToken()));
                    break;
                case "clusCoef":
                    g.setClusteringCoefficient(Double.parseDouble(subtoken.nextToken()));
                    break;
                case "L":
                    g.setAvgPathLength(Double.parseDouble(subtoken.nextToken()));
                    break;
                case "diameter":
                    g.setDiameter(Double.parseDouble(subtoken.nextToken()));
                    break;
                case "gamma":
                    g.setGamma(Double.parseDouble(subtoken.nextToken()));
                    break;
                case "NaN":
                    break;
            }
        }

    }
 
}
