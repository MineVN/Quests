package me.manaki.plugin.quests.utils.data;

import java.util.Collection;

public enum ValueType {

    STRING {
        @Override
        public boolean compare(String source, Object... objects) {
            if (source.equalsIgnoreCase("*")) return true;
            String[] a = source.split(";");
            String type = a[0];

            // List
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
            switch (type) {
                case "and":
                    if (objects.length != a.length - 1) return false;
                    for (int i = 1; i < a.length; i++) {
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


                // OR
                case "or":
                    for (int i = 1; i < a.length; i++) {
                        for (Object object : objects) {
                            if (object.equals(a[i])) return true;
                        }
                    }
                    return false;


                // START WITH
                case "startwith":
                    for (int i = 1; i < a.length; i++) {
                        for (Object object : objects) {
                            if (object.toString().startsWith(a[i])) return true;
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
