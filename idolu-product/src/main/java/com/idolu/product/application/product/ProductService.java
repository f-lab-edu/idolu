package com.idolu.product.application.product;

import com.idolu.product.application.product.command.ProductCreateCommand;
import com.idolu.product.application.product.command.ProductSearchCommand;
import com.idolu.product.application.product.command.ProductUpdateCommand;
import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.ProductDiscount;
import com.idolu.product.domain.product.ProductImage;
import com.idolu.product.domain.product.type.ImageType;
import com.idolu.product.global.exception.ProductUpdateException;
import com.idolu.product.infrastructure.out.persistence.adapter.*;
import com.idolu.product.presentation.product.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.Comparator;
import java.util.List;

import static com.idolu.product.global.exception.ErrorCode.PRODUCT_INVALID_UPDATED_TIME;

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
        return categoryAdapter.validateCategoriesExist(command.getCategories())
                .flatMap((categories) -> productAdapter.createProduct(command.toEntity(categories)));
    }

    /**
     * 1. 회원사 정보 조회
     * - 회원사가 없다면 예외 반환
     * 2. 카테고리 리스트 조회
     * - 카테고리가 없다면 예외 반환
     * 3. 카테고리 리스트에 대한 상품 정보 조회(최신순) 및 반환
     * - 마지막 product_id 기준으로 20개 조회
     */
    public Mono<ProductListResponse> selectProducts(ProductSearchCommand productSearchCommand) {
        return storeAdapter.findByStoreCode(productSearchCommand.getStoreCode())
                .zipWhen(data -> categoryAdapter.findByCategoryCodeLike(productSearchCommand.getCategoryCode()))
                .flatMapMany(TupleUtils.function((store, categories) -> productAdapter.selectProducts(productSearchCommand, store, categories)))
                .collectList()
                .map(productItemDtos -> ProductListResponse.builder()
                        .products(productItemDtos)
                        .build());

    }

    public Mono<ProductDetailResponse> selectProductByProductId(Long productId) {
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
     * 7. 상품 세부 정보(카테고리, 할인, 이미지) 업데이트
     */
    public Mono<Long> updateProduct(ProductUpdateCommand command) {
        return productAdapter.findById(command.getProductId())
                .flatMap(product -> {
                    if (!product.getUpdatedAt().isEqual(command.getUpdatedAt())) {
                        return Mono.error(new ProductUpdateException(PRODUCT_INVALID_UPDATED_TIME, PRODUCT_INVALID_UPDATED_TIME.getMessage().formatted(product.getProductId())));
                    }

                    return Mono.just(product.changeInfo(command));
                })
                .zipWhen(product -> categoryAdapter.validateCategoriesExist(command.getCategories()))
                .map(TupleUtils.function(Product::withProductCategories))
                .flatMap(productAdapter::updateProduct)
                .map(Product::getProductId);
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
                .discountOneTime(product.getDiscountOneTime())
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
