package CA.graph;

/**
 *
 * @author Jeaneth Machicao
 */
public class MNode extends JNode {  
   
    private String metabolite;
    public MNode(int cd, String _metabolite) {
        super(cd);
        metabolite = _metabolite;
        wordsTimeEvolution = new StringBuffer();
    }

    public MNode(int cd, int state) {
        super(cd);
        currState = state;
        wordsTimeEvolution = new StringBuffer();
    }
    
    public String getName()
    {
        return metabolite;
    }
    
}
