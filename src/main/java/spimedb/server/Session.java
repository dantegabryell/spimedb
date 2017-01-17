package spimedb.server;

import com.google.common.util.concurrent.RateLimiter;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectFloatHashMap;
import org.infinispan.commons.util.concurrent.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spimedb.util.bloom.UnBloomFilter;

import java.net.SocketAddress;
import java.util.Set;

/**
 * interactive session (ie, Client as seen from Server)
 * TODO: https://github.com/undertow-io/undertow/blob/master/examples/src/main/java/io/undertow/examples/sessionhandling/SessionServer.java
 */
public class Session {

    /** bandwidth throttle, in bytes per second */
    final RateLimiter outRate = RateLimiter.create(1000);

    final Set<Task> active = new ConcurrentHashSet<>();

    final ObjectFloatHashMap<String> attention = new ObjectFloatHashMap<>();

    final int BLOOM_SIZE = 64 * 1024;
    final UnBloomFilter<String> sent = new UnBloomFilter<>(BLOOM_SIZE, String::getBytes);

    private final SocketAddress peer;

    public Session(SocketAddress peerAddress) {

        this.peer = peerAddress;

    }

    @Override
    public String toString() {
        //return "session:{\"peer\":\"" + peer + "\"}";
        return peer.toString();
    }

    static final AttachmentKey<Session> SESSION = AttachmentKey.create(Session.class);

    public static Session session(HttpServerExchange ex) {
        Session s = ex.getConnection().getAttachment(SESSION);
        if (s == null) {
            ex.getConnection().putAttachment(SESSION, s = newSession(ex.getConnection().getPeerAddress()));
        }
        return s;
    }

    @NotNull
    private static Session newSession(SocketAddress peerAddress) {
        Session s = new Session(peerAddress);
        return s;
    }

    public static Session session(WebSocketHttpExchange ex, @Nullable WebSocketChannel c) {

        Session s = ex!=null ? ex.getAttachment(SESSION) : null;
        if (s == null) {

            if (c == null)
                throw new NullPointerException();

            s = newSession(c.getPeerAddress());

            c.setAttribute("session", s);
            if (ex!=null)
                ex.putAttachment(SESSION, s);
        }

        return s;
    }

    public static Session session(WebSocketChannel socket) {
        return session(null, socket);
    }
}
