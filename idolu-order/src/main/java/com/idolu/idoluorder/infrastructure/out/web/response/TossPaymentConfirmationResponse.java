package com.idolu.idoluorder.infrastructure.out.web.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * https://docs.tosspayments.com/reference#payment-%EA%B0%9D%EC%B2%B4
 * 토스 결제 Payment 객체
 */
@Getter
public class TossPaymentConfirmationResponse {

    private String version;
    private String paymentKey;
    private String type;
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private String method;
    private BigDecimal totalAmount;
    private BigDecimal balanceAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private Boolean useEscrow;
    private String lastTransactionKey;
    private BigDecimal suppliedAmount;
    private BigDecimal vat;
    private Boolean cultureExpense;
    private BigDecimal taxFreeAmount;
    private Integer taxExemptionAmount;
    private List<CancelInfo> cancels;
    private CardInfo card;
    private VirtualAccount virtualAccount;
    private RefundReceiveAccount refundReceiveAccount;
    private String secret;
    private MobilePhone mobilePhone;
    private GiftCertificate giftCertificate;
    private Transfer transfer;
    private Map<String, String> metadata;
    private Receipt receipt;
    private Checkout checkout;
    private EasyPay easyPay;
    private String country;
    private TossFailureResponse failure;
    private CashReceipt cashReceipt;
    private List<CashReceiptHistory> cashReceipts;
    private Discount discount;

    public static class CancelInfo {
        private BigDecimal cancelAmount;
        private String cancelReason;
        private BigDecimal taxFreeAmount;
        private Integer taxExemptionAmount;
        private BigDecimal refundableAmount;
        private BigDecimal transferDiscountAmount;
        private BigDecimal easyPayDiscountAmount;
        private String canceledAt;
        private String transactionKey;
        private String receiptKey;
        private String cancelStatus;
        private String cancelRequestId;
        private Boolean isPartialCancelable;
    }

    public static class CardInfo {
        private BigDecimal amount;
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private Integer installmentPlanMonths;
        private String approveNo;
        private Boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private Boolean isInterestFree;
        private String interestPayer;
    }

    public static class VirtualAccount {
        private String accountType;
        private String accountNumber;
        private String bankCode;
        private String customerName;
        private String dueDate;
        private String refundStatus;
        private Boolean expired;
        private String settlementStatus;
    }

    public static class RefundReceiveAccount {
        private String bankCode;
        private String accountNumber;
        private String holderName;
    }

    public static class MobilePhone {
        private String customerMobilePhone;
        private String settlementStatus;
        private String receiptUrl;
    }

    public static class GiftCertificate {
        private String approveNo;
        private String settlementStatus;
    }

    public static class Transfer {
        private String bankCode;
        private String settlementStatus;
    }

    public static class Receipt {
        private String url;
    }

    public static class Checkout {
        private String url;
    }

    public static class EasyPay {
        private String provider;
        private BigDecimal amount;
        private BigDecimal discountAmount;
    }

    @Getter
    public static class TossFailureResponse {
        private String code;
        private String message;
    }

    public static class CashReceipt {
        private String type;
        private String receiptKey;
        private String issueNumber;
        private String receiptUrl;
        private BigDecimal amount;
        private BigDecimal taxFreeAmount;
    }

    public static class CashReceiptHistory {
        private String receiptKey;
        private String orderId;
        private String orderName;
        private String type;
        private String issueNumber;
        private String receiptUrl;
        private String businessNumber;
        private String transactionType;
        private Integer amount;
        private Integer taxFreeAmount;
        private String issueStatus;
        private TossFailureResponse failure;
        private String customerIdentityNumber;
        private String requestedAt;
    }

    public static class Discount {
        private Integer amount;
    }
}
