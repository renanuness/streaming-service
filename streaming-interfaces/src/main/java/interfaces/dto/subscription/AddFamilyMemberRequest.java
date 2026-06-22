package interfaces.dto.subscription;

import jakarta.validation.constraints.NotBlank;

public record AddFamilyMemberRequest(
        @NotBlank(message = "ID do membro é obrigatório")
        String memberId
) {}
