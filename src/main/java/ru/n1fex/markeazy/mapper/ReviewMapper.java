package ru.n1fex.markeazy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.n1fex.markeazy.dto.ReviewDto;
import ru.n1fex.markeazy.entity.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target="authorId", expression = "java(review.getUser().getId())")
    @Mapping(target="authorName", expression = "java(review.getUser().getName())")
    ReviewDto toReviewDto(Review review);

}
