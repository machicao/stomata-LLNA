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
import weka.core.Utils;

/**
 *
 * @author Jeaneth Machicao
 */
public class SelectRuleStomata_CalisiaZebrina extends Thread {

    private static final String outpath = "ResultadosRedesEstomatos/Calisia+Zebrina/arffs";
    private static final String path = "RedesDB/DatasetEstomatos/Calisia+Zebrina/";
    private final String rulesDB[];

    public SelectRuleStomata_CalisiaZebrina(String _rulesDB[]) {
        rulesDB = _rulesDB;
    }

    public static void main(String[] args) throws Exception {
//
        String rules1[] = {"B-S02", "B-S78", "B-S027", "B-S028", "B-S0126", "B-S0278", "B-S01278", "B-S012378", "B0-S0347", "B0-S2358", "B0-S134578", "B0-S0134568", "B0-S01345678", "B3-S37", "B3-S458", "B3-S1678", "B3-S01456", "B3-S03567", "B3-S015678", "B3-S025678", "B6-S13457", "B7-S02", "B7-S15", "B7-S28", "B7-S78", "B7-S027", "B7-S028", "B7-S0127", "B7-S0278", "B7-S1248", "B7-S01245", "B7-S01247", "B7-S01278", "B7-S012478", "B7-S045678", "B7-S145678", "B7-S0123578", "B8-S8", "B8-S28", "B8-S78", "B8-S028", "B8-S123", "B8-S278", "B8-S0178", "B8-S0278", "B8-S1237", "B02-S134568", "B02-S345678", "B02-S1234568", "B05-S35678", "B06-S01268", "B06-S01346", "B07-S8", "B07-S48", "B07-S0148", "B08-S123", "B08-S5678", "B08-S13456", "B08-S012356", "B08-S0134568", "B08-S01345678", "B12-S012347", "B14-S1268", "B14-S012456", "B14-S024568", "B16-S02345", "B24-S04567", "B24-S12456", "B24-S012456", "B25-S2345678", "B28-S78", "B28-S012356", "B34-S13", "B34-S2456", "B34-S01678", "B34-S02456", "B34-S024568", "B35-S12456", "B36-S", "B36-S06", "B36-S58", "B36-S345678", "B37-S0138", "B37-S0236", "B37-S1234", "B37-S12347", "B37-S23457", "B37-S012348", "B38-S268", "B38-S0345", "B38-S1678", "B38-S02367", "B47-S12357"};
        String rules2[] = {"B48-S034567", "B57-S378", "B57-S0378", "B57-S01278", "B58-S0378", "B67-S1248", "B67-S2368", "B67-S01256", "B67-S23678", "B68-S012478", "B78-S8", "B78-S02", "B78-S27", "B78-S28", "B78-S78", "B78-S027", "B78-S028", "B78-S278", "B78-S0157", "B78-S0278", "B78-S1248", "B78-S012456", "B78-S012458", "B78-S024578", "B012-S017", "B012-S0137", "B013-S23467", "B014-S04567", "B016-S58", "B016-S468", "B016-S2345", "B016-S23457", "B016-S023457", "B016-S023458", "B234568-S034578", "B234578-S0123678", "B0123478-S03678", "B0123568-S01237", "B0134578-S014678", "B0134578-S01234568", "B0134678-S4678", "B0235678-S012578", "B0235678-S1234567", "B01234567-S02345", "B01234567-S02678", "B01234568-S024567", "B01234578-S45678", "B017-S23568", "B018-S1258", "B018-S15678", "B036-S034567", "B036-S01345678", "B038-S023468", "B067-S13458", "B068-S01268", "B068-S13458", "B068-S013467", "B078-S8", "B078-S25678", "B167-S2348", "B167-S01248", "B167-S034568", "B167-S0234578", "B167-S012345678", "B168-S01245", "B347-S0123678", "B356-S0158", "B356-S2378", "B356-S2567", "B356-S01567", "B357-S014", "B357-S125", "B357-S168", "B467-S0145", "B467-S01458", "B467-S123578", "B468-S03478", "B478-S1367", "B478-S01268"};
        String rules3[] = {"B0126-S1278", "B0127-S1234", "B0127-S1235678", "B0148-S2378", "B0156-S135678", "B0237-S2345", "B0237-S23458", "B0237-S34568", "B0238-S04567", "B0238-S25678", "B0245-S0578", "B0267-S123468", "B0268-S1245678", "B0278-S012468", "B0345-S02456", "B0345-S12456", "B0378-S12345", "B0456-S056", "B0456-S123678", "B0578-S023578", "B0678-S0134567", "B1234-S12456", "B1234-S0124568", "B1256-S0145678", "B1348-S124567", "B1358-S04678", "B1358-S235678", "B1567-S0125", "B1567-S0126", "B1567-S0235", "B2357-S234567", "B2567-S13568", "B3567-S017", "B3567-S025", "B3567-S0256", "B3567-S0457", "B3567-S0468", "B3567-S1268", "B3567-S2567", "B3567-S2568", "B3567-S013678", "B3568-S167", "B3568-S0147", "B3568-S0256", "B3568-S1267", "B3568-S1268", "B3568-S1457", "B3568-S2567", "B01234-S023456", "B01235-S02", "B01235-S14568", "B01235-S014567", "B01235-S124568", "B01237-S1245", "B01267-S35", "B01267-S123457", "B01267-S0123467", "B01567-S0146", "B01567-S1257", "B01568-S78", "B01568-S3467", "B01578-S013", "B02368-S0134578", "B02568-S023468", "B02578-S01346", "B02578-S245678", "B03578-S014678", "B03678-S035", "B12347-S0134567", "B14578-S0345678", "B14678-S0345678", "B15678-S0126", "B15678-S0127"};
        String rules4[] = {"B23578-S1234567", "B34678-S056", "B35678-S025", "B35678-S126", "B35678-S0258", "B35678-S0268", "B35678-S1467", "B35678-S2367", "B35678-S012578", "B45678-S012345678", "B012345-S026", "B012378-S023458", "B012378-S123457", "B012568-S0145678", "B012578-S346", "B012678-S0126", "B012678-S012348", "B012678-S123468", "B012678-S0123468", "B013678-S058", "B014567-S3678", "B014567-S03678", "B014567-S345678", "B014567-S1245678", "B014567-S01245678", "B023467-S14567", "B034567-S06", "B034567-S1456", "B034568-S6", "B034568-S018", "B123467-S0678", "B123468-S123", "B124678-S1678", "B125678-S012345", "B0123578-S0234567", "B158-S01248", "B158-S01257", "B236-S01245678", "B0146-S034678", "B0235-S0124568", "B0146-S02345678", "B0167-S023", "B0134-S12456", "B0234-S24578", "B0234-S013", "B0134-S012456", "B02378-S235678", "B02378-S0234578", "B02678-S0123458", "B03456-S02678", "B03456-S012578", "B03467-S23678", "B04567-S03678", "B04568-S014678", "B04678-S012345678", "B234-S14568", "B156-S0127", "B157-S34567", "B145-S24568", "B157-S012467", "B47-S0134567", "B48-S15678", "B48-S014578", "B478-S24567", "B478-S012678", "B478-S245678", "B035678-S34567", "B045678-S145678", "B02378-S23458", "B02378-S234578"};
        String rules5[] = {"B0123678-S0135", "B1234678-S023678", "B01345678-S015", "B1234678-S145678", "B0124568-S01457", "B0123457-S0678", "B0234567-S0234578", "B0124568-S0145678", "B0123457-S1234567", "B0234568-S01678", "B02345678-S045", "B0234578-S678", "B12345678-S03678", "B1345678-S1578", "B12345678-S013678", "B1345678-S01578", "B0125678-S025", "B0125678-S12568", "B2345678-S12678", "B0123456-S235", "B0123456-S0235", "B0123456-S1235", "B0145678-S124678", "B0123456-S01235", "B01245678-S01245", "B0124567-S013456", "B0124567-S345678", "B0124567-S0125678", "B0124567-S1345678", "B01245678-S1345678", "B0124568-S678", "B027-S1238", "B126-S0123478", "B126-S0345678", "B027-S04567", "B045-S124568", "B034-S012456", "B128-S345", "B024-S12456", "B027-S34567", "B058-S07", "B045-S0234678", "B045-S1234678", "B035-S124568", "B045-S01234678", "B024-S012456", "B034-S135678", "B057-S024", "B047-S45", "B056-S158", "B057-S468", "B127-S3456", "B128-S01236", "B025-S01", "B056-S0348", "B134-S05678", "B126-S12347", "B124-S12456", "B128-S012356", "B067-S02367", "B245-S0234568", "B235-S12456", "B135-S03456", "B157-S0125", "B235-S024568", "B238-S25678", "B148-S0158", "B156-S012378", "B146-S0345678", "B034678-S23678", "B02357-S013678"};
        String rules6[] = {"B0168-S23457", "B0134-S024568", "B0134-S124568", "B0234-S0125678", "B0145-S0124568", "B0137-S023458", "B0235-S024568", "B0137-S0234578", "B368-S012568", "B578-S37", "B578-S037", "B0124-S345", "B378-S2345678", "B0123-S345", "B578-S378", "B578-S0126", "B346-S234567", "B345-S124568", "B368-S68", "B578-S0378", "B367-S018", "B3678-S67", "B3468-S0567", "B3578-S0157", "B3467-S048", "B3678-S02467", "B4678-S3458", "B3678-S236", "B4678-S4567", "B3458-S0278", "B4567-S01237", "B3678-S12567", "B3578-S1256", "B4678-S01458", "B3456-S24678", "B3467-S0567", "B3678-S012568", "B5678-S01256", "B01237-S1245678", "B01238-S3458", "B01238-S4567", "B01238-S03457", "B01238-S04567", "B01238-S34578", "B01238-S124567", "B01245-S12456", "B01245-S014568", "B01245-S0124568", "B01245-S1345678", "B01246-S145678", "B01248-S01237", "B01268-S12348", "B01278-S026", "B01278-S02348", "B01345-S04568", "B01345-S12456", "B01345-S24568", "B01345-S124568", "B01345-S01345678", "B01346-S0257", "B01347-S014567", "B01378-S23458", "B01378-S023457", "B01457-S0124567", "B01457-S2345678", "B01458-S45678", "B01467-S0345678", "B01467-S01235678", "B02345-S01456", "B02345-S24568", "B02346-S023", "B02347-S024567", "B02347-S124567"};
        String rules7[] = {"B05678-S023", "B05678-S02578", "B05678-S12378", "B05678-S25678", "B05678-S0123567", "B12345-S04568", "B12345-S24568", "B12345-S024568", "B12345-S1345678", "B12357-S2678", "B12357-S134567", "B12368-S145678", "B12378-S02345", "B12378-S23457", "B12378-S012678", "B12378-S123457", "B12378-S234578", "B12378-S0123457", "B12378-S0234578", "B12567-S0135678", "B12678-S0123467", "B12678-S0123468", "B14567-S0123678", "B013467-S27", "B23457-S01578", "B24568-S01678", "B24578-S0124567", "B34567-S01234578", "B34568-S05", "B34568-S168", "B34578-S38", "B012345-S24568", "B012345-S023678", "B012345-S0345678", "B012347-S1456", "B012457-S01245678", "B012467-S03456", "B012467-S345678", "B012478-S024", "B012478-S01234567", "B013456-S1345678", "B013456-S2345678", "B013457-S0123456", "B034568-S012468", "B123678-S04678", "B123678-S23678", "B014568-S45678", "B014678-S1234678", "B014678-S2345678", "B015678-S34567", "B015678-S012678", "B023456-S01235", "B023456-S01678", "B023458-S013678", "B023578-S34678", "B367-S236", "B578-S01267", "B578-S01278", "B567-S01236", "B123456-S01345", "B234567-S01456", "B234567-S123678", "B234567-S234578", "B5678-S134578", "B01278-S023468", "B13567-S1678", "B13568-S2678", "B13568-S12678"};
        String rules8[] = {"B367-S0178", "B346-S146", "B347-S267", "B345-S148", "B378-S1268", "B368-S1278", "B456-S2357", "B367-S1256", "B378-S2678", "B358-S1567", "B678-S012478", "B0123-S124567", "B678-S145678", "B368-S12568", "B378-S23458", "B345-S4568", "B0567-S0248", "B0256-S023678", "B0567-S0346", "B0247-S014568", "B0245-S124568", "B0248-S0235678", "B0247-S1245678", "B0247-S2345678", "B0567-S02347", "B0567-S12357", "B0468-S0234678", "B0348-S25678", "B0478-S3", "B0467-S012345678", "B0568-S45", "B0247-S14567", "B1345-S012456", "B1237-S34568", "B1246-S023678", "B0568-S1578", "B1238-S34578", "B1245-S12456", "B1278-S0123", "B1235-S024568", "B0568-S1245678", "B0568-S02357", "B0568-S02456", "B0568-S02567", "B1267-S12346", "B1345-S12456", "B1235-S345678", "B1268-S0345678", "B1367-S345678", "B1456-S02345678", "B1368-S1345678", "B2368-S012356", "B2457-S024567", "B1568-S01278", "B2378-S0234578", "B1378-S02345", "B2348-S01234678", "B1378-S23457", "B1457-S124678", "B1467-S0134678", "B1367-S014678", "B1467-S0345678", "B3678-S1258", "B3578-S023", "B0458-S14678", "B1245-S0124568", "B1258-S125678", "B2378-S234578", "B23467-S04567", "B012348-S1234568", "B012356-S4678", "B012357-S02678", "B012357-S23678", "B012358-S02"};

        SelectRuleStomata_CalisiaZebrina thread1 = new SelectRuleStomata_CalisiaZebrina(rules1);
        SelectRuleStomata_CalisiaZebrina thread2 = new SelectRuleStomata_CalisiaZebrina(rules2);
        SelectRuleStomata_CalisiaZebrina thread3 = new SelectRuleStomata_CalisiaZebrina(rules3);
        SelectRuleStomata_CalisiaZebrina thread4 = new SelectRuleStomata_CalisiaZebrina(rules4);
        SelectRuleStomata_CalisiaZebrina thread5 = new SelectRuleStomata_CalisiaZebrina(rules5);
        SelectRuleStomata_CalisiaZebrina thread6 = new SelectRuleStomata_CalisiaZebrina(rules6);
        SelectRuleStomata_CalisiaZebrina thread7 = new SelectRuleStomata_CalisiaZebrina(rules7);
        SelectRuleStomata_CalisiaZebrina thread8 = new SelectRuleStomata_CalisiaZebrina(rules8);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();

//=============================================
//(1-2) extract classical features and classify
//=============================================
        //FeatExtractStomata_AlbicansHumidityV2.extractClassicalFeatures();
        //runWekaClassification("ResultadosRedesEstomatos/HumidityConditions/classicalarffs/");
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

        String networkClasses[] = {"zebrina", "calissia"};
        String networkAcronClasses[] = {"zebrina", "calissia"};
        int T = 350;

        //System.out.println("Rule:\t" + rule);
        //CUtil.criateFolder(outpath + "/" + rule);
        ArrayList<Feature> featuresSamplesMedias = new ArrayList<>();
        ArrayList<Feature> featuresSamplesDistributionENTROPY = new ArrayList<>();
        ArrayList<Feature> featuresSamplesDistributionWORD = new ArrayList<>();
        ArrayList<Feature> featuresSamplesDistributionLEMPEL = new ArrayList<>();
        ArrayList<Feature> featuresSamplesDistributionALL = new ArrayList<>();
        for (int i = 0; i < networkClasses.length; i++) {
            //System.out.println("Network :\t" + networkClasses[i]);
            String networkClass = networkAcronClasses[i];
            int networkClassIndex = i;
            String networksPath = path + networkClasses[i] + "/";
            IO io = new IO(networksPath, ".txt");
            String[] fileName = io.getNames();//regular expression
            for (int f = 0; f < fileName.length; f++) {
                ArrayList<Double> distribMedias = new ArrayList<>();
                ArrayList<Double> distribEntropy = new ArrayList<>();
                ArrayList<Double> distribWordL = new ArrayList<>();
                ArrayList<Double> distribLempelZiv = new ArrayList<>();

                for (int Threshold = 50; Threshold <= 200; Threshold = Threshold + 10) { 
                    Model cn = OpenNetworkFromCentroidsFiles.open(io.getDirectory() + fileName[f], Threshold);
                    AbstractCAGraph AC = new LifeLikeCAGraph(cn, rule);
                    AC.random(0.51);
                    AC.step(T - 1);
                    int LIMIT = 40;
                    double featEntropy[] = FeatureExtractorCAGraph.runEntropyShannon(AC);
                    double featLZ[] = FeatureExtractorCAGraph.runLempelZiv(AC);
                    double featWordL[] = FeatureExtractorCAGraph.runWordFrequency(AC, LIMIT);
                    int numBins = 20;
                    double featsHistoEntropy[] = Feature.histogramBinRange(featEntropy, numBins, 0.0d, 1.0d);//Freq/N(#nodes)                    
                    double featsHistoWL[] = Feature.histogramBinRange(featWordL, numBins, 0.0d, 1.0d);//Freq/N(#nodes)
                    double featsHistoLZ[] = Feature.histogramBinRange(featLZ, numBins, 0.4d, 1.4d);//Freq/N(#nodes)                    
                    double featsMeanStdEntropy[] = Feature.meanStd(featEntropy);
                    double featsMeanStdWL[] = Feature.mean(featWordL);//meanStd   
                    double featsMeanStdLZ[] = Feature.mean(featLZ);//meanStd                            

                    for (int j = 0; j < featsHistoLZ.length; j++) {
                        featsHistoEntropy[j] = featsHistoEntropy[j] / cn.getN();
                        featsHistoWL[j] = featsHistoWL[j] / LIMIT;
                        featsHistoLZ[j] = featsHistoLZ[j] / cn.getN();

                        distribEntropy.add(featsHistoEntropy[j]);
                        distribWordL.add(featsHistoWL[j]);
                        distribLempelZiv.add(featsHistoLZ[j]);
                    }
                    distribMedias.add(featsMeanStdEntropy[0]);//Entropy-medias
                    distribMedias.add(featsMeanStdWL[0]);//Word freq - medias
                    distribMedias.add(featsMeanStdLZ[0]);//Lempel Ziv - medias     
                }
                //concatenar distEntropy + distribWL  + distribLempelZiv [xi,....xt,....Li,...Lt] 
                ArrayList<Double> allDistrib = new ArrayList<>();
                allDistrib.addAll(distribEntropy);
                allDistrib.addAll(distribWordL);
                allDistrib.addAll(distribLempelZiv);

                featuresSamplesDistributionENTROPY.add(new Feature(CUtil.arrayListToDoubleArray(distribEntropy), networkClass, networkClassIndex));
                featuresSamplesDistributionWORD.add(new Feature(CUtil.arrayListToDoubleArray(distribWordL), networkClass, networkClassIndex));
                featuresSamplesDistributionLEMPEL.add(new Feature(CUtil.arrayListToDoubleArray(distribLempelZiv), networkClass, networkClassIndex));
                featuresSamplesDistributionALL.add(new Feature(CUtil.arrayListToDoubleArray(allDistrib), networkClass, networkClassIndex));
                featuresSamplesMedias.add(new Feature(CUtil.arrayListToDoubleArray(distribMedias), networkClass, networkClassIndex));

            }
        }
        String arffFileMedia = outpath + "/" + rule + "_mediasEstomatos.arff";
        String arffFile1 = outpath + "/" + rule + "_distribEntropy.arff";
        String arffFile2 = outpath + "/" + rule + "_distribWordL.arff";
        String arffFile3 = outpath + "/" + rule + "_distribLempelZiv.arff";
        String arffFile4 = outpath + "/" + rule + "_distribEstomatosAllDistrib.arff";

        Feature.saveArffFile(featuresSamplesMedias, (arffFileMedia), rule + "_medias");
        Feature.saveArffFile(featuresSamplesDistributionENTROPY, (arffFile1), rule + "distEntropy");
        Feature.saveArffFile(featuresSamplesDistributionWORD, (arffFile2), rule + "distWL");
        Feature.saveArffFile(featuresSamplesDistributionLEMPEL, (arffFile3), rule + "distLZ");
        Feature.saveArffFile(featuresSamplesDistributionALL, (arffFile4), rule + "distSWL");

        String result = "Rule:\t" + rule;
        String classificador = "KNN-1";

        double ratemedia[] = new double[2];
        double rate1[] = new double[2];
        double[] rate2 = new double[2];
        double[] rate3 = new double[2];
        double[] rate4 = new double[2];

        int kfold = 5;//VALIDATION
        int numExperiments = 100;//100
        try {
            ratemedia = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arffFileMedia);
            rate1 = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arffFile1);
            rate2 = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arffFile2);
            rate3 = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arffFile3);
            rate4 = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arffFile4);

        } catch (Exception ex) {
            System.err.println(ex);
        }

        String ratioMedia = Utils.doubleToString(ratemedia[0], 3, 2) + "(\\pm " + Utils.doubleToString(ratemedia[1], 3, 2) + ")";
        String ratio1 = Utils.doubleToString(rate1[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate1[1], 3, 2) + ")";
        String ratio2 = Utils.doubleToString(rate2[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate2[1], 3, 2) + ")";
        String ratio3 = Utils.doubleToString(rate3[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate3[1], 3, 2) + ")";
        String ratio4 = Utils.doubleToString(rate4[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate4[1], 3, 2) + ")";

        result += "\t" + ratio1 + "\t" + ratio2 + "\t" + ratio3 + "\t" + ratio4 + "\t" + ratioMedia;
        //result += "\t" + ratio1 + "\t" + ratio3 + "\t" + ratio4 + "\t" + ratioMedia;

//        removingFile(arffFileMedia);//removing arffFile
//        removingFile(arffFile1);//removing arffFile
//        removingFile(arffFile2);//removing arffFile
//        removingFile(arffFile3);//removing arffFile
//        removingFile(arffFile4);//removing arffFile
        System.out.println(result);
    }

    public static void extractClassicalFeatures() {

        String networkClasses[] = {"zebrina", "calissia"};
        String networkAcronClasses[] = {"zebrina", "calissia"};

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
                for (int Threshold = 50; Threshold <= 200; Threshold = Threshold + 10) {
                    //for (double Threshold = threI; Threshold <= threF; Threshold = Threshold + threSteps) {
                    Model oldcn = OpenNetworkFromCentroidsFiles.open(io.getDirectory() + fileName[f], Threshold);
                    Model cn = Model.newInstance(oldcn);
                    System.out.print("Threshold=" + Threshold + "\t" + cn.getN() + "\t" + cn.getNumberEdges() + "\t");
                    Feature ftemp = new ComplexNetworkFeatures(cn, 6);
                    ftemp.print();
                    featThresholdConcat.addAll(ftemp.getFeaturesArrayList());
                }
                Feature feat = new Feature(CUtil.arrayListToDoubleArray(featThresholdConcat), networkClass);
                feat.setClassIdx(networkClassIndex);
                featuresSamples.add(feat);
            }
        }
        String arrfFile = "ResultadosRedesEstomatos/Calisia+Zebrina/classicalarffs/feats_classic.arff";
        //Feature.saveMatlabFile(featuresSamples, file + "_classic.mat");
        Feature.saveArffFile(featuresSamples, (arrfFile), "classical");

        //classifyin
        String result = "Classical:";
        String classificador = "KNN-1";

        double rate1[] = new double[2];

        int kfold = 5;//VALIDATION
        int numExperiments = 100;//100
        try {
            rate1 = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arrfFile);
        } catch (Exception ex) {
            System.err.println(ex);
        }

        String ratio1 = Utils.doubleToString(rate1[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate1[1], 3, 2) + ")";

        //result += "\t" + ratio1 + "\t" + ratio2 + "\t" + ratio3 + "\t" + ratio4 + "\t" + ratioMedia;
        result += "\t" + ratio1;

        System.out.println(result);

    }

}
