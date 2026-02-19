package CA.graph;

import CA.graph.JNode;
import CA.graph.OrderNode;
//import Jama.EigenvalueDecomposition;
//import Jama.Matrix;
import Utils.CUtil; 
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Jeaneth Machicao
 */
public class Model {

    protected int n;
    protected HashMap<Integer, JNode> nodes;

    private double avgDegree;
    private double clusteringCoefficient;
    private double avgPathLength;
    private double diameter;
    //some networks have
    private double gamma;

    public Model() {
        nodes = new HashMap<>();
    }

    public Model(int n) {
        nodes = new HashMap<>();
        this.n = n;
        for (int code = 0; code < n; code++) {
            nodes.put(code, new JNode(code));
        }
    }

    public static Model newInstance(Model old) {
        //criate a copy of Model, but with isolated nodes removed
        //and with IDs re-assigned
        old.removingIsolatedNodesfromNetwork();
        int novoN = old.getN();
        Model novo = new Model(novoN);
        HashMap<Integer, Integer> matching = new HashMap<>();

        int i = 0;
        for (int x : old.getNodes().keySet()) {
            matching.put(x, i++);
        }

        for (int x : old.getNodes().keySet()) {
            ArrayList<Integer> neighbor = old.getNode(x).getNeighbor();
            for (Integer neig : neighbor) {
                if (matching.containsKey(neig)) {
                    int idneig = (int) matching.get(neig);
                    novo.getNode((int) matching.get(x)).addNeighbor(idneig);
                }
            }

        }

        return novo;

    }

    public int getN() {
        return nodes.size();
    }

    public HashMap<Integer, JNode> getNodes() {
        return nodes;
    }

    public void setNodes(int ns, HashMap<Integer, JNode> nds) {
        n = ns;
        nodes = nds;
    }

    public JNode getNode(int code) {
        return nodes.get(code);
    }

    //ussado no LLNA original para ruido
    public boolean removeNode(int code) {
        if (!nodes.containsKey(code)) {
            System.out.println("code " + code + " not found!");
            return false;
        } else {
            nodes.remove(code);
            return true;
        }
    }

     
    //count the number of alive neighbors from the current state, this mean before updating next state
    public int countNumAliveNeighbors(int x) {
        int sumalives = 0;
        try {
            List<Integer> neigs = nodes.get(x).getNeighbor();
            for (Integer id : neigs) {
                if (nodes.get(id).getCurrentState() == 1) {//ALIVE = 1                
                    sumalives++;
                }
            }

        } catch (NullPointerException ex) {
            System.err.println("deu erro em =:" + x);
            // o leitor de arquivos na OpenNetworkEdgeList linha:296 [codesource] le exlcusivamente redes direcionadas!! *******************************
        }
        return sumalives;
    }

    public void print() {
 
        System.out.println("Número de Nodes: " + n);

        for (int x : getNodes().keySet()) {
            nodes.get(x).print();
            List<Integer> neigs = nodes.get(x).getNeighbor();
            for (Integer id : neigs) {
                System.out.print("\t");
                nodes.get(id).print();
                //System.out.println("\t\t\t" + nodes.get(i).getWeight(nodes.get(neigh.get(j))));
            }
        }
    }

    public int[] neighbors(int k) {
        ArrayList<Integer> listNeig = nodes.get(k).getNeighbor();
        int[] neig = new int[listNeig.size()];
        for (int i = 0; i < listNeig.size(); i++) {
            neig[i] = listNeig.get(i);
        }
        return neig;
    }
    //hierachical degree level 2

    public int getK2(int k) {
        int degree2 = 0;
        ArrayList<Integer> neigh = nodes.get(k).getNeighbor();
        for (int j = 0; j < neigh.size(); j++) {
            degree2 += getNode(neigh.get(j)).getNeighbor().size();
        }
        return degree2;

    }

    //update k2 into eacho CNOde
    private void calculateK2() {
        for (int x : getNodes().keySet()) {
            ArrayList<Integer> neigh = getNode(x).getNeighbor();
            int degree2 = 0;
            for (int j = 0; j < neigh.size(); j++) {
                degree2 += getNode(neigh.get(j)).getNeighbor().size();
            }
            getNode(x).setK2(degree2);
        }
    }

    public List<Integer> getNotNeigbors(int k) {
        List<Integer> listNeig = nodes.get(k).getNeighbor();
        List<Integer> universe = getIDsNodes();
        ArrayList<Integer> notNeig = CUtil.setDifference(universe, listNeig);
        return notNeig;
    }

    public ArrayList<Integer> getIDsNodes() {

        List<JNode> listNodes = new ArrayList<>(this.nodes.values());
        ArrayList<Integer> listIDs = new ArrayList<>();
        for (JNode jNode : listNodes) {
            listIDs.add(jNode.getCode());
        }

        return listIDs;
    }

    public ArrayList<Integer> getIDsNodesByDegree() {

        List<JNode> listNodes = new ArrayList<>(this.nodes.values());

        Comparator<JNode> DEGREE_ORDER = new Comparator<JNode>() {
            @Override
            public int compare(JNode e1, JNode e2) {
                return e1.getK() - e2.getK();
            }
        };
//This function will reoder in ascendet order according to the degree. 
        Collections.sort(listNodes, DEGREE_ORDER);

        ArrayList<Integer> listIDs = new ArrayList<>();
//        System.out.println("printing ids ");
        for (JNode jNode : listNodes) {
            listIDs.add(jNode.getCode());
        }

        //System.out.println("iterator degree pointer");
        return listIDs;
    }

    public ArrayList<Integer> getIDsNodesByBarycentricHeuristicOrder() {

        ArrayList< Integer> listIDs = OrderNode.Baryheuristic(this);

        return listIDs;
    }

    public ArrayList<Integer> getDegreeByDegreeOrder() {

        List<JNode> listNodes = new ArrayList<>(this.nodes.values());

        Comparator<JNode> DEGREE_ORDER = new Comparator<JNode>() {
            @Override
            public int compare(JNode e1, JNode e2) {
                return e1.getK() - e2.getK();
            }
        };
        //This function will reoder in ascendet order according to the degree. 
        Collections.sort(listNodes, DEGREE_ORDER);

        ArrayList<Integer> listDegree = new ArrayList<>();
        //System.out.println("printing ids ");
        for (JNode jNode : listNodes) {
            listDegree.add(jNode.getK());
        }

        //System.out.println("iterator degree pointer");
        return listDegree;
    }

    public Map<Integer, Integer> getUniqueDegreeList() {
        Map<Integer, Integer> map = new HashMap<>(); //degree, id index array

        //Set<Integer> uniqueDegree = new HashSet<>(getDegreeByDegreeOrder());
        Set<Integer> uniqueDegree = new HashSet<>();
        for (int x : getNodes().keySet()) {
            uniqueDegree.add(nodes.get(x).getK());
        }
        List<Integer> degOrdered = new ArrayList<>(uniqueDegree);
        Collections.sort(degOrdered);

        int id = 0;
        for (Integer d : degOrdered) {
            map.put(d, id++); // DEGREE : id
        }

        return map;

    }

    public Map<Integer, ArrayList<Integer>> getArrayNodesPerUniqueDegree() {
        Map<Integer, ArrayList<Integer>> map = new HashMap<>();//degree, id index array
        for (int x : getNodes().keySet()) {
            int k = nodes.get(x).getK();
            if (!map.containsKey(k)) {
                ArrayList<Integer> listNodes = new ArrayList<>();
                listNodes.add(x);
                map.put(k, listNodes);
            } else {
                map.get(k).add(x);
            }
        }
        return map;
    }

//    public ArrayList<Integer> getIDsNodesByLaplacianMatrix() {
//        //
//        double degree[][] = new double[getN()][getN()];
//        double adj[][] = getAdjacencyMatrix();
//        for (int x : getNodes().keySet()) {
//            int idx = getNode(x).getCode();
//            degree[idx][idx] = getNode(idx).getK();
//        }
//
//        Matrix K = new Matrix(degree);
//        Matrix A = new Matrix(adj);
//        Matrix laplacianMatrix = K.minus(A);
//        // compute the spectral decomposition
//        EigenvalueDecomposition e = laplacianMatrix.eig();
//        Matrix eigenValue = e.getD();
//
//        Integer indices[] = sortEigenValues(eigenValue.getArray());
//        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(indices));
//        return intList;
//    }

    private static Integer[] sortEigenValues(double d[][]) {
        //Sort eigenvalues and corresponding vectors.       
        int n = d.length;
        double diag[] = new double[n];
        for (int i = 0; i < n; i++) {
            diag[i] = d[i][i];
        }

        Integer idx[] = CUtil.sortArray(diag);
        return idx;
    }

    //Metodo feito so para  testar  as dist. binarias e analizar se existe
    //dependencia no ordenamento por grau quando varios nos tem o mesmo grau.FF
    public ArrayList<Integer> getIDsNodesByDegreeRandomRange() {

        ArrayList<Integer> listIDs = getIDsNodesByDegree();
        ArrayList<Integer> novaListaIDs = new ArrayList();
        //##############################
        //aleatorizar todos os nós com mesmo grau, já que não necesariamente 
        //terá uma ordenacao deterministica
        int oldgrau = getNode(listIDs.get(0)).getK();//1st degree
        int oldindexL = 0;
        int oldindexR = 0;
        for (int pos = 1; pos < listIDs.size(); pos++) {
            int idx = listIDs.get(pos);
            int grau = getNode(idx).getK();

            if (grau > oldgrau) {//marcadores
                oldgrau = grau;
                oldindexR = pos;
                //random within indexL e indexR
                int random_order[] = CUtil.getSetRandomNumbers(oldindexR, oldindexL);
                for (int i = 0; i < random_order.length; i++) {
                    novaListaIDs.add(listIDs.get(random_order[i]));
                }
                oldindexL = oldindexR;
            }

        }

        //##############################
        //System.out.println("iterator degree pointer");
        return novaListaIDs;
    }
    //metodo para selecionar aleatoriamente um representante de cada grau
    //metodo feito para evitar o desampate do ordenamento por grau
    //usando como 2nd ordenamento o grau hierachico

    public ArrayList<Integer> getIDsIteratorOneNodesPerDegree() {
        ArrayList<Integer> listIDs = getIDsNodesByDegree();
        ArrayList<Integer> novaListaIDs = new ArrayList();
        //##############################
        //pegar aleatoriamente um no representate de cada grau diferente
        Random rand = new Random();
        int oldgrau = getNode(listIDs.get(0)).getK();//1st degree
        int oldindexL = 0;
        int oldindexR = 0;
        for (int pos = 1; pos < listIDs.size(); pos++) {
            int idx = listIDs.get(pos);
            int grau = getNode(idx).getK();

            if (grau > oldgrau) {//marcadores
                oldgrau = grau;
                oldindexR = pos;
                //random within indexL e indexR
                int random_integer = rand.nextInt(oldindexR - oldindexL) + oldindexL;
                novaListaIDs.add(listIDs.get(random_integer));
                oldindexL = oldindexR;
            }

        }

        //##############################
        //System.out.println("iterator degree pointer");
        return novaListaIDs;

    }

    public ArrayList<Integer> getIteratorPerDegreeThenHierachical() {
        calculateK2();
        //System.out.println("K2 calculated ... done");
        //ordenar por grau hierarchico
        Comparator<JNode> HIERAR_DEGREE_ORDER = new Comparator<JNode>() {
            @Override
            public int compare(JNode e1, JNode e2) {
                return e1.getK2() - e2.getK2();
            }
        };
        ArrayList<Integer> listIDs = getIDsNodesByDegree();
        ArrayList<Integer> novaListaIDs = new ArrayList();
        ArrayList<JNode> temp = new ArrayList();

        //##############################
        //ordenar por grau hierachico        
        int oldgrau = getNode(listIDs.get(0)).getK();//1st degree
        temp.add(getNode(listIDs.get(0)));
        for (int pos = 1; pos < listIDs.size(); pos++) {
            int idx = listIDs.get(pos);
            int grau = getNode(idx).getK();

            if (grau > oldgrau) {
                oldgrau = grau;
                Collections.sort(temp, HIERAR_DEGREE_ORDER);//reorder in ascendet order according to the hierachical degree. 
                for (JNode jNode : temp) {
                    novaListaIDs.add(jNode.getCode());
                }
                //reinitialize
                temp = new ArrayList();
            } else {
                temp.add(getNode(listIDs.get(pos)));
            }

        }

        return novaListaIDs;

    }

    public int getNumberEdges() {
        int nNeighbors = 0;

        for (Map.Entry<Integer, JNode> entry : nodes.entrySet()) {
            nNeighbors += entry.getValue().getNeighbor().size();
        }

        return nNeighbors / 2;
    }
 

    public double calculateAvgDegree() {
        //Calculando a distribuição dos graus            
        for (int ki = 0; ki < getN(); ki++) {
            avgDegree += getNode(ki).getK();
        }
        avgDegree /= getN();
        return avgDegree;
    }

    /**
     * @return the avgDegree
     */
    public double getAvgDegree() {
        calculateAvgDegree();
        return avgDegree;
    }

    public double[][] getAdjacencyMatrix() {
        double matrix[][] = new double[getN()][getN()];
        for (int x : getNodes().keySet()) {
            int code = getNode(x).getCode();
            for (Integer j : getNode(code).getNeighbor()) {
                matrix[code][j] = 1;
            }
        }
        return matrix;
    }

    /**
     * @param avgDegree the avgDegree to set
     */
    public void setAvgDegree(double avgDegree) {
        this.avgDegree = avgDegree;
    }

    /**
     * @return the clusteringCoefficient
     */
    public double getClusteringCoefficient() {
        return clusteringCoefficient;
    }

    /**
     * @param clusteringCoefficient the clusteringCoefficient to set
     */
    public void setClusteringCoefficient(double clusteringCoefficient) {
        this.clusteringCoefficient = clusteringCoefficient;
    }

    /**
     * @return the avgPathLength
     */
    public double getAvgPathLength() {
        return avgPathLength;
    }

    /**
     * @param avgPathLength the avgPathLength to set
     */
    public void setAvgPathLength(double avgPathLength) {
        this.avgPathLength = avgPathLength;
    }

    /**
     * @return the diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * @param diameter the diameter to set
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * @return the gamma
     */
    public double getGamma() {
        return gamma;
    }

    /**
     * @param gamma the gamma to set
     */
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void saveModelFile(String outFile) {
        try {
            PrintWriter writer = new PrintWriter(outFile, "UTF-8");
            writer.write("avgDegree=" + calculateAvgDegree() + "\n");
            writer.write("==============\n");

            for (int i = 0; i < n; i++) {
                writer.write(nodes.get(i).getCode() + " ");
                ArrayList<Integer> neigh = nodes.get(i).getNeighbor();
                for (int j = 0; j < neigh.size(); j++) {
                    writer.write(nodes.get(neigh.get(j)).getCode() + " ");
                }
                writer.write("\n");
            }
            writer.close();

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.err.println(ex);
        }

    }

    public void removingIsolatedNodesfromNetwork() {
        for (Iterator<Integer> it = getNodes().keySet().iterator(); it.hasNext();) {
            int x = it.next();
            double degree = 1.0d * getNode(x).getNeighbor().size();//get numNeighbors
            if (degree == 0) {
                //remove from neigbors
                it.remove();
            }
        }
    }

    /**
     * Method to add noise on the network, rate 10%, 20% etc This method, add
     * and remove same number of nodes. The degree is mantainedS
     *
     * @param noiseRate
     */
    public void noiseNetwork(int noiseRate) {
        int nNodes = getNodes().size();
        int nEdges = getNumberEdges();
        int nNoiseEdges = (noiseRate * nEdges) / 100;
        Random random = new Random(104711L);

        // acrescentando nNoiseEdges/2 arestas
        int count = 0;
        while (count < nNoiseEdges / 2) {

            // sorteio de dois nros entre 1 e nNodes
            int nodeA = random.nextInt(nNodes);
            int nodeB = random.nextInt(nNodes);

            if (nodeA != nodeB) {
                if (!getNodes().get(nodeA).isNeighbor(nodeB)) {
                    getNode(nodeA).addNeighbor(nodeB);
                    getNode(nodeB).addNeighbor(nodeA);
                }
                count++;
            }
        }

        // removendo nNoiseEdges/2 arestas
        count = 0;
        while (count < nNoiseEdges / 2) {
            int nodeA = random.nextInt(nNodes);
            int sizeA = getNode(nodeA).getNeighbor().size();
            if (sizeA != 0) {
                int positionB = random.nextInt(sizeA);
                int nodeB = getNode(nodeA).getNeighbor().get(positionB);

                getNode(nodeA).deleteNeighbor((Integer) nodeB);
                getNode(nodeB).deleteNeighbor((Integer) nodeA);

                count++;
            }
        }
    }

}
