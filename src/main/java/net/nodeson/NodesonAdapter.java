package net.nodeson;

import lombok.NonNull;

public interface NodesonAdapter<T> {

    String serialize(@NonNull T source);

    T deserialize(@NonNull Class<? extends T> type, @NonNull String json);
}
