package com.idolu.product.application.product.command;

import com.idolu.product.domain.product.type.ImageType;
import lombok.Builder;
import lombok.Getter;

import static com.idolu.product.domain.product.type.ImageType.toImageType;


@Getter
public class ProductImageUpdateCommand {

    private ImageType imageType;

    private String url;

    private Integer sortNumber;

    @Builder
    public ProductImageUpdateCommand(String imageType, String url, Integer sortNumber) {
        this.imageType = toImageType(imageType);
        this.url = url;
        this.sortNumber = sortNumber;
    }
}
