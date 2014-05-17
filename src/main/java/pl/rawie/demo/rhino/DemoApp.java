package pl.rawie.demo.rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DemoApp {
    public static void main(String[] args) {
        evaluateAndPrint("1 + 1");
        evaluateAndPrint("var i = 10\ni + 1");
        evaluateAndPrint("var s = 'foo'\ns + 'bar'");
        evaluateAndPrint("var obj = JSON.parse('{\"foo\":\"bar\"}')\nobj.foo");
        evaluateAndPrint("var obj = JSON.parse('{\"foo\":\"bar\"}')\nobj.foo.length > 5");
        evaluateAndPrint("var obj = JSON.parse('{\"foo\":1000, \"bar\":200}')\nobj.foo > 500 && obj.bar > 100");
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("json", "{\"foo\":1000, \"bar\":200}");
        evaluateAndPrint("var obj = JSON.parse(json)\nobj.foo + obj.bar", parms);
    }

    private static void evaluateAndPrint(String js) {
        evaluateAndPrint(js, Collections.<String, Object>emptyMap());
    }

    private static void evaluateAndPrint(String js, Map<String, Object> parms) {
        System.out.println("js> " + js);
        for (Map.Entry<String, Object> entry : parms.entrySet())
            System.out.println(entry.getKey() + " = " + entry.getValue());
        Object result = evaluate(js, parms);
        System.out.println("result = " + result);
    }

    private static Object evaluate(String js, Map<String, Object> parms) {
        Context ctx = Context.enter();
        try {
            Scriptable scope = ctx.initStandardObjects();

            for (Map.Entry<String, Object> entry : parms.entrySet()) {
                Object wrapped = Context.javaToJS(entry.getValue(), scope);
                ScriptableObject.putProperty(scope, entry.getKey(), wrapped);
            }

            Object result = ctx.evaluateString(scope, js, "demo-rhino", 1, null);
            return result;
        } finally {
            Context.exit();
        }
    }
}
