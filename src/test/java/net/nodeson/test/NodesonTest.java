package net.nodeson.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.nodeson.Nodeson;
import net.nodeson.NodesonObject;
import net.nodeson.NodesonParser;

public class NodesonTest {

    @Getter
    @ToString
    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true)
    public static class TestObject {

        private transient int id;

        private String name;

        private int x, y, z;
    }

    public static void main(String[] args) {
        TestObject testObject = new TestObject(5, "itzstonlex", 45, 6, 901);

        testObjectParsing(Nodeson.common(), testObject);
        testObjectParsing(Nodeson.parallel(), testObject);
    }

    public static void testObjectParsing(NodesonParser parser, TestObject testObject) {
        System.out.println("\n" + parser.getClass() + ":");

        long fullTime = 0;
        long startTime = System.currentTimeMillis();

        String json = parser.parseTo(testObject);
        System.out.println("Json: " + json);

        System.out.println(">> Speed time: " + (System.currentTimeMillis() - startTime) + "ms");
        fullTime += System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        TestObject converted = parser.parseFrom(json, TestObject.class);
        System.out.println("Conversion: " + converted);

        System.out.println(">> Speed time: " + (System.currentTimeMillis() - startTime) + "ms");
        fullTime += System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();

        NodesonObject nodesonObject = parser.wrap(converted);
        System.out.println(nodesonObject);

        System.out.println(">> Speed time: " + (System.currentTimeMillis() - startTime) + "ms");
        fullTime += System.currentTimeMillis() - startTime;

        System.out.println(">> Full Speed time: " + fullTime + "ms");
    }
}
