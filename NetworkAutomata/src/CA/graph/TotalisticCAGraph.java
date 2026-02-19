package CA.graph;
 
import Utils.OpenNetworkEdgeList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jeaneth Machicao
 */
public final class TotalisticCAGraph extends AbstractCAGraph {

    private final Model network; 
    public final int N;
    private String rule; //rule     
    public int R;
    public int K;
    int neigs;// = R * 2 + 1;
    int numBits;// = neigs * K - (neigs - 1);//total de bits da regra
    int[] rules;//= new int[numBits];

    //  public int ITERATIONS = 1;//time 0 is considered
    @Override
    public Model getNetwork() {
        return network;
    }

    /**
     *
     * @param cn
     * @param k : num estados
     * @param raio: raio de vizinhanca
     * @param _rule :regra
     */
    public TotalisticCAGraph(Model cn, int k, int raio, String _rule) {
        network = cn;
        R = raio;
        K = k;
        rule = _rule;
        neigs = R * 2 + 1;
        numBits = neigs * K - (neigs - 1);//total de bits da regra
        rules = new int[numBits];

        //!!//initGraph();
        parseRule(rule);

        //remove nodes from network with isolated nodes 
        network.removingIsolatedNodesfromNetwork();

        N = network.getN();
    }

    @Override
    public void removingIsolatedNodesfromNetwork() {
        network.removingIsolatedNodesfromNetwork();
    }

    @Override
    public void noiseNetwork(int noiseRate) {
        int nNodes = network.getNodes().size();
        int nEdges = network.getNumberEdges();

        int nNoiseEdges = (noiseRate * nEdges) / 100;
        Random random = new Random(104711L);

        // acrescentando nNoiseEdges/2 arestas
        int count = 0;
        while (count < nNoiseEdges / 2) {

            // sorteio de dois nros entre 1 e nNodes
            int nodeA = random.nextInt(nNodes);
            int nodeB = random.nextInt(nNodes);

            if (nodeA != nodeB) {
                if (!network.getNodes().get(nodeA).isNeighbor(nodeB)) {
                    network.getNode(nodeA).addNeighbor(nodeB);
                    network.getNode(nodeB).addNeighbor(nodeA);
                }
                count++;
            }
        }

        // removendo nNoiseEdges/2 arestas
        count = 0;
        while (count < nNoiseEdges / 2) {
            int nodeA = random.nextInt(nNodes);
            int sizeA = network.getNode(nodeA).getNeighbor().size();
            if (sizeA != 0) {
                int positionB = random.nextInt(sizeA);
                int nodeB = network.getNode(nodeA).getNeighbor().get(positionB);

                network.getNode(nodeA).deleteNeighbor((Integer) nodeB);
                network.getNode(nodeB).deleteNeighbor((Integer) nodeA);

                count++;
            }
        }
    }

    @Override
    public ArrayList<Integer> getIteratorNodesByDegree() {
        return network.getIDsNodesByDegree();
    }
 

    public static void mainX(String[] args) {
        Model cn = OpenNetworkEdgeList.openIgnoringHeaders("redesDB/ring/ring_n=500_k=2_i=1.txt", 2);
        Model network = Model.newInstance(cn);
        String rule = "244";
        int k = 3, r = 1;
        AbstractCAGraph automata = new TotalisticCAGraph(cn, k, r, rule);
        automata.removingIsolatedNodesfromNetwork();
        automata.random(0.5);

        System.out.println("Automata");
        long tiempoInicio = System.currentTimeMillis();

        ArrayList<Integer> iteratorDegreeOrder = automata.getIteratorNodesByDegree();
        for (Integer x : iteratorDegreeOrder) {
            System.out.print(x + "\t");
        }
        System.out.println("");
        for (int j = 0; j < 350; j++) {
            automata.printBoard();
            automata.step(1);
        }
        long totalTiempo = System.currentTimeMillis() - tiempoInicio;
        System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");
        System.out.println("----------------------------");

    }

 
    public void setNewRule(String text) {
        rule = text; // tiene que quedar la regla fixada
        parseRule(rule);
    }

    @Override
    public void step(int days) {
        for (int g = 0; g < days; g++) {
            runStep();
            ITERATIONS++;
        }

    }

    @Override
    public int executeRules(int currentstate, ArrayList<KV> neigsCurrentStateList) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public int executeRules(int sumPattern) {
        int p = numBits - 1 - sumPattern;
        return rules[p];
    }
 
    // Runs one iteration
    private void runStep() {
        for (int x : network.getNodes().keySet()) {
            int currentstate = network.getNode(x).getCurrentState();
            int stateNeigs = sumStatesFromNeigs(x);
            int suma = currentstate + stateNeigs;
            //System.out.println("curr "+currentstate +"sum "+stateNeigs+"\t res="+suma +"\trule = "+(numBits-suma-1));
            network.getNode(x).setNextState(executeRules(suma));
        }
        //update states
        for (int x : network.getNodes().keySet()) {
            network.getNode(x).setCurrState(network.getNode(x).getNextState());//update
        }
    }

    public int sumStatesFromNeigs(int x) {
        int sum = 0;
        List<Integer> neigbors = network.getNode(x).getNeighbor();
        //System.out.print("id "+x+"\t neigs="+neigbors.size()+"\t suma=");
        for (Integer id : neigbors) {
            int state = network.getNode(id).getCurrentState();
            sum += state;
        }
        //System.out.println(sum);
        return sum;
    }
//initialprobablity is not used here, e.g k=3?

    @Override
    public void random(double initialProbability) {
        long seed = 1;//104711
        Random randGen = new Random(seed);

        //Random randGen = new Random();
        int state = 0;
        // Initial State:
        for (int x : network.getNodes().keySet()) {
            state = randGen.nextInt(K);//return values 0, 1, 2 depending on number of states K                        
            network.getNode(x).setCurrState(state);
        }
    }

    @Override
    public void printAdjacency() {
        //TODO()
    }

    @Override
    public void printBoard() {
        ArrayList<Integer> iteratorDegreeOrder = getIteratorNodesByDegree();
        for (Integer x : iteratorDegreeOrder) {
            System.out.print(network.getNode(x).getCurrentState() + "\t");
        }
        System.out.println("");
    }

    @Override
    public double[] population() {
        double[] population = new double[2];
        int numAlives = 0;
        for (int x : network.getNodes().keySet()) {
            if (network.getNode(x).getCurrentState() == ALIVE) {
                numAlives++;
            }
        }
        population[0] = (N - numAlives) * 1.0d / N * 1.0d;
        population[1] = (numAlives) * 1.0d / N * 1.0d;
        return population;
    }

    @Override
    public String getRule() {
        return rule;
    }

    /*
     * Function to read and parse the  Rules 
     * rule: 30, 50, 150, depending on totalistic CA family
     */
    @Override
    protected void parseRule(String str) {
        int intRule = Integer.parseInt(str);
        rules = new int[numBits];
        int i = numBits - 1;
        try {
            while (intRule > 0) {
                rules[i--] = intRule % K;
                intRule = (int) Math.floor(intRule / K);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Rule number is not allow for values of k=" + K + " and r=" + R);
        }
    }

    @Override
    public void loadInit(int[] master) {

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
    public void randomWiringEdge(boolean addOrRemove) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
 