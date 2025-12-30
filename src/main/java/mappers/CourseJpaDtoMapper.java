package mappers;

import models.CourseJpa;
import dtos.CourseDto;

public class CourseJpaDtoMapper {

    private CourseJpaDtoMapper(){}

    public static CourseJpa toCourseJpa(CourseDto courseDto) {
        return new CourseJpa(courseDto.id(), courseDto.name(), courseDto.description());
    }

    public static CourseDto toCourseDto(CourseJpa courseJpa) {
        return new CourseDto(courseJpa.getId(), courseJpa.getName(), courseJpa.getDescription());
    }

}
