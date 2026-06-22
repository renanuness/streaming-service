package subscription.model.valueObject;
public record PaymentResult(
        boolean success,
        String transactionId,
        String status,
        String errorMessage,
        String receiptUrl
) {
    public static PaymentResult success(String transactionId, String receiptUrl) {
        return new PaymentResult(true, transactionId, "APPROVED", null, receiptUrl);
    }

    public static PaymentResult failure(String errorMessage) {
        return new PaymentResult(false, null, "DECLINED", errorMessage, null);
    }
}
