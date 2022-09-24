package net.nodeson;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class Node {

    @Getter
    private String name;

    private Object value;

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }
}
