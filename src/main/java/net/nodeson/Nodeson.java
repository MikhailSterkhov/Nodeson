package net.nodeson;

import net.nodeson.parse.CommonNodesonParser;
import net.nodeson.parse.ParallelNodesonParser;

public final class Nodeson {

    private static final Nodeson instance = new Nodeson();

    private static final NodesonParser COMMON = new CommonNodesonParser();
    private static final NodesonParser PARALLEL = new ParallelNodesonParser(COMMON);

    public static Nodeson getNodesonInstance() {
        return instance;
    }

    public static NodesonParser common() {
        return COMMON;
    }

    public static NodesonParser parallel() {
        return PARALLEL;
    }

    public <T> T convert(String json, Class<T> type) {
        return COMMON.convert(json, type);
    }

    public <T> T parallelConvert(String json, Class<T> type) {
        return PARALLEL.convert(json, type);
    }

    public NodesonObject wrap(Object object) {
        return COMMON.toNodeson(object);
    }

    public NodesonObject parallelWrap(Object object) {
        return PARALLEL.toNodeson(object);
    }

    public String parse(Object object) {
        return COMMON.parse(object);
    }

    public String parallelParse(Object object) {
        return PARALLEL.parse(object);
    }
}
