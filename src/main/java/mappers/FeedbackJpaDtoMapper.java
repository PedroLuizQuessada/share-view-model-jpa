package mappers;

import models.FeedbackJpa;
import dtos.FeedbackDto;

import java.util.Objects;

public class FeedbackJpaDtoMapper {

    private FeedbackJpaDtoMapper() {}

    public static FeedbackJpa toFeedbackJpa(FeedbackDto feedbackDto) {
        return new FeedbackJpa(feedbackDto.id(),
                Objects.isNull(feedbackDto.student()) ? null : UserJpaDtoMapper.toUserJpa(feedbackDto.student()),
                Objects.isNull(feedbackDto.clazz()) ? null : ClassJpaDtoMapper.toClassJpa(feedbackDto.clazz()),
                feedbackDto.rating(), feedbackDto.description(), new java.sql.Date(feedbackDto.evaluationDate().getTime()));
    }

    public static FeedbackDto toFeedbackDto(FeedbackJpa feedbackJpa) {
        return new FeedbackDto(feedbackJpa.getId(),
                Objects.isNull(feedbackJpa.getStudentJpa()) ? null : UserJpaDtoMapper.toUserDto(feedbackJpa.getStudentJpa()),
                Objects.isNull(feedbackJpa.getClassJpa()) ? null : ClassJpaDtoMapper.toClassDto(feedbackJpa.getClassJpa()),
                feedbackJpa.getRating(), feedbackJpa.getDescription(), feedbackJpa.getEvaluationDate());
    }

}
