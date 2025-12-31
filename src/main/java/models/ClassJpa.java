package models;

import enums.UserType;
import exceptions.BadJpaArgumentException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "classes")
@Getter
@NoArgsConstructor
public class ClassJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course", referencedColumnName = "id", nullable = false)
    private CourseJpa courseJpa;

    @ManyToMany
    @JoinTable(
            name = "class_students",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<UserJpa> studentsJpa = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "class_teachers",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<UserJpa> teachersJpa = new ArrayList<>();

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    public ClassJpa(Long id, CourseJpa courseJpa, List<UserJpa> studentsJpa, List<UserJpa> teachersJpa, Date startDate) {
        String message = "";

        try {
            validateCourse(courseJpa);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        try {
            validateStartDate(startDate);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        if (!message.isEmpty())
            throw new BadJpaArgumentException(message);

        this.id = id;
        this.courseJpa = courseJpa;
        this.studentsJpa = studentsJpa;
        this.teachersJpa = teachersJpa;
        this.startDate = startDate;
    }

    public void addTeacher(UserJpa teacher) {
        validateTeacher(teacher);

        if (teachersJpa.stream().anyMatch(teacherJpa -> teacherJpa.getId().equals(teacher.getId())))
            throw new BadJpaArgumentException("Professor já consta na classe para ser armazenado no banco de dados.");

        teachersJpa.add(teacher);
    }

    public void addStudent(UserJpa student) {
        validateStudent(student);

        if (studentsJpa.stream().anyMatch(studentJpa -> studentJpa.getId().equals(student.getId())))
            throw new BadJpaArgumentException("Aluno já consta na classe para ser armazenado no banco de dados.");

        studentsJpa.add(student);
    }

    public void removeTeacher(UserJpa teacher) {
        validateTeacher(teacher);

        if (teachersJpa.stream().noneMatch(teacherJpa -> teacherJpa.getId().equals(teacher.getId())))
            throw new BadJpaArgumentException("Professor não consta na classe para ser armazenado no banco de dados.");

        teachersJpa.remove(teacher);
    }

    public void removeStudent(UserJpa student) {
        validateStudent(student);

        if (studentsJpa.stream().noneMatch(studentJpa -> studentJpa.getId().equals(student.getId())))
            throw new BadJpaArgumentException("Aluno não consta na classe para ser armazenado no banco de dados.");

        studentsJpa.remove(student);
    }

    private void validateTeacher(UserJpa teacher) {
        if (Objects.isNull(teacher) || !teacher.getUserType().equals(UserType.TEACHER)) {
            throw new BadJpaArgumentException("Professor inválido a ser adicionado na classe para ser armazenado no banco de dados.");
        }
    }

    private void validateStudent(UserJpa student) {
        if (Objects.isNull(student) || !student.getUserType().equals(UserType.STUDENT)) {
            throw new BadJpaArgumentException("Aluno inválido a ser adicionado na classe para ser armazenado no banco de dados.");
        }
    }

    private void validateCourse(CourseJpa courseJpa) {
        if (Objects.isNull(courseJpa))
            throw new BadJpaArgumentException("A classe deve possuir um curso para ser armazenada no banco de dados.");
    }

    private void validateStartDate(Date startDate) {
        if (Objects.isNull(startDate))
            throw new BadJpaArgumentException("A classe deve possuir uma data de início para ser armazenada no banco de dados.");
    }
}
