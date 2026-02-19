package CA.graph.mainEstomatos;

import Utils.Caracterization.JWekaClassifier;
import weka.core.Utils;

/**
 *
 * @author Jeaneth Machicao
 */
public class SVM_LLNA_estomatos {

    public static void main(String[] args) {
        mainCalissiaClassical(args);
    }
            
    public static void mainCethenanteClassical(String args[]) {
        String P = "ResultadosRedesEstomatos/LightConditions (cthentante)/classicalarffs/";

        String arrfFile[] = {P + "degree.arff",
            P + "hiera2.arff", P + "hierar3.arff", P + "clust.arff", P + "path.arff", P + "pearson.arff", P + "feats_classic.arff",};

        //classifyin
        String result = "Classical:";
        String classificador = "SupportVectorMachine";

        double rate[] = new double[2];

        int kfold = 10;//VALIDATION
        int numExperiments = 100;//100
        for (int i = 0; i < arrfFile.length; i++) {

            try {
                rate = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arrfFile[i]);
            } catch (Exception ex) {
                System.err.println(ex);
            }

            String ratio1 = Utils.doubleToString(rate[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate[1], 3, 2) + ")";

            //result += "\t" + ratio1 + "\t" + ratio2 + "\t" + ratio3 + "\t" + ratio4 + "\t" + ratioMedia;
            result = arrfFile[i] + "\t" + ratio1;

            System.out.println(result);
        }
    }

    public static void mainCalissiaClassical(String args[]) {
        String P = "ResultadosRedesEstomatos/HumidityConditions/classicalarffs/";
        String arrfFile[] = {P + "degree.arff",
            P + "hiera2.arff", P + "hierar3.arff",P + "clust.arff", P + "path.arff", P + "pearson.arff", P + "feats_classic.arff",  "ResultadosRedesEstomatos/HumidityConditions/arffs/B014-S04567_distribEntropy.arff"};

        //classifyin
        String result = "Classical:";
        //String classificador = "SupportVectorMachine";
        String classificador = "KNN-1";

        double rate[] = new double[2];

        int kfold = 5;//VALIDATION
        int numExperiments = 100;//100
        for (int i = 0; i < arrfFile.length; i++) {

            try {
                rate = JWekaClassifier.classificarMultipleRuns(classificador, numExperiments, kfold, arrfFile[i]);
            } catch (Exception ex) {
                System.err.println(ex);
            }

            String ratio1 = Utils.doubleToString(rate[0], 3, 2) + "(\\pm " + Utils.doubleToString(rate[1], 3, 2) + ")";

            //result += "\t" + ratio1 + "\t" + ratio2 + "\t" + ratio3 + "\t" + ratio4 + "\t" + ratioMedia;
            result = arrfFile[i] + "\t" + ratio1;

            System.out.println(result);
        }
    }

}
