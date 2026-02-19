package Utils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set; 

/**
 *
 * @author Jeaneth Machicao
 */
public class CUtil {

    public static void printMatrix(double m[][]) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public static void printMatrix(int m[][]) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public static void printArray(double m[]) {
        for (int i = 0; i < m.length; i++) {
            System.out.print(m[i] + "\t");
        }
    }

    public static void printArray(int m[]) {
        for (int i = 0; i < m.length; i++) {
            System.out.print(m[i] + "\t");
        }
    }

    /**
     * The probability p^l_i is the number of constant words of length l divided
     * by the number of all words found in the time series of node i. The
     * maximal possible word length is given by the length T of the time series
     * analyzed
     *
     * @param word
     * @param L
     * @return
     */
    public static double probabilityOfConstantWordsOfLengthL(String word, int L) {

        int n = word.length();
        double prob = 0.0d;
        if (n < L) {
            System.err.println("L should be lower than the word length");
            return prob;
        }
        int numberOfAllWordsFound = n - L + 1;
        String searchForO = genString(L, "0");
        String searchFor1 = genString(L, "1");
        int counterZero = 0;
        int counterOnes = 0;
        for (int i = 0; i < numberOfAllWordsFound; i++) {
            if (word.substring(i, i + L).equals(searchForO)) {
                counterZero++;
            }
            if (word.substring(i, i + L).equals(searchFor1)) {
                counterOnes++;
            }
        }
        //System.out.println(counterZero);
        //System.out.println(counterOnes);

        prob = (counterZero + counterOnes) * 1.0d / numberOfAllWordsFound * 1.0d;

        return prob;
    }

    private static String genString(int L, String c) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < L; i++) {
            string.append(c);
        }
        return string.toString();
    }


    public static void criateFolder(String directoryName) {

        File theDir = new File(directoryName);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + directoryName);
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
                System.err.println("SecurityException " + se);
            }
            if (result) {
                //System.out.println("__DIR created");
            }
        }
    }

    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    /**
     * Sort a Map class by its values in ascending order
     *
     * @param unsortMap
     * @return
     */
    public static Map<String, Double> sortMapByValues(Map<String, Double> unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map sortedMap = new LinkedHashMap();
        int i = 0;
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
//            if (++i >= num) {
//                break;
//            }
        }
        return sortedMap;
    }

    public static int nSetIntersection(ArrayList<?> A, ArrayList<?> B) {
        ArrayList intersection = new ArrayList(A);
        intersection.retainAll(B); // A∩B
        int inter = intersection.size();
        return inter;
    }

    public static int nSetUnion(ArrayList<?> A, ArrayList<?> B) {
        Set union = new HashSet(A);
        union.addAll(B);// A∪B 
        int inter = union.size();
        return inter;
    }

    public static <T> ArrayList<T> setUnion(List<T> A, List<T> B) {
        Set<T> set = new HashSet<T>();

        set.addAll(A);
        set.addAll(B);

        return new ArrayList<T>(set);
    }

    public static <T> ArrayList<T> setIntersection(List<T> A, List<T> B) {
        ArrayList<T> list = new ArrayList<T>();

        for (T t : A) {
            if (B.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public static <T> ArrayList<T> setDifference(List<T> A, List<T> B) {
        ArrayList<T> difference = new ArrayList<T>(A);
        difference.removeAll(B);// A\B 
        return difference;
    }

    public static double[] fromListToArray(List<Double> serie) {
        double[] serieDouble = new double[serie.size()];
        for (int k = 0; k < serieDouble.length; k++) {
            serieDouble[k] = serie.get(k);
        }
        return serieDouble;
    }

    public static <T extends Object> List<List<T>> split(List<T> list, int bins) {
////        public static List<Integer> list = new ArrayList<Integer>();
////    list.add(0);
////    list.add(1); 
////    list.add(6);
////    list.add(100);
////
////    int bins = 4;
////    List<List<Integer>> lists = split(list, bins);
////    System.out.println(lists); // [[0, 1, 2], [3, 4, 5], [6, 0, 1], [2, 3, 4, 5, 6, 100]]
////    
        List<List<T>> lists = new ArrayList<>();
        int targetSize = (int) (list.size() / bins);
        int rest = list.size() % bins;
        int i = 0;
        for (i = 0; i < targetSize * (bins - 1); i += targetSize) {
            lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
        }
        lists.add(list.subList(i, Math.min(i + targetSize + rest, list.size())));
        return lists;
    }

    private static int geraMascara(int numBits) {
        int ret = 0;
        for (int cont = 0; cont < numBits; cont++) {
            ret = ret << 1;
            ret = ret | 1;
        }
        return ret;
    }
    //BigInteger

    public static BigInteger[] smallest(int p, BigInteger c[]) // p: posiçao, c: valor 
    { // solo crear replica con los 64 primeros bits, es decir con el primer BigInteger: master[0]
        BigInteger resultado[] = c.clone();
        resultado[0] = c[0].flipBit(p - 1);// Nota: BigInteger comienza el flip desde 0
        return resultado;
    }

    //More than one position to initial condition
    public static BigInteger[] smallest(int p[], BigInteger c[]) // p: posiçao, c: valor 
    { // solo crear replica con los 64 primeros bits, es decir con el primer BigInteger: master[0]
        BigInteger resultado[] = c.clone();
        resultado[0] = c[0].flipBit(p[0] - 1);// primera vez
        for (int i = 1; i < p.length; i++) {
            resultado[0] = resultado[0].flipBit(p[i] - 1);// Nota: BigInteger comienza el flip desde 0
        }
        return resultado;
    }
    //long

    public static long smallest(int p, long c, int num) // p: posiçao, c: valor  , num: sizeBYTE
    {
        long b = pegarBit(p, c);
        b = b == 1 ? 0 : 1; // trocar bits
        long d = c & (geraMascara(num - p) << p) | (b << (p - 1)) | (c & (geraMascara(p - 1)));
        return d;
    }
    //int

    public static int smallest(int p, int c, int num) // p: posiçao, c: valor  , num: sizeBYTE
    {
        int b = pegarBit(p, c);
        b = b == 1 ? 0 : 1; // trocar bits
        int d = c & (geraMascara(num - p) << p) | (b << (p - 1)) | (c & (geraMascara(p - 1)));
        return d;
    }
    //byte

    public static byte smallest(int p, byte c, int num) // p: posiçao, c: valor  , num: sizeBYTE
    {
        byte b = pegarBit(p, c);
        b = (byte) (b == 1 ? 0 : 1); // trocar bits
        byte d = (byte) (c & (geraMascara(num - p) << p) | (b << (p - 1)) | (c & (geraMascara(p - 1))));
        return d;
    }
    //int

    private static int pegarBit(int p, int c) // p: posiçao, c: valor 
    {
        int result = (c & (1 << (p - 1))) >>> (p - 1);
        return result;
    }

    //byte
    public static int pegarBit(int p, BigInteger c) // p: posiçao, c: valor 
    {
        if (c.testBit(p)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static byte pegarBit(int p, byte c) // p: posiçao, c: valor 
    {
        byte result = (byte) ((c & (1 << (p - 1))) >>> (p - 1));
        return result;
    }

    //long
    private static long pegarBit(int p, long c) // p: posiçao, c: valor 
    {
        long result = ((c & (1 << (p - 1))) >>> (p - 1));
        return result;
    }

    public static double[] calculateMeanStdDvt(double values[]) {
        double mean = 0.0d;
        double stddvt = 0.0d;
        int count = values.length;
        for (int i = 0; i < count; i++) {
            mean += values[i];
        }
        // calculate mean
        mean = mean / count; // M = SUM/(N)
        for (int i = 0; i < values.length; i++) {
            double val = values[i];
            stddvt += Math.pow(val - mean, 2);  // SUM += (X - M)^2
        }
        // calculate std dvt
        stddvt = Math.pow((stddvt / count), 0.5); // S = (SUM/(N) )^(1/2)

        return new double[]{mean, stddvt};

    }

    public static void mainTest(String args[]) {
        /*long valor = -5434;
         int pos = 1;
         Byte b = new Byte(valor);
         System.out.println(b.toString());
         System.out.println(pegarBit(4, 5));
         System.out.println(smallest(pos, valor, 32));
         long val = smallest(pos, valor, 32);
         Byte b1 = new Byte(val);
         System.out.println(b1);*/

        int position[] = {40, 15};
        BigInteger big[] = {new BigInteger("602903206")};
        BigInteger result[] = smallest(position, big);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i].toString());
        }

    }

    public static double[] arrayListToDoubleArray(ArrayList listaDouble) {
        double ret[] = new double[listaDouble.size()];
        for (int cont = 0; cont < listaDouble.size(); cont++) {
            ret[cont] = (Double) listaDouble.get(cont);
        }
        return ret;
    }

    /**
     *
     * @param data an array to sort
     * @return an index array according to the data ordered //this method uses
     * JAVA 8.
     */
    public static Integer[] sortArray(final double data[]) {
        //final Integer[] idx = {0, 1, 2, 3};
        //final double[] data = {1.7f, -0.3f, 2.1f, 0.5f};

        final Integer[] idx = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
            idx[i] = i;
        }

        Arrays.sort(idx, (final Integer a, final Integer b) -> Double.compare(data[a], data[b]));
 
        //int[] intArray = Arrays.stream(idx).mapToInt(Integer::intValue).toArray();
        return idx;
    }

    public static int[] getSetRandomNumbers(int upperbound, int lowerbound) {
        int tam = upperbound - lowerbound;
        Random rand = new Random();
        Set<Integer> conjunto = new HashSet<Integer>(tam);
        int lista[] = new int[tam];
        int i = 0;
        for (int cont = 0; cont < tam; cont++) {

            // adiciona un # no set, no repetitions
            int random_integer = rand.nextInt(upperbound - lowerbound) + lowerbound;
            int valorNovo = random_integer;
            if (!conjunto.add(valorNovo)) // si hay un repetido, intentar de nuevo
            {
                cont--;

            } else {
                lista[i++] = valorNovo;
            }

        }

        return lista;
    }

    /**
     * Sum equal size matrizes!
     *
     */
    public static double[][] sumMatrizes(double[][] a, double[][] b) {
        double sum[][] = new double[a.length][a[0].length];
        for (int i = 0; i < sum.length; i++) {
            for (int j = 0; j < sum.length; j++) {
                sum[i][j] = a[i][j]
                        + b[i][j];
            }

        }
        return sum;
    }

    public static double[] convertMatrixToArray(double[][] matrix) {
        double[] array = new double[matrix.length * matrix.length];

        int k = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                array[k++] = matrix[i][j];
            }
        }
        return array;
    }

    public static double[] sumRowsMatrix(double[][] m) {
        double[] sum = new double[m.length];
        for (int i = 0; i < m.length; i++) {
            double s = 0.0;
            for (int j = 0; j < m[0].length; j++) {
                s += m[i][j];
            }
            sum[i] = s * 1.0 / (m[0].length * 1.0);
        }
        return sum;
    }

    public static double[] sumColsMatrix(double[][] m) {
        double[] sum = new double[m[0].length];
        for (int j = 0; j < m[0].length; j++) {
            double s = 0.0;
            for (int i = 0; i < m.length; i++) {
                s += m[i][j];
            }
            sum[j] = s * 1.0 / (m.length * 1.0);
        }
        return sum;
    }

    public static void mainX(String[] args) {
        int[][] data = {//matrix quadrada
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3},
            {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8},
            {1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 7},
            {1, 1, 1, 1, 1, 5, 5, 5, 1, 1, 1, 1, 6},
            {1, 1, 1, 1, 1, 5, 5, 5, 5, 1, 1, 1, 5},
            {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 4},
            {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 3},
            {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 2},
            {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -2},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -3}};
        for (int i = 0; i < data.length; i++) {
            for (int k = 0; k < i; k++) {
                System.out.print("\t");
            }
            for (int j = i+1; j < data.length; j++) {
                System.out.print(data[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public static void main3(String[] args) {
        double[][] data = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 5, 5, 5, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 5, 5, 5, 5, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 1},
        {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 1},
        {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 1},
        {1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
        double[] sum = sumColsMatrix(data);
        for (int i = 0; i < sum.length; i++) {
            System.out.println(sum[i]);

        }
    }

    public static void main2(String[] args) {
        int[] vals = getSetRandomNumbers(10, 5);
        for (int i = 0; i < vals.length; i++) {
            System.out.println(vals[i]);
        }
    }
}
