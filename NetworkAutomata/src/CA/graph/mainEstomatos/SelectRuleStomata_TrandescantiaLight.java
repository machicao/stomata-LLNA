package CA.graph.mainEstomatos;

import CA.graph.AbstractCAGraph;
import CA.graph.FeatureExtractorCAGraph;
import CA.graph.LifeLikeCAGraph;
import Utils.CUtil;
import Utils.Caracterization.Feature;
import Utils.Caracterization.JWekaClassifier;
import Utils.IO;
import Utils.OpenNetworkFromCentroidsFiles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import CA.graph.Model;
import org.apache.commons.io.FileUtils;
import weka.core.Utils;

/**
 * @author Jeaneth Machicao
 */
public class SelectRuleStomata_TrandescantiaLight extends Thread {

    private static final String outpath = "ResultadosRedesEstomatos/LightConditions/arffs";
    private static final String path = "RedesDB/DatasetEstomatos/LightConditions/ruleselection/";

    static String networkClasses[] = {"natural", "4horas", "24horas"};
    static String networkAcronClasses[] = {"NAT", "L4", "L24"};

    private final String rulesDB[];
    private final String threadName;

    public SelectRuleStomata_TrandescantiaLight(String _rulesDB[], String thread) {
        rulesDB = new String[_rulesDB.length];
        System.arraycopy(_rulesDB, 0, rulesDB, 0, _rulesDB.length);
        threadName = thread;
    }

    private static ArrayList<String> divideConquer(String rulesFile) {
        ArrayList<String> listRules = new ArrayList<>();
        try {
            BufferedReader File = new BufferedReader(new FileReader(new File(rulesFile)));
            String rule;
            while ((rule = File.readLine()) != null) {
                listRules.add(rule);
            }
        } catch (IOException e) {
            System.err.println(e + "divideConquer");
        }
        return listRules;
    }

    public static void main(String[] args) throws Exception {

        //extractClassicalFeatures();
        for (int r = 1; r <= 4; r++) {
            String main = "ResultadosRedesEstomatos/LightConditions/regrasfaltantes" + r + ".txt";
            mainThreads(main);
        }

    }

    public static void mainRules(String[] args) throws Exception {

        String rules[] = {
            "B12345678-S0123568",
            "B024678-S123468",
            "B1568-S3578",
            "B12367-S1235",
            "B24-S1256",
            "B1358-S1367",
            "B34678-S34568",
            "B01245678-S348",
            "B0123468-S0368",
            "B2457-S1278"

        };

        for (String r : rules) {
            System.out.println(extractProposedFeatures(r));
        }
    }

    public static void mainThreads(String fileRule) throws Exception {

        String main = fileRule;
        System.out.println("Running ESTOMATOS with set " + main);
        ArrayList<String> DB = divideConquer(main);
        int numberRules = DB.size(); //1002                
        int bins = 6;
        List<List<String>> splittedDB = CUtil.split(DB, bins);
        int size = (int) (numberRules / bins);//200
        int rest = DB.size() % bins;//2
        String rules1[] = new String[size];
        String rules2[] = new String[size];
        String rules3[] = new String[size];
        String rules4[] = new String[size];
        String rules5[] = new String[size];
        String rules6[] = new String[size + rest];

        rules1 = splittedDB.get(0).toArray(rules1);
        rules2 = splittedDB.get(1).toArray(rules2);
        rules3 = splittedDB.get(2).toArray(rules3);
        rules4 = splittedDB.get(3).toArray(rules4);
        rules5 = splittedDB.get(4).toArray(rules5);
        rules6 = splittedDB.get(5).toArray(rules6);

        //garbage
        //DB = null;
        //splittedDB = null;
        //-------------------------------------
        SelectRuleStomata_TrandescantiaLight thread1 = new SelectRuleStomata_TrandescantiaLight(rules1, main + "_thread1.txt");
        SelectRuleStomata_TrandescantiaLight thread2 = new SelectRuleStomata_TrandescantiaLight(rules2, main + "_thread2.txt");
        SelectRuleStomata_TrandescantiaLight thread3 = new SelectRuleStomata_TrandescantiaLight(rules3, main + "_thread3.txt");
        SelectRuleStomata_TrandescantiaLight thread4 = new SelectRuleStomata_TrandescantiaLight(rules4, main + "_thread4.txt");
        SelectRuleStomata_TrandescantiaLight thread5 = new SelectRuleStomata_TrandescantiaLight(rules5, main + "_thread5.txt");
        SelectRuleStomata_TrandescantiaLight thread6 = new SelectRuleStomata_TrandescantiaLight(rules6, main + "_thread6.txt");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
    }

    @Override
    public void run() {
        extracFeatures();
    }

    public void extracFeatures() {
        String outputFile = outpath + "/" + threadName;
        for (String r : rulesDB) {
            try {
                FileUtils.writeStringToFile(new File(outputFile), extractProposedFeatures(r), true);
            } catch (IOException ex) {
                System.err.println(ex + "FileUtils.writeStringToFile");
            }
        }

    }

    public static String extractProposedFeatures(String rule) {
        //CUtil.criateFolder(outpath + rule);
        int T = 350;
        int Threshold = 110;
        ArrayList<Feature> featuresSamplesDistribution = new ArrayList<>();
        for (int i = 0; i < networkClasses.length; i++) {
//            System.out.println("Network :\t" + networkClasses[i]);
            String networkClass = networkAcronClasses[i];
            int networkClassIndex = i;

            String networksPath = path + networkClasses[i] + "/";
            IO io = new IO(networksPath, ".txt");
            String[] fileName = io.getNames();//regular expression

            for (int f = 0; f < fileName.length; f++) {
                Model cn = OpenNetworkFromCentroidsFiles.open(io.getDirectory() + fileName[f], Threshold);
                AbstractCAGraph AC = new LifeLikeCAGraph(cn, rule);
                AC.random(0.51);
                AC.step(T - 1);
                ArrayList<Double> distribEntropy = new ArrayList<>();
                ArrayList<Double> distribLempelZiv = new ArrayList<>();

                double featEntropy[] = FeatureExtractorCAGraph.runEntropyShannon(AC);
                double featLZ[] = FeatureExtractorCAGraph.runLempelZiv(AC);
                int numBins = 20;

                double featsHistoEntropy[] = Feature.histogramBinRange(featEntropy, numBins, 0.0d, 1.0d);//Freq/N(#nodes)
                double featsHistoLZ[] = Feature.histogramBinRange(featLZ, numBins, 0.4d, 1.4d);//Freq/N(#nodes)       

                for (int j = 0; j < featsHistoLZ.length; j++) {
                    featsHistoEntropy[j] = featsHistoEntropy[j] / cn.getN();
                    featsHistoLZ[j] = featsHistoLZ[j] / cn.getN();

                    distribEntropy.add(featsHistoEntropy[j]);
                    distribLempelZiv.add(featsHistoLZ[j]);
                }

                //concatenar distEntropy + distribLempelZiv [xi,....xt,....Li,...Lt]
                ArrayList<Double> allDistrib = new ArrayList<>();
                allDistrib.addAll(distribEntropy);
                allDistrib.addAll(distribLempelZiv);

                featuresSamplesDistribution.add(new Feature(CUtil.arrayListToDoubleArray(allDistrib), networkClass, networkClassIndex));
            }
        }

        String arffFile = outpath + "/" + rule + "_distrib.arff";

        Feature.saveArffFile(featuresSamplesDistribution, (arffFile), "distributionSLZ");

        String result = "Rule:\t" + rule;
        String classificador = "KNN-1";

        double rate = 0.;
        int kfold = 2;//rule-selection
        try {
            rate = JWekaClassifier.calculateRate(classificador, arffFile, kfold);

        } catch (Exception ex) {
            System.err.println(ex);
        }
        String ratio = Utils.doubleToString(rate, 3, 2);

        result += "\t" + ratio;

        removingFile(arffFile);//removing arffFile

        System.out.println(result);
        return result + "\n";
    }

    public static void removingFile(String file) {
        new File(file).delete();
    }

}
