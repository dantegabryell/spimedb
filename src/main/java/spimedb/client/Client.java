package spimedb.client;

import org.teavm.jso.JSObject;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.css.ElementCSSInlineStyle;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import spimedb.client.leaflet.*;
import spimedb.index.BudgetMerge;
import spimedb.index.PriBag;

import java.util.Arrays;
import java.util.HashMap;

import static org.teavm.jso.json.JSON.stringify;
import static spimedb.client.JS.get;
import static spimedb.client.JS.getFloat;

/**
 * SpimeDB Client UI - converted to JS with TeaVM, running in browser
 */
public class Client {

    static void setVisible(ElementCSSInlineStyle element, boolean visible) {
        CSSStyleDeclaration es = element.getStyle();
        if (!visible)
            es.setProperty("display", "none");
        else
            es.removeProperty("display");
    }


    final PriBag<String> tag = new PriBag<>(16, BudgetMerge.add, new HashMap<>());
    final PriBag<String> obj = new PriBag<>(128, BudgetMerge.add, new HashMap<>());


    public final ClientWebSocket attn = ClientWebSocket.newSocket("attn");
    public final ClientWebSocket shell = ClientWebSocket.newSocket("shell");


    protected void init() {

        shell.send("db");

            /*document.getBody().appendChild(div).appendChild(
                    document.createTextNode(JSON.stringify(ws)));*/
    }


    public Client() {

        attn.setOnData((x) -> {

        });


        shell.setOnOpen(this::init);
        shell.setOnData((x) -> {
            System.out.println(stringify(x));
        });


        HTMLDocument doc = HTMLDocument.current();
//        HTMLElement div = document.createElement("div");
//        div.appendChild(document.createTextNode("TeaVM generated element"));


        HTMLElement mapContainer = doc.createElement("div");
        mapContainer.setAttribute("id", "view");
        doc.getBody().appendChild(mapContainer);


        newMap(mapContainer);
    }

    private void newMap(HTMLElement container) {
        LeafletMap map = LeafletMap.create(container, LeafletMapOptions.create());
        map.setView(LatLng.create(40, -80), 13);
        JsConsumer mapChange = (e) -> {
            JSObject bounds = map.getBounds();
            //parse this noise
            //{"_southWest":{"lat":39.932380403490875,"lng":-80.50094604492188},"_northEast":{"lat":40.082274490356966,"lng":-79.62203979492189}}
            JSObject sw = get(bounds, "_southWest");
            JSObject ne = get(bounds, "_northEast");

            float[][] b = new float[][] {
                new float[] { getFloat(sw, "lng"), getFloat(sw, "lat") },
                new float[] { getFloat(ne, "lng"), getFloat(ne, "lat") }
            };

            System.out.println(Arrays.toString(b[0]) + Arrays.toString(b[1]));

            attn.send("whereLonLat(" + "[" + Arrays.toString(b[0]) + "," + Arrays.toString(b[1]) + "])");

        };
        map.onLoad(mapChange);
        map.onZoomEnd(mapChange);
        map.onMoveEnd(mapChange);

        TileLayer.create("http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", TileLayerOptions.create()
                /*.attribution("&copy; <a href=\"http://www.openstreetmap.org/copyright\">OpenStreetMap</a> " +
                        "contributors")*/)
                .addTo(map);
        //map.onClick((LeafletMouseEvent event) -> click(event.getLatlng()));
    }

    public static void main(String[] args) {

        new Client();


    }

}
