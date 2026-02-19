package methods.complexNetwork;

import CA.graph.Model;
import CA.graph.CNode;
import Utils.Caracterization.Feature;
import java.util.ArrayList;

/**
 *
 * @author Jeaneth Machicao
 */
public class ComplexNetworkFeaturesv2 extends Feature {

    public ComplexNetworkFeaturesv2(Model model, int finalM) {
        super(finalM * 3 + 1);
        int numFeatures = 3;
        float r = 0.0f;
        float m1 = 0.0f, m11 = 0.0f;
        float m2 = 0.0f;
        float M = 0.0f;
        CNode nodei;
        CNode nodej;
        float[] clusteringCoefficients = new float[model.getN()];

        //System.out.println(features.length);
        // Calculando degree e degreeWeight
        for (int i = 0; i < model.getN(); i++) {
            nodei = model.getNode(i);
            clusteringCoefficients[i] = getClusteringCoefficient(model, i);

            ArrayList<Integer> neigh = nodei.getNeighbor();
            for (int j = 0; j < neigh.size(); j++) {
                nodej = model.getNode(neigh.get(j));
                //nodei.clusteringCoefficient2 = getClusteringCoefficient(model, neigh.at(j));
                if (nodej.getCode() > i) {
                    m11 += (nodei.getNeighbor().size() + nodej.getNeighbor().size()) / 2.0;
                    M++;
                    m1 += nodei.getNeighbor().size() * nodej.getNeighbor().size();
                    m2 += (nodei.getNeighbor().size() * nodei.getNeighbor().size() + nodej.getNeighbor().size() * nodej.getNeighbor().size()) / 2;
                }

            }

            features[0] += nodei.getNeighbor().size();
            features[1] += clusteringCoefficients[i];
            //if(i%10 == 0) System.out.println(i +"");
        }

        features[0] /= model.getN();
        features[1] /= model.getN();

        featuresNames[0] = "MEAN_DEGREE";
        featuresNames[1] = "MEAN_CLUSTERING_COEFFICIENT";

        for (int m = 2; m <= finalM; m++) {
            int pos1 = numFeatures * (m - 1);
            for (int i = 0; i < model.getN(); i++) {
                nodei = model.getNode(i);
                features[pos1] += Math.pow(nodei.getNeighbor().size() - features[0], m);
                features[pos1 + 1] += Math.pow(clusteringCoefficients[i] - features[1], m);
            }
            features[pos1] = features[pos1] / model.getN();
            features[pos1 + 1] = features[pos1 + 1] / model.getN();

            featuresNames[pos1] = "M" + m + "_DEGREE";
            featuresNames[pos1 + 1] = "M" + m + "_CLUSTERING_COEFFICIENT";
        }

        features[finalM * numFeatures - 1] = getAvgShortestPath(model);
        featuresNames[finalM * numFeatures - 1] = "AVG_SHORTEST_PATH";

        m11 /= M;
        m1 = m1 - M * m11;
        m2 = m2 - M * m11;
        m1 /= M;
        m2 /= M;
        r = m1 / m2;
        features[finalM * numFeatures] = r;
        featuresNames[finalM * numFeatures] = "PEARSON_CORRELATION";

    }

    private float getClusteringCoefficient(Model model, int nod) {
        float ci = 0.0f;
        CNode node = model.getNode(nod);
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
            return 0.0f;
        }
    }

    private double getAvgShortestPath(Model model) {
        double avg = Dikjstra.avgShortestPath(model);
        return avg;
    }

    public static void Main(String[] args) {
        int avgK = 4;
        int m = avgK / 2;
        int m0 = 3;
        //Model model = new ScaleFreeNetwork(500, m0, m, true);
        //ComplexNetworkFeaturesv2 c = new ComplexNetworkFeaturesv2(model, 1);
        //c.print();
    }
}
