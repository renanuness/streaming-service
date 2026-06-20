package identity.service;

import identity.model.aggregate.User;

public class AuthenticationDomainService {
    public boolean verifyCredentials(User user, String rawPassword) {
        return user.isActive() && user.verifyCredentials(rawPassword);
    }
}
