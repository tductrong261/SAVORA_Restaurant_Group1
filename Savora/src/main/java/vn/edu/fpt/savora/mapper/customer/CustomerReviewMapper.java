package vn.edu.fpt.savora.mapper.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.savora.dto.customer.response.CustomerReviewResponse;
import vn.edu.fpt.savora.entity.Review;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface CustomerReviewMapper extends BaseMapper<Review, CustomerReviewResponse> {

    @Override
    @Mapping(target = "dishName", source = "dish.dishName")
    @Mapping(target = "orderCode", source = "order.orderCode")
    CustomerReviewResponse toResponse(Review entity);
}
