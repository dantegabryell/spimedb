package spimedb.client;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;

/**
 * Created by me on 1/16/17.
 */
public class JS {

    @JSBody(params = { "instance", "index" }, script = "return instance[index];")
    public static native float getFloat(JSObject instance, String index);

    @JSBody(params = { "instance", "index" }, script = "return instance[index];")
    public static native JSArray getArray(JSObject instance, String index);

    @JSBody(params = { "instance", "index" }, script = "return instance[index];")
    public static native String getString(JSObject instance, String index);

    @JSBody(params = { "instance", "index", "defaultVal" }, script = "return instance[index] || defaultVal;")
    public static native String getString(JSObject instance, String index, String defaultVal);

    @JSBody(params = "obj", script = "return Array.isArray(obj);")
    public static native boolean isArray(JSObject obj);

    @JSBody(params = "obj", script = "return typeof obj === 'number';")
    public static native boolean isNumber(JSObject obj);


    @JSBody(params = { "instance", "index" }, script = "return instance[index];")
    public static native JSObject get(JSObject instance, String index);

    @JSBody(params = { "instance", "index" }, script = "return instance[index];")
    public static native JSObject get(JSObject instance, JSObject index);

    @JSBody(params = { "instance", "index", "obj" }, script = "instance[index] = obj;")
    public static native void set(JSObject instance, JSObject index, JSObject obj);

    @JSBody(params = { "instance" }, script = "return parseFloat(instance);")
    public static native float toFloat(JSObject instance);

}
