package interfaces.controllers;

import application.identity.dto.CreateUserRequest;
import application.identity.dto.UserResponse;
import application.identity.service.UserApplicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserApplicationService userService;

    public UserController(UserApplicationService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.registerUser(request);
    }
}
