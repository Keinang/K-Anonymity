package App.Model;

import java.io.Serializable;

/**
 * Created by Keinan.Gilad on 9/18/2016.
 */
public class Vertex implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -863722503598895395L;
	private String name;

    public Vertex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Vertex && ((Vertex) obj).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
