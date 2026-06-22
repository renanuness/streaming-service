package subscription.model.valueObject;

import shared.exception.BusinessRuleException;
import java.util.Collections;
import java.util.List;

public record Plan(
        PlanType type,
        Money price,
        PlanDuration duration,
        List<Feature> features
) {

    public Plan {
        if (type == null) {
            throw new BusinessRuleException("Tipo de plano não pode ser nulo");
        }
        if (price == null) {
            throw new BusinessRuleException("Preço do plano não pode ser nulo");
        }
        if (duration == null) {
            throw new BusinessRuleException("Duração do plano não pode ser nula");
        }
        if (features == null || features.isEmpty()) {
            throw new BusinessRuleException("Plano deve ter pelo menos uma feature");
        }
    }

    public List<Feature> features() {
        return Collections.unmodifiableList(features);
    }

    public boolean allowsMultipleMembers() {
        return type.allowsMultipleMembers();
    }

    public int maxMembers() {
        return type.maxMembers();
    }

    public boolean hasFeature(String featureName) {
        return features.stream()
                .anyMatch(f -> f.name().equalsIgnoreCase(featureName));
    }

    public static Plan basic() {
        return new Plan(
                PlanType.BASIC,
                Money.brl(19.90),
                PlanDuration.monthly(),
                List.of(
                        Feature.unlimitedMusic(),
                        Feature.noAds()
                )
        );
    }

    public static Plan premium() {
        return new Plan(
                PlanType.PREMIUM,
                Money.brl(29.90),
                PlanDuration.monthly(),
                List.of(
                        Feature.unlimitedMusic(),
                        Feature.noAds(),
                        Feature.offlineMode(),
                        Feature.highQuality()
                )
        );
    }

    public static Plan familiar() {
        return new Plan(
                PlanType.FAMILIAR,
                Money.brl(39.90),
                PlanDuration.monthly(),
                List.of(
                        Feature.unlimitedMusic(),
                        Feature.noAds(),
                        Feature.offlineMode(),
                        Feature.highQuality(),
                        Feature.familySharing()
                )
        );
    }
}