package Utils;

import java.util.ArrayList;

/**
 *
 * @author Jeaneth Machicao
 */
public class DataSet {

    public String rule;
    public String data;
    public ArrayList lista = new ArrayList();
    public ArrayList listaDsvStd = new ArrayList();

    public DataSet(String _rule, String _data, ArrayList list) {
        rule = _rule;
        data = _data;
        lista = (ArrayList) list.clone();
    }

    public DataSet(String _rule, ArrayList list) {
        rule = _rule;
        data = "";
        lista = (ArrayList) list.clone();
    }

    public DataSet(String _rule, String _data) {
        rule = _rule;
        data = _data;
    }

    public void set(double val) {
        lista.add(val);
    }

    public double getVal(int pos) {
        return (Double) lista.get(pos);
    }

    public void setDsvStd(double val) {
        listaDsvStd.add(val);
    }

    public double[] getLista() {
        double ret[] = new double[lista.size()];
        for (int cont = 0; cont < lista.size(); cont++) {
            ret[cont] = (Double) lista.get(cont);
        }
        return ret.clone();
    }

    public double[] getListaDsvStd() {
        double ret[] = new double[listaDsvStd.size()];
        for (int cont = 0; cont < listaDsvStd.size(); cont++) {
            ret[cont] = (Double) listaDsvStd.get(cont);
        }
        return ret.clone();
    }

    @Override
    public String toString() {
        String salida = rule + " , " + data + "  ";
        if (listaDsvStd.size() <= 0) {
            for (Object lista1 : lista) {
                salida += lista1 + ",";
            }
        } else if (listaDsvStd.size() > 0) {
            salida += "\n";
            for (Object listaDsvStd1 : listaDsvStd) {
                salida += listaDsvStd1 + ",";
            }
        }
        return salida;
        //imprime rule - value -  dsv std
    }
}