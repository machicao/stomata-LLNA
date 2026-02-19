/*
 * OTCAGraph
 * Outer-totalistic cellular automata on graphs. Carsten Marr a,∗, Marc-Thorsten Hütt
 */
package CA.graph;

import static CA.graph.AbstractCAGraph.ALIVE;
import static CA.graph.AbstractCAGraph.DEATH; 
import Utils.OpenNetworkEdgeList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *
 * @author Jeaneth Machicao
 */
public class OTCAGraph extends AbstractCAGraph {

    protected Model network;
//#    private PrefGraph prefusegraph;
    protected int N;
    protected char PLUS = '+';
    protected char MINUS = '-';
    protected char ONE = '1';
    protected char ZERO = '0';
    protected char ALPHA = '*';
    protected char GAMMA = '*';

    // regra : (+,1) ; (0, 1) ; (-,0)..
    protected final double KAPPA;//default
    private String Rule = "(+,-)"; //Marr's Rule
    int defaultConstantBorder = 0;

    public OTCAGraph(Model n, double kappa, String rule) {
        network = n;
        KAPPA = kappa;
        Rule = rule;
//#        initGraph();
        parseRule(Rule);
        //remove nodes from network with isolated nodes 
        removingIsolatedNodesfromNetwork();
        N = network.getN();
    }

    public OTCAGraph(Model n, double kappa) {
        network = n;
        KAPPA = kappa;
//#        initGraph();
        parseRule(Rule);
        //remove nodes from network with isolated nodes 
        removingIsolatedNodesfromNetwork();

        N = network.getN();
    }

    @Override
    public void removingIsolatedNodesfromNetwork() {
        network.removingIsolatedNodesfromNetwork();
    }

    @Override
    public ArrayList<Integer> getIteratorNodesByDegree() {

        ///network.getIteratorNodesByDegree();
        System.out.println("ordering Graph by Degree");
        return network.getIDsNodesByDegree();
    }


    public static void mainX(String[] args) {
        int avgK = 6;
        int m = avgK / 2;
        int m0 = 10;
        double kappa = 0.5;
        String rule = "(+,-)";
        //Model network = new ScaleFreeNetwork(200, m0, m, true);
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("BD/R/ring/ring_n=10_i=3.txt", 10, false);
        //Model old = new OpenNetworkEdgeList().open("RedesDB/RedesDB#1/geo/geo_n=500_k=6_i=1.txt");
        Model old= OpenNetworkEdgeList.openIgnoringHeaders("redesDB/redesLiterariasPartialLematization/8autores/autor2/livro1.net", 0);
        Model cn = Model.newInstance(old);
        AbstractCAGraph automata = new OTCAGraph(cn, kappa, rule);
        automata.random(0.5);
        //automata.printAdjacency();
        //int vecinos[] = automata.Neighbor(8);
        //for (int i = 0; i < vecinos.length; i++) {
        //    System.out.println(vecinos[i]);
        // }
        System.out.println("Automata");
        long tiempoInicio = System.currentTimeMillis();

        for (int j = 0; j < 10; j++) {
            //System.out.println("----------------------------");
            automata.printBoard();
            automata.step(1);

        }
        long totalTiempo = System.currentTimeMillis() - tiempoInicio;
        System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");

        System.out.println("----------------------------");

    }

 
    public void setNewRule(String text) {
        Rule = text; // tiene que quedar la regla fixada
        // reinicio os densityores da regra
        parseRule(Rule);
    }

    /*
     * Function to read and parse the  Rules
     * dynamic: (+,-)
     */

    @Override
    protected void parseRule(String dynamic) {
        //(0,+)
        ALPHA = dynamic.charAt(1);
        GAMMA = dynamic.charAt(3);
    }

    @Override
    public void step(int days) {
        for (int g = 0; g < days; g++) {
            runStep();
            ITERATIONS++;
        }

    }

    protected int dynamic(char var, int currentState) {
        switch (var) { //==1    
            case '1':
                return ALIVE;
            case '0':
                return DEATH;
            case '+': //remain state
                return currentState;
            case '-'://Flipp
                return 1 - currentState;
            default:
                return -1;
        }
    }

    // Runs one iteration
    private void runStep() {
        for (int x : network.getNodes().keySet()) {
            int currentstate = network.getNode(x).getCurrentState();
            double numAlives = 1.0d * countNumAliveNeighbors(x);//get numNeighbors
            double degree = 1.0d * network.getNode(x).getNeighbor().size();
            double density = (numAlives / degree);
            //System.out.print(density+"\t");//density
            if (density <= KAPPA) {
                network.getNode(x).setNextState(dynamic(ALPHA, currentstate));
            } else if (density > KAPPA) {
                network.getNode(x).setNextState(dynamic(GAMMA, currentstate));
            }
            //System.out.println("=> "+nextMatrix[x] +"\t");
        }

        //update states
        for (int x : network.getNodes().keySet()) {
            network.getNode(x).setCurrState(network.getNode(x).getNextState());//update           
        }
    }

    @Override
    public int executeRules(int currentstate, ArrayList<KV> neigsCurrentStateList) {
        int numAlives = 0;
        int degree = neigsCurrentStateList.size();
        for (KV entry : neigsCurrentStateList) {
            if (entry.getValue() == ALIVE) {
                numAlives++;
            }
        }
        double density = 0.0;
        if (degree == 0)//This can happen when an edge is removed! e.g barmutau
        {
            density = 0;
        } else {
            density = ((1.0d * numAlives) / (1.0d * degree));//[0, 1] !! rede nao deve conter nodos isolados
        }
        if (density <= KAPPA) {
            return dynamic(ALPHA, currentstate);
        } else if (density > KAPPA) {
            return dynamic(GAMMA, currentstate);
        }
        return DEATH;
    }

    public int countNumAliveNeighbors(int x) {
        int sumalives = 0;
        List<Integer> neigs = network.getNode(x).getNeighbor();
        //System.out.print("<"+neigs.get(0)+","+x+","+neigs.get(1)+">");
        //System.out.print("<"+network.getNode(neigs.get(0)).getCurrentState()+",  "+network.getNode(x).getCurrentState()+"  ,"+network.getNode(neigs.get(1)).getCurrentState()+"> ; ");
        for (Integer id : neigs) {
            if (network.getNode(id).getCurrentState() == ALIVE) {
                sumalives++;
            }
        }
        //return sumalives+network.getNode(x).getCurrentState();//totalistic
        return sumalives;//outer-totalistic
    }
 
    @Override
    public void random(double initialProbability) {
        long seed = 104711L;//104711
        Random randGen = new Random(seed);
        //Random randGen = new Random();
        double random = 0;
        //boolean random;
        // Initial State:
        for (int x : network.getNodes().keySet()) {
            random = randGen.nextDouble();
            if (random <= initialProbability) {
                network.getNode(x).setCurrState(ALIVE);
//#                PrefNode.born(prefusegraph.getNode(i));
            } else {
                network.getNode(x).setCurrState(DEATH);
//#                PrefNode.die(prefusegraph.getNode(i));
            }
//            random = randGen.nextBoolean();
            //int state = (random) ? 1 : 0;
            //network.getNode(x).setCurrState(state);
//#           PrefNode.setState(prefusegraph.getNode(x), state);
        }
    }

    //load initial conditions correspoding to a replica of the already states but one bit difference
    // Nota: If networks contains N nodes, and the position is higher than N, avoid!
    //Since, the network is prunned  to remove isolated nodes, then N from original file would not contains
    // the position thay in main class could setup. Thus. One trick is to cross on a FOR all values in order of
    // nodesID and then choose the position specified. 
    //For example: nodes id: 1, 2, 3, 5, 6    has N = 5 and states {0, 1, 1, 0, 1}
    // I will consider that i would known already which is the new N (after removal) thus, 
    // the IDS do not correspond to the position of the master index array
    // for the example the replica would be  {0, 1, 1, 0, 0}
    @Override
    public void loadInit(int[] master) {
        // Initial State:
        int a = 0;
        for (int x : network.getNodes().keySet()) {
            if (master[a] == ALIVE) {
                network.getNode(x).setCurrState(ALIVE);
//#                PrefNode.born(prefusegraph.getNode(i));                
            } else {
                network.getNode(x).setCurrState(DEATH);
//#                PrefNode.die(prefusegraph.getNode(i));                
            }
            a++;

        }
    }

 
    @Override
    public void printBoard() {
        for (int i = 0; i < N; i++) {
            System.out.print(network.getNode(i).getCurrentState() + "\t");
        }
        System.out.println("");
    }

    @Override
    public double[] population() {
        double[] population = new double[2];
        int numAlives = 0;
        for (int i = 0; i < N; i++) {
            if (network.getNode(i).getCurrentState() == ALIVE) {
                numAlives++;
            }
        }
        population[0] = (N - numAlives) * 1.0d / N * 1.0d;
        population[1] = (numAlives) * 1.0d / N * 1.0d;
        return population;
    }

    @Override
    public String getRule() {
        return Rule;
    }

    @Override
    public Model getNetwork() {
        return network;
    }

    @Override
    public void printAdjacency() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getCurrentStates() {
        int states[] = new int[N];
        int a = 0;
        for (int x : network.getNodes().keySet()) {
            states[a++] = network.getNode(x).getCurrentState();
        }
        return states;

    }

    @Override
    public void noiseNetwork(int noiseRate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void randomWiringEdge(boolean addOrRemove) {
        Random random = new Random();
        int nNodes = network.getNodes().size();

        int count = 0;
        if (addOrRemove == true) //add a new edge
        {
            int nodeA = random.nextInt(nNodes);
            int nodeB = random.nextInt(nNodes);
            do {
                if (nodeA != nodeB) {
                    if (!network.getNodes().get(nodeA).isNeighbor(nodeB)) {
                        network.getNode(nodeA).addNeighbor(nodeB);
                        //network.getNode(nodeB).addNeighbor(nodeA);
                        count++;
                    }
                }
            } while (count < 1);
        } else { //remove an edge
            count = 0;
            do {
                int nodeA = random.nextInt(nNodes);
                int sizeA = network.getNode(nodeA).getNeighbor().size();
                if (sizeA != 0) {
                    int positionB = random.nextInt(sizeA);
                    int nodeB = network.getNode(nodeA).getNeighbor().get(positionB);

                    network.getNode(nodeA).deleteNeighbor((Integer) nodeB);
                    //network.getNode(nodeB).deleteNeighbor((Integer) nodeA);

                    count++;
                }
            } while (count < 1);
        }
    }

}
