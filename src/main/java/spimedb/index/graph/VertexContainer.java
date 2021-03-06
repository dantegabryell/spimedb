package spimedb.index.graph;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * TODO
 *  change this to one Map<Pair<E,V>,Byte> where the integer's value bits mean:
 *      0 = incoming (off/on)
 *      1 = outgoing (off/on)
 *
 */
public class VertexContainer<V,E> implements Serializable
{
    final Set<Pair<V,E>> incoming;

    final Set<Pair<E,V>> outgoing;

    public VertexContainer(Set<Pair<V,E>> incoming, Set<Pair<E,V>> outgoing) {
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    @Override
    public String toString() {
        return "{" +
                "i=" + incoming +
                ",o=" + outgoing +
                '}';
    }

    /**
     * .
     *
     * @param e
     */
    public boolean addIncomingEdge(V s, E e) {
        return incoming.add(pair(s, e));
    }

    @NotNull private Pair pair(Object x, Object y) {
        return Tuples.pair(x, y);
    }

    /**
     * .
     *
     * @param e
     */
    public boolean addOutgoingEdge(E e, V t) {
        return outgoing.add(Tuples.pair(e, t));
    }

    /**
     * .
     *
     * @param e
     */
    public boolean removeIncomingEdge(V s, E e) {
        return incoming.remove(pair(s, e));
    }

    /**
     * .
     *
     * @param e
     */
    public boolean removeOutgoingEdge(E e, V t)    {
        return outgoing.remove(Tuples.pair(e, t));
    }


    public boolean containsOutgoingEdge(E e, V t) {
        return outgoing.contains(Tuples.pair(e, t));
    }
}
