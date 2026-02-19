package CA.graph.mainEstomatos;



import CA.graph.AbstractCAGraph;
import CA.graph.FeatureExtractorCAGraph;
import CA.graph.LifeLikeCAGraph;
import Utils.CUtil;
import Utils.IO;
import Utils.OpenNetworkFromCentroidsFiles;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import java.io.IOException;
import java.util.ArrayList;
import CA.graph.Model;

/**
 *
 * @author Jeaneth Machicao
 */
public class TEP_SpatioTimeFromStomataNetworks extends Thread {

    private static final String path = "/NetworkAutomata/RedesDB/DatasetEstomatos/LightConditions (zebrina)/validation/";
    private static final String outpath = "/Users/jeaneth/Documents/STDs/RedesEstomatos/";

    private static final String[] classes = {"natural", "4horas", "24horas"};    
    private static final int numLinesHeader = 0;
    private final String rulesDB[];

    public TEP_SpatioTimeFromStomataNetworks(String _rulesDB[]) {
        rulesDB = _rulesDB;
    }

    public static void main(String[] args) throws Exception {
        mainProposed();
    }

    public static void mainProposed() throws Exception {

        String rules1[] = {"B12345-S04568"};
        String rules2[] = {"B01346-S0257"};
        String rules3[] = {"B3567-S0468"};
        String rules4[] = {"B0568-S1578"};
        String rules5[] = {"B125678-S012345"};
        String rules6[] = {"B34-S024568"};
        String rules7[] = {"B35678-S012578"};
        String rules8[] = {"B02345-S01456"};
        String rules9[] = {"B056-S0348"};
        String rules10[] = {"B245-S0234568"}; 


        TEP_SpatioTimeFromStomataNetworks thread1 = new TEP_SpatioTimeFromStomataNetworks(rules1);
        TEP_SpatioTimeFromStomataNetworks thread2 = new TEP_SpatioTimeFromStomataNetworks(rules2);
        TEP_SpatioTimeFromStomataNetworks thread3 = new TEP_SpatioTimeFromStomataNetworks(rules3);
        TEP_SpatioTimeFromStomataNetworks thread4 = new TEP_SpatioTimeFromStomataNetworks(rules4);
        TEP_SpatioTimeFromStomataNetworks thread5 = new TEP_SpatioTimeFromStomataNetworks(rules5);
        TEP_SpatioTimeFromStomataNetworks thread6 = new TEP_SpatioTimeFromStomataNetworks(rules6);
        TEP_SpatioTimeFromStomataNetworks thread7 = new TEP_SpatioTimeFromStomataNetworks(rules7);
        TEP_SpatioTimeFromStomataNetworks thread8 = new TEP_SpatioTimeFromStomataNetworks(rules8);
        TEP_SpatioTimeFromStomataNetworks thread9 = new TEP_SpatioTimeFromStomataNetworks(rules9);
        TEP_SpatioTimeFromStomataNetworks thread10 = new TEP_SpatioTimeFromStomataNetworks(rules10);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread10.start();

    }

    @Override
    public void run() {
        extracFeatures();
    }

    public void extracFeatures() {
        for (String r : rulesDB) {
            extractProposedFeatures(r);
        }
    }

    public static void extractProposedFeatures(String rule) {

        int T = 350;
        CUtil.criateFolder(outpath + rule);

        for (int i = 0; i < classes.length; i++) {
            String networkClass = classes[i];
            String networksPath = path + classes[i] + "/";
            IO io = new IO(networksPath, ".txt");
            String[] fileName = io.getNames();//regular expression
            for (int f = 0; f < fileName.length; f++) {
                String net = fileName[f];
                System.out.println("Network :\t" + classes[i] + "\trede\t" + net);
                for (int Threshold = 50; Threshold <= 200; Threshold = Threshold + 10) {
                    Model cn1 = OpenNetworkFromCentroidsFiles.open(io.getDirectory() + fileName[f], Threshold);
                    Model cn = Model.newInstance(cn1);
                    AbstractCAGraph AC = new LifeLikeCAGraph(cn, rule);
                    AC.random(0.51);
                    AC.step(T - 1);
                    ArrayList<Integer> iteratorDegreeOrder = cn.getDegreeByDegreeOrder();
                    double[] grau = iteratorDegreeOrder.stream().mapToDouble(j -> j).toArray();
                    double spatiotime[][] = FeatureExtractorCAGraph.extractSpatioTimeDiagram(AC);
                    //saveMatlabFile(spatiotime, grau, outpath + rule + "/" + networkClass + "_" + net +"_"+Threshold+ ".mat");
                    saveMatlabFile(spatiotime, grau, outpath + rule + "/" + networkClass + "_spec" +f+"_"+Threshold+ ".mat");
                }
            }

        }
        System.out.println("done!");

    }

    public static void saveMatlabFile(double[][] matrix, double[] degree, String output) {
        MLDouble ML_matrix = new MLDouble("spatiotime", matrix);
        MLDouble ML_grau = new MLDouble("degree", degree, degree.length);

        //2. write arrays to file
        ArrayList<MLArray> ML_Lista = new ArrayList<>();
        ML_Lista.add(ML_matrix);
        ML_Lista.add(ML_grau);

        try {
            MatFileWriter matFileWriter = new MatFileWriter(output, ML_Lista); //"features.mat"
        } catch (IOException ex) {
            System.err.println("");
        }
    }
}
