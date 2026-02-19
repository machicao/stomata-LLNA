package Utils.Caracterization;

import Utils.CUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import weka.core.Utils;
import weka.classifiers.Evaluation;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SpreadSubsample;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Standardize;

/**
 *
 * @author Jeaneth Machicao
 */
public class JWekaClassifier {

    private final static String sep = "\t";
    private final static String newline = "\n";
    public final static String SMO = "SupportVectorMachine";
    public final static String J48 = "DecisionTree";
    public final static String KNN = "KNN-1";
    public final static String NAIVEBAYES = "NaiveBayes";
    public final static String BAYESNET = "BayesNet";

    private static Classifier createClassifier(String type) {

        if (type.contains(SMO)) {
            return new SMO();    //super vector machine
        } else if (type.contains(J48)) {
            return new J48();// tree decision
        } else if (type.contains(KNN)) {
            return new IBk(1);// KNN1
        } else if (type.contains(NAIVEBAYES)) {
            return new NaiveBayes();// naive bayes
        } else if (type.contains(BAYESNET)) {
            return new BayesNet();//  bayes net
        }
        return null;
    }

    /**
     * Run Classification: runs any Weka' classifier. Classifier available: SMO,
     * J48, KNN, NAIVEBAYES under 10-cross-validation
     *
     * @param classifierType: KNN, SMO, J48, NAIVEBAYES
     * @param arffFile : ARFF File
     * @param outFile: Results File: ex. output.csv
     * @param applyFilter
     * @throws java.lang.Exception
     */
    public static void runClassification(String classifierType, String arffFile, String outFile) throws Exception {
        File file = new File(arffFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + arffFile + "' does not exist!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(arffFile));

        //dataset reading
        Instances data = new Instances(reader);
        data.setClassIndex(data.numAttributes() - 1);

        //using classifier and  10-cross-validation
        Classifier cls = createClassifier(classifierType);
        Evaluation eval = new Evaluation(data);
        Random rand = new Random(1947498L);  // using seed
        int folds = 10;
        eval.crossValidateModel(cls, data, folds, rand);

        //print results on file
        Writer w = new FileWriter(outFile);
        try (BufferedWriter writer = new BufferedWriter(w)) {
            writer.write(tableMain(data, eval, cls).toString());
        }
        data.delete();
    }

    public static void calculateRateSubSampleFilter(String classifierType, String arffFile, String outFile, int kfold, int seed) throws Exception {
        File file = new File(arffFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + arffFile + "' does not exist!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(arffFile));

        //dataset reading
        Instances data = new Instances(reader);
        data.setClassIndex(data.numAttributes() - 1);

        //applyFilter
        data = applyFilterSubSample(data, seed);

        //using classifier and  10-cross-validation
        Classifier cls = createClassifier(classifierType);
        Evaluation eval = new Evaluation(data);
        Random rand = new Random(1947498L);  // using seed
        int folds = kfold;
        eval.crossValidateModel(cls, data, folds, rand);

 
        data.delete();
    }

    public static double calculateRateSubSampleFilter(String classifierType, String arffFile, int kfold, int seed) throws Exception {
        File file = new File(arffFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + arffFile + "' does not exist!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(arffFile));

        //dataset reading
        Instances data = new Instances(reader);
        data.setClassIndex(data.numAttributes() - 1);
        //apply filter
        data = applyFilterSubSample(data, seed);

        //using classifier and  10-cross-validation
        Classifier cls = createClassifier(classifierType);
        Evaluation eval = new Evaluation(data);
        Random rand = new Random(1947498L);  // using seed
        int folds = kfold;
        eval.crossValidateModel(cls, data, folds, rand);

        data.delete();
        return eval.pctCorrect();
    }

    /**
     * Run Classification with any classifier from WEKA : KNN, J48, BAYESNET,
     * NAIVEBAYES and return the rate in format e.g. 95.6%
     *
     * @param classifierType
     * @param arffFile
     * @param kfold
     * @return
     * @throws Exception
     */
    public static double calculateRate(String classifierType, String arffFile, int kfold) throws Exception {
        File file = new File(arffFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + arffFile + "' does not exist!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(arffFile));

        //dataset reading
        Instances data = new Instances(reader);
        data.setClassIndex(data.numAttributes() - 1);

        //using classifier and  10-cross-validation
        Classifier cls = createClassifier(classifierType);
        Evaluation eval = new Evaluation(data);
        Random rand = new Random(1947498L);  // using seed
        int folds = kfold;
        eval.crossValidateModel(cls, data, folds, rand);

        data.delete();
        return eval.pctCorrect();
    }

    //No funciona!!! 
    public static double calculateRateOnTheFly(String classifierType, ArrayList<Feature> arff, int kfold) throws Exception {
               
        //dataset reading on the fly (RAM)
        Instances data = Feature.ToWekaInstances(arff);
        data.setClassIndex(data.numAttributes() - 1);

        //using classifier and  10-cross-validation
        Classifier cls = createClassifier(classifierType);
        Evaluation eval = new Evaluation(data);
        Random rand = new Random(1947498L);  // using seed
        int folds = kfold;
        eval.crossValidateModel(cls, data, folds, rand);

        data.delete();
        return eval.pctCorrect();
    }

    /**
     *
     * @param classifierType: KNN,SMO, J48, NAIVEBAYES
     * @param numExperiments: number of experiments (e.g. 10 runs of 10-folds
     * experiments)
     * @param arffFile: ARFF File e.g iris.arff outFile: Results File: ex.
     * iris_KNN-1_best.csv
     * @throws Exception
     */
    public static double[] classificarMultipleRuns(String classifierType, int numExperiments, int kfolds, String arffFile) throws Exception {
        File file = new File(arffFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + arffFile + "' does not exist!");
        }
        //==============================
        // 1. load data and set class index
        BufferedReader reader = new BufferedReader(new FileReader(arffFile));
        //==============================
        //2. read dataset
        Instances data = new Instances(reader);
        data.setClassIndex(data.numAttributes() - 1);

        // other options
        int runs = numExperiments;
        int folds = kfolds;//-10 fold cross validation

        Classifier cls = createClassifier(classifierType);
        //System.out.println("Classifier: " + cls.getClass().getName());
        //==============================
        // 3. perform each experiment
        double acuracia[] = new double[runs];
        double kappa[] = new double[runs];
        HashMap<Integer, StringBuilder> resultsKfolds = new HashMap<>();//<key_NumExperiment, value_Summary Information>
        for (int nEx = 0; nEx < runs; nEx++) {
            int seed = nEx + 7777;// seed data (every experiment should have a different seed)
            Random rand = new Random(seed);// seed used to randomized on fix seed
            Instances randData = new Instances(data);
            randData.randomize(rand);
//            if (randData.classAttribute().isNominal()) {
//                randData.stratify(folds);
//            }

            Evaluation eval = new Evaluation(randData);
            //==============================
            // 4. perform cross-validation
            for (int n = 0; n < folds; n++) {
                Instances train = randData.trainCV(folds, n);
                Instances test = randData.testCV(folds, n);
                // the above code is used by the StratifiedRemoveFolds filter, the
                // code below by the Explorer/Experimenter:
                // Instances train = randData.trainCV(folds, n, rand);

                Standardize filter = new Standardize();
                filter.setInputFormat(train);  // initializing the filter once with training set
                Instances newTrain = Filter.useFilter(train, filter);  // configures the Filter based on train instances and returns filtered instances
                Instances newTest = Filter.useFilter(test, filter);    // create new test set

                // build and evaluate classifier
                Classifier clsCopy = Classifier.makeCopy(cls);
                clsCopy.buildClassifier(newTrain); //train
                eval.evaluateModel(clsCopy, newTest);//test

            }
            acuracia[nEx] = eval.pctCorrect();
            kappa[nEx] = eval.kappa();
            
            //==============================
            // 5. output evaluation
//            System.out.println("=== Running experiment " + (nEx + 1) + " ===");            
//            System.out.println("Classifier: " + cls.getClass().getName() + " " + Utils.joinOptions(cls.getOptions()));
//            System.out.println("Dataset: " + data.relationName());
//            System.out.println("Folds: " + folds);
//            System.out.println("Seed: " + seed);
//            System.out.println();
//            System.out.println("=== " + folds + "-fold Cross-validation run " + (nEx + 1) + " ===");
//            System.out.println(eval.toSummaryString(false));//full information

            // 6. saving summarization results on ArrayList
            StringBuilder summaryResultsKFold = tableMain(data, eval, cls);
            resultsKfolds.put(nEx, summaryResultsKFold);
        }
        //==============================
        //7.1 Select best experiment based on maximum rate        
        double max = 0.0d;
        int bestExperiment = 0;
        for (int i = 0; i < kappa.length; i++) {
            if (acuracia[i] >= max) {
                max = acuracia[i];
                bestExperiment = i;
            }
        }
        //7.2 Select worst experiment based on minimum rate        
        double min = 100.0d;
        int worstExperiment = 0;
        for (int i = 0; i < kappa.length; i++) {
            if (acuracia[i] <= min) {
                min = acuracia[i];
                worstExperiment = i;
            }
        }

        //==============================
        //8.1 Print WEKA results of the maximum rate of the nExperiment on file
        String outFileBest = arffFile.substring(0, arffFile.length() - 5) + "_" + classifierType + "_best.csv";//-.arff(5 letters)
        //!//Writer w1 = new FileWriter(outFileBest);
        //!//try (BufferedWriter writer = new BufferedWriter(w1)) {
        //System.out.println("saving best experiment " + bestExperiment + " on " + outFileBest);
        //!//    writer.write(resultsKfolds.get(bestExperiment).toString());//prints summaryzation weka
        //!//}

        //8.2 Print WEKA results of the minimum rate of the nExperiment on file
        String outFileWorst = arffFile.substring(0, arffFile.length() - 5) + "_" + classifierType + "_worst.csv";//-.arff(5 letters)
        //!//Writer w2 = new FileWriter(outFileWorst);
        //!//try (BufferedWriter writer = new BufferedWriter(w2)) {
        //System.out.println("saving worst experiment " + worstExperiment + " on " + outFileWorst);
        //!//    writer.write(resultsKfolds.get(worstExperiment).toString());//prints summaryzation weka
        //!//}

        //==============================
        //9. calculate mean and std dvt of all experiments
        double acuraciaMeanStdDvt[] = CUtil.calculateMeanStdDvt(acuracia);
        double kappaMeanStdDvt[] = CUtil.calculateMeanStdDvt(kappa);

//        System.out.println("Classifier: " + cls.getClass().getName());
        //System.out.format("Correctly Classified:\t %f%%\t(\\pm %f)\n", acuraciaMeanStdDvt[0], acuraciaMeanStdDvt[1]);
        //System.out.format("Kappa statistic:     \t%f%%\t(\\pm %f)\n", kappaMeanStdDvt[0], kappaMeanStdDvt[1]);
        return acuraciaMeanStdDvt;

    }

    /**
     * Save on StringBuilder a concatenation of succes rate table To save this
     * data, just do toString();
     *
     * @param data
     * @param eval
     * @return
     * @throws Exception
     */
    private static StringBuilder tableSucessRate(Instances data, Evaluation eval) throws Exception {
        int m_NumInstances = data.numInstances();

        StringBuilder text = new StringBuilder();
        text.append("Correctly Classified Instances").append(sep);
        text.append(Utils.doubleToString(eval.correct(), 12, 4)).append(sep).append(Utils.doubleToString(eval.pctCorrect(), 12, 4)).append("% " + newline);
        //System.out.print("\t " + Utils.doubleToString(eval.pctCorrect(), 12, 4) + "% \t");
        text.append("Incorrectly Classified Instances").append(sep);
        text.append(Utils.doubleToString(eval.incorrect(), 12, 4)).append(sep).append(Utils.doubleToString(eval.pctIncorrect(), 12, 4)).append("% " + newline);
        text.append("Kappa statistic").append(sep);
        text.append(Utils.doubleToString(eval.kappa(), 12, 4)).append(newline);
        text.append("Mean absolute error").append(sep);
        text.append(Utils.doubleToString(eval.meanAbsoluteError(), 12, 4)).append(newline);
        text.append("Root mean squared error").append(sep);
        text.append(Utils.doubleToString(eval.rootMeanSquaredError(), 12, 4)).append(newline);
        text.append("Relative absolute error").append(sep);
        text.append(Utils.doubleToString(eval.relativeAbsoluteError(), 12, 4)).append("% " + newline);
        text.append("Root relative squared error").append(sep);
        text.append(Utils.doubleToString(eval.rootRelativeSquaredError(), 12, 4)).append("% " + newline);
        text.append("Total Number of Instances").append(sep);
        text.append(Utils.doubleToString(m_NumInstances, 12, 4)).append(newline);
        return text;
    }

    /**
     * Save on StringBuilder a concatenation of the table ROC, To save this
     * data, just do toString();
     *
     * @param data
     * @param eval
     * @return
     */
    private static StringBuilder tableROC(Instances data, Evaluation eval) {
        int m_NumClasses = data.numClasses();
        String m_ClassNames[] = new String[m_NumClasses];
        for (int i = 0; i < m_NumClasses; i++) {
            m_ClassNames[i] = data.classAttribute().value(i);
        }

        StringBuilder text = new StringBuilder();
        text.append("=== Detailed Accuracy By Class ===").append(newline);
        text.append(sep).append("TP Rate");
        text.append(sep).append("FP Rate");
        text.append(sep).append("Precision");
        text.append(sep).append("Recall");
        text.append(sep).append("F-Measure");
        text.append(sep).append("ROC Area");
        text.append(sep).append("Class");
        text.append(newline);

        for (int i = 0; i < m_NumClasses; i++) {
            text.append(sep).append(Utils.doubleToString(eval.truePositiveRate(i), 7, 3));
            text.append(sep).append(Utils.doubleToString(eval.falsePositiveRate(i), 7, 3));
            text.append(sep).append(Utils.doubleToString(eval.precision(i), 7, 3));
            text.append(sep).append(Utils.doubleToString(eval.recall(i), 7, 3));
            text.append(sep).append(Utils.doubleToString(eval.fMeasure(i), 7, 3));
            double rocVal = eval.areaUnderROC(i);
            text.append(sep).append(Utils.doubleToString(rocVal, 7, 3)).append(sep);
            text.append(m_ClassNames[i]);
            text.append(newline);

        }

        text.append("Weighted Avg.");
        text.append(sep).append(Utils.doubleToString(eval.weightedTruePositiveRate(), 7, 3));
        text.append(sep).append(Utils.doubleToString(eval.weightedFalsePositiveRate(), 7, 3));
        text.append(sep).append(Utils.doubleToString(eval.weightedPrecision(), 7, 3));
        text.append(sep).append(Utils.doubleToString(eval.weightedRecall(), 7, 3));
        text.append(sep).append(Utils.doubleToString(eval.weightedFMeasure(), 7, 3));
        text.append(sep).append(Utils.doubleToString(eval.weightedAreaUnderROC(), 7, 3));
        text.append(newline);
        text.append(newline);
        text.append(newline);

        double[][] matrix = eval.confusionMatrix();
        text.append("=== Confusion Matrix ===").append(newline);
        for (int i = 0; i < m_NumClasses; i++) {
            text.append(sep).append(m_ClassNames[i]);

        }
        text.append(newline);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                text.append(sep).append(matrix[i][j]);
            }
            text.append(sep).append(" | ").append(m_ClassNames[i]).append(newline);
        }

        return text;
    }

    /**
     * Save on StringBuilder a concatenation of all the information about the
     * already classified k-fold To save this data, just do toString();
     *
     * @param data
     * @param eval
     * @param cls
     * @return
     * @throws Exception
     */
    private static StringBuilder tableMain(Instances data, Evaluation eval, Classifier cls) throws Exception {
        StringBuilder text = new StringBuilder();
        text.append("Relation Name: ").append(data.relationName()).append(newline);
        text.append("Num Classes: ").append(data.numClasses()).append(newline);
        text.append("Num Instances: ").append(data.numInstances()).append(newline);
        text.append("Num Attributes: ").append(data.numAttributes()).append(newline);
        text.append("**************************************************").append(newline);
        text.append("Used Classifier: ").append(cls.getClass().getName()).append(newline);
        text.append("**************************************************").append(newline);
        text.append(tableSucessRate(data, eval));
        text.append(newline);
        text.append(tableROC(data, eval));
        text.append(newline);
        return text;
    }

    /**
     * Run kmeans algorithm and return the clusters assignments for each of the
     * intances of the original arff. Note: This code treats both cases arrfs
     * with classes and without classes ok!
     *
     * @param arffFile
     * @param numClusters
     * @return
     * @throws java.lang.Exception
     */
    public static HashMap<Integer, Integer> runClusteringKmeans(String arffFile, int numClusters) throws Exception {
        File file = new File(arffFile);
        if (!file.exists()) {
            throw new IllegalArgumentException("File '" + arffFile + "' does not exist!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(arffFile));

        //dataset reading original data
        Instances data = new Instances(reader);
        Instances dataClusterer = null;
        if (data.classIndex() == -1) {//if data has no class on arrf file
            dataClusterer = data;
        } else //if data has class on arff file ->> remove it
        {
            // remove class attribute, make class-less
            data.setClassIndex(data.numAttributes() - 1);//last atribute is the class index      
            Remove filter = new weka.filters.unsupervised.attribute.Remove();
            filter.setAttributeIndices("" + (data.classIndex() + 1));
            try {
                filter.setInputFormat(data);
                dataClusterer = Filter.useFilter(data, filter);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setSeed(1947498);  // using seed
        kmeans.setNumClusters(numClusters);
        kmeans.buildClusterer(dataClusterer);

        int[] assignments = kmeans.getAssignments();
        HashMap<Integer, Integer> assignmentsMap = new HashMap();//Instance #-> Cluster #        

        for (int i = 0; i < assignments.length; i++) {
            System.out.printf("Instance %d -> Cluster %d\n", i, assignments[i]);
            assignmentsMap.put(i, assignments[i]);
        }
        return assignmentsMap;

 
        //data.delete();
    }

    private static Instances applyFilterSubSample(Instances data, int seed) throws Exception {
        //apply filter

        SpreadSubsample ff = new SpreadSubsample();
        ff.setDistributionSpread(1.0d);
        ff.setRandomSeed(seed);//seed
        ff.setInputFormat(data);  // initializing the filter once with training set

        Instances filteredInstances = Filter.useFilter(data, ff);
        return filteredInstances;
    }

    public static void Main(String[] args) throws Exception {

        //String outfile = "data/iris_resutlados.arff";
        //Classificador.classificarMultipleRuns(JWekaClassifier.KNN, 10, file);
        int seed = 3879;
        Random rnd = new Random(seed);
        //for (int i = 1; i <= 6; i++) {
        //String file = "ResultadosRedesMetabolicasNature/classicalarffs/" + i + ".arff";
        String P = "ResultadosRedesMetabolicasNature/Experiment#4/classicalarffs/";
        String files[] = {P + "1.arff", P + "2.arff", P + "3.arff", P + "4.arff", P + "5.arff", P + "6.arff", P + "_classic.arff",};
        int numExperiments = 100;
        int kfold = 3;

        double vector1[] = new double[numExperiments];
        double vector2[] = new double[numExperiments];
        double vector3[] = new double[numExperiments];
        double vector4[] = new double[numExperiments];
        for (String file1 : files) {

            for (int j = 0; j < numExperiments; j++) {
//                vector1[j] = JWekaClassifier.calculateRateSubSampleFilter(JWekaClassifier.KNN, file1, kfold, rnd.nextInt());
//                vector2[j] = JWekaClassifier.calculateRateSubSampleFilter(JWekaClassifier.NAIVEBAYES, file1, kfold, rnd.nextInt());
//                vector3[j] = JWekaClassifier.calculateRateSubSampleFilter(JWekaClassifier.J48, file1, kfold, rnd.nextInt());
                vector4[j] = JWekaClassifier.calculateRateSubSampleFilter(JWekaClassifier.SMO, file1, kfold, rnd.nextInt());
            }
            String ratio1 = Utils.doubleToString(Statistics.calculaMedia(vector1), 3, 2) + " (\\pm " + Utils.doubleToString(Statistics.calculaStdDvt(vector1), 3, 2) + ")";
            String ratio2 = Utils.doubleToString(Statistics.calculaMedia(vector2), 3, 2) + " (\\pm " + Utils.doubleToString(Statistics.calculaStdDvt(vector2), 3, 2) + ")";
            String ratio3 = Utils.doubleToString(Statistics.calculaMedia(vector3), 3, 2) + " (\\pm " + Utils.doubleToString(Statistics.calculaStdDvt(vector3), 3, 2) + ")";
            String ratio4 = Utils.doubleToString(Statistics.calculaMedia(vector4), 3, 2) + " (\\pm " + Utils.doubleToString(Statistics.calculaStdDvt(vector4), 3, 2) + ")";

            String result = "\t" + ratio1 + "\t" + ratio2 + "\t" + ratio3 + "\t" + ratio4;
            System.out.println(result);

        }

        //}
        //JWekaClassifier.runClusteringKmeans(file, 3);
    }


    public static void main4(String[] args) throws Exception {
        //String P = "D:\\Dropbox\\doutorado\\programas\\ComplexSystemsAnalysis\\ResultadosRedesLiterarias\\arffs\\combining best\\";
        //String files[] = {P+"B0123578-S14678_distribEntropy.arff",P+"B0134-S012357_mediasLiterarias.arff",P+"B01467-S02678_distribLiterariastodo.arff",P+"B02578-S37_distribLempelZiv.arff",P+"B035-S1235_distribWordL.arff",P+"B123568-S123_distribWordL.arff",P+"B148-S378_distribEntropy.arff",P+"B148-S5_mediasLiterarias.arff",P+"B2346-S13578_distribLempelZiv.arff",P+"B45678-S13678_distribLiterariastodo.arff"};
        String P = "ResultadosRedesLiterarias/classicalarffs/";
        String files[] = {P + "_classic.arff", P + "1.arff", P + "2.arff", P + "3.arff", P + "4.arff", P + "5.arff", P + "6.arff"};
        int numExperiments = 1000;
        int kfold = 5;
        for (String file1 : files) {
            double[] res1 = JWekaClassifier.classificarMultipleRuns(JWekaClassifier.KNN, numExperiments, kfold, file1);//10 experiments, 5fold
            double[] res2 = JWekaClassifier.classificarMultipleRuns(JWekaClassifier.NAIVEBAYES, numExperiments, kfold, file1);//10 experiments, 5fold
            double[] res3 = JWekaClassifier.classificarMultipleRuns(JWekaClassifier.J48, numExperiments, kfold, file1);//10 experiments, 5fold
            String r1 = Utils.doubleToString(res1[0], 3, 2) + "(\\pm " + Utils.doubleToString(res1[1], 3, 2) + ")";
            String r2 = Utils.doubleToString(res2[0], 3, 2) + "(\\pm " + Utils.doubleToString(res2[1], 3, 2) + ")";
            String r3 = Utils.doubleToString(res3[0], 3, 2) + "(\\pm " + Utils.doubleToString(res3[1], 3, 2) + ")";

            System.out.println(r1 + "\t" + r2 + "\t" + r3);

        }

    }


    public static void mainEstomatosClassicalLigthConditions() {
        String classificadores[] = {"KNN-1"};
        String P = "ResultadosRedesEstomatos/LightConditions/classicalarffs/";
        String arffFile[] = {P + "feats_classicT=50_10_200 (validation).arff", P + "grau.arff", P + "h2.arff", P + "h3.arff", P + "clust.arff", P + "path.arff", P + "pearson.arff"};
        String nomes[] = {"all", "grau", "h2", "h3", "clust", "path", "pearson"};
        String result = "";
        for (int j = 0; j < arffFile.length; j++) {

            for (int i = 0; i < classificadores.length; i++) {
                double rate[] = new double[2];
                int numExperiments = 100;
                int kfold = 4;//validation-dataset
                try {
                    rate = JWekaClassifier.classificarMultipleRuns(classificadores[i], numExperiments, kfold, arffFile[j]);
                } catch (Exception ex) {
                    System.err.println(ex);
                }
                String ratio = Utils.doubleToString(rate[0], 3, 2) + "\t " + Utils.doubleToString(rate[1], 3, 2) + "";
                result = "\t" + ratio;
            }
            System.out.println(nomes[j] + "\t" + result);
        }
    }

    public static void mainEstomatosLightConditions() {

        System.out.println("Testing best arff");
        mainEstomatosClassicalLigthConditions();
        String classificadores[] = {"KNN-1"};
        String arffFile = "ResultadosRedesEstomatos/LightConditions/arffst=50_10_200 (600 rules)/";

        //String rules[] = {"B-S02", "B-S78", "B-S027", "B-S028", "B-S0126", "B-S0278", "B-S01278", "B-S012378", "B0-S0347", "B0-S2358", "B0-S134578", "B0-S0134568", "B0-S01345678", "B3-S37", "B3-S458", "B3-S1678", "B3-S01456", "B3-S03567", "B3-S015678", "B3-S025678", "B6-S13457", "B7-S02", "B7-S15", "B7-S28", "B7-S78", "B7-S027", "B7-S028", "B7-S0127", "B7-S0278", "B7-S1248", "B7-S01245", "B7-S01247", "B7-S01278", "B7-S012478", "B7-S045678", "B7-S145678", "B7-S0123578", "B8-S8", "B8-S28", "B8-S78", "B8-S028", "B8-S123", "B8-S278", "B8-S0178", "B8-S0278", "B8-S1237", "B02-S134568", "B02-S345678", "B02-S1234568", "B05-S35678", "B06-S01268", "B06-S01346", "B07-S8", "B07-S48", "B07-S0148", "B08-S123", "B08-S5678", "B08-S13456", "B08-S012356", "B08-S0134568", "B08-S01345678", "B12-S012347", "B14-S1268", "B14-S012456", "B14-S024568", "B16-S02345", "B24-S04567", "B24-S12456", "B24-S012456", "B25-S2345678", "B28-S78", "B28-S012356", "B34-S13", "B34-S2456", "B34-S01678", "B34-S02456", "B34-S024568", "B35-S12456", "B36-S", "B36-S06", "B36-S58", "B36-S345678", "B37-S0138", "B37-S0236", "B37-S1234", "B37-S12347", "B37-S23457", "B37-S012348", "B38-S268", "B38-S0345", "B38-S1678", "B38-S02367", "B47-S12357", "B48-S034567", "B57-S378", "B57-S0378", "B57-S01278", "B58-S0378", "B67-S1248", "B67-S2368", "B67-S01256", "B67-S23678", "B68-S012478", "B78-S8", "B78-S02", "B78-S27", "B78-S28", "B78-S78", "B78-S027", "B78-S028", "B78-S278", "B78-S0157", "B78-S0278", "B78-S1248", "B78-S012456", "B78-S012458", "B78-S024578", "B012-S017", "B012-S0137", "B013-S23467", "B014-S04567", "B016-S58", "B016-S468", "B016-S2345", "B016-S23457", "B016-S023457", "B016-S023458", "B234568-S034578", "B234578-S0123678", "B0123478-S03678", "B0123568-S01237", "B0134578-S014678", "B0134578-S01234568", "B0134678-S4678", "B0235678-S012578", "B0235678-S1234567", "B01234567-S02345", "B01234567-S02678", "B01234568-S024567", "B01234578-S45678", "B017-S23568", "B018-S1258", "B018-S15678", "B036-S034567", "B036-S01345678", "B038-S023468", "B067-S13458", "B068-S01268", "B068-S13458", "B068-S013467", "B078-S8", "B078-S25678", "B167-S2348", "B167-S01248", "B167-S034568", "B167-S0234578", "B167-S012345678", "B168-S01245", "B347-S0123678", "B356-S0158", "B356-S2378", "B356-S2567", "B356-S01567", "B357-S014", "B357-S125", "B357-S168", "B467-S0145", "B467-S01458", "B467-S123578", "B468-S03478", "B478-S1367", "B478-S01268", "B0126-S1278", "B0127-S1234", "B0127-S1235678", "B0148-S2378", "B0156-S135678", "B0237-S2345", "B0237-S23458", "B0237-S34568", "B0238-S04567", "B0238-S25678", "B0245-S0578", "B0267-S123468", "B0268-S1245678", "B0278-S012468", "B0345-S02456", "B0345-S12456", "B0378-S12345", "B0456-S056", "B0456-S123678", "B0578-S023578", "B0678-S0134567", "B1234-S12456", "B1234-S0124568", "B1256-S0145678", "B1348-S124567", "B1358-S04678", "B1358-S235678", "B1567-S0125", "B1567-S0126", "B1567-S0235", "B2357-S234567", "B2567-S13568", "B3567-S017", "B3567-S025", "B3567-S0256", "B3567-S0457", "B3567-S0468", "B3567-S1268", "B3567-S2567", "B3567-S2568", "B3567-S013678", "B3568-S167", "B3568-S0147", "B3568-S0256", "B3568-S1267", "B3568-S1268", "B3568-S1457", "B3568-S2567", "B01234-S023456", "B01235-S02", "B01235-S14568", "B01235-S014567", "B01235-S124568", "B01237-S1245", "B01267-S35", "B01267-S123457", "B01267-S0123467", "B01567-S0146", "B01567-S1257", "B01568-S78", "B01568-S3467", "B01578-S013", "B02368-S0134578", "B02568-S023468", "B02578-S01346", "B02578-S245678", "B03578-S014678", "B03678-S035", "B12347-S0134567", "B14578-S0345678", "B14678-S0345678", "B15678-S0126", "B15678-S0127", "B23578-S1234567", "B34678-S056", "B35678-S025", "B35678-S126", "B35678-S0258", "B35678-S0268", "B35678-S1467", "B35678-S2367", "B35678-S012578", "B45678-S012345678", "B012345-S026", "B012378-S023458", "B012378-S123457", "B012568-S0145678", "B012578-S346", "B012678-S0126", "B012678-S012348", "B012678-S123468", "B012678-S0123468", "B013678-S058", "B014567-S3678", "B014567-S03678", "B014567-S345678", "B014567-S1245678", "B014567-S01245678", "B023467-S14567", "B034567-S06", "B034567-S1456", "B034568-S6", "B034568-S018", "B123467-S0678", "B123468-S123", "B124678-S1678", "B125678-S012345", "B0123578-S0234567", "B158-S01248", "B158-S01257", "B236-S01245678", "B0146-S034678", "B0235-S0124568", "B0146-S02345678", "B0167-S023", "B0134-S12456", "B0234-S24578", "B0234-S013", "B0134-S012456", "B02378-S235678", "B02378-S0234578", "B02678-S0123458", "B03456-S02678", "B03456-S012578", "B03467-S23678", "B04567-S03678", "B04568-S014678", "B04678-S012345678", "B234-S14568", "B156-S0127", "B157-S34567", "B145-S24568", "B157-S012467", "B47-S0134567", "B48-S15678", "B48-S014578", "B478-S24567", "B478-S012678", "B478-S245678", "B035678-S34567", "B045678-S145678", "B02378-S23458", "B02378-S234578", "B0123678-S0135", "B1234678-S023678", "B01345678-S015", "B1234678-S145678", "B0124568-S01457", "B0123457-S0678", "B0234567-S0234578", "B0124568-S0145678", "B0123457-S1234567", "B0234568-S01678", "B02345678-S045", "B0234578-S678", "B12345678-S03678", "B1345678-S1578", "B12345678-S013678", "B1345678-S01578", "B0125678-S025", "B0125678-S12568", "B2345678-S12678", "B0123456-S235", "B0123456-S0235", "B0123456-S1235", "B0145678-S124678", "B0123456-S01235", "B01245678-S01245", "B0124567-S013456", "B0124567-S345678", "B0124567-S0125678", "B0124567-S1345678", "B01245678-S1345678", "B0124568-S678", "B027-S1238", "B126-S0123478", "B126-S0345678", "B027-S04567", "B045-S124568", "B034-S012456", "B128-S345", "B024-S12456", "B027-S34567", "B058-S07", "B045-S0234678", "B045-S1234678", "B035-S124568", "B045-S01234678", "B024-S012456", "B034-S135678", "B057-S024", "B047-S45", "B056-S158", "B057-S468", "B127-S3456", "B128-S01236", "B025-S01", "B056-S0348", "B134-S05678", "B126-S12347", "B124-S12456", "B128-S012356", "B067-S02367", "B245-S0234568", "B235-S12456", "B135-S03456", "B157-S0125", "B235-S024568", "B238-S25678", "B148-S0158", "B156-S012378", "B146-S0345678", "B034678-S23678", "B02357-S013678", "B0168-S23457", "B0134-S024568", "B0134-S124568", "B0234-S0125678", "B0145-S0124568", "B0137-S023458", "B0235-S024568", "B0137-S0234578", "B368-S012568", "B578-S37", "B578-S037", "B0124-S345", "B378-S2345678", "B0123-S345", "B578-S378", "B578-S0126", "B346-S234567", "B345-S124568", "B368-S68", "B578-S0378", "B367-S018", "B3678-S67", "B3468-S0567", "B3578-S0157", "B3467-S048", "B3678-S02467", "B4678-S3458", "B3678-S236", "B4678-S4567", "B3458-S0278", "B4567-S01237", "B3678-S12567", "B3578-S1256", "B4678-S01458", "B3456-S24678", "B3467-S0567", "B3678-S012568", "B5678-S01256", "B01237-S1245678", "B01238-S3458", "B01238-S4567", "B01238-S03457", "B01238-S04567", "B01238-S34578", "B01238-S124567", "B01245-S12456", "B01245-S014568", "B01245-S0124568", "B01245-S1345678", "B01246-S145678", "B01248-S01237", "B01268-S12348", "B01278-S026", "B01278-S02348", "B01345-S04568", "B01345-S12456", "B01345-S24568", "B01345-S124568", "B01345-S01345678", "B01346-S0257", "B01347-S014567", "B01378-S23458", "B01378-S023457", "B01457-S0124567", "B01457-S2345678", "B01458-S45678", "B01467-S0345678", "B01467-S01235678", "B02345-S01456", "B02345-S24568", "B02346-S023", "B02347-S024567", "B02347-S124567", "B05678-S023", "B05678-S02578", "B05678-S12378", "B05678-S25678", "B05678-S0123567", "B12345-S04568", "B12345-S24568", "B12345-S024568", "B12345-S1345678", "B12357-S2678", "B12357-S134567", "B12368-S145678", "B12378-S02345", "B12378-S23457", "B12378-S012678", "B12378-S123457", "B12378-S234578", "B12378-S0123457", "B12378-S0234578", "B12567-S0135678", "B12678-S0123467", "B12678-S0123468", "B14567-S0123678", "B013467-S27", "B23457-S01578", "B24568-S01678", "B24578-S0124567", "B34567-S01234578", "B34568-S05", "B34568-S168", "B34578-S38", "B012345-S24568", "B012345-S023678", "B012345-S0345678", "B012347-S1456", "B012457-S01245678", "B012467-S03456", "B012467-S345678", "B012478-S024", "B012478-S01234567", "B013456-S1345678", "B013456-S2345678", "B013457-S0123456", "B034568-S012468", "B123678-S04678", "B123678-S23678", "B014568-S45678", "B014678-S1234678", "B014678-S2345678", "B015678-S34567", "B015678-S012678", "B023456-S01235", "B023456-S01678", "B023458-S013678", "B023578-S34678", "B367-S236", "B578-S01267", "B578-S01278", "B567-S01236", "B123456-S01345", "B234567-S01456", "B234567-S123678", "B234567-S234578", "B5678-S134578", "B01278-S023468", "B13567-S1678", "B13568-S2678", "B13568-S12678", "B367-S0178", "B346-S146", "B347-S267", "B345-S148", "B378-S1268", "B368-S1278", "B456-S2357", "B367-S1256", "B378-S2678", "B358-S1567", "B678-S012478", "B0123-S124567", "B678-S145678", "B368-S12568", "B378-S23458", "B345-S4568", "B0567-S0248", "B0256-S023678", "B0567-S0346", "B0247-S014568", "B0245-S124568", "B0248-S0235678", "B0247-S1245678", "B0247-S2345678", "B0567-S02347", "B0567-S12357", "B0468-S0234678", "B0348-S25678", "B0478-S3", "B0467-S012345678", "B0568-S45", "B0247-S14567", "B1345-S012456", "B1237-S34568", "B1246-S023678", "B0568-S1578", "B1238-S34578", "B1245-S12456", "B1278-S0123", "B1235-S024568", "B0568-S1245678", "B0568-S02357", "B0568-S02456", "B0568-S02567", "B1267-S12346", "B1345-S12456", "B1235-S345678", "B1268-S0345678", "B1367-S345678", "B1456-S02345678", "B1368-S1345678", "B2368-S012356", "B2457-S024567", "B1568-S01278", "B2378-S0234578", "B1378-S02345", "B2348-S01234678", "B1378-S23457", "B1457-S124678", "B1467-S0134678", "B1367-S014678", "B1467-S0345678", "B3678-S1258", "B3578-S023", "B0458-S14678", "B1245-S0124568", "B1258-S125678", "B2378-S234578", "B23467-S04567", "B012348-S1234568", "B012356-S4678", "B012357-S02678", "B012357-S23678", "B012358-S02"};
        String rules[] = {"B0145-S0124568", "B34-S024568", "B1367-S014678", "B125678-S012345", "B1345-S12456", "B02345678-S045", "B1567-S0125", "B3467-S048", "B14-S024568", "B01238-S124567"};
        for (int k = 0; k < rules.length; k++) {
            String result = "";
            for (int i = 0; i < classificadores.length; i++) {
                double rate[] = new double[2];
                int numExperiments = 100;
                int kfold = 4;//validation-dataset
                String arff = arffFile + "/" + rules[k] + "_distrib.arff";
                try {
                    rate = JWekaClassifier.classificarMultipleRuns(classificadores[i], numExperiments, kfold, arff);

                } catch (Exception ex) {
                    System.err.println(ex);
                }

                String ratio = Utils.doubleToString(rate[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate[1], 3, 2) + ")";

                result += "\t" + ratio;

            }
            System.out.println(rules[k] + "\t" + result);
        }

    }


    public static void mainX(String[] args) {
        //mainRuido4modelos();
        //mainSocialNetClassical();
        //mainEstomatosLightConditions();
        //mainEstomatosClassicalLigthConditions();
    }
}
