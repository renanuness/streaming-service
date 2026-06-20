package identity.service;

import identity.model.aggregate.User;
import identity.model.valueObject.DateOfBirth;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import identity.model.valueObject.Password;

import java.time.LocalDate;

public class UserRegistrationDomainService {
    public User register(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ) {
        FullName fullName = new FullName(firstName, lastName);
        Email userEmail = new Email(email);
        Password userPassword = new Password(password);
        DateOfBirth userDateOfBirth = new DateOfBirth(dateOfBirth);

        return new User(fullName, userEmail, userPassword, userDateOfBirth);
    }
}
