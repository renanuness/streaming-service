package subscription.model.valueObject;

import shared.exception.BusinessRuleException;

public record Feature(String name, String description) {

    public Feature {
        if (name == null || name.isBlank()) {
            throw new BusinessRuleException("Nome da feature não pode ser vazio");
        }
        if (description == null || description.isBlank()) {
            throw new BusinessRuleException("Descrição da feature não pode ser vazia");
        }
    }

    public static Feature unlimitedMusic() {
        return new Feature("MÚSICA ILIMITADA", "Acesso ilimitado a todas as músicas");
    }

    public static Feature offlineMode() {
        return new Feature("MODO OFFLINE", "Baixe músicas para ouvir sem internet");
    }

    public static Feature noAds() {
        return new Feature("SEM ANÚNCIOS", "Ouça música sem interrupções");
    }

    public static Feature highQuality() {
        return new Feature("ALTA QUALIDADE", "Streaming em qualidade máxima (320kbps)");
    }

    public static Feature familySharing() {
        return new Feature("COMPARTILHAMENTO FAMILIAR", "Até 5 membros da família");
    }
}