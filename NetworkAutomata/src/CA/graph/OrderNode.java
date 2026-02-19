package CA.graph;
  
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Jeaneth Machicao
 */
public class OrderNode implements Comparable {

    int m_Index;
    double m_Average;

    public OrderNode(int _mindex, int _maverage) {
        this.m_Index = _mindex;
        this.m_Average = _maverage;
    }

    public OrderNode(int _mindex) {
        this.m_Index = _mindex;
        this.m_Average = 0;
    }

    public OrderNode() {
        m_Index = 0;
        m_Average = 0;
    }

    private void setIndex(int i) {
        this.m_Index = i;
    }

    private void setAverage(double x) {
        this.m_Average = x;
    }

    public int getIndex() {
        return this.m_Index;
    }

    public double getAverage() {
        return this.m_Average;
    }

 
 
 
    private static int positionInOrder(int i, ArrayList<OrderNode> tmpOrder) {
        int _ReTurnValue = -1;
        int _SizeOfOrderArray = tmpOrder.size();
        if (i > _SizeOfOrderArray) {
            _ReTurnValue = -1;
        } else {
            for (int j = 0; j < _SizeOfOrderArray; j++) {
                if (tmpOrder.get(j).getIndex() == i) {
                    _ReTurnValue = j;
                    break;
                }
            }
        }

        return _ReTurnValue;
    }

 
    public static ArrayList< Integer> Baryheuristic(Model network) {
        //public ArrayList< Integer> Baryheuristic(ArrayList< OrderNode> m_NameList, ArrayList<ArrayList<Integer>> m_EdgeInfo) {
        int N = network.getN();
        ArrayList< OrderNode> m_NameList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            m_NameList.add(new OrderNode(i));
        }

        HashMap<Integer, JNode> m_EdgeInfo = network.getNodes();

        ArrayList< Integer> _OriginalName = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            // store original name of these index;
            //_OriginalName.add(NameList.get(i).getIndex());
            _OriginalName.add(m_NameList.get(i).getIndex());
        }
        ArrayList<OrderNode> _TmpOrder = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            OrderNode neWI = new OrderNode(i, 0);
            //neWI.setIndex(i);
            //neWI.setAverage(0);
            _TmpOrder.add(neWI);
        }
        int _CountIterate = 0;
//        while (_CountIterate < 1000 * N) {
        while (_CountIterate < 50 * N) {
            for (int i = 0; i < N; i++) {
                int _p1 = positionInOrder(i, _TmpOrder);
                double _sum = _p1;
                // this is the node to get information about neighbors
                ArrayList< Integer> _edge1 = m_EdgeInfo.get(i).getNeighbor();
                //ArrayList< Integer> _edge1 = m_EdgeInfo.get(i);
                // the lenght of adjacient list of node1                                 
                int _lenght_neighbor_node1 = (int) _edge1.size();
                if (_lenght_neighbor_node1 > 0) {//(the number of adjacient nodes of node1)
                    for (int j = 0; j < _lenght_neighbor_node1; j++) {
                        // now try to access the neighbor node
                        int _next_node_name = _edge1.get(j);
                        int _p2 = positionInOrder(_next_node_name, _TmpOrder);
                        _sum += _p2;
                    }
                }
                _TmpOrder.get(_p1).setAverage(_sum / ((_lenght_neighbor_node1 + 1.0)));
            }

            //Collections.sort(_TmpOrder, (OrderNode s1, OrderNode s2) -> s1.getAverage() > s2.getAverage() ? 1 : 0); //tuples
            Collections.sort(_TmpOrder);

            _CountIterate += 1;
        } // done for _TmpOrder
        // Modify the based on tmpOrder;
        ArrayList<Integer> listIDs = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            //m_NameList[i].setIndex( _OriginalName[_TmpOrder[i].getIndex()] );            
            //m_NameList.get(i).setIndex(_OriginalName.get(_TmpOrder.get(i).getIndex()));
            listIDs.add(_OriginalName.get(_TmpOrder.get(i).getIndex()));
        }
//        for (int i = 0; i < N; i++) {
//            System.out.println("* " + m_NameList.get(i).getIndex());
//
//        }

        //return m_NameList;
        return listIDs;
    }

    @Override
    public int compareTo(Object o) {
        double avg = ((OrderNode) o).getAverage();
        /* For Ascending order*/
        //return this.m_Average > avg ? 1 : 0;
        if (this.m_Average < avg) {
            return -1;
        }
        if (this.m_Average > avg) {
            return 1;
        }
        return 0;

    }

    @Override
    public String toString() {
        return String.valueOf(this.m_Index);
    }
}
