package methods.complexNetwork;

//http://www.sanfoundry.com/java-program-mplement-dijkstras-algorithm-using-priority_queue/
//https://canvas.bham.ac.uk/courses/10065/files/1491635    
// Dijkstra's algorithm to find shortest path from s to all other nodes

import CA.graph.Model;
import CA.graph.JNode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jeaneth Machicao
 */


public class Dikjstra {


    public static int[] runDikjstra(Model G, int s) {
        int[] dist = new int[G.getN()];  // shortest known distance from "s"
        int[] pred = new int[G.getN()];  // preceeding node in path
        boolean[] visited = new boolean[G.getN()]; // all false initially

        for (int i = 0; i < dist.length; i++) {
            dist[i] = -1;
        }
        dist[s] = 0;//not self-loop

        for (int i = 0; i < dist.length; i++) {
            int next = minVertex(dist, visited);
            //System.out.println("i " + i + "\nnext " + next);            
            visited[next] = true;

            // The shortest path to next is dist[next] and via pred[next].             
            int[] n = G.neighbors(next);
            //System.out.println("neig "+n[0]);
            for (int j = 0; j < n.length; j++) {
                int v = n[j];
                //System.out.print("dist "+dist[next]+"\t");
                int d = dist[next] + 1;//dist[next] +WEIGTH
                if (dist[v] < d) {
                    dist[v] = d;
                    pred[v] = next;
                }
            }
        }
        return pred;  // (ignore pred[s]==0!)
    }

    private static int minVertex(int[] dist, boolean[] v) {
        int x = Integer.MAX_VALUE;
        int y = -1;   // graph not connected, or no unvisited vertices 
        for (int i = 0; i < dist.length; i++) {
            if (!v[i] && dist[i] < x) {
                //System.out.println("if "+i);
                y = i;
                x = dist[i];
            }
        }
        //System.out.println("y "+y);
        return y;
    }

    public static double avgShortestPath(Model G) {
 
        int n = G.getN();
        double avg = 0;
        for (int s = 0; s < n; s++) {
            int[] distances = runDikjstra(G, s);
            for (int i = 0; i < distances.length; i++) {
                int j = distances[i];
                //System.out.print(j + "\t");
            }
            //System.out.println("");
            for (int i = 0; i < distances.length; i++) {
                if (distances[i] != 0 && i != s) {//everything but the diagonal only INFERIOR MATRIX TRIANGLE 
                    avg += distances[i];
                }
            }
        }
        //System.out.println("avg " + avg);
        double l = (avg / ((n * n * 1.0) - n) / 2.0);
        return l;
    }

    //---------------------------------------------------------------
    public static int[] findShortestPaths(Model cn, int node) {
        HashMap<Integer, JNode> adjList = cn.getNodes();
        int[] distanceMatrix = new int[cn.getN() + 1];
        for (int i = 0; i < distanceMatrix.length; i++) {
            distanceMatrix[i]=-1;
        }

        // calcular menores distancias para cada i
        int dist = 1;
        ArrayList<Integer> neighborsToVisit = new ArrayList<>();
        ArrayList<Integer> visitedNeighbors = new ArrayList<>();

        ArrayList<Integer> neighbors = adjList.get(node).getNeighbor();

        for (Integer neighbor : neighbors) {
            neighborsToVisit.add(neighbor);// adicionar os vizinhos do noh de referencia ao vetor visitedNeighbors
        }

//            System.out.println("size: " + neighborsToVisit.size());
        do {
            for (Integer neighToVisit : neighborsToVisit) {
                int neighbor = neighToVisit;
                if (distanceMatrix[neighbor] == -1) {
                    distanceMatrix[neighbor] = dist;
                }
            }

            visitedNeighbors.clear();
            visitedNeighbors.addAll(neighborsToVisit);
            neighborsToVisit.clear();

            for (Integer visitedNeighbor : visitedNeighbors) {
                ArrayList<Integer> nneighbors = adjList.get(visitedNeighbor).getNeighbor();
                for (Integer nneighbor : nneighbors) {
                    int n = nneighbor;
                    if (distanceMatrix[n] == -1) {
                        neighborsToVisit.add(nneighbor);
                    }
                }
            }
            dist++;

        } while (neighborsToVisit.size() > 0);
        System.gc();
        return distanceMatrix;

    }

    public static double averagePathLength(Model cn) {
        HashMap<Integer, JNode> adjList = cn.getNodes();

        int nNodes = adjList.size();     
        double sum = 0;

        for (int i = 0; i <= nNodes - 1; i++) {
            int distanceMatrix[] = findShortestPaths(cn, i); // menores caminhos entre cada par de vertices
            for (int j = i + 1; j <= nNodes; j++) {
                int distance = distanceMatrix[j];
                //
                if (distance != -1 && i != j) {
                    sum = sum + distance;
                }
            }
            //System.out.println("");
        }
        
//        System.out.println("count "+count);
        return sum / ((nNodes * nNodes - nNodes) / 2.0);
    }
 
}
