package net.nodeson;

import lombok.NonNull;

public interface NodesonAdapter<T, C> {

    void serialize(@NonNull T source, @NonNull C converter);

    T deserialize(@NonNull C converter);
}
