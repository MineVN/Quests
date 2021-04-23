package me.manaki.plugin.quests.utils.data;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonToken;
import org.apache.logging.log4j.core.appender.routing.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public enum ValueType {

    STRING {
        @Override
        public boolean compare(String source, Object... objects) {
            if (source.equalsIgnoreCase("*")) return true;
            String[] a = source.split(";");
            String type = a[0];

            // List
            System.out.println(objects[0].getClass().getName());
            if (objects.length == 1 && objects[0] instanceof Collection) {
                var list = (Collection<String>) objects[0];
                for (String s : a) {
                    if (!list.contains(s)) return false;
                }
                return true;
            }

            if (!source.contains(";")) {
                return source.equalsIgnoreCase(objects[0].toString());
            }

            // AND
            if (type.equals("and")) {
                if (objects.length != a.length - 1) return false;
                for (int i = 1 ; i < a.length ; i++) {
                    if (objects.length < i) return false;
                    boolean contain = false;
                    for (Object object : objects) {
                        if (object.equals(a[i])) {
                            contain = true;
                            break;
                        }
                    }
                    if (!contain) return false;
                }
                return true;
            }

            // OR
            else if (type.equals("or")) {
                for (int i = 1 ; i < a.length ; i++) {
                    for (Object object : objects) {
                        if (object.equals(a[i])) return true;
                    }
                }
                return false;
            }

            return false;
        }
    },

    NUMBER {
        @Override
        public boolean compare(String source, Object... objects) {
            if (!source.contains(" ")) return Double.valueOf(source).equals(Double.parseDouble(objects[0].toString()));
            var type = source.split(" ")[0];
            var v = Double.parseDouble(source.split(" ")[1]);
            switch (type) {
                case "<":
                    return Double.parseDouble(objects[0].toString()) < v;
                case ">":
                    return Double.parseDouble(objects[0].toString()) > v;
                case "<=":
                    return Double.parseDouble(objects[0].toString()) <= v;
                case ">=":
                    return Double.parseDouble(objects[0].toString()) >= v;
                case "=":
                    return Double.parseDouble(objects[0].toString()) == v;
            }

            return false;
        }
    };

    ValueType() {}

    public abstract boolean compare(String source, Object... objects);

}
