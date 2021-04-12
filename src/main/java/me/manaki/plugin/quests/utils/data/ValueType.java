package me.manaki.plugin.quests.utils.data;

public enum ValueType {

    STRING {
        @Override
        public boolean compare(String source, Object... objects) {
            if (source.equalsIgnoreCase("*")) return true;
            if (!source.contains(";")) {
                return source.equals(objects[0]);
            }
            String[] a = source.split(";");
            String type = a[0];

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
