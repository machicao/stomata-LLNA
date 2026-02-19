package Utils.Caracterization;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Jeaneth Machicao
 */
public class Feature implements Serializable {

    protected int nFeatures;
    protected String[] featuresNames;
    protected double[] features;
    private String className;
    private int classIdx;//an index correspoding to the respective className (1-ALTURA) (2-LARGURA)
    private static final Logger fLogger = Logger.getLogger(Feature.class.getPackage().getName());

    public Feature(double[] _features, String _nameClass) {
        nFeatures = _features.length;
        featuresNames = new String[nFeatures];
        features = _features;
        className = _nameClass;
        for (int i = 0; i < nFeatures; i++) {
            featuresNames[i] = "f" + (i + 1); //Weka starts at 1
        }
    }

    public Feature(double[] _features, String _nameClass, int _indexClass) {
        nFeatures = _features.length;
        featuresNames = new String[nFeatures];
        features = _features;
        className = _nameClass;
        classIdx = _indexClass;
        for (int i = 0; i < nFeatures; i++) {
            featuresNames[i] = "f" + (i + 1); //Weka starts at 1
        }
    }

    public Feature(double[] _features, String _nameClass, String _featuresNames[]) {
        nFeatures = _features.length;
        featuresNames = _featuresNames;
        features = _features;
        className = _nameClass;
    }

    public Feature(int n) {
        nFeatures = n;
        features = new double[nFeatures];
        featuresNames = new String[nFeatures];
        for (int i = 0; i < nFeatures; i++) {
            features[i] = 0.0f;
            featuresNames[i] = "f" + i;
        }
    }

    public Feature(Feature f) {
        nFeatures = f.nFeatures;
        featuresNames = new String[nFeatures];
        features = new double[nFeatures];
        classIdx = f.classIdx;
        for (int i = 0; i < nFeatures; i++) {
            features[i] = f.features[i];
            featuresNames[i] = f.featuresNames[i];
        }
        className = f.getNameClass();
    }

    public double[] getFeatures() {
        return features;
    }

    public ArrayList<Double> getFeaturesArrayList() {
        ArrayList<Double> listFeatures = new ArrayList<>();
        for (int i = 0; i < features.length; i++) {
            listFeatures.add(features[i]);
        }
        return listFeatures;
    }

    public void setFeatures(double[] features) {
        this.features = features;
    }

    public String[] getFeaturesNames() {
        return featuresNames;
    }

    public void setFeaturesNames(String[] featuresNames) {
        this.featuresNames = featuresNames;
    }

    public int getnFeatures() {
        return nFeatures;
    }

    public void setnFeatures(int nFeatures) {
        this.nFeatures = nFeatures;
    }

    public String getNameClass() {
        return className;
    }

    public int getNumAtributes() {
        return featuresNames.length;
    }

    public void setNameClass(String _nameClass) {
        className = _nameClass;
    }

    public int getClassIdx() {
        return classIdx;
    }

    public void setClassIdx(int classIdx) {
        this.classIdx = classIdx;
    }

    public void print() {

        for (int i = 0; i < features.length - 1; i++) {
            System.out.print(features[i] + "\t");
        }
        System.out.print(features[features.length - 1]);
        System.out.print("\n");
    }

    @Override
    public String toString() {
        String cadena = "";
        for (int i = 0; i < features.length - 1; i++) {
            cadena += features[i] + "\t";
        }
        cadena += features[features.length - 1];
        //cadena += "\n";
        return cadena;
    }

    public void printLabels() {
        System.out.print("\t");
        for (int i = 0; i < features.length - 1; i++) {

            System.out.print(featuresNames[i] + "\t");
        }
        System.out.print(featuresNames[features.length - 1]);
        System.out.print("\n");
    }

    public void sum(Feature f) {
        for (int i = 0; i < features.length; i++) {
            features[i] += f.features[i];
        }
    }

    public void divide(double d) {
        for (int i = 0; i < features.length; i++) {
            features[i] /= d;
        }
    }

    public void minus(Feature feature) {
        for (int i = 0; i < features.length; i++) {
            features[i] -= feature.features[i];
        }
    }

    public void minusPow(Feature feature) {
        for (int i = 0; i < features.length; i++) {
            features[i] = (features[i] - feature.features[i]) * (features[i] - feature.features[i]);

        }
    }

    /* 
     * This can be generalized to restrict the range of values in the dataset between 
     * any arbitrary points  a  and  b  using X'=a+((X - Xmin)(b-a)/(Xmax- Xmin))
     */
    private static double[] scalingFeatures(double[] A, double a, double b) {
        double X[] = new double[A.length];
        System.arraycopy(A, 0, X, 0, A.length);
        double minmax[] = minmax(X);
        double Xmin = minmax[0];
        double Xmax = minmax[1];
        for (int i = 0; i < A.length; i++) {
            X[i] = a + ((X[i] - Xmin) * (b - a) / (Xmax - Xmin));
        }
        return X;
    }

    public static double[] mean(double A[]) {
        double mean = Statistics.calculaMedia(A);
        return new double[]{mean};
    }

    public static double[] meanStd(double A[]) {
        double mean = Statistics.calculaMedia(A);
        double stddvt = Statistics.calculaStdDvt(A);
        return new double[]{mean, stddvt};
    }

    public static double[] minmax(double[] A) {
        double copia[] = new double[A.length];
        System.arraycopy(A, 0, copia, 0, A.length);
        Arrays.sort(copia);//sorts into ascendent order
        return new double[]{copia[0], copia[A.length - 1]};//min - max values of the array feature
    }

    public static double[] minmax(double[][] A) {
        double minmax[] = new double[2];
        minmax[0] = 10000;
        minmax[1] = -10000;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (minmax[0] >= A[i][j]) {
                    minmax[0] = A[i][j];
                }
                if (minmax[1] <= A[i][j]) {
                    minmax[1] = A[i][j];
                }
            }

        }

        return minmax;//min - max values of the array feature
    }

    public static int[] minmax(int[] A) {
        int copia[] = new int[A.length];
        System.arraycopy(A, 0, copia, 0, A.length);
        Arrays.sort(copia);//sorts into ascendent order
        return new int[]{copia[0], copia[A.length - 1]};//min - max values of the array feature
    }

    public static double[] normalize(double[] A) {
        return scalingFeatures(A, 0.0d, 1.0d);
    }

    public static double[] standardize(double[] A) {
        //X'= x - mean(x)/std
        double X[] = new double[A.length];
        double meanStd[] = meanStd(A);
        double mean = meanStd[0], std = meanStd[1];
        for (int i = 0; i < A.length; i++) {
            X[i] = (A[i] - mean) / std;
        }
        return X;
    }

    /**
     *
     * Calculates Histograma from data over their min-max values
     *
     * @param data
     * @param numBins
     * @return
     */
    public static int[] histogram(int[] data, int numBins) {
        final int[] result = new int[numBins];
        int[] minmax = minmax(data);
        int min = minmax[0], max = minmax[1];

        final double binSize = (max - min) / (numBins - 1);

        for (double d : data) {
            int bin = (int) ((d - min) / binSize);
            if (bin < 0) {
                System.err.println("this data is smaller than min");

            } else if (bin >= numBins) {
                System.err.println("tthis data point is bigger than max ");
            } else {
                result[bin] += 1;
            }
        }
        return result;
    }

    public static double[] histogram(double[] data, int numBins) {
        final double[] result = new double[numBins];
        double[] minmax = minmax(data);
        double min = minmax[0], max = minmax[1];

        final double binSize = (max - min) / (numBins - 1);

        for (double d : data) {
            int bin = (int) ((d - min) / binSize);
            if (bin < 0) {
                System.err.println("this data is smaller than min");

            } else if (bin >= numBins) {
                System.err.println("tthis data point is bigger than max ");
            } else {
                result[bin] += 1;
            }
        }
        return result;
    }

    public static double[] histogram(double[][] data, int numBins) {
        final double[] result = new double[numBins];
        double[] minmax = minmax(data);
        double min = minmax[0], max = minmax[1];

        final double binSize = (max - min) / (numBins - 1);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int bin = (int) ((data[i][j] - min) / binSize);
                if (bin < 0) {
                    System.err.println("this data is smaller than min");

                } else if (bin >= numBins) {
                    System.err.println("tthis data point is bigger than max ");
                } else {
                    result[bin] += 1;
                }
            }
        }
        return result;

    }

    /**
     * Calculates Histograma from data considering outer min-max values
     *
     * @param data
     * @param numBins
     * @param min
     * @param max
     * @return
     */
    public static double[] histogramBinRange(double[][] data, int numBins, double min, double max) {
        final double[] result = new double[numBins];

        final double binSize = (max - min) / (numBins);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                int bin = (int) ((data[i][j] - min) / binSize);
                if (bin < 0) {
                    //System.err.println("this data is smaller than min");
                    result[0] += 1;
                } else if (bin >= numBins) {
                    //System.err.println("tthis data point is bigger than max ");
                    result[numBins - 1] += 1;
                } else {
                    result[bin] += 1;
                }
            }
        }
        return result;
    }

    /**
     * Calculates Histograma from data considering outer min-max values
     *
     * @param data
     * @param numBins
     * @param min
     * @param max
     * @return
     */
    public static double[] histogramBinRange(double[] data, int numBins, double min, double max) {
        final double[] result = new double[numBins];

        final double binSize = (max - min) / (numBins);

        for (double d : data) {
            int bin = (int) ((d - min) / binSize);
            if (bin < 0) {
                //System.err.println("this data is smaller than min");
                result[0] += 1;
            } else if (bin >= numBins) {
                //System.err.println("tthis data point is bigger than max ");
                result[numBins - 1] += 1;
            } else {
                result[bin] += 1;
            }
        }
        return result;
    }

    public static int[] histogramBinRange(int[] data, int numBins, double min, double max) {
        final int[] result = new int[numBins];

        final double binSize = (max - min) / (numBins);

        for (double d : data) {
            int bin = (int) ((d - min) / binSize);
            if (bin < 0) {
                //System.err.println("this data is smaller than min");
                result[0] += 1;
            } else if (bin >= numBins) {
                //System.err.println("tthis data point is bigger than max ");
                result[numBins - 1] += 1;
            } else {
                result[bin] += 1;
            }
        }
        return result;
    }

    public static void Main(String[] args) {
        double[] data = {0.0, 0.0, 0.4, 0.4, 0.4, 0.9, 1.0, 1.0, 1.0};
        double rest[] = Feature.histogramBinRange(data, 3, 0.5, 1.5);
        for (int i = 0; i < rest.length; i++) {
            System.out.print(rest[i] + "\t");
        }
    }

 
    public static List<Feature> unserialize(String nameFile) {
        //deserialize the quarks.ser file
        List<Feature> recoveredList = new ArrayList<Feature>();
        try {
            //use buffering
            InputStream file = new FileInputStream(nameFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                recoveredList = (List<Feature>) input.readObject();
            } finally {
                input.close();
            }
        } catch (ClassNotFoundException ex) {
            fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
        } catch (IOException ex) {
            fLogger.log(Level.SEVERE, "Cannot perform input.", ex);
        }
        return recoveredList;
    }

    public static void serialize(List<Feature> lista, String nameFile) {
        try {
// Serialize data object to a file
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(nameFile));
            output.writeObject(lista);
            output.close();

        } catch (IOException e) {
        }
    }

 
    public static void saveMatlabFile(List<Feature> listFeatures, String fileMATOutput) {
        //convert data feature to matrix
        int numFeatures = listFeatures.get(0).getnFeatures();
        int numSamples = listFeatures.size();
        //f1, f2, f3, .... clase1
        //f1, f2, f3, .... clase2
        double[][] featureMatrix = new double[numSamples][numFeatures];
        String classes[] = new String[numSamples];
        double idxClasses[] = new double[numSamples];
        for (int i = 0; i < numSamples; i++) {
            System.arraycopy(listFeatures.get(i).getFeatures(), 0, featureMatrix[i], 0, numFeatures);
            classes[i] = listFeatures.get(i).getNameClass();
            idxClasses[i] = (double) listFeatures.get(i).getClassIdx() + 1;//matlab index starts at i=1
        }
        System.out.println("");
        //1. First create example arrays
        MLDouble ML_Featurematrix = new MLDouble("featurematrix", featureMatrix);
        MLChar ML_Classes = new MLChar("legendclass", classes);
        MLDouble ML_IndexClasses = new MLDouble("k", idxClasses, idxClasses.length);//classes

        //2. write arrays to file
        ArrayList<MLArray> ML_Lista = new ArrayList<>();
        ML_Lista.add(ML_Featurematrix);
        ML_Lista.add(ML_Classes);
        ML_Lista.add(ML_IndexClasses);

        try {
            MatFileWriter matFileWriter = new MatFileWriter(fileMATOutput, ML_Lista); //"features.mat"
        } catch (IOException ex) {
            System.err.println("");
        }
    }
 

    /*
     * Export the database as a Weka DataSet
     */
    public static Instances ToWekaInstances(List<Feature> feat) {
        Instances wekaInstances = null;
        int numFeatures = feat.get(0).getNumAtributes();
        FastVector attributes = new FastVector(numFeatures + 1);
        String attr[] = feat.get(0).getFeaturesNames();

        // add the numeric feature attributes 
        for (int i = 0; i < numFeatures; i++) {
            attributes.addElement(new Attribute(attr[i]));
        }
        attributes.addElement(new Attribute("class"));

        for (Feature feature : feat) {
            double temp[] = feature.getFeatures();
            double f[] = new double[numFeatures + 1];
            System.arraycopy(temp, 0, f, 0, numFeatures);
            f[numFeatures] = (double) feature.getClassIdx();
            Instance i1 = new Instance(1.0, f);
            wekaInstances.add(i1);
        }

        return wekaInstances;
    }

    public static void saveArffFile(List<Feature> lista, String pathOutput, String wekaName) {
        WriterFeatureToWEKA wekaObj = new WriterFeatureToWEKA();
        for (Feature f : lista) {
            wekaObj.addFeature(f);
        }
        //print        
        wekaObj.printWeka(pathOutput, wekaName);
    }
}
