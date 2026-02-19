package CA.graph;

import static CA.graph.OTCAGraph.ALIVE;
import static CA.graph.OTCAGraph.DEATH; 
import Utils.OpenNetworkEdgeList;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jeaneth Machicao
 */
public class LifeLikeCAGraph extends AbstractCAGraph {

    public long seed;
    private final Model network; 
    public final int N;
    private String LifeGameType; //Conway`s Game of life"B2/S23"
    protected int[] Born = {0, 0, 0, 0, 0, 0, 0, 0, 0};    //Born        NULO 1 2 3 4 5 6 7 UNIVERSO
    protected int[] Survive = {0, 0, 0, 0, 0, 0, 0, 0, 0}; //Survive     NULO 1 2 3 4 5 6 7 UNIVERSO
    int neigsNewmann = Born.length; // 8 +1 (including itself)

    //  public int ITERATIONS = 1;//time 0 is considered
    @Override
    public Model getNetwork() {
        return network;
    }

        public LifeLikeCAGraph(Model cn, String rule ) {
        network = cn;

        LifeGameType = rule;
        //!!//initGraph();
        parseRule(LifeGameType);

        //remove nodes from network with isolated nodes 
        removingIsolatedNodesfromNetwork();

        N = network.getN(); 

    }
 

    @Override
    public void removingIsolatedNodesfromNetwork() {
        network.removingIsolatedNodesfromNetwork();
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
 
    //Metodo feito so para  testar  as dist. binarias e analizar se existe
    //dependencia no ordenamento por grau quando varios nos tem o mesmo grau.
    public ArrayList<Integer> getIteratorNodesByDegreeRandom() {
        return network.getIDsNodesByDegreeRandomRange();
    }

    //metodo para selecionar aleatoriamente um representante de cada grau
    public ArrayList<Integer> getIteratorOneNodesPerDegree() {
        return network.getIDsIteratorOneNodesPerDegree();
    }

    //metodo feito para evitar o desampate do ordenamento por grau
    //usando como 2nd ordenamento o grau hierachico
    public ArrayList<Integer> getIteratorPerDegreeThenHierachical() {
        return network.getIteratorPerDegreeThenHierachical();
    }

    public static void mainX(String[] args) {
        int avgK = 6;
        int m = avgK / 2;
        int m0 = 10;
        //Model network = new ScaleFreeNetwork(200, m0, m, true);
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("/Users/jeaneth/Documents/redesSMALLS/regular/regular_n=40_k=8_i=1.txt");
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("/Users/jeaneth/Documents/redesSMALLS/barabasi/barabasi_n=200_k=8_i=1.txt");
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("/Users/jeaneth/Documents/redesSMALLS/barabasi/barabasi_n=10_k=2_i=1.txt");
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("teste_n=7_k=2_i=1.txt");
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("/Users/jeaneth/Documents/redesSMALLS/erdos/" + "erdos_n=200_k=8_i=1.txt");
        //Model network = new OpenNetworkFrzomR().openIgnoringHeaders("/Users/jeaneth/Documents/redesSMALLS/erdos/" + "erdos_n=200_k=4_i=1.txt");

        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("erdos_n=200_k=8_i=1.txt");
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("redesSMALLS/barabasi/barabasi_n=50_k=4_i=1.txt");
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("redesBIGS/barabasi/barabasi_n=1000_k=4_i=1.txt");
        //Model network = new OpenNetworkEdgeList().openIgnoringHeaders("redesBIGS/erdos/erdos_n=1000_k=4_i=1.txt");
        //Model cn = OpenNetworkEdgeList.openIgnoringHeaders("RedesDB/RedesDB#1/watts/watts_n=1000_k=4_p=0.1_i=1.txt", 2);
        //Model cn = new OpenNetworkEdgeList().open("redesDB/erdos/erdos_n=1000_k=4_i=1.txt");
        //Model cn = OpenNetworkEdgeFormat.open("/Users/jeaneth/Dropbox/lifelikeSciReport/dataset/test/twitter/12831.edges", 0);
//        Model cn = OpenNetworkEdgeList.openIgnoringHeaders("redesDB/exMatSuppAutoria.network", 0);
        Model cn = OpenNetworkEdgeList.openIgnoringHeaders("testSCG.txt", 0);
        Model network = Model.newInstance(cn);
        String LifeGameType = "B01-S23";//"B25/S8";
        AbstractCAGraph automata = new LifeLikeCAGraph(network, LifeGameType);
        //automata.noiseNetwork(10);
        automata.removingIsolatedNodesfromNetwork();
        automata.random(0.12);

        //int vecinos[] = automata.Neighbor(8);
        //for (int i = 0; i < vecinos.length; i++) {
        //    System.out.println(vecinos[i]);
        // }
        System.out.println("Automata");
        long tiempoInicio = System.currentTimeMillis();

        ArrayList<Integer> iteratorDegreeOrder = automata.getIteratorNodesByDegree();
        for (Integer x : iteratorDegreeOrder) {
            System.out.print(x + "\t");
        }        
        System.out.println("");
        for (int j = 0; j < 10; j++) {
            automata.printBoard();
            automata.step(1);

        }

//        System.out.println("Shannon Entropy: " + NetCAFeatureExtractor.entropyShannon(automata));
//        System.out.println("Word Entropy: " + NetCAFeatureExtractor.entropyWord(automata));
//        System.out.println("Fourier Partial Energy ");
//        int numSections = 4;
//        NetCAFeatureExtractor.averageFourierPartialEnergy(automata, numSections);
//        for (int j = 0; j < numSections; j++) {
//            for (Integer x : iteratorDegreeOrder) {
//                double[] fouriers = network.getNode(x).getCellFourierPartialEnergy();
//                System.out.print(fouriers[j] + "\t");
//            }
//            System.out.println("");
//        }
        long totalTiempo = System.currentTimeMillis() - tiempoInicio;
        System.out.println("El tiempo de demora es :" + totalTiempo + " miliseg");
        System.out.println("----------------------------");

    }

//#    @Override
//#    public PrefGraph getGraph() {
//#        return prefusegraph;
//#    }
    public void setNewRule(String text) {
        LifeGameType = text; // tiene que quedar la regla fixada
        Born = new int[neigsNewmann]; // a ZERO
        Survive = new int[neigsNewmann];// a ZERO
        parseRule(LifeGameType);
    }

    @Override
    public void step(int days) {
        for (int g = 0; g < days; g++) {
            runStep();
            ITERATIONS++;
        }

    }

    /**
     * range [0.888 - 1.000] --> UNIVERSO [0.777 - 0.888] --> 7 [0.666 - 0.777]
     * --> 6 [0.555 - 0.666] --> 5 [0.444 - 0.555] --> 4 [0.333 - 0.444] --> 3
     * [0.222 - 0.333] --> 2 [0.111 - 0.222] --> 1 [0.000 - 0.110] --> NULO
     * private int range(double val) { int resp = -1; if (val == 1.0) { resp =
     * 8; } else if (val >= (6.0 / 7.0) && val < (7.0 / 7.0)) { resp = 7; } else
     * if (val >= (5.0 / 7.0) && val < (6.0 / 7.0)) { resp = 6; } else if (val
     * >= (4.0 / 7.0) && val < (5.0 / 7.0)) { resp = 5; } else if (val >= (3.0 /
     * 7.0) && val < (4.0 / 7.0)) { resp = 4; } else if (val >= (2.0 / 7.0) &&
     * val < (3.0 / 7.0)) { resp = 3; } else if (val >= (1.0 / 7.0) && val <
     * (2.0 / 7.0)) { resp = 2; } else if (val > 0.0 && val < (1.0 / 7.0)) {
     * resp = 1; } else if (val == 0.0) { resp = 0; } //int resp = (int)
     * Math.round(val * 9.0); return resp; }
     */
    private int range(double val) {
        int resp = -1;
        if (val >= 8.0 / 9.0 && val <= 1.000) {
            resp = 8;
        } else if (val >= (7.0 / 9.0) && val < (8.0 / 9.0)) {
            resp = 7;
        } else if (val >= (6.0 / 9.0) && val < (7.0 / 9.0)) {
            resp = 6;
        } else if (val >= (5.0 / 9.0) && val < (6.0 / 9.0)) {
            resp = 5;
        } else if (val >= (4.0 / 9.0) && val < (5.0 / 9.0)) {
            resp = 4;
        } else if (val >= (3.0 / 9.0) && val < (4.0 / 9.0)) {
            resp = 3;
        } else if (val >= (2.0 / 9.0) && val < (3.0 / 9.0)) {
            resp = 2;
        } else if (val >= (1.0 / 9.0) && val < (2.0 / 9.0)) {
            resp = 1;
        } else if (val >= (0.0 / 9.0) && val < (1.0 / 9.0)) {
            resp = 0;
        }
        return resp;
    }

//    private Iterator<Integer> getIteratorNodesIDs() {
//        Iterator<Integer> it = network.getNodes().keySet().iterator();
//        return it;
//
//    }
    // Runs one iteration
    private void runStep() {
        for (int x : network.getNodes().keySet()) {
            int currentstate = network.getNode(x).getCurrentState();
            double numAlives = 1.0d * countNumAliveNeighbors(x);
            double degree = 1.0d * network.getNode(x).getNeighbor().size();//get numNeighbors

            double density = ((1.0d * numAlives) / (1.0d * degree));//[0, 1] !! rede nao deve conter nodos isolados

            int val = range(density);//[0, 8]

            if (currentstate == ALIVE) { //==1               
                if (Survive[val] == 1) {//S3   ativo   B1357/S2468                  
                    network.getNode(x).setNextState(ALIVE);
                } else {
                    network.getNode(x).setNextState(DEATH);
                }
            } else if (currentstate == DEATH) {
                if (Born[val] == 1) {
                    network.getNode(x).setNextState(ALIVE);
                } else {
                    network.getNode(x).setNextState(DEATH);
                }
            }

            //System.out.println("[" + network.getNode(x).getCode() + "] (" + currentstate + ") numAlives " + numAlives + " degree " + degree + " density " + density + " val" + val + " next  " + network.getNode(x).getCurrentState());
        }
        //update states
        for (int x : network.getNodes().keySet()) {
            network.getNode(x).setCurrState(network.getNode(x).getNextState());//update
//#            PrefNode.setState(prefusegraph.getNode(x), network.getNode(x).getCurrentState());
        }
    }

    public int countNumAliveNeighbors(int x) {
        return network.countNumAliveNeighbors(x);
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

        int val = range(density);//[0, 8]

        if (currentstate == ALIVE) { //==1               
            if (Survive[val] == 1) {//S3   ativo   B1357/S2468                  
                return ALIVE;
            } else {
                return DEATH;
            }
        } else if (currentstate == DEATH) {
            if (Born[val] == 1) {
                return ALIVE;
            } else {
                return DEATH;
            }
        }
        return DEATH;
    }

////    @Override
////    public void random(double initialProbability) {
////        long seed = 104711L;//104711
////        Random randGen = new Random(seed);
////        boolean random;
////        // Initial State:
////        for (int x : network.getNodes().keySet()) {
////            random = randGen.nextBoolean();
////            int state = (random) ? 1 : 0;
////            network.getNode(x).setCurrState(state);
////        }
////    }
//    @Override random(double initialProbability) {
//        long seed = 104711L;//104711
//        Random randGen = new Random(seed);
//        double random = 0;
//        // Initial State:
//        for (int x : network.getNodes().keySet()) {
//            random = randGen.nextDouble();
//
//            if (random <= initialProbability) {
//                network.getNode(x).setCurrState(DEATH);
////#                PrefNode.born(prefusegraph.getNode(i));
//            } else {
//                network.getNode(x).setCurrState(ALIVE);
////S#                PrefNode.die(prefusegraph.getNode(i));
//            }
////#           PrefNode.setState(prefusegraph.getNode(x), state);
//        }
//    }
    @Override
    public void random(double initialProbability) {
        long Seed = 104711L;//104711L (USADO EN TODOS OS TESTES) (824737002L USADO EN REDES METABOLICAS)
        //long Seed= 824737002L ;        
        //long Seed = -6736138642144824497L;// vou usar esse seed para os testes de SIZE

        Random randGen = new Random(Seed);
        //Random randGen = new Random(seed);//estomatos cthenante
        //Random randGen = new Random();

        double random = 0;
        // Initial State:
        for (int x : network.getNodes().keySet()) {
            random = randGen.nextDouble();
            //puting 1 for all nodes leaves and maximal degree
            //deterministc approach for initial conditions
//            if (network.getNode(x).getK() == 1) {
//                network.getNode(x).setCurrState(ALIVE);
//            } else {
//                network.getNode(x).setCurrState(DEATH);
//            }
            //stochastic initial conditions

            if (random <= initialProbability) {
                network.getNode(x).setCurrState(DEATH);
            } else {
                network.getNode(x).setCurrState(ALIVE);
            }

        }
    }

    @Override
    public void printAdjacency() {
////        int matrix[][] = new int[N][N];
////        //for (int i = 0; i < N; i++) {
////        Iterator<Integer> it = getIteratorNodes();
////        int i=0;
////        while(it.hasNext()){
////            int code = it.next();            
////            for (Integer j : network.getNode(code).getNeighbor()) {
////                matrix[i++][j] = 1;
////            }
////        }
////
////        for (int i = 0; i < N; i++) {
////            for (int j = 0; j < N; j++) {
////                System.out.print(matrix[i][j] + " ");
////            }
////            System.out.println();
////        }

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
        return LifeGameType;
    }

    /*
     * Function to read and parse the  Rules
     * rule: B23/S36
     */
    @Override
    protected void parseRule(String rule) {
        int Smode = 1;
        int Bmode = 2;
        int noMode = 0;
        int currentMode = noMode;
        for (int i = 0; i < rule.length(); i++) {
            char curChar = rule.charAt(i);
            if (curChar == 'S' || curChar == 's') {
                currentMode = Smode;
            } else if (curChar == 'B' || curChar == 'b') {
                currentMode = Bmode;
            } else if (currentMode == Smode) {
                switch (curChar) {
                    case '0':
                        Survive[0] = 1;
                        break;
                    case '1':
                        Survive[1] = 1;
                        break;
                    case '2':
                        Survive[2] = 1;
                        break;
                    case '3':
                        Survive[3] = 1;
                        break;
                    case '4':
                        Survive[4] = 1;
                        break;
                    case '5':
                        Survive[5] = 1;
                        break;
                    case '6':
                        Survive[6] = 1;
                        break;
                    case '7':
                        Survive[7] = 1;
                        break;
                    case '8':
                        Survive[8] = 1;
                        break;
                    default:
                        break;
                }
            } else if (currentMode == Bmode) {
                switch (curChar) {
                    case '0':
                        Born[0] = 1;
                        break;
                    case '1':
                        Born[1] = 1;
                        break;
                    case '2':
                        Born[2] = 1;
                        break;
                    case '3':
                        Born[3] = 1;
                        break;
                    case '4':
                        Born[4] = 1;
                        break;
                    case '5':
                        Born[5] = 1;
                        break;
                    case '6':
                        Born[6] = 1;
                        break;
                    case '7':
                        Born[7] = 1;
                        break;
                    case '8':
                        Born[8] = 1;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void loadInit(int[] master) {
        //#    private void initGraph() {
//#        prefusegraph = new PrefGraph();
//#        for (Iterator<Integer> it = getIteratorNodesIDs(); it.hasNext();) {
//#            int x = it.next();
//#            prefusegraph.addPrefNode(x, DEATH);
//#        }
//#        //EDGES
//#        for (Iterator<Integer> it = getIteratorNodesIDs(); it.hasNext();) {
//#            int x = it.next();
//#            List<Integer> neigs = network.getNode(x).getNeighbor();
//#            for (Integer neig : neigs) {
//#                prefusegraph.addPrefEdge(prefusegraph.getNode(x), prefusegraph.getNode(neig)); // source:nodes.get(i)    target: nodes.get(hood[j])
//#            }
//#
//#        }
//#    }      
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
    public int[] getCurrentStates() {
        int states[] = new int[N];
        int a = 0;
        for (int x : network.getNodes().keySet()) {
            states[a++] = network.getNode(x).getCurrentState();
        }
        return states;
    }

}
