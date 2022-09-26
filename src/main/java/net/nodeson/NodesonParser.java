package net.nodeson;

import lombok.NonNull;

public interface NodesonParser {

    NodesonObject wrap(@NonNull Object src);

    NodesonObject wrap(@NonNull String parsedLine);

    <T> T parseFrom(@NonNull NodesonObjectBuilder nodesonObject, @NonNull Class<T> type);

    <T> T parseFrom(@NonNull NodesonObject nodesonObject, @NonNull Class<T> type);

    <T> T parseFrom(@NonNull String parsedLine, @NonNull Class<T> type);

    String parseTo(@NonNull NodesonObject nodesonObject);

    String parseTo(@NonNull Object src);
}
