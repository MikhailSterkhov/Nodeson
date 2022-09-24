package net.nodeson.token;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public enum TypeToken {

    BEGIN,

    END,

    DELIMIT,

    NODE_NAME,

    NODE_VALUE,
}
