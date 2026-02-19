package Utils.Caracterization;

import java.util.ArrayList;
import Utils.DataSet;

/**
 *
 * @author Jeaneth Machicao
 */
public class Statistics {

    private final String classe;
    private final String subclasse;

    public Statistics(String alg, String modo) {
        classe = alg;
        subclasse = modo;
    }

    public DataSet calculateMean(String algoritmo, ArrayList<DataSet> lista) {
        int sizeT = lista.get(0).lista.size(); // tamanho de arraylist de cualquier dataset       
        int sizeSamples = lista.size(); 
        double temp[] = new double[sizeT]; 
        for (int fila = 0; fila < sizeT; fila++) { 
            for (int col = 0; col < sizeSamples; col++) {
                temp[fila] += (Double) lista.get(col).getVal(fila);
            }
        }
        DataSet data = new DataSet(algoritmo, subclasse);
        System.out.println();
        System.out.print(algoritmo + subclasse + " Media   ");
        for (int fila = 0; fila < sizeT; fila++) { 
            double val = temp[fila] / sizeSamples;
            data.set(val); // media                        
            System.out.print(val + ";");
        }
        System.out.println();
        return data;
    }

    public DataSet calculateStdDvt(String algoritmo, ArrayList<DataSet> lista) {
        DataSet result = calculateMean(algoritmo, lista);//MEDIA
        double mean[] = result.getLista();
        int sizeT = lista.get(0).lista.size();  
        int sizePwd = lista.size(); 
        double stddvt[] = new double[sizeT]; 
        for (int fila = 0; fila < sizeT; fila++) { 
            for (int col = 0; col < sizePwd; col++) { 
                double val = (Double) lista.get(col).getVal(fila);
                stddvt[fila] += Math.pow(val - mean[fila], 2);  // SUM += (X - M)^2
            }
            // calcular std dvt
            stddvt[fila] = Math.pow((stddvt[fila] / (sizePwd)), 0.5); // S = (SUM/(N) )^(1/2)
        }
        System.out.print(algoritmo + subclasse + " DsvStd   ");
        for (int fila = 0; fila < sizeT; fila++) {
            result.setDsvStd(stddvt[fila]); 
            System.out.print(stddvt[fila] + ";");
        }
        System.out.println();
        return result;

    }

    public DataSet calcMediaDsvStd(ArrayList<DataSet> lista) {
        return calculateStdDvt(classe, lista);
    }

    public static double calculaMedia(double[] lista) {
        double sum = 0.0d;
        for (int fila = 0; fila < lista.length; fila++) { 
            sum = sum + lista[fila];
        }
        return sum / (1.0 * lista.length);
    }

    public static double calculaStdDvt(double lista[]) {
        double media = calculaMedia(lista);
        double stddvt = 0.0d;
        for (int fila = 0; fila < lista.length; fila++) {

            stddvt += Math.pow(lista[fila] - media, 2);  // SUM += (X - M)^2                        
        }
        // calcular std dvt
        stddvt = Math.pow((stddvt / (lista.length)), 0.5); // S = (SUM/(N) )^(1/2)

        return stddvt;

    }

    public static double sum(double lista[]) {
        double suma = 0.0d;
        for (int fila = 0; fila < lista.length; fila++) {

            suma += lista[fila];
        }
        return suma;
    }

    public static double calculaMedia(int[] lista) {
        double sum = 0.0d;
        for (int fila = 0; fila < lista.length; fila++) { 
            sum = sum + lista[fila];
        }
        return sum / (1.0 * lista.length);
    }

    public static double calculaStdDvt(int lista[]) {
        double media = calculaMedia(lista);
        double stddvt = 0.0d;
        for (int fila = 0; fila < lista.length; fila++) {

            stddvt += Math.pow(lista[fila] - media, 2);  // SUM += (X - M)^2                        
        }
        // calcular std dvt
        stddvt = Math.pow((stddvt / (lista.length)), 0.5); // S = (SUM/(N) )^(1/2)

        return stddvt;

    }

    public static double sum(int lista[]) {
        double suma = 0.0d;
        for (int fila = 0; fila < lista.length; fila++) {

            suma += lista[fila];
        }
        return suma;
    }

}
