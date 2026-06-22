package interfaces.controllers.core;

import application.core.service.UserApplicationService;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserController {
    private final UserApplicationService userApplicationService;

    public UserController(@Qualifier("coreUserApplicationService") UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }
}
