package simplicialcomplex;

import com.sun.net.httpserver.Filter;

/**
 * Created by Andrii on 6/1/2015.
 */


abstract public class Simplex implements Comparable<Simplex> {

    protected int _findex = -1;
    protected Chain _chain;

    public int setfindex (int i) {
        assert(_findex < 0);
        if (_findex < 0)
            _findex = i;
        return _findex;
    }

    public int decrement_findex () {
        if (_findex < 0)
            _findex--;
        return _findex;
    }

    public Chain setChain (Chain c) {
        if (_chain != null)
            throw new IllegalStateException("Simplex " + this.toString() +
                    "already has chain entry: " +
                    _chain.toString());
        _chain = c;
        return _chain;
    }

    public void clearChain () {
        _chain = null;
    }

    public int findex () {
        return _findex;
    }

    public Chain chain () {
        return _chain;
    }
    abstract public int dimension();

    abstract public Simplex copy();

    abstract public int[] vertices();

    abstract public int[] vertices(int verts[]);

    public boolean subset(Simplex s) {
        int stop = dimension();
        int s_stop = s.dimension();
        if (stop > s_stop)
            return false;
        return subset(s, new int[stop+1], new int[s_stop+1]);
    }

    public boolean subset(Simplex s, int[] v, int[] sv) {
        int stop = dimension();
        int s_stop = s.dimension();
        if (stop > s_stop)
            return false;
        vertices(v);
        s.vertices(sv);
        int current = 0;
        int s_current = 0;
        while (current <= stop) {
            int to_find = v[current++];
            while(s_current <= s_stop) {
                if (sv[s_current++] == to_find)
                    break;
                else if (s_current > s_stop)
                    return false;
            }
        }
        assert(current == (stop+1));
        return true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Simplex))
            return false;
        Simplex s = (Simplex) obj;
        int this_dimension = this.dimension();
        int s_dimension = s.dimension();
        if (s_dimension == this_dimension)


            return false;
        return true;
    }

    abstract boolean slt(Simplex s);

    abstract boolean seq(Simplex s);



    public static Simplex makeEdge (int v1, int v2, int findex) {
        Simplex s = Packed2Simplex.makeEdge(v1, v2);
        s.setfindex(findex);
        return s;
    }

    public static Simplex makePoint (int v, int findex) {
        Simplex s = Packed2Simplex.makePoint(v);
        s.setfindex(findex);
        return s;
    }

    public Simplex addVertex(int v) {
        int[] v_old = vertices();
        int[] v_new = new int[v_old.length+1];
        for (int i = 0; i < v_old.length; i++)
            v_new[i] = v_old[i];
        v_new[v_old.length] = v;
        return null;
    }

    public Simplex addVertex (int v, int f) {
        Simplex s = this.addVertex(v);
        s.setfindex(f);
        return s;
    }

    public String toString() {
        int[] vertices = this.vertices();
        String base = "<";
        if (_findex >= 0)
            base = String.format("<(%d)", _findex);
        if (vertices.length == 0)
            return base + ">";
        else if (_findex >= 0)
            base = base + " ";
        for (int i = 0; i < vertices.length-1; i++) {
            base = base + String.format("%d, ", vertices[i]);
        }
        base = base + String.format("%d>", vertices[vertices.length-1]);
        return base;
    }

    public static int[] vertex_sort (int[] vertices) {

        if ((vertices.length <= 1))
            return vertices;

        for (int j = vertices.length - 1; j > 0; j--) {
            for (int i = 0; i < j; i++) {
                if (vertices[i+1] < vertices[i]) {
                    int dummy = vertices[i];
                    vertices[i] = vertices[i+1];
                    vertices[i+1] = dummy;
                }
            }
        }
        return vertices;
    }

    public static double[] dist_sort (double[] distances) {

        if ((distances.length <= 1))
            return distances;

        for (int j = distances.length - 1; j > 0; j--) {
            for (int i = 0; i < j; i++) {
                if (distances[i+1] < distances[i]) {
                    double dummy = distances[i];
                    distances[i] = distances[i+1];
                    distances[i+1] = dummy;
                }
            }
        }
        return distances;
    }

    public static int[] simplex_reverse_sort (int[] vertices) {

        if (vertices.length <= 1)
            return vertices;

        for (int j = vertices.length - 1; j > 0; j--) {
            for (int i = 0; i < j; i++) {
                if (vertices[i+1] > vertices[i]) {
                    int dummy = vertices[i];
                    vertices[i] = vertices[i+1];
                    vertices[i+1] = dummy;
                }
            }
        }
        return vertices;
    }

    abstract public Simplex[] boundaryArray();


    static Simplex random() {
        int length = (int) Math.floor((Math.random() * 9.0));
        if (length > 8)
            length = 8;
        if (length < 1)
            length = 1;
        int[] vertices = new int[length];
        for(int i = 0; i < length; i++) {
            int x = 1 + (int) Math.floor((Math.random() * 1000.0));
            vertices[i] = x;
        }
        vertex_sort(vertices);
        for(int i = 0; i < length-1; i++) {
            if (vertices[i] == vertices[i+1]) {
                for(int j = i+1; j < length; j++) {
                    vertices[j]++;

                }
            }
        }

        return null;
    }

}
