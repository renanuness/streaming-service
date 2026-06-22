package application.subscription.service;


import application.subscription.dto.*;
import shared.exception.ResourceNotFoundException;
import shared.exception.ForbiddenException;
import shared.model.valueObject.UserId;
import subscription.model.aggregate.Subscription;
import subscription.model.entity.Transaction;
import subscription.model.valueObject.*;
import subscription.repository.SubscriptionRepository;
import subscription.service.SubscriptionDomainService;
import subscription.service.PaymentDomainService;
import shared.domain.DomainEventPublisher;
import shared.exception.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriptionApplicationService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionDomainService subscriptionDomainService;
    private final PaymentDomainService paymentDomainService;
    private final DomainEventPublisher eventPublisher;

    public SubscriptionApplicationService(
            SubscriptionRepository subscriptionRepository,
            SubscriptionDomainService subscriptionDomainService,
            PaymentDomainService paymentDomainService,
            DomainEventPublisher eventPublisher
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionDomainService = subscriptionDomainService;
        this.paymentDomainService = paymentDomainService;
        this.eventPublisher = eventPublisher;
    }

    public SubscriptionResponse createSubscription(String userId, String planType) {
        UserId userIdVO = new UserId(userId);
        Plan plan = getPlanByType(planType);

        List<Subscription> existingSubscriptions = subscriptionRepository.findByUserId(userIdVO);

        Subscription subscription = subscriptionDomainService.createSubscription(
                userIdVO, plan, existingSubscriptions
        );

        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);

        return toResponse(subscription);
    }

    public SubscriptionResponse activateSubscription(
            String subscriptionId,
            String lastFourDigits,
            String holderName,
            String expirationDate,
            String brand
    ) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        CreditCard creditCard = new CreditCard(
                lastFourDigits,
                holderName,
                YearMonth.parse(expirationDate),
                CardBrand.valueOf(brand),
                CardStatus.ACTIVE
        );

        subscriptionDomainService.activateSubscription(subscription, creditCard);

        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);

        return toResponse(subscription);
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse findById(String subscriptionId) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        return toResponse(subscription);
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse findActiveByUserId(String userId) {
        UserId id = new UserId(userId);
        Subscription subscription = subscriptionRepository.findActiveByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma assinatura ativa encontrada"));

        return toResponse(subscription);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> findByUserId(String userId) {
        UserId id = new UserId(userId);
        return subscriptionRepository.findByUserId(id)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse refundPayment(String subscriptionId, String transactionId) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        TransactionId transactionIdVO = new TransactionId(transactionId);
        Transaction transaction = paymentDomainService.refundPayment(subscription, transactionIdVO);

        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);

        return toTransactionResponse(transaction);
    }

    public TransactionResponse processPayment(String subscriptionId, double amount, String merchant) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        Money amountVO = Money.brl(amount);

        Transaction transaction = paymentDomainService.processPaymentSafely(
                subscription, amountVO, merchant
        );
        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);

        return toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(String subscriptionId) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        return subscription.transactions()
                .stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());
    }

    public SubscriptionResponse changePlan(String subscriptionId, String newPlanType, boolean chargeDifference) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        Plan newPlan = getPlanByType(newPlanType);

        subscriptionDomainService.changePlan(subscription, newPlan, chargeDifference);

        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);

        return toResponse(subscription);
    }

    public SubscriptionResponse addFamilyMember(String subscriptionId, String memberId) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        UserId memberIdVO = new UserId(memberId);

        subscriptionDomainService.addFamilyMember(subscription, memberIdVO);

        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);

        return toResponse(subscription);
    }

    public SubscriptionResponse removeFamilyMember(String subscriptionId, String memberId) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        subscription.removeFamilyMember(new UserId(memberId));

        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);

        return toResponse(subscription);
    }

    public void cancelSubscription(String subscriptionId, String userId) {
        SubscriptionId id = new SubscriptionId(subscriptionId);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assinatura não encontrada"));

        if (!subscription.isOwner(new UserId(userId))) {
            throw new ForbiddenException("Apenas o titular pode cancelar a assinatura");
        }

        subscription.cancel();

        subscriptionRepository.save(subscription);
        subscription.publishEvents(eventPublisher);
    }

    private Plan getPlanByType(String planType) {
        return switch (planType.toUpperCase()) {
            case "BASIC" -> Plan.basic();
            case "PREMIUM" -> Plan.premium();
            case "FAMILIAR" -> Plan.familiar();
            default -> throw new BusinessRuleException("Tipo de plano inválido: " + planType);
        };
    }

    private SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.id().value(),
                subscription.userId().value(),
                subscription.plan().type().name(),
                subscription.plan().price().toString(),
                subscription.status().name(),
                subscription.creditCard() != null ? subscription.creditCard().maskedNumber() : null,
                subscription.familyMemberCount(),
                subscription.startedAt(),
                subscription.expiresAt(),
                subscription.cancelledAt()
        );
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.id().value(),
                transaction.amount().toString(),
                transaction.merchant(),
                transaction.type().name(),
                transaction.status().name(),
                transaction.timestamp(),
                transaction.failureReason()
        );
    }
}