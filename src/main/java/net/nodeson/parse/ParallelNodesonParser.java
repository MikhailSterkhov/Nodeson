package net.nodeson.parse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.nodeson.NodesonObject;
import net.nodeson.NodesonParser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ParallelNodesonParser extends AbstractNodesonParser {

    private static final ExecutorService PARALLEL_POOL =
            Executors.newCachedThreadPool();

    private NodesonParser impl;

    @Override
    public NodesonObject toNodeson(@NonNull Object src) {
        return CompletableFuture.supplyAsync(() -> impl.toNodeson(src), PARALLEL_POOL).join();
    }

    @Override
    public NodesonObject toNodeson(@NonNull String parsedLine) {
        return CompletableFuture.supplyAsync(() -> impl.toNodeson(parsedLine), PARALLEL_POOL).join();
    }

    @Override
    public <T> T convert(@NonNull String parsedLine, @NonNull Class<T> type) {
        return CompletableFuture.supplyAsync(() -> impl.convert(parsedLine, type), PARALLEL_POOL).join();
    }

    @Override
    public String parse(@NonNull NodesonObject nodesonObject) {
        return CompletableFuture.supplyAsync(() -> impl.parse(nodesonObject), PARALLEL_POOL).join();
    }
}
