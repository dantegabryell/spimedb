package automenta.netention.run;

import automenta.netention.data.ClimateViewerSources;
import automenta.netention.data.SchemaOrg;
import automenta.netention.net.HttpCache;
import automenta.netention.net.Wikipedia;
import automenta.netention.net.proxy.URLSensor;
import automenta.netention.web.ClientResources;
import automenta.netention.web.Web;

/**
 * Created by me on 6/14/15.
 */
public class ClimateEditor {

    final static String cachePath = "cache";

    public static void main(String[] args) throws Exception {

        /*final static String eq4_5week = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.atom";

        public USGSEarthquakes() {
            super("USGSEarthquakes", eq4_5week, 128);*/


        //new IRCBot(s.db, "RAWinput", "irc.freenode.net", "#netention");


        HttpCache httpCache = new HttpCache(cachePath);



        //Self s = new Self();

        Web j = new Web()
                .add("/wikipedia", new Wikipedia(httpCache))
//                .add("/api/tag", new PathHandler() {
//
//                    @Override
//                    public void handleRequest(HttpServerExchange exchange) throws Exception {
//
//                        ArrayList<Object> av = Lists.newArrayList(s.allValues());
//                        byte[] b = Core.jsonAnnotated.writeValueAsBytes(av);
//                        Web.send(b, exchange, "application/json" );
//                    }
//                })
                .add("/", ClientResources.handleClientResources())
                .start("localhost", 8080);



        SchemaOrg.load(null);
//        logger.info("Loading ClimateViewer (ontology)");
//        new ClimateViewer(s.db);
//        logger.info("Loading Netention (ontology)");
//        NOntology.load(s.db);

        //InfiniPeer.local("i", cachePath, 32000);
        new ClimateViewerSources() {

            @Override
            public void onLayer(String id, String name, String kml, String icon, String currentSection) {
                URLSensor r = new URLSensor(currentSection + "/" + id, name, kml, icon);
                //p.add(r);
            }

            @Override
            public void onSection(String name, String id, String icon) {

            }
        };


//        int webPort = 9090;
//
//        SpacetimeWebServer s = SpacetimeWebServer(
//                //ElasticSpacetime.temporary("cv", -1),
//                ElasticSpacetime.local("cv", "cache", true),
//                "localhost",
//                webPort);

//        /*
//        //EXAMPLES
//        {
////            s.add("irc",
////                    new IRCBot(s.db, "RAWinput", "irc.freenode.net", "#netention", "#nars"
////                            /*"#archlinux", "#jquery"*/).serverChannel
////            );
//
//            s.add("eq", new USGSEarthquakes());
//
//
//            //new IRCBot(s.db, "RAWinput", "irc.freenode.net", "#netention");
//            //new FileTailWindow(s.db, "netlog", "/home/me/.xchat2/scrollback/FreeNode/#netention.txt").start();
//
//            s.add("demo", new SimulationDemo.SimpleSimulation("DroneSquad3"));
//            /*s.addPrefixPath("/sim", new WebSocketCore(
//                    new SimpleSimulation("x")
//            ).handler());*/
//        }
//        */
    }



}
