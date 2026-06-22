package subscription.service;

import shared.model.valueObject.UserId;
import subscription.model.aggregate.Subscription;
import subscription.model.valueObject.*;
import shared.exception.BusinessRuleException;
import java.util.List;


public class SubscriptionDomainService {

    public Subscription createSubscription(
            UserId userId,
            Plan plan,
            List<Subscription> existingSubscriptions
    ) {
        boolean hasActiveSubscription = existingSubscriptions.stream()
                .anyMatch(Subscription::isActive);

        if (hasActiveSubscription) {
            throw new BusinessRuleException(
                    "Usuário já possui uma assinatura ativa. Cancele a atual antes de criar uma nova."
            );
        }

        return new Subscription(userId, plan);
    }

    public Subscription activateSubscription(Subscription subscription, CreditCard creditCard) {
        if (!creditCard.canProcessPayment()) {
            throw new BusinessRuleException(
                    "Não é possível ativar a assinatura: cartão não está ativo ou está vencido"
            );
        }

        subscription.activate(creditCard);
        return subscription;
    }

    public Subscription changePlan(
            Subscription subscription,
            Plan newPlan,
            boolean chargeDifference
    ) {
        Plan currentPlan = subscription.plan();

        if (currentPlan.type() == newPlan.type()) {
            throw new BusinessRuleException("Novo plano é igual ao plano atual");
        }

        if (chargeDifference && newPlan.price().isGreaterThan(currentPlan.price())) {
        }

        subscription.changePlan(newPlan);
        return subscription;
    }

    public void validatePlanChange(Subscription subscription, Plan newPlan) {
        if (!subscription.isActive()) {
            throw new BusinessRuleException("Só é possível alterar o plano de uma assinatura ativa");
        }

        if (subscription.plan().type() == newPlan.type()) {
            throw new BusinessRuleException("Novo plano é igual ao plano atual");
        }
    }

    public void addFamilyMember(Subscription subscription, UserId memberId) {
        if (!subscription.plan().allowsMultipleMembers()) {
            throw new BusinessRuleException(
                    "Apenas o plano FAMILIAR permite adicionar membros. " +
                            "Seu plano atual: " + subscription.plan().type().description()
            );
        }

        if (subscription.familyMemberCount() >= 5) {
            throw new BusinessRuleException(
                    "Limite máximo de 5 membros atingido. Remova um membro para adicionar outro."
            );
        }

        subscription.addFamilyMember(memberId);
    }

    public boolean canSubscribe(UserId userId, List<Subscription> existingSubscriptions) {
        return existingSubscriptions.stream()
                .noneMatch(s -> s.isActive() || s.status() == SubscriptionStatus.SUSPENDED);
    }

    public Plan suggestUpgrade(Subscription subscription, int familyMemberCount) {
        if (familyMemberCount > 1 && !subscription.plan().allowsMultipleMembers()) {
            return Plan.familiar();
        }

        return null;
    }
}