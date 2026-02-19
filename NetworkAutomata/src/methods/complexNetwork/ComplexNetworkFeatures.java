package methods.complexNetwork;

import CA.graph.Model;
import CA.graph.JNode;
import Utils.Caracterization.Feature;
import Utils.OpenNetworkEdgeList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jeaneth Machicao
 */
public class ComplexNetworkFeatures extends Feature {

    public static double PearsonDegreeCorrelation(Model cn) {
        HashMap<Integer, JNode> adjList = cn.getNodes();
        ArrayList<Integer> visited = new ArrayList<>();

        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;

        double nedges = 0;

        for (Map.Entry<Integer, JNode> entry : adjList.entrySet()) {
            int key = entry.getKey();

            JNode nodeNeighbors = entry.getValue();
            double degree_node = nodeNeighbors.getK();

            for (int i = 0; i < nodeNeighbors.getNeighbor().size(); i++) {
                int neighbor = nodeNeighbors.getNeighbor().get(i);

                if (!visited.contains(neighbor) && key != neighbor) {

                    JNode neighborNeighbors = adjList.get(neighbor);

                    double degree_neighbor = neighborNeighbors.getK();

                    sum1 = sum1 + (degree_node * degree_neighbor);
                    sum2 = sum2 + 0.5 * (degree_node + degree_neighbor);
                    sum3 = sum3 + (0.5 * (Math.pow(degree_node, 2) + Math.pow(degree_neighbor, 2)));
                }
            }
            nedges = nedges + degree_node;
            visited.add(key);
        }

        nedges = nedges / 2;

        double v1 = (1 / nedges * sum1) - (Math.pow(((1 / nedges) * sum2), 2));
        double v2 = (1 / nedges * sum3) - (Math.pow(((1 / nedges) * sum2), 2));

        double r = v1 / v2;

        return r;
    }

    public ComplexNetworkFeatures(Model model, int finalM) {
        super(finalM);
        double r = 0.0d;
        double m1 = 0.0d, m11 = 0.0d;
        double m2 = 0.0d;
        double M = 0.0d;
        JNode nodei;
        JNode nodej;
        //double[] clusteringCoefficients = new double[model.getN()];
        double[] degree2 = new double[model.getN()];
        double[] degree3 = new double[model.getN()];

        //System.out.println(features.length);
        // Calculando degree e degreeWeight
        //for (int i = 0; i < model.getN(); i++) {
        for (int x : model.getNodes().keySet()) {
            nodei = model.getNode(x);
            //clusteringCoefficients[x] = getClusteringCoefficient(model, x);//

            ArrayList<Integer> neigh = nodei.getNeighbor();
            for (int j = 0; j < neigh.size(); j++) {
                nodej = model.getNode(neigh.get(j));
                degree2[x] += nodej.getNeighbor().size();
                ////nodei.clusteringCoefficient2 = getClusteringCoefficient(model, neigh.at(j));
                if (nodej.getCode() > x) {
                    m11 += (nodei.getNeighbor().size() + nodej.getNeighbor().size()) / 2.0;
                    M++;
                    m1 += nodei.getNeighbor().size() * nodej.getNeighbor().size();
                    m2 += (nodei.getNeighbor().size() * nodei.getNeighbor().size() + nodej.getNeighbor().size() * nodej.getNeighbor().size()) / 2;
                }

                ArrayList<Integer> neighj = nodej.getNeighbor();
                for (int k = 0; k < neighj.size(); k++) {
                    //if(model.getNode(neighj.get(k)).isNeighbor(i)) {
                    //    clusteringCoefficients[i] ++;
                    //}
                    degree3[x] += model.getNode(neighj.get(k)).getNeighbor().size();
                }
            }

            features[0] += nodei.getNeighbor().size();
            features[1] += degree2[x];
            features[2] += degree3[x];
            //features[3] += clusteringCoefficients[x];

            //if(i%10 == 0) System.out.println(i +"");
        }

        features[0] /= model.getN();
        features[1] /= model.getN();
        features[2] /= model.getN();
        //features[3] /= model.getN();
        //features[4] = getAvgShortestPath(model); //!!!!ATIVAR DEPOIS
        features[3] = getAvgShortestPath(model); //!!!!ATIVAR DEPOIS

        featuresNames[0] = "MEAN_DEGREE";
        featuresNames[1] = "MEAN_HIERC_DEGREE_2";
        featuresNames[2] = "MEAN_HIERC_DEGREE_3";
        //featuresNames[3] = "MEAN_CLUSTERING_COEFFICIENT";
        //featuresNames[4] = "AVG_SHORTEST_PATH";
        featuresNames[3] = "AVG_SHORTEST_PATH";

//        m11 /= M;
//        m1 = m1 - M * m11;
//        m2 = m2 - M * m11;
//        m1 /= M;
//        m2 /= M;
////        r = m1 / m2;
        //features[5] = r;

        //Gisele Pearson Correlation Method
        //features[5] = PearsonDegreeCorrelation(model);

        //featuresNames[5] = "PEARSON_CORRELATION";
        
        features[4] = PearsonDegreeCorrelation(model);
        featuresNames[4] = "PEARSON_CORRELATION";
        
 
        
    }

    private double getClusteringCoefficient(Model model, int nod) {
        double ci = 0.0d;
        JNode node = model.getNode(nod);
        ArrayList<Integer> neigh = node.getNeighbor();
        for (int j = 0; j < neigh.size(); j++) {
            for (int k = j + 1; k < neigh.size(); k++) {
                if (model.getNode(neigh.get(j)).isNeighbor(neigh.get(k))) {
                    ci++;
                }
            }
        }

        if (node.getK() > 1) {
            return (2 * ci) / (node.getK() * (node.getK() - 1));
        } else {
            return 0.0d;
        }
    }

    private double getAvgShortestPath(Model model) {
//        double avg = Dikjstra.avgShortestPath(model);
//        return avg; 
        double avg = Dikjstra.averagePathLength(model);
        return avg;
    }

    public static void mainX(String[] args) {
        int avgK = 10;
        int m = avgK / 2;
        int m0 = m + 1;
        //Model model = new ScaleFreeNetwork(1000, m0, m, true);
        //Model n = new ScaleFreeNetwork(1000, m0, m, true);
        //Model n = new OpenNetworkEdgeList().openIgnoringHeaders("redesDB/erdos/erdos_n=500_k=4_i=1.txt");
        //4.0	0.0	16.728	0.0	69.904	0.0	0.27493333899974826	0.9641717076301575

        //Model old = new OpenNetworkEdgeList().openIgnoringHeaders("redesDB/erdos/erdos_n=500_k=4_i=3.txt");
        Model old = new OpenNetworkEdgeList().open("redesDB/erdos/erdos_n=500_k=4_i=8.txt");
        //Model old = new OpenNetworkEdgeList().openIgnoringHeaders("redesDB/teste.txt");
        //Model old = new OpenNetworkEdgeList().openIgnoringHeaders("redesDB/watts/watts_n=500_k=4_p=0.1_i=1.txt");
        //Model old = OpenNetworkFromCentroidsFiles.openIgnoringHeaders("RedesDB/DatasetEstomatos/umidade90/CO2H3L_centroids.txt", 100);
        Model n = Model.newInstance(old);
        ComplexNetworkFeatures c = new ComplexNetworkFeatures(n, 6);
        c.print();

    }
}