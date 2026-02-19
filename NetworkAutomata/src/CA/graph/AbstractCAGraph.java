package CA.graph;
 
import java.util.ArrayList;

/**
 *
 * @author Jeaneth Machicao
 */
public abstract class AbstractCAGraph {
public long seed;
    public static final int ALIVE = 1;
    public static final int DEATH = 0;
    protected int ITERATIONS = 1;//time 0 is considered    

    public abstract String getRule();

    public abstract void step(int days);
    public abstract int executeRules(int currentstate, ArrayList<KV>  neigsCurrentStateList);//Used by Lyapunov

    public abstract void printAdjacency();

    public abstract Model getNetwork();

    public abstract ArrayList<Integer> getIteratorNodesByDegree(); 

    public abstract void printBoard();

    public abstract void random(double initialProbability); 
    public abstract double[] population();

    public int getITERATIONS() {
        return ITERATIONS;
    }

    public abstract void loadInit(int[] master);

    public abstract int[] getCurrentStates();

    protected abstract void parseRule(String r);

    public abstract void noiseNetwork(int noiseRate);
    
    public abstract void randomWiringEdge(boolean addOrRemove);

    public abstract void removingIsolatedNodesfromNetwork();

}
