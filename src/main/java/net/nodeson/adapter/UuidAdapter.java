package net.nodeson.adapter;

import lombok.NonNull;
import net.nodeson.NodesonParser;

import java.util.UUID;

public class UuidAdapter extends AbstractStringAdapter<UUID> {

    public UuidAdapter(NodesonParser callingSource) {
        super(callingSource);
    }

    @Override
    public void serialize(@NonNull UUID source, @NonNull StringBuffer converter) {
        converter.append(source);
    }

    @Override
    public UUID deserialize(@NonNull StringBuffer converter) {
        return UUID.fromString(converter.toString());
    }
}
