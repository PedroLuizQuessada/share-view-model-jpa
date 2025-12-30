package models;

import exceptions.BadJpaArgumentException;
import enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_type")
    private UserType userType;

    public UserJpa(Long id, String email, String password, UserType userType) {
        String message = "";

        try {
            validateEmail(email);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        try {
            validatePassword(password);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        try {
            validateUserType(userType);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        if (!message.isEmpty())
            throw new BadJpaArgumentException(message);

        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || email.isEmpty())
            throw new BadJpaArgumentException("O usuário deve possuir um e-mail para ser armazenado no banco de dados.");

        if (email.length() > 45)
            throw new BadJpaArgumentException("O e-mail do usuário deve possuir até 45 caracteres para ser armazenado no banco de dados.");

        if (!EmailValidator.getInstance().isValid(email))
            throw new BadJpaArgumentException("E-mail inválido para ser armazenado no banco de dados.");
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password))
            throw new BadJpaArgumentException("O usuário deve possuir uma senha para ser armazenado no banco de dados.");

        if (password.length() > 255)
            throw new BadJpaArgumentException("Falha ao gerar senha criptografada do usuário, favor contactar o administrador.");
    }

    private void validateUserType(UserType userType) {
        if (Objects.isNull(userType))
            throw new BadJpaArgumentException("O usuário deve possuir tipo para ser armazenado no banco de dados.");
    }

}
