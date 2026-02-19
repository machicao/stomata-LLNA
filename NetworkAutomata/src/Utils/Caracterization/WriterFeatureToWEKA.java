package Utils.Caracterization;
 
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author Jeaneth Machicao
 */
public final class WriterFeatureToWEKA {

    private boolean isHeader;
    private ArrayList<Feature> features;
    private Set<String> classes;
    private FileWriter writer;
    private PrintWriter outfile;
    private int nFeatures;

    public WriterFeatureToWEKA() {

        features = new ArrayList<>();
        classes = new HashSet<>();

    }

    /**
     * Constructs my own Weka file, from Instance (weka package)
     *
     * @param data
     */
    public WriterFeatureToWEKA(Instances data) {
        super();
        nFeatures = data.numAttributes() - 1;//Minus the class attribute in Weka
        int nInstances = data.numInstances();
        for (int i = 0; i < nInstances; i++) {
            Instance aInstance = data.instance(i);
            double[] attributes = aInstance.toDoubleArray();
            int classIndex = aInstance.classIndex();
            String className = aInstance.toString(classIndex);
            Feature newFeature = new Feature(attributes, className);
            addFeature(newFeature);

        }

    }

    public static void saveArff(Instances outputArff, String outputFile) {
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(outputArff);
            saver.setFile(new File(outputFile));
            saver.writeBatch();
        } catch (IOException e) {
            System.err.println("Não foi possível SALVAR arff: " + e);
        }
    }

    public void addFeature(Feature feature) {
        features.add(feature);
        classes.add(feature.getNameClass());
    }

    public void printWeka(String path, String nameWeka) {
        try {
            nFeatures = features.get(0).getnFeatures();
            writer = new FileWriter(path);
            outfile = new PrintWriter(writer);
            writeHeader(nameWeka);
            writeData(nameWeka);
            close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void writeHeader(String nameWeka) {
        String[] classNames = classes.toArray(new String[0]);
        String[] featuresNames = features.get(0).getFeaturesNames();
        int numClasses = classNames.length;

        outfile.print("@RELATION " + nameWeka + "\n");
        for (int i = 0; i < nFeatures; i++) {
            outfile.print("@ATTRIBUTE " + featuresNames[i] + " REAL\n");
        }
        outfile.print("@ATTRIBUTE class {" + classNames[0]);
        for (int i = 1; i < numClasses; i++) {
            outfile.print("," + classNames[i]);
        }
        outfile.print("}\n@DATA\n");
        isHeader = true;
    }

    private void writeData(String nameWeka) {
        if (!isHeader) {
            writeHeader(nameWeka);
        }
        for (int i = 0; i < features.size(); i++) {
            double[] vfeatures = features.get(i).getFeatures();
            if (nFeatures > 0) {
                for (int j = 0; j < nFeatures; j++) {
                    outfile.print(vfeatures[j] + ",");
                }
            }
            outfile.print(features.get(i).getNameClass() + "\n");
        }
    }

    private void close() {
        try {
            outfile.flush();
            writer.close();
            outfile.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    } 
}
