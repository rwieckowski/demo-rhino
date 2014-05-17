package pl.rawie.demo.rhino;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class DemoApp {
    public static void main(String[] args) {
        evaluateAndPrint("1 + 1");
        evaluateAndPrint("var i = 10\ni + 1");
        evaluateAndPrint("var s = 'foo'\ns + 'bar'");
        evaluateAndPrint("var json = JSON.parse('{\"foo\":\"bar\"}')\njson.foo");
        evaluateAndPrint("var json = JSON.parse('{\"foo\":\"bar\"}')\njson.foo.length > 5");
        evaluateAndPrint("var json = JSON.parse('{\"foo\":1000, \"bar\":200}')\njson.foo > 500 && json.bar > 100");
    }

    private static void evaluateAndPrint(String js) {
        System.out.println("js> " + js);
        Object result = evaluate(js);
        System.out.println("result = " + result);
    }

    private static Object evaluate(String js) {
        Context ctx = Context.enter();
        try {
            Scriptable scope = ctx.initStandardObjects();
            Object result = ctx.evaluateString(scope, js, "demo-rhino", 1, null);
            return result;
        } finally {
            Context.exit();
        }
    }
}
