package simplicialcomplex;

/**
 * Created by Andrii on 6/1/2015.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class SimplexStream
        implements Iterable<Simplex>, Iterator<Simplex> {

    public abstract boolean hasNext();
    public abstract Simplex next();
    public void remove() {
        throw new UnsupportedOperationException();
    }
    public abstract int size();
    public abstract int maxDimension();
    public double convert_filtration_index(int fi) {
        return (double) fi;
    }
    public enum ComparisonType { LT, LE, EQ, GE, GT };
    public static class Tail {
        static final int ENTRIES_DEFAULT_LENGTH = 100;
        Simplex[] entries;
        int current;
        Tail next;
        // make an empty SimplexStream.Segment with the default length
        public Tail() {
            entries = new Simplex[ENTRIES_DEFAULT_LENGTH];
            current = 0;
            next = null;
        }

        // make an empty SimplexStream.Segment with the given length
        public Tail(int len) {
            entries = new Simplex[len];
            current = 0;
            next = null;
        }

        public String toString() {
            return String.format("[QT(%d:%d): {%d}%s]",
                    current, entries.length,
                    System.identityHashCode(entries),
                    (next != null)?("->"+next.toString()):"");
        }

        public Tail enqueue(Simplex s) {
            if (current < entries.length) {
                assert(next == null);
                entries[current++] = s;
                return this;
            } else {
                next = new Tail(entries.length);
                next.entries[next.current++] = s;
                return next;
            }
        }
    }


    public static class Head {

        Simplex[] entries;
        int current;
        Tail orig;

        // don't use
        protected Head() {
        }

        // make a head for a Tail
        public Head(Tail q) {
            entries = q.entries;
            current = q.current;
            orig = q;
        }

        // Duplicate a Head. Note that interating with a copy has no effect
        // on the original, and no effect on the underlying Tail.
        public Head copy() {
            Head tmp = new Head();
            tmp.entries = entries;
            tmp.current = current;
            tmp.orig = orig;
            return tmp;
        }

        public String toString() {
            return String.format("[QH(%d:%d): {%d} (%s)]",
                    current, entries.length,
                    System.identityHashCode(entries),
                    orig.toString());
        }


        public Simplex nextEntry() {
            if (current == entries.length) {
                // System.out.printf("nextEntry() for %s\n", toString());
                Tail n = orig.next;
                entries = n.entries;
                current = 0;
                orig = n;
            }
            return entries[current++];
        }

        public boolean lessThan (Head q) {
            return ((entries != q.entries) || (current < q.current));
        }



        public boolean eql(Head q) {
            return ((entries == q.entries) || (current == q.current));
        }
    }

    public static class Stack extends SimplexStream {

        // Rather like the Queue code above, but this is even simpler --
        // the building block of a segmented stack.
        protected static class Segment {
            protected final Simplex[] entries;
            protected int current;
            protected Segment next;

            // don't use
            protected Segment() {
                entries = null;
                current = 0;
                next = null;
            }

            // make an empty SimplexStream.Segment with a specified length
            protected Segment(int len) {
                entries = new Simplex[len];
                current = 0;
                next = null;
            }

            public String toString() {
                return String.format("[Seg(%d:%d): {%d}%s]",
                        current, entries.length,
                        System.identityHashCode(entries),
                        (next != null)?("->"+next.toString()):"");
            }

            protected Segment push(Simplex s) {
                if (current < entries.length) {
                    entries[current++] = s;
                    return this;
                } else {
                    Segment tmp = new Segment(entries.length);
                    tmp.next = this;
                    tmp.entries[tmp.current++] = s;
                    return tmp;
                }
            }
        }

        protected static int STACK_SEGMENT_LENGTH = 10;

        protected final Segment[] segments;
        protected final int findex_bound;
        protected final int dimension_bound;
        protected final int segment_length;
        protected int size;

        protected int segments_index;
        protected int simplex_index;

        // don't use
        protected Stack() {
            segment_length = 0;
            segments = null;
            dimension_bound = 0;
            findex_bound = 0;
            segments_index = 0;
            simplex_index = 0;
            size = 0;
        }

        public Stack(int max_findex, int max_d) {
            segment_length = STACK_SEGMENT_LENGTH;
            dimension_bound = (max_d + 1);
            findex_bound = (max_findex + 1);
            segments = new Segment[dimension_bound * findex_bound];
            segments_index = 0;
            simplex_index = 0;
            size = 0;
        }

        public String toString() {
            return String.format("[STK(%d,%d): %d/%d, si=%d/%d]",
                    dimension_bound, findex_bound,
                    segments_index, segments.length,
                    simplex_index, segment_length);
        }





        public int size() {
            return size;
        }

        public int maxDimension() {
            return (dimension_bound - 1);
        }

        public boolean hasNext() {
            if (segments[segments_index] != null) {
                if (simplex_index < segments[segments_index].current)
                    return true;
                else {
                    simplex_index = 0;
                    Segment next = segments[segments_index].next;
                    segments[segments_index] = next;
                    if (next != null) {
                        assert(0 < next.current);
                        return true;
                    }
                }
            }

            assert(segments[segments_index] == null);
            assert(simplex_index == 0);

            while ((segments_index < segments.length) &&
                    (segments[segments_index] == null))
                segments_index++;

            if (segments_index == segments.length)
                return false;

            assert(segments[segments_index] != null);
            assert(0 < segments[segments_index].current);
            return true;
        }

        public Simplex next() {
            Segment seg = segments[segments_index];
            Simplex s;
            if (seg != null) {
                if (simplex_index < seg.current) {
                    s = seg.entries[simplex_index];
                    seg.entries[simplex_index++] = null;
                    assert(size > 0);
                    size--;
                    return s;
                } else {
                    simplex_index = 0;
                    seg = segments[segments_index] = segments[segments_index].next;
                    if (seg != null) {
                        assert(0 < seg.current);
                        s = seg.entries[simplex_index];
                        seg.entries[simplex_index++] = null;
                        assert(size > 0);
                        size--;
                        return s;
                    }
                }
            }

            assert(segments[segments_index] == null);
            assert(simplex_index == 0);

            while ((segments_index < segments.length) &&
                    ((seg = segments[segments_index]) == null))
                segments_index++;

            if (segments_index == segments.length)
                return null;

            assert(segments[segments_index] != null);
            assert(0 < seg.current);
            s = seg.entries[simplex_index];
            seg.entries[simplex_index++] = null;
            assert(size > 0);
            size--;
            return s;
        }

        protected static class StackIterator implements Iterator<Simplex> {
            protected Segment[] segments;
            protected int segments_index;
            protected Segment current_seg;
            protected int simplex_index;
            protected int fixed_dimension;
            protected final int dimension_bound;

            protected StackIterator() {
                segments = null;
                segments_index = 0;
                current_seg = null;
                simplex_index = 0;
                fixed_dimension = -1;
                dimension_bound = 0;
            }

            public StackIterator(Stack stack) {
                segments = stack.segments;
                segments_index = 0;
                current_seg = null;
                simplex_index = 0;
                fixed_dimension = -1;
                dimension_bound = stack.dimension_bound;
            }

            public StackIterator(Stack stack, int dimension) {
                segments = stack.segments;
                segments_index = -1;
                current_seg = null;
                simplex_index = 0;
                if ((dimension < 0) || (dimension >= stack.dimension_bound))
                    throw new IllegalArgumentException(dimension +
                            " must be >= 0 and <= " +
                            stack.dimension_bound);
                fixed_dimension = dimension;
                dimension_bound = stack.dimension_bound;
            }

            public boolean hasNext() {
                while(true) {
                    while (current_seg == null) {
                        if (segments_index >= segments.length)
                            return false;
                        else {
                            if (fixed_dimension >= 0) {
                                if (segments_index < 0)
                                    segments_index = fixed_dimension;
                                else
                                    segments_index += dimension_bound;
                                if (segments_index >= segments.length)
                                    return false;
                                else
                                    current_seg = segments[segments_index];
                            } else
                                current_seg = segments[segments_index++];
                        }
                    }
                    while (current_seg != null) {
                        while ((simplex_index < current_seg.current) &&
                                (current_seg.entries[simplex_index] == null))
                            simplex_index++;
                        if (simplex_index < current_seg.current)
                            return true;
                        else {
                            simplex_index = 0;
                            current_seg = current_seg.next;
                        }
                    }
                }
            }

            public Simplex next() {
                while(true) {
                    while (current_seg == null) {
                        if (segments_index >= segments.length)
                            throw new NoSuchElementException();
                        else {
                            if (fixed_dimension >= 0) {
                                if (segments_index < 0)
                                    segments_index = fixed_dimension;
                                else
                                    segments_index += dimension_bound;
                                if (segments_index >= segments.length)
                                    throw new NoSuchElementException();
                                else
                                    current_seg = segments[segments_index];
                            } else
                                current_seg = segments[segments_index++];
                        }
                    }
                    while (current_seg != null) {
                        while ((simplex_index < current_seg.current) &&
                                (current_seg.entries[simplex_index] == null))
                            simplex_index++;
                        if (simplex_index < current_seg.current) {
                            return current_seg.entries[simplex_index++];
                        } else {
                            simplex_index = 0;
                            current_seg = current_seg.next;
                        }
                    }
                }
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        public Iterator<Simplex> iterator() {
            return new StackIterator(this);
        }
        public Iterator<Simplex> iterator(int d) {
            return new StackIterator(this, d);
        }
    }
}

