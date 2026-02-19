package Utils;

import CA.graph.JNode;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import CA.graph.Model;

/**
 *
 * @author Jeaneth Machicao
 */
public class JGraphEdgeListWriter extends JGraphAbstractWriter {

    protected String fileType = ".edgelist";

    @Override
    public void write(String nameFile, Model graph) {
        HashMap<Integer, JNode> nodes = graph.getNodes();
        int numNodes = graph.getN();
        String whiteSpace = " ";
        try {
            FileOutputStream file = new FileOutputStream(new File(nameFile + fileType));
            BufferedOutputStream buffer = new BufferedOutputStream(file);

            for (int in : graph.getNodes().keySet()) {
                ArrayList<Integer> vecinos = graph.getNode(in).getNeighbor();
                if (vecinos.size() > 0) {
                    String from = String.valueOf(in + 1);
                    for (Integer out : vecinos) {
                        String to = whiteSpace + String.valueOf(out + 1) + "\n";
                        buffer.write(from.getBytes(modeUTF8));
                        buffer.write(to.getBytes(modeUTF8));
                    }
                }
            }
            //finish
            buffer.close();

        } catch (IOException ex) {
            System.err.println(ex);

        }
    }
 

}
