package vn.edu.fpt.savora.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.savora.dto.admin.response.AdminUserResponse;
import vn.edu.fpt.savora.entity.User;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface AdminUserMapper extends BaseMapper<User, AdminUserResponse> {

    @Override
    @Mapping(
            target = "fullName",
            expression = "java((entity.getFirstName() + \" \" + entity.getLastName()).trim())"
    )
    @Mapping(
            target = "gender",
            expression = "java(entity.getGender() != null ? entity.getGender().name() : \"OTHER\")"
    )
    @Mapping(
            target = "roleName",
            expression = "java(entity.getRoles() != null && !entity.getRoles().isEmpty() ? entity.getRoles().iterator().next().getRoleName() : \"CUSTOMER\")"
    )
    @Mapping(
            target = "status",
            expression = "java(entity.getStatus() != null ? entity.getStatus().name() : \"ACTIVE\")"
    )
    AdminUserResponse toResponse(User entity);
}
