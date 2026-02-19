package CA.graph.mainEstomatos;

import Utils.CUtil;
import Utils.Caracterization.Feature;
import Utils.Caracterization.JWekaClassifier;
import Utils.IO;
import CA.graph.AbstractCAGraph;
import CA.graph.FeatureExtractorCAGraph;
import CA.graph.LifeLikeCAGraph;
import Utils.OpenNetworkFromCentroidsFiles;
import java.util.ArrayList;
import methods.complexNetwork.ComplexNetworkFeatures;
import CA.graph.Model;

/**
 *
 * @author Jeaneth Machicao
 */
public class FeatExtractStomata_Callisia extends Thread {

    private static final String outpath = "ResultadosRedesEstomatos/arffs";
    private static final String path = "RedesDB/DatasetEstomatos/";
    private final String rulesDB[];

    public FeatExtractStomata_Callisia(String _rulesDB[]) {
        rulesDB = _rulesDB;
    }

    public static void main(String[] args) throws Exception {

        String rules1[] = {"B01678-S0457"};
        String rules2[] = {"B035678-S023456"};
        String rules3[] = {"B1245678-S3456"};
        String rules4[] = {"B0157-S457"};
        String rules5[] = {"B35678-S03456"};
        String rules6[] = {"B1245678-S03456"};
        String rules7[] = {"B35678-S13456"};
        String rules8[] = {"B01245678-S3456"};
        String rules9[] = {"B035678-S0123456"};
        String rules10[] = {"B135678-S03456"};

//
        FeatExtractStomata_Callisia thread1 = new FeatExtractStomata_Callisia(rules1);
        FeatExtractStomata_Callisia thread2 = new FeatExtractStomata_Callisia(rules2);
        FeatExtractStomata_Callisia thread3 = new FeatExtractStomata_Callisia(rules3);
        FeatExtractStomata_Callisia thread4 = new FeatExtractStomata_Callisia(rules4);
        FeatExtractStomata_Callisia thread5 = new FeatExtractStomata_Callisia(rules5);
        FeatExtractStomata_Callisia thread6 = new FeatExtractStomata_Callisia(rules6);
        FeatExtractStomata_Callisia thread7 = new FeatExtractStomata_Callisia(rules7);
        FeatExtractStomata_Callisia thread8 = new FeatExtractStomata_Callisia(rules8);
        FeatExtractStomata_Callisia thread9 = new FeatExtractStomata_Callisia(rules9);
        FeatExtractStomata_Callisia thread10 = new FeatExtractStomata_Callisia(rules10);
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
        
//=============================================
//(2) classify
//=============================================
//        String databaseFile = "DATABASE_RULES.txt";
//        try {
//            BufferedReader File = new BufferedReader(new FileReader(new File(databaseFile)));
//            String rule;
//            while ((rule = File.readLine()) != null) {
//                runWekaClassification(outpath + "/" + rule + "/");
//            }
//        } catch (IOException e) {
//            System.err.println(e);
//        }
        
//=============================================
//(1-2) extract classical features and classify
//=============================================
//        FeatExtractStomata_Callisia.extractClassicalFeatures();
       //runWekaClassification("ResultadosRedesEstomatos/classicalarffs/");
    }

    @Override
    public void run() {
        extracFeatures();
    }

    public static void runWekaClassification(String ruleFolder) throws Exception {

        IO io = new IO(ruleFolder, ".arff");
        String arffFiles[] = io.getFiles();
        String arffNames[] = io.getNames();
        String directory = io.getDirectory();
        for (int i = 0; i < arffFiles.length; i++) {
            String arffName = arffNames[i].replace(".arff", ""); //removinf .arff extension
            System.out.print("classifying arffs " + arffName);
            JWekaClassifier.runClassification(JWekaClassifier.KNN, arffFiles[i], directory + "/__" + arffName + "_KNN.csv");
//            JWekaClassifier.runClassification(JWekaClassifier.J48, arffFiles[i], directory + "/__" + arffName + "_J48.csv");
//            JWekaClassifier.runClassification(JWekaClassifier.NAIVEBAYES, arffFiles[i], directory + "/__" + arffName + "_NB.csv");
            //JWekaClassifier.runClassification(JWekaClassifier.BAYESNET, arffFiles[i], directory + "/__" + arffName + "_BayesNet.csv");

            System.out.println();
        }

    }

    public void extracFeatures() {
        for (String r : rulesDB) {
            extractProposedFeatures(r);
        }
    }

    public static void extractProposedFeatures(String rule) {

        String networkClasses[] = {"ambiente", "umidade90", "umidade90CO2"};
        String networkAcronClasses[] = {"AMB", "UM90", "CO2"};
        int T = 350;

        System.out.println("Rule:\t" + rule);
        CUtil.criateFolder(outpath + "/" + rule);
        ArrayList<Feature> featuresSamplesDistribution = new ArrayList<>();
        ArrayList<Feature> featuresSamplesMedias = new ArrayList<>();
        for (int i = 0; i < networkClasses.length; i++) {
            System.out.println("Network :\t" + networkClasses[i]);
            String networkClass = networkAcronClasses[i];
            int networkClassIndex = i;
            String networksPath = path + networkClasses[i] + "/";
            IO io = new IO(networksPath, ".txt");
            String[] fileName = io.getNames();//regular expression

            for (int f = 0; f < fileName.length; f++) {

                ArrayList<Double> distribEntropy = new ArrayList<>();
                ArrayList<Double> distribLempelZiv = new ArrayList<>();

                ArrayList<Double> meanEntropy = new ArrayList<>();
                ArrayList<Double> meanLempelZiv = new ArrayList<>();
                for (int Threshold = 50; Threshold <= 200; Threshold = Threshold + 30) {
                    Model cn = OpenNetworkFromCentroidsFiles.open(io.getDirectory() + fileName[f], Threshold);
                    AbstractCAGraph AC = new LifeLikeCAGraph(cn, rule);
                    AC.random(0.51);
                    AC.step(T - 1);
                    double featEntropy[] = FeatureExtractorCAGraph.runEntropyShannon(AC);
                    double featLZ[] = FeatureExtractorCAGraph.runLempelZiv(AC);
                    int numBins = 20;

                    double featsHistoEntropy[] = Feature.histogramBinRange(featEntropy, numBins, 0.0d, 1.0d);//Freq/N(#nodes)
                    double featsMeanStdEntropy[] = Feature.meanStd(featEntropy);

                    double featsHistoLZ[] = Feature.histogramBinRange(featLZ, numBins, 0.4d, 1.4d);//Freq/N(#nodes)
                    double featsMeanStdLZ[] = Feature.mean(featLZ);//meanStd                    

                    for (int j = 0; j < featsHistoLZ.length; j++) {
                        featsHistoEntropy[j] = featsHistoEntropy[j] / cn.getN();
                        featsHistoLZ[j] = featsHistoLZ[j] / cn.getN();

                        distribEntropy.add(featsHistoEntropy[j]);
                        distribLempelZiv.add(featsHistoLZ[j]);
                    }
                    meanEntropy.add(featsMeanStdEntropy[0]);
                    meanLempelZiv.add(featsMeanStdLZ[0]);
                }
                //concatenar distEntropy + distribLempelZiv [xi,....xt,....Li,...Lt]
                ArrayList<Double> allDistrib = new ArrayList<>();
                allDistrib.addAll(distribEntropy);
                allDistrib.addAll(distribLempelZiv);

                ArrayList<Double> allMeans = new ArrayList<>();
                allMeans.addAll(meanEntropy);
                allMeans.addAll(meanLempelZiv);

                featuresSamplesDistribution.add(new Feature(CUtil.arrayListToDoubleArray(allDistrib), networkClass, networkClassIndex));
                featuresSamplesMedias.add(new Feature(CUtil.arrayListToDoubleArray(allMeans), networkClass, networkClassIndex));
            }
        }
        String file = outpath + "/" + rule + "/" + rule;

        Feature.saveArffFile(featuresSamplesMedias, (file + "_medias.arff"), rule+"_medias");
        Feature.saveArffFile(featuresSamplesDistribution, (file + "_distrib.arff"), rule+"_distrib");

        //Feature.saveMatlabFile(featuresSamplesMedias, file + "_medias.mat");
        //Feature.saveMatlabFile(featuresSamplesDistribution, file + "_distribution.mat");

    }

    public static void extractClassicalFeatures() {
        String networkClasses[] = {"ambiente", "umidade90", "umidade90CO2"};
        String networkAcronClasses[] = {"AMB", "UM90", "CO2"};
        


        ArrayList<Feature> featuresSamples = new ArrayList<>();

        for (int i = 0; i < networkClasses.length; i++) {
            System.out.print("Network :\t" + networkClasses[i]);
            String networkClass = networkAcronClasses[i];
            int networkClassIndex = i;
            String networksPath = path + networkClasses[i] + "/";
            IO io = new IO(networksPath, ".txt");
            String[] fileName = io.getNames();
            for (int f = 0; f < fileName.length; f++) {
                System.out.println(fileName[f]);
                ArrayList<Double> featThresholdConcat = new ArrayList<>();
                for (int Threshold = 50; Threshold <= 200; Threshold = Threshold + 30) {
                    Model oldcn = OpenNetworkFromCentroidsFiles.open(io.getDirectory() + fileName[f], Threshold);
                    Model cn = Model.newInstance(oldcn);
                    System.out.print("Threshold="+Threshold+"\t" + cn.getN() + "\t"+cn.getNumberEdges()+"\t");
                    Feature ftemp = new ComplexNetworkFeatures(cn, 6);
                    ftemp.print();
                    featThresholdConcat.addAll(ftemp.getFeaturesArrayList());
                }
                Feature feat = new Feature(CUtil.arrayListToDoubleArray(featThresholdConcat), networkClass);
                feat.setClassIdx(networkClassIndex);
                featuresSamples.add(feat);
            }
        }
        String file = "ResultadosRedesEstomatos/classicalarffs/feats";
        Feature.saveMatlabFile(featuresSamples, file + "_classic.mat");
        Feature.saveArffFile(featuresSamples, (file + "_classic.arff"), "classical");

    }

}
