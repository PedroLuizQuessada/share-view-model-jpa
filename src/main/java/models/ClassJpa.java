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
            name = "class_teachers",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<UserJpa> teachersJpa = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "class_students",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<UserJpa> studentsJpa = new ArrayList<>();

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    public ClassJpa(Long id, CourseJpa courseJpa, List<UserJpa> teachersJpa, List<UserJpa> studentsJpa, Date startDate) {
        String message = "";

        try {
            validateCourse(courseJpa);
        }
        catch (RuntimeException e) {
            message = message + " " + e.getMessage();
        }

        if (!Objects.isNull(teachersJpa))
            teachersJpa.forEach(this::validateTeacher);

        if (!Objects.isNull(studentsJpa))
            studentsJpa.forEach(this::validateStudent);

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
        this.teachersJpa = teachersJpa;
        this.studentsJpa = studentsJpa;
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

    public void removeTeacher(UserJpa removeTeacher) {
        validateTeacher(removeTeacher);
        if (Objects.isNull(this.teachersJpa))
            this.teachersJpa = new ArrayList<>();

        boolean removed = this.teachersJpa.removeIf(teacherJpa -> teacherJpa.getId().equals(removeTeacher.getId()));
        if (!removed)
            throw new BadJpaArgumentException("Professor não consta na classe para ser armazenado no banco de dados.");
    }

    public void removeStudent(UserJpa removeStudent) {
        validateStudent(removeStudent);
        if (Objects.isNull(this.studentsJpa))
            this.studentsJpa = new ArrayList<>();

        boolean removed = this.studentsJpa.removeIf(studentJpa -> studentJpa.getId().equals(removeStudent.getId()));
        if (!removed)
            throw new BadJpaArgumentException("Aluno não consta na classe para ser armazenado no banco de dados.");
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
