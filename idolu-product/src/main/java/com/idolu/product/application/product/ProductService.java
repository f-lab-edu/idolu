package com.idolu.product.application.product;

import com.idolu.product.application.product.command.ProductCreateCommand;
import com.idolu.product.application.product.command.GetProductsByCategoryAndStoreCommand;
import com.idolu.product.application.product.command.ProductStockUpdateCommand;
import com.idolu.product.application.product.command.ProductUpdateCommand;
import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.ProductDiscount;
import com.idolu.product.domain.product.ProductImage;
import com.idolu.product.domain.product.type.ImageType;
import com.idolu.product.global.annotation.DistributedLock;
import com.idolu.product.global.common.ProductException;
import com.idolu.product.infrastructure.out.persistence.adapter.*;
import com.idolu.product.presentation.product.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.Comparator;
import java.util.List;

import static com.idolu.product.global.common.ResponseCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductAdapter productAdapter;
    private final CategoryAdapter categoryAdapter;
    private final StoreAdapter storeAdapter;
    private final ProductImageAdapter productImageAdapter;
    private final ProductDiscountAdapter productDiscountAdapter;

    public Mono<Long> createProduct(ProductCreateCommand command) {
        return categoryAdapter.findByCategoryId(command.getCategoryId())
                .flatMap(category -> productAdapter.createProduct(command.toEntity(category.getCategoryId())));
    }

    /**
     * 1. 회원사 정보 조회
     * - 회원사가 없다면 예외 반환
     * 2. 카테고리 리스트 조회
     * - 카테고리가 없다면 예외 반환
     * 3. 카테고리 리스트에 대한 상품 정보 조회(최신순) 및 반환
     * - 마지막 product_id 기준으로 20개 조회
     */
    public Mono<ProductListResponse> getProductsByCategoryAndStore(GetProductsByCategoryAndStoreCommand productSearchCommand) {
        return storeAdapter.findByStoreCode(productSearchCommand.getStoreCode())
                .zipWhen(store -> categoryAdapter.findByCategoryId(productSearchCommand.getCategoryId()))
                .flatMapMany(TupleUtils.function((store, category) -> productAdapter.getProductByCategoryAndStore(productSearchCommand, store.getStoreId())))
                .collectList()
                .map(productItemDtos -> ProductListResponse.builder()
                        .products(productItemDtos)
                        .build());
    }

    public Mono<ProductDetailResponse> getProductByProductId(Long productId) {
        return Mono.zip(productAdapter.findById(productId),
                        productImageAdapter.findByProductId(productId).collectList(),
                        productDiscountAdapter.findByProductId(productId).collectList())
                .map(TupleUtils.function(this::generateProductDetailResponse));
    }

    /**
     * 1. 상품 정보 조회
     * - 상품 정보가 없다면 예외 반환
     * 2. 마지막 수정일시 다른 경우 예외 반환
     * 3. 업데이트할 카테고리 정보 조회
     * - 카테고리 정보가 없다면 예외 반환
     * 5. 상품 정보 업데이트
     * 6. Version 다른 경우 예외 반환
     * 7. 상품 세부 정보(할인, 이미지) 업데이트
     */
    public Mono<Long> updateProduct(ProductUpdateCommand command) {
        return productAdapter.findById(command.getProductId())
                .flatMap(product -> {
                    if (!product.getUpdatedAt().isEqual(command.getUpdatedAt())) {
                        return Mono.error(new ProductException(PRODUCT_INVALID_UPDATED_TIME));
                    }

                    return Mono.just(product.changeInfo(command));
                })
                .flatMap(product -> categoryAdapter.findByCategoryId(command.getCategoryId())
                        .then(Mono.just(product)))
                .flatMap(productAdapter::updateProduct)
                .map(Product::getProductId);
    }

    @DistributedLock(lockName = "productStock", identifier = "productId", paramClassType = ProductStockUpdateCommand.class)
    public Mono<Boolean> updateProductStock(ProductStockUpdateCommand command) {
        return productAdapter.findById(command.getProductId())
                .flatMap(product -> {
                    Product updatedProduct = product.updateStock(command.getStock(), command.getStockType());
                    if (updatedProduct.getStock() < 0) {
                        return Mono.error(new ProductException(PRODUCT_INSUFFICIENT_STOCK));
                    }

                    return productAdapter.updateStock(updatedProduct);
                })
                .thenReturn(true);
    }

    private ProductDetailResponse generateProductDetailResponse(Product product, List<ProductImage> productImages, List<ProductDiscount> productDiscounts) {
        return ProductDetailResponse.builder()
                .productId(product.getProductId())
                .productIdentifier(product.getProductIdentifier())
                .name(product.getName())
                .productStatus(product.getProductStatus())
                .applyRoundDiscount(product.getApplyRoundDiscount())
                .price(PriceInformationDto.builder()
                        .basicPrice(product.getBasicPrice())
                        .sellingPrice(product.getSellingPrice())
                        .discountRate(product.getDiscountRate())
                        .build())
                .term(SubscriptionTermDto.builder()
                        .contractPeriod(product.getContractPeriod())
                        .contractPeriodUnitCode(product.getContractPeriodUnitCode())
                        .servicePeriod(product.getServicePeriod())
                        .servicePeriodUnitCode(product.getServicePeriodUnitCode())
                        .build())
                .productInformation(product.getProductInformation())
                .images(productImages.stream()
                        .filter(productImage -> productImage.getImageType().equals(ImageType.MAIN))
                        .sorted(Comparator.comparing(ProductImage::getSortNumber))
                        .map(productImage -> ProductImageDto.builder()
                                .url(productImage.getUrl())
                                .build())
                        .toList())
                .detailImages(productImages.stream()
                        .filter(productImage -> productImage.getImageType().equals(ImageType.DETAIL))
                        .sorted(Comparator.comparing(ProductImage::getSortNumber))
                        .map(productImage -> ProductImageDto.builder()
                                .url(productImage.getUrl())
                                .build())
                        .toList())
                .discountRounds(productDiscounts.stream()
                        .sorted(Comparator.comparing(ProductDiscount::getDiscountRound))
                        .map(productDiscount -> DiscountRoundDto.builder()
                                .discountCode(productDiscount.getDiscountCode())
                                .discountRound(productDiscount.getDiscountRound())
                                .discountValue(productDiscount.getDiscountValue())
                                .build())
                        .toList())
                .build();
    }
}
