package vn.edu.fpt.savora.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.savora.dto.admin.response.AdminDishResponse;
import vn.edu.fpt.savora.entity.Dish;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface AdminDishMapper extends BaseMapper<Dish, AdminDishResponse> {

    @Override
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(
            target = "status",
            expression = "java(entity.getStatus() != null ? entity.getStatus().name() : \"AVAILABLE\")"
    )
    AdminDishResponse toResponse(Dish entity);
}
