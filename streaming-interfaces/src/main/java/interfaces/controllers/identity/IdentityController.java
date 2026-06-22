package interfaces.controllers.identity;

import application.identity.dto.CreateUserRequest;
import application.identity.dto.UserResponse;
import application.identity.service.UserApplicationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    private final UserApplicationService userService;

    public IdentityController(@Qualifier("identityUserApplicationService") UserApplicationService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.registerUser(request);
    }
}
