package interfaces.controllers.subscription;

import application.subscription.dto.SubscriptionResponse;
import application.subscription.dto.TransactionResponse;
import application.subscription.service.SubscriptionApplicationService;
import interfaces.dto.subscription.ActivateSubscriptionRequest;
import interfaces.dto.subscription.CreateSubscriptionRequest;
import interfaces.dto.subscription.ProcessPaymentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionApplicationService subscriptionService;

    public SubscriptionController(SubscriptionApplicationService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        SubscriptionResponse response = subscriptionService.createSubscription(
                request.userId(),
                request.planType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<SubscriptionResponse> activate(
            @PathVariable String id,
            @Valid @RequestBody ActivateSubscriptionRequest request
    ) {
        SubscriptionResponse response = subscriptionService.activateSubscription(
                id,
                request.lastFourDigits(),
                request.holderName(),
                request.expirationDate(),
                request.brand()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<TransactionResponse> processPayment(
            @PathVariable String id,
            @Valid @RequestBody ProcessPaymentRequest request
    ) {
        TransactionResponse response = subscriptionService.processPayment(
                id,
                request.amount(),
                request.merchant()
        );
        return ResponseEntity.ok(response);
    }
}