package com.idolu.product.domain.product.type;

import com.idolu.product.global.exception.BaseException;

import java.util.Arrays;

import static com.idolu.product.global.exception.ErrorCode.ENUM_VALIDATE_FAILED;

public enum ImageType {
    MAIN, DETAIL;

    public ImageType toImageType(String type) {
        return Arrays.stream(ImageType.values())
                .filter(imageType -> imageType.name().equals(type))
                .findAny()
                .orElseThrow(() -> new BaseException(ENUM_VALIDATE_FAILED, ENUM_VALIDATE_FAILED.getMessage().formatted(type)));

    }
}
