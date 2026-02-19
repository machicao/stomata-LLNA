package CA.graph;

import java.util.ArrayList;

/**
 *
 * @author Jeaneth Machicao
 */
public abstract class CNode {

    protected int code;
    private String name;
    protected ArrayList<Integer> neighbor;
    protected ArrayList<Double> neighborWeight;
    protected double kWeight;
    protected int k2;
    private int distance = Integer.MAX_VALUE;

    public CNode(int cd) {
        this.code = cd;
        neighbor = new ArrayList<>();
        neighborWeight = new ArrayList<>();
        kWeight = 0;
        k2=0;
        name = String.valueOf(cd);//converts integer id to string
    }

    public CNode(int cd, String n) {
        this.code = cd;
        this.name=n;
        neighbor = new ArrayList<>();
        neighborWeight = new ArrayList<>();
        kWeight = 0;
                k2=0;
        name = String.valueOf(cd);//converts integer id to string
    }

    public int getDistance() {
        return distance;
    }
    
    public void setName(String n)
    {
        this.name= n;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setKWeight(double kWeight) {
        this.kWeight = kWeight;
    }

    public ArrayList<Integer> getNeighbor() {                
        return neighbor;
    }

    public ArrayList<Double> getNeighborWeight() {
        return neighborWeight;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    
    public int getK2()
    {
        return k2;
    }
    
    public void setK2(int val)
    {
        k2=val;
    }

    public void addNeighbor(int node) {
        if (!isNeighbor(node)) {
            neighbor.add(node);
            //System.out.println(kWeight + "," + value);
        }
    }

    public void deleteNeighbor(int node) {
        if (isNeighbor(node)) {
            neighbor.remove((Integer) node);
            //System.out.println(kWeight + "," + value);
        }
    }

    public double getkWeight() {
        return kWeight;
    }

    public void setWeight(int pos, double value) {
        neighborWeight.set(pos, value);
    }

    public double getWeight(CNode node) {
        for (int i = 0; i < neighbor.size(); i++) {
            if (neighbor.get(i) == node.getCode()) {
                return neighborWeight.get(i);
            }
        }
        return -1.0f;
    }

    public boolean isNeighbor(int node) {
        for (int i = 0; i < neighbor.size(); i++) {
            if (neighbor.get(i) == node) {
                return true;
            }
        }
        return false;
    }

    public int getK() {
        return neighbor.size();
    }

    
    public void print() {
        System.out.println("\t" + code);
    }

//    CNode copy() {
//        CNode node = new CNode(this.code);
//        node.kWeight = this.kWeight;
//        node.neighbor = this.neighbor;
//        node.neighborWeight = this.neighborWeight;
//
//        return node;
//    }
    void setCode(int index) {
        this.code = index;
    }

}
