package CA.grid;
import java.util.Random;

/**
 *
 * @author Jeaneth Machicao
 */
public final class WolframCA {

    public int[] cells;     // An array of 0s and 1s 
    int generation;  // How many generations?    
    int N;
    int[] rules;     // An array to store the ruleset, for example {0,1,1,0,1,1,0,1}

    public WolframCA(int[] r, int n) {
        N = n;
        rules = r;
        cells = new int[N];
        restart();
    }

    public WolframCA(String r, int n) {
        N = n;
        rules = parseRule(r);
        cells = new int[N];
        restart();
    }

    // Set the rules of the WolframCA
    public void setRules(int[] r) {
        rules = r;
    }

    public void loadInit(int[] src) {
        for (int x = 0; x < N; x++) {
            cells[x] = src[x];
        }
    }

    // Make a random ruleset
    public void random(double initialProbability) {
        Random randGen = new Random(1);
        double random = 0;
        for (int i = 0; i < N; i++) {
            cells[i] = randGen.nextDouble() < initialProbability ? 1 : 0;
        }
    }

    // Reset to generation 0
    public void restart() {
        for (int i = 0; i < N; i++) {
            cells[i] = 0;
        }
        cells[N / 2] = 1;    // We arbitrarily start with just the middle cell having a state of "1"
        generation = 0;
    }

    public void step(int num) {
        for (int i = 0; i < num; i++) {
            iterate();
        }
    }

    // The process of creating the new generation
    void iterate() {
        // First we create an empty array for the new values
        int[] nextgen = new int[N];
        // For every spot, determine new state by examing current state, and neighbor states
        // Ignore edges that only have one neighor
        for (int i = 0; i < N; i++) {
            int L = (i - 1) == -1 ? N - 1 : i - 1;
            int C = i;
            int R = (i + 1) == N ? 0 : i + 1;      // Right neighbor state
            nextgen[i] = executeRules(L, C, R); // Compute next generation state based on ruleset
        }
        // Copy the array into current value
        System.arraycopy(nextgen, 0, cells, 0, N); //update CA  
        //cells = (int[]) nextgen.clone();
        generation++;
    }

    // Implementing the Wolfram rules
    // Could be improved and made more concise, but here we can explicitly see what is going on for each case
    public int executeRules(int a, int b, int c) {
        if (a == 1 && b == 1 && c == 1) {
            return rules[0];
        } else if (a == 1 && b == 1 && c == 0) {
            return rules[1];
        } else if (a == 1 && b == 0 && c == 1) {
            return rules[2];
        } else if (a == 1 && b == 0 && c == 0) {
            return rules[3];
        } else if (a == 0 && b == 1 && c == 1) {
            return rules[4];
        } else if (a == 0 && b == 1 && c == 0) {
            return rules[5];
        } else if (a == 0 && b == 0 && c == 1) {
            return rules[6];
        } else if (a == 0 && b == 0 && c == 0) {
            return rules[7];
        }
        return 0;
    }

    public void print() {
        for (int i = 0; i < N; i++) {
            if (cells[i] == 1) {
                System.out.print("♦");
            } else {
                System.out.print(" ");
            }

        }
        System.out.println();
    }

    private int parseRule(int[] bits) {
        long mult = 1;
        int sum = 0;
        for (int i = 7; i >= 0; i--) {
            sum += bits[i] * (mult);
            mult *= 2;
        }
        return sum;

    }

    private int[] parseRule(String str) {
        int rule = Integer.parseInt(str);
        int celulas[] = new int[8];
        String mybyte = Long.toBinaryString(rule);
        int size = mybyte.length();
        int j = 0;
        for (int i = 7; i >= 0; i--) {
            if (j < size) {
                celulas[i] = String.valueOf(mybyte.charAt(size - 1 - j)).equals("1") ? 1 : 0;
            } else {
                break;
            }
            j++;
        }
        return celulas;
    }

//    public static void main(String[] args) {
//        WolframCA ca;   // An instance object to describe the Wolfram basic Cellular Automata
//
//        //int[] ruleset = {0, 1, 0, 1, 1, 0, 1, 0};    // An initial rule system
//        int N = 100;
//        ca = new WolframCA("30", N); // Initialize WolframCA        
//        ca.random(0.2);
//        for (int i = 0; i < 40; i++) {
//            ca.iterate();
//            ca.print();
//        }
//
//    }
}
