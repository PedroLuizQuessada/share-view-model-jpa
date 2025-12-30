package models;

import exceptions.BadJpaArgumentException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "feedbacks")
@Getter
@NoArgsConstructor
public class FeedbackJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student", referencedColumnName = "id", nullable = false)
    private UserJpa studentJpa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class", referencedColumnName = "id", nullable = false)
    private ClassJpa classJpa;

    @Column(nullable = false)
    private Integer rating;

    @Column
    private String description;

    @Column(name = "evaluation_date", nullable = false)
    private Date evaluationDate;

    public FeedbackJpa(Long id, UserJpa studentJpa, ClassJpa classJpa, Integer rating, String description, Date evaluationDate) {
        String message = "";

        try {
            validateStudentJpa(studentJpa);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        try {
            validateClassJpa(classJpa);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        try {
            validateRating(rating);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        try {
            validateDescription(description);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        try {
            validateEvaluationDate(evaluationDate);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        if (!message.isEmpty())
            throw new BadJpaArgumentException(message);

        this.id = id;
        this.studentJpa = studentJpa;
        this.classJpa = classJpa;
        this.rating = rating;
        this.description = description;
        this.evaluationDate = evaluationDate;
    }

    private void validateStudentJpa(UserJpa studentJpa) {
        if (Objects.isNull(studentJpa))
            throw new BadJpaArgumentException("O feedback deve possuir um aluno para ser armazenado no banco de dados.");
    }

    private void validateClassJpa(ClassJpa classJpa) {
        if (Objects.isNull(classJpa))
            throw new BadJpaArgumentException("O feedback deve possuir uma turma para ser armazenado no banco de dados.");
    }

    private void validateRating(Integer rating) {
        if (Objects.isNull(rating))
            throw new BadJpaArgumentException("O feedback deve possuir uma nota para ser armazenado no banco de dados.");
    }

    private void validateDescription(String description) {
        if (!Objects.isNull(description) && description.length() > 255)
            throw new BadJpaArgumentException("A descrição do feedback deve possuir até 255 caracteres para ser armazenado no banco de dados.");
    }

    private void validateEvaluationDate(Date evaluationDate) {
        if (Objects.isNull(evaluationDate))
            throw new BadJpaArgumentException("O feedback deve possuir uma data de avaliação para ser armazenado no banco de dados.");
    }
}
