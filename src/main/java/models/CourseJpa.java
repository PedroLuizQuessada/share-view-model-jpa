package models;

import exceptions.BadJpaArgumentException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor
public class CourseJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    public CourseJpa(Long id, String name, String description) {
        String message = "";

        try {
            validateName(name);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        if (!message.isEmpty())
            throw new BadJpaArgumentException(message);

        this.id = id;
        this.name = name;
        this.description = description;
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.isEmpty())
            throw new BadJpaArgumentException("O curso deve possuir um nome para ser armazenado no banco de dados.");
    }

}
