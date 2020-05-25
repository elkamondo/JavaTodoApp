package io.github.elkamondo.utils;

import java.util.UUID;

public interface GeneratedIdentifier {

    default String generateId(int length) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }

}
