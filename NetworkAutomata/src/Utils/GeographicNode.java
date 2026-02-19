package Utils;

import CA.graph.JNode;

/**
 *
 * @author Jeaneth Machicao
 */
public class GeographicNode extends JNode {

    double x;
    double y;
    double z;

    public GeographicNode(int cod, double xx, double yy) {
        super(cod);
        x = xx;
        y = yy;
    }
    public GeographicNode(int cod, double xx, double yy, double zz) {
        super(cod);
        x = xx;
        y = yy;
        z= zz;
    }

    public int getNodeID() {
        return getCode();
    }

    public double getXcoord() {
        return x;
    }

    public double getYcoord() {
        return y;
    }
    public double getZcoord() {
        return z;
    }
}
