package CA.graph.mainEstomatos;

import Utils.IO;
import Utils.CUtil;
import Utils.JGraphEdgeListWriter;
import Utils.OpenNetworkFromCentroidsFiles;
import static Utils.OpenNetworkFromCentroidsFiles.open;
import CA.graph.Model;

/**
 *
 * @author Jeaneth Machicao
 */
public class GenerateNetworksStomata {

    public static final String PATH = "/NetworkAutomata/RedesDB/DatasetEstomatosReviewv2/";

    public static void main(String[] args) {
        String classesCalisia[] =  {"natural", "humidity90CO2", "humidity90"};
        String classesZebrina[] = {"4h", "24h", "natural"}; 
        String classesCthenante[] = {"4h", "24h"};
        
        mainCallisiaZebrina("C02 (Callisia Repens)/",classesCalisia, 4.65935, 400,  640, 16);//num_descriptors = 16        
        mainCallisiaZebrina("LightConditions (zebrina)/",classesZebrina, 4.65935, 400,  640, 16);//num_descriptors = 16
        
        mainCthenante("LightConditions (Ctenanthe)/", classesCthenante, 0.347, 140,  260, 20);  //num_descriptors = 7
    }

    public static void mainCallisiaZebrina(String tipo, String classes[], double escala, double threI, double threF, double threSteps) {    
        threI = threI/escala;
        threF = threF/escala;
        threSteps= threSteps/escala;
        for (int c = 0; c < classes.length; c = c + 1) {
            IO io = new IO(PATH + tipo + classes[c] + "/", ".txt");
            String directory = io.getDirectory();
            System.out.println(directory);
            String files[] = io.getFiles();
            String names[] = io.getNames();

            for (int i = 0; i < files.length; i++) {
                String arquivo = names[i].replace(".txt", "");
                System.out.println(arquivo);

                for (double Threshold = threI; Threshold <= threF; Threshold = Threshold + threSteps) {

                    Model old = open(directory + names[i], Threshold * 1.0);
                    Model cn = Model.newInstance(old);
                    JGraphEdgeListWriter writer = new JGraphEdgeListWriter();
                    String output = PATH + tipo + "graphs/" + classes[c] + "/";
                    CUtil.criateFolder(PATH + tipo + "graphs/");
                    CUtil.criateFolder(output);
                    writer.write(output + arquivo + "T=" + Math.round(Threshold * escala), cn);
                    System.out.println("Threshold=" + Math.round(Threshold * escala) + "\t" + cn.getN() + "\t" + cn.getNumberEdges() + "\t");
                }
            }
        }
    }

    public static void mainCthenante(String tipo, String classes[], double escala, double threI, double threF, double threSteps) {
        threI = threI/escala;
        threF = threF/escala;
        threSteps= threSteps/escala;

        for (int c = 0; c < classes.length; c = c + 1) {
            IO io = new IO(PATH + tipo + classes[c] + "/", ".txt");
            String directory = io.getDirectory();
            System.out.println(directory);
            String files[] = io.getFiles();
            String names[] = io.getNames();

            for (int i = 0; i < files.length; i++) {
                String arquivo = names[i].replace(".txt", "");
                System.out.println(arquivo);

                for (double Threshold = threI; Threshold <= threF; Threshold = Threshold + threSteps) {

                    Model old = OpenNetworkFromCentroidsFiles.openCthenante(directory + names[i], Threshold * 1.0);
                    Model cn = Model.newInstance(old);
                    JGraphEdgeListWriter writer = new JGraphEdgeListWriter();
                    String output = PATH + tipo + "graphs/" + classes[c] + "/";
                    CUtil.criateFolder(PATH + tipo + "graphs/");
                    CUtil.criateFolder(output);
                    writer.write(output + arquivo + "T=" + Math.round(Threshold * escala), cn);
                    System.out.println("Threshold=" + Math.round(Threshold * escala) + "\t" + cn.getN() + "\t" + cn.getNumberEdges() + "\t");
                }
            }
        }

    }

}
