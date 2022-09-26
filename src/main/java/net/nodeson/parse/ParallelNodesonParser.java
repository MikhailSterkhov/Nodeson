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
    public NodesonObject wrap(@NonNull Object src) {
        return CompletableFuture.supplyAsync(() -> impl.wrap(src), PARALLEL_POOL).join();
    }

    @Override
    public NodesonObject wrap(@NonNull String parsedLine) {
        return CompletableFuture.supplyAsync(() -> impl.wrap(parsedLine), PARALLEL_POOL).join();
    }

    @Override
    public <T> T parseFrom(@NonNull String parsedLine, @NonNull Class<T> type) {
        return CompletableFuture.supplyAsync(() -> impl.parseFrom(parsedLine, type), PARALLEL_POOL).join();
    }

    @Override
    public String parseTo(@NonNull NodesonObject nodesonObject) {
        return CompletableFuture.supplyAsync(() -> impl.parseTo(nodesonObject), PARALLEL_POOL).join();
    }
}
