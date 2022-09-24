package net.nodeson;

import lombok.NonNull;

public interface NodesonParser {

    NodesonObject toNodeson(@NonNull Object src);

    NodesonObject toNodeson(@NonNull String parsedLine);

    <T> T convert(@NonNull NodesonObjectBuilder nodesonObject, @NonNull Class<T> type);

    <T> T convert(@NonNull NodesonObject nodesonObject, @NonNull Class<T> type);

    <T> T convert(@NonNull String parsedLine, @NonNull Class<T> type);

    String parse(@NonNull NodesonObject nodesonObject);

    String parse(@NonNull Object src);
}
