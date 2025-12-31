package mappers;

import models.ClassJpa;
import dtos.ClassDto;

import java.util.Objects;
import java.util.stream.Collectors;

public class ClassJpaDtoMapper {

    private ClassJpaDtoMapper() {}

    public static ClassJpa toClassJpa(ClassDto classDto) {
        return new ClassJpa(classDto.id(),
                Objects.isNull(classDto.course()) ? null : CourseJpaDtoMapper.toCourseJpa(classDto.course()),
                Objects.isNull(classDto.teachers()) ? null : classDto.teachers().stream().map(UserJpaDtoMapper::toUserJpa).collect(Collectors.toList()),
                Objects.isNull(classDto.students()) ? null : classDto.students().stream().map(UserJpaDtoMapper::toUserJpa).collect(Collectors.toList()),
                new java.sql.Date(classDto.startDate().getTime()));
    }

    public static ClassDto toClassDto(ClassJpa classJpa) {
        return new ClassDto(classJpa.getId(),
                Objects.isNull(classJpa.getCourseJpa()) ? null : CourseJpaDtoMapper.toCourseDto(classJpa.getCourseJpa()),
                Objects.isNull(classJpa.getTeachersJpa()) ? null : classJpa.getTeachersJpa().stream().map(UserJpaDtoMapper::toUserDto).collect(Collectors.toList()),
                Objects.isNull(classJpa.getStudentsJpa()) ? null : classJpa.getStudentsJpa().stream().map(UserJpaDtoMapper::toUserDto).collect(Collectors.toList()),
                new java.util.Date(classJpa.getStartDate().getTime()));
    }

}
