package com.idolu.product.domain.product.type;

import java.util.Arrays;

public enum ImageType {
    MAIN, DETAIL;

    public static ImageType toImageType(String type) {
        return Arrays.stream(ImageType.values())
                .filter(imageType -> imageType.name().equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("타입을 찾을 수 없습니다. ImageType: %s".formatted(type)));

    }
}
