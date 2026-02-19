package CA.graph;

/**
 *
 * @author Jeaneth Machicao
 */
public final class KV {

    private int key;
    private int value;

    public KV(int c, int s) {
        setKey(c);
        setValue(s);
    }

    /**
     * @return the key
     */
    public int getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(int key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

}