package Utils;

import java.nio.charset.Charset;
import CA.graph.Model;

/**
 *
 * @author Jeaneth Machicao
 */
public abstract class JGraphAbstractWriter {
    protected Charset modeUTF8 = Charset.forName("UTF-8");
    public abstract void write(String file, Model g);
}
