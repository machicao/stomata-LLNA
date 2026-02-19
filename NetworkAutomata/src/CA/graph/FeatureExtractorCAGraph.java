package CA.graph;

import Utils.CUtil; 
import Utils.lempelziv.LempelZiv;
import java.util.ArrayList; 
import java.util.regex.Pattern;

/**
 *
 * @author Jeaneth Machicao
 */
public class FeatureExtractorCAGraph {

    /**
     * Run the Shannon' entropy
     *
     * @param CA
     * @return an array containing each entropy value of every cell
     */
    public static double[] runEntropyShannon(AbstractCAGraph CA) {
        double sumTotal = 0.0d;
        double a, b;
        Model network = CA.getNetwork();
        double ShannonEntropy[] = new double[network.getN()];
        int z = 0;
        for (int x : network.getNodes().keySet()) {
            double probZero = (1.0d * network.getNode(x).getNumDeaths() / CA.getITERATIONS());
            double probOnes = (1.0d * network.getNode(x).getNumAlives() / CA.getITERATIONS());
            if (probZero == 0) {
                a = 0;
            } else {
                a = probZero * (Math.log(probZero) / Math.log(2.0));
            }
            if (probOnes == 0) {
                b = 0;
            } else {
                b = probOnes * (Math.log(probOnes) / Math.log(2.0));
            }
            network.getNode(x).setCellShannonEntropy(-(a + b));
            ShannonEntropy[z++] = -(a + b);
            sumTotal -= (a + b);
        }
        return ShannonEntropy;
    }

    /**
     * Run the TSallis' entropy
     * https://www.researchgate.net/publication/221184819_Comparison_of_Shannon_Renyi_and_Tsallis_Entropy_Used_in_Decision_Trees
     *
     * @param CA
     * @return an array containing each entropy value of every cell
     */
    public static double[] runTsallisEntropy(AbstractCAGraph CA, double alpha) {

        Model network = CA.getNetwork();
        double TsallisEntropy[] = new double[network.getN()];
        int z = 0;
        for (int x : network.getNodes().keySet()) {
            double probZero = (1.0d * network.getNode(x).getNumDeaths() / CA.getITERATIONS());
            double probOnes = (1.0d * network.getNode(x).getNumAlives() / CA.getITERATIONS());

            //Tsallis form #1
            double sum = Math.pow(probZero, alpha) + Math.pow(probOnes, alpha);
            double I = (1 / (1 - alpha)) * Math.log(sum);

            //Tsallis form #2
            //double sum= Math.pow(probZero, alpha)*logq(probZero, alpha)+ Math.pow(probOnes, alpha)*logq(probOnes, alpha);
            //double I = -1.0*sum;
            network.getNode(x).setCellTsallisEntropy(I);
            TsallisEntropy[z++] = I;
        }
        return TsallisEntropy;
    }

    public static double[][] extractSpatioTimeDiagram(AbstractCAGraph CA) {

        Model network = CA.getNetwork();
        int numNodes = network.getN();
        int numIter = CA.getITERATIONS();
        double spatiotime[][] = new double[numIter][network.getN()];

        //for (int x : network.getNodes().keySet()) {
        ArrayList<Integer> iteratorDegreeOrder = CA.getIteratorNodesByDegree();
        for (Integer x : iteratorDegreeOrder) {
            String word = network.getNode(x).getWordsTimeEvolution();
            for (int i = 0; i < word.length(); i++) {
                spatiotime[i][x] = word.charAt(i) == '1' ? 1.0d : 0.0d;
            }
        }
        return spatiotime;
    }

    public static double logq(double x, double q) {
        return (Math.pow(x, q) - 1.0) / (1.0 - q);
    }

    /**
     * Run the Word Entropy (according to Marr's paper)
     *
     * @param CA
     * @return an array containing each word-entropy value of every cell
     */
    public static double[] runEntropyWord(AbstractCAGraph CA) {
        Model network = CA.getNetwork();
        double[] wordEntropy = new double[network.getN()];
        int z = 0;
        for (int x : network.getNodes().keySet()) {
            double sum = 0.0d;
            //System.out.println(wordsTimeEvolution[i]);
            for (int L = 1; L < CA.getITERATIONS(); L++) {
                String word = network.getNode(x).getWordsTimeEvolution();
                double prob = CUtil.probabilityOfConstantWordsOfLengthL(word, L);
                //System.out.print(prob+", ");

                if (prob == 0) {
                    sum -= 0;
                } else {
                    sum -= prob * Math.log(prob) / Math.log(2.0);
                }
            }
            network.getNode(x).setCellWordEntropy(sum);
            wordEntropy[z++] = sum;
        }
        return wordEntropy;
    }

    /**
     * Run the LempelZiv method
     *
     * @param CA
     * @return an array containing each LempelZiv value of every cell
     */
    public static double[] runLempelZiv(AbstractCAGraph CA) {
        Model network = CA.getNetwork();
        double lempelZiv[] = new double[network.getN()];
        int z = 0;
        for (int x : network.getNodes().keySet()) {
            //for (Iterator<Integer> it = network.getNodes().keySet().iterator(); it.hasNext();) {
            //    int x = it.next();
            double LZ = LempelZiv.CalculateVectorInformation(network.getNode(x).getWordsTimeEvolution());
            network.getNode(x).setCellLempelZivComplexity(LZ);
            lempelZiv[z++] = LZ;
            //normalizar de acordo ao maximo lempel ziv
            //e depois de acordo ao maximo grau... despues
            //1. normalizar de acordo ao lempelZiv
        }
        return lempelZiv;
    }

    /**
     * Run the Shannon' entropy extracted features only form the 1-kcoreness
     *
     * @param CA
     * @param notkcoreList
     * @return an array containing each entropy value of every cell
     */
    public static double[] runEntropyShannonNonKcore(AbstractCAGraph CA, ArrayList<Integer> notkcoreList) {
        double sumTotal = 0.0d;
        double a, b;
        Model network = CA.getNetwork();
        double ShannonEntropy[] = new double[network.getN()];
        int z = 0;
        for (int x : notkcoreList) {
            double probZero = (1.0d * network.getNode(x).getNumDeaths() / CA.getITERATIONS());
            double probOnes = (1.0d * network.getNode(x).getNumAlives() / CA.getITERATIONS());
            if (probZero == 0) {
                a = 0;
            } else {
                a = probZero * (Math.log(probZero) / Math.log(2.0));
            }
            if (probOnes == 0) {
                b = 0;
            } else {
                b = probOnes * (Math.log(probOnes) / Math.log(2.0));
            }
            network.getNode(x).setCellShannonEntropy(-(a + b));
            ShannonEntropy[z++] = -(a + b);
            sumTotal -= (a + b);
        }
        return ShannonEntropy;
    }

    /**
     * Run the LempelZiv method extract measurements only from nodes on the
     * 1-kcoreness (nonkcore)
     *
     * @param CA
     * @param notkcoreList
     * @return an array containing each LempelZiv value of every cell
     */
    public static double[] runLempelZivNonKcore(AbstractCAGraph CA, ArrayList<Integer> notkcoreList) {
        Model network = CA.getNetwork();
        double lempelZiv[] = new double[network.getN()];
        int z = 0;
        for (int x : notkcoreList) {
            //for (Iterator<Integer> it = network.getNodes().keySet().iterator(); it.hasNext();) {
            //    int x = it.next();
            double LZ = LempelZiv.CalculateVectorInformation(network.getNode(x).getWordsTimeEvolution());
            network.getNode(x).setCellLempelZivComplexity(LZ);
            lempelZiv[z++] = LZ;
            //normalizar de acordo ao maximo lempel ziv
            //e depois de acordo ao maximo grau... despues
            //1. normalizar de acordo ao lempelZiv
        }
        return lempelZiv;
    }

    /**
     * Run the Word-Length frequency extracted features only from nodes in the
     * 1-kcoreness
     *
     * @param CA
     * @return an array containing the frequency of each combination of counting
     * words, 1, 11, 111, 1111,... 1^40 (considera somente 40, nao precisa mais)
     */
    public static double[] runWordFrequencyNonKcore(AbstractCAGraph CA, int LIMIT, ArrayList<Integer> notkcoreList) {
        //int LIMIT = 40;
        double[] wordFreq = new double[LIMIT];
        Model network = CA.getNetwork();
        double countTotalWords = 0;
        for (int x : notkcoreList) {
            String str = network.getNode(x).getWordsTimeEvolution();
            Pattern pattern = Pattern.compile("[0]{1,}");
            String[] split = pattern.split(str);
            for (int i = 1; i < split.length; i++) {//starts from 1, avoid empty character
                if (split[i].length() < LIMIT) {
                    int count = split[i].length();
                    wordFreq[count]++;
                    countTotalWords++;
                }
            }
        }
        //normalize
        //featsHistoWL[] = Feature.histogramBinRange(featWordL, numBins, 0.0d, 1.0d);
        for (int i = 0; i < LIMIT; i++) {
            wordFreq[i] = wordFreq[i] / (countTotalWords * 1.0);
        }

        return wordFreq;
    }

    /*
     * Obtains the 1s and 0s evolution as a temporal serie 
     */
    private static double[] getNodeTemporalSerie(AbstractCAGraph CA, int x) {

        //a partir de wordsTimeEvolution converti a double[] de 1 e 0 
        Model network = CA.getNetwork();
        String stringSerie = network.getNode(x).getWordsTimeEvolution();
        int T = stringSerie.length();
        double serie[] = new double[T];
        for (int i = 0; i < T; i++) {
            serie[i] = String.valueOf(stringSerie.charAt(i)).equals("1") ? 1.0d : 0.0d;
        }
        return serie;
    }

    /**
     * Run the Word-Length frequency
     *
     * @param CA
     * @return an array containing the frequency of each combination of counting
     * words, 1, 11, 111, 1111,... 1^40 (considera somente 40, nao precisa mais)
     */
    public static double[] runWordFrequency(AbstractCAGraph CA, int LIMIT) {
        //int LIMIT = 40;
        double[] wordFreq = new double[LIMIT];
        Model network = CA.getNetwork();
        double countTotalWords = 0;
        for (int x : network.getNodes().keySet()) {
            String str = network.getNode(x).getWordsTimeEvolution();
            Pattern pattern = Pattern.compile("[0]{1,}");
            String[] split = pattern.split(str);
            for (int i = 1; i < split.length; i++) {//starts from 1, avoid empty character
                if (split[i].length() < LIMIT) {
                    int count = split[i].length();
                    wordFreq[count]++;
                    countTotalWords++;
                }
            }
        }
        //normalize
        //featsHistoWL[] = Feature.histogramBinRange(featWordL, numBins, 0.0d, 1.0d);
        for (int i = 0; i < LIMIT; i++) {
            wordFreq[i] = wordFreq[i] / (countTotalWords * 1.0);
        }

        return wordFreq;
    }
 

}
