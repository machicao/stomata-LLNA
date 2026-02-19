package CA.graph;
 

/**
 *
 * @author Jeaneth Machicao
 */
public class CAGraphFactory {

    private String type;
    private Model cn;
    private double kappa;
    private String Rule;
    private int N;

    /**
     *
     * @param _type: "OT", "LIFELIKECAGRAPH"; "SOT", "SLIFELIKE"
     * @param _rule
     * @param _kappa
     * @param _cn
     */
    public CAGraphFactory(String _type, String _rule, double _kappa, Model _cn) { // type= "OT"   "(+,-)"  kappa=0.3
        type = _type;
        cn = _cn;
        kappa = _kappa;
        Rule = _rule;
        N = cn.getN();
    }

    /**
     *
     * @param _type: "OT", "LIFELIKECAGRAPH"; "SOT", "SLIFELIKE"
     * @param _rule
     * @param _kappa
     * @param _cn
     */
    public CAGraphFactory(String _type, String _rule, Model _cn) { //type="LIFELIKE"   (B3/S23)
        cn = _cn;
        Rule = _rule;
        type = _type;
        N = cn.getN();
    }


    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the cn
     */
    public Model getCn() {
        return cn;
    }

    /**
     * @param cn the cn to set
     */
    public void setCn(Model cn) {
        this.cn = cn;
    }

    /**
     * @return the kappa
     */
    public double getKappa() {
        return kappa;
    }

    /**
     * @param kappa the kappa to set
     */
    public void setKappa(double kappa) {
        this.kappa = kappa;
    }

    /**
     * @return the Rule
     */
    public String getRule() {
        return Rule;
    }

    /**
     * @param Rule the Rule to set
     */
    public void setRule(String Rule) {
        this.Rule = Rule;
    }

    /**
     * @return the N
     */
    public int getN() {
        return N;
    }

    /**
     * @param N the N to set
     */
    public void setN(int N) {
        this.N = N;
    }
}
