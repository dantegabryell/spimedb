package spimedb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.bytebuddy.ByteBuddy;
import org.infinispan.commons.util.concurrent.ConcurrentHashSet;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spimedb.index.graph.MapGraph;
import spimedb.index.graph.travel.BreadthFirstTravel;
import spimedb.index.graph.travel.CrossComponentTravel;
import spimedb.index.graph.travel.UnionTravel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * https://neo4j.com/developer/java/#_using_neo4j_s_embedded_java_api
 * Created by me on 1/8/17.
 */
public class Tags {

    final static Logger logger = LoggerFactory.getLogger(Tags.class);

//    @JsonIgnore
//    public final MutableGraph<String> inh = GraphBuilder.directed().allowsSelfLoops(false).expectedNodeCount(512).nodeOrder(ElementOrder.unordered()).build();

    final MapGraph<String,String> graph = new MapGraph<String,String>(new ConcurrentHashMap<>(),
            ConcurrentHashSet::new);



//    @JsonIgnore
//    public final Map<String,Tag> tag = new ConcurrentHashMap<>();

    @JsonIgnore
    public final Map<String, Class> tagClasses = new ConcurrentHashMap<String, Class>();
    @JsonIgnore
    public final ClassLoader cl = ClassLoader.getSystemClassLoader();
    @JsonIgnore
    public final ByteBuddy tagProxyBuilder = new ByteBuddy();

    public Tags() {
    }

//    public Tag get(String id, boolean createIfUnknown) {
//        if (createIfUnknown) {
//            return tag.computeIfAbsent(id, Tag::new);
//        } else {
//            return tag.get(id);
//        }
//    }


    @Override
    public String toString() {
        //return Joiner.on('\n').join(inh.traversal().E().toStream().iterator());
        return graph.toString();
    }

    public void tag(String x, String[] parents) {

        synchronized (graph) {
            graph.removeVertex(x);
            graph.addVertex(x);
            for (String y : parents) {
                graph.addEdge(y, x, "inh");
            }
        }

//        int n = 0;
//        Vertex[] pp = new Vertex[parents.length];
//        for (String s : parents) {
//            if (s.isEmpty()) { //HACK
//                logger.warn("{} includes an empty string tag");
//                continue;
//            }
//            pp[n++] = addVertex(s);
//        }
//
//        Vertex x = addVertex(X);
//        String xs = x.id().toString() + " ";
//        for (Vertex y : pp) {
//            if (y!=null) { //HACK null-check for above condition
//                String xy = xs + y.id();
//                if (!inh.edges(xy).hasNext())
//                    y.addEdge("inh", x, T.id, xy);
//
//            }
//        }

    }


    /**
     * computes the set of subtree (children) tags held by the extension of the input (parent) tags
     *
     * @param parentTags if empty, searches all tags; otherwise searches the specified tags and all their subtags
     */
    public Iterable<String> tagsAndSubtags(@Nullable String... parentTags) {

        return new SubTags(graph, parentTags);

//        if (parentTags == null || parentTags.length == 0) {
//            //return Iterators.transform(inh.vertices(), Element::label);
//            return inh.traversal().V().toStream();
//        } else {
//            //awful but should work
//            return Streams.concat(
//                inh.traversal().V(parentTags).repeat(outE("inh").bothV().dedup()).emit().toStream(),
//                inh.traversal().V(parentTags).toStream()
//            ).distinct();
//        }


//
//
//        Set<String> s = new HashSet<>();
//        for (String x : parentTags) {
//            if (s.add(x)) {
//                inh.
//                s.addAll(inh.successors(x));
//            }
//        }
//        return s;
    }

    public Class[] resolve(String... tags) {
        Class[] c = new Class[tags.length];
        int i = 0;
        for (String s : tags) {
            Class x = tagClasses.get(s);
            if (x == null)
                throw new NullPointerException("missing class: " + s);
            c[i++] = x;
        }
        return c;
    }

    public Class the(String tagID, String... supertags) {

        synchronized (tagClasses) {
            if (tagClasses.containsKey(tagID))
                throw new RuntimeException(tagID + " class already defined");

            Class[] s = resolve(supertags);
            Class proxy = tagProxyBuilder.makeInterface(s).name("_" + tagID).make().load(cl).getLoaded();
            tagClasses.put(tagID, proxy);
            return proxy;
        }

        //.subclass(NObject.class).implement(s)
                /*.method(any())
                .intercept(MethodDelegation.to(MyInterceptor.class)
                        .andThen(SuperMethodCall.INSTANCE)
                        .defineField("myCustomField", Object.class, Visibility.PUBLIC)*/
                /*.make()
                .load(cl)
                .getLoaded();*/
    }

    public Set<String> tags() {
        return graph.vertexSet();
    }

    private static class SubTags<V,E> extends UnionTravel<V,E,Object> {
        public SubTags(MapGraph<V,E> graph, V... parentTags) {
            super(graph, parentTags);
        }

        @Override protected CrossComponentTravel<V, E, Object> get(V start, MapGraph<V, E> graph, Map<V, Object> seen) {
            return new BreadthFirstTravel<>(graph, start, seen);
        }
    }
}
