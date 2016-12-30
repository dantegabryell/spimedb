package spimedb.sense;

import org.junit.Test;
import spimedb.db.SpimeDB;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

/**
 * Created by me on 12/29/16.
 */
public class ImportGeoJSONTest {

    @Test
    public void test1() throws IOException {
        InputStream resourceAsStream = ImportGeoJSONTest.class.getClassLoader().getResourceAsStream("eq.geojson");
        SpimeDB db = new SpimeDB();
        new ImportGeoJSON(resourceAsStream, db);

        db.print(System.out);
        System.out.println(db.tag);

        assertTrue(db.size() > 10);
    }

}