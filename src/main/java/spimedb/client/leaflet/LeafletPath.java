package spimedb.client.leaflet;

/**
 *
 * @author Alexey Andreev
 */
public interface LeafletPath extends Layer {
    LeafletPath addTo(LeafletMap map);
}
