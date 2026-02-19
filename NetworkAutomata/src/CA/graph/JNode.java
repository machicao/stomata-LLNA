package CA.graph;

import static CA.graph.AbstractCAGraph.ALIVE;
import static CA.graph.AbstractCAGraph.DEATH; 

/**
 *
 * @author Jeaneth Machicao
 */
public class JNode extends CNode {

    protected int currState; // 1- 0  current state
    protected int nextState; // 1- 0  current state

    //
    protected int numDeaths = 0;
    protected int numAlives = 0;
    protected StringBuffer wordsTimeEvolution;////save the states in string format during the time evolution
    protected double cellShannonEntropy;
    protected double cellTsallisEntropy;
    protected double cellWordEntropy;
    protected double[] cellFourierPartialEnergy;
    protected double cellLempelZivComplexity;
    private double cellLEProfile;

    public JNode(int cd) {
        super(cd);
        wordsTimeEvolution = new StringBuffer();
    }

    public JNode(int cd, String name) {
        super(cd, name);
        wordsTimeEvolution = new StringBuffer();
    }

    public JNode(int cd, String name, int state) {
        super(cd, name);
        currState = state;
        wordsTimeEvolution = new StringBuffer();
    }

    public JNode(int cd, int state) {
        super(cd);
        currState = state;
        wordsTimeEvolution = new StringBuffer();
    }

    public int getCurrentState() {
        return currState;
    }

    public void setCurrState(int state) {
        currState = state;
        if (currState == ALIVE) {
            numAlives++;
            wordsTimeEvolution.append("1");
        }
        if (currState == DEATH) {
            numDeaths++;
            wordsTimeEvolution.append("0");
        }
    }

    public int getNextState() {
        return nextState;
    }

    public void setNextState(int state) {
        nextState = state;
    }

    public void born() {
        currState = ALIVE;
    }

    public void dead() {
        currState = DEATH;
    }

    JNode copy() {
        JNode node = new JNode(this.code);
        node.kWeight = this.kWeight;
        node.neighbor = this.neighbor;
        node.neighborWeight = this.neighborWeight;

        return node;
    }

//    public static void main(String args[]) {
//        int numeronodos = 1000;
//        int avgNodesNeig = 6;
//        int ALIVE = 1;
//        int DEATH = 0;
//        JNode[] nodos = new JNode[numeronodos];
//        for(int i=0 ; i< numeronodos  ; i++)
//        {            
//            if(Math.random() <0.5 )
//                nodos[i] = new JNode(i, DEATH);
//            else
//                nodos[i] = new JNode(i, ALIVE);
//        }
//
//        for(int i=0 ; i< numeronodos*avgNodesNeig; i++)
//        {
//            Random num = new Random();
//            nodos[num.nextInt(numeronodos-1)].addNeighbour(nodos[num.nextInt(numeronodos-1)]);
//        }
//
//        int count =nodos[0].countAliveNeighbors();
//        System.out.println(count);
//
//    }
    /**
     * @return the numDeaths
     */
    public int getNumDeaths() {
        return numDeaths;
    }

    /**
     * @return the numAlives
     */
    public int getNumAlives() {
        return numAlives;
    }

    /**
     * @return the wordsTimeEvolution
     */
    public String getWordsTimeEvolution() {
        return wordsTimeEvolution.toString();
    }

    /**
     * @return the cellShannonEntropy
     */
    public double getCellShannonEntropy() {
        return cellShannonEntropy;
    }

    /**
     * @param cellShannonEntropy the cellShannonEntropy to set
     */
    public void setCellShannonEntropy(double cellShannonEntropy) {
        this.cellShannonEntropy = cellShannonEntropy;
    }

    public double getCellTsallisEntropy() {
        return cellTsallisEntropy;
    }
    public void setCellTsallisEntropy(double val) {
        this.cellTsallisEntropy = val;
    }

    /**
     * @return the cellFourierPartialEnergy
     */
    public double[] getCellFourierPartialEnergy() {
        return cellFourierPartialEnergy;
    }

    /**
     * @param cellFourierPartialEnergy the cellFourierPartialEnergy to set
     */
    public void setCellFourierPartialEnergy(double[] cellFourierPartialEnergy) {
        this.cellFourierPartialEnergy = cellFourierPartialEnergy;
    }

    /**
     * @return the cellLempelZivComplexity
     */
    public double getCellLempelZivComplexity() {
        return cellLempelZivComplexity;
    }

    /**
     * @param lz
     */
    public void setCellLempelZivComplexity(double lz) {
        this.cellLempelZivComplexity = lz;
    }

    /**
     * @return the cellWordEntropy
     */
    public double getCellWordEntropy() {
        return cellWordEntropy;
    }

    /**
     * @param cellWordEntropy the cellWordEntropy to set
     */
    public void setCellWordEntropy(double cellWordEntropy) {
        this.cellWordEntropy = cellWordEntropy;
    }

    @Override
    public void print() {
        System.out.println("\t" + code);
    }

    /**
     * @return the cellLEProfile
     */
    public double getCellLEProfile() {
        return cellLEProfile;
    }

    /**
     * @param cellLEProfile the cellLEProfile to set
     */
    public void setCellLEProfile(double cellLEProfile) {
        this.cellLEProfile = cellLEProfile;
    }
}