package vn.edu.fpt.savora.mapper.auth;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.edu.fpt.savora.dto.auth.request.RegisterRequest;
import vn.edu.fpt.savora.dto.auth.response.AuthUserResponse;
import vn.edu.fpt.savora.entity.Role;
import vn.edu.fpt.savora.entity.User;
import vn.edu.fpt.savora.enums.Gender;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface AuthUserMapper extends BaseMapper<User, AuthUserResponse> {

    @Override
    @Mapping(
            target = "fullName",
            expression = "java((user.getFirstName() + \" \" + user.getLastName()).trim())"
    )
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "roleName", ignore = true)
    @Mapping(target = "redirectUrl", ignore = true)
    AuthUserResponse toResponse(User user);

    @Mapping(
            target = "fullName",
            expression = "java((user.getFirstName() + \" \" + user.getLastName()).trim())"
    )
    @Mapping(target = "roleId", source = "primaryRole.roleId")
    @Mapping(target = "roleName", source = "primaryRole.roleName")
    @Mapping(target = "redirectUrl", source = "redirectUrl")
    AuthUserResponse toAuthUserResponse(User user, Role primaryRole, String redirectUrl);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "firstName", expression = "java(request.getFirstName().trim())")
    @Mapping(target = "lastName", expression = "java(request.getLastName().trim())")
    @Mapping(target = "email", expression = "java(request.getEmail().trim().toLowerCase())")
    @Mapping(target = "phone", expression = "java(request.getPhone() != null ? request.getPhone().trim() : null)")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "mapGender")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "avatarUrl", constant = "https://res.cloudinary.com/dej5mxdrt/image/upload/v1780061324/OIP_dbbjuo.jpg")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);

    @Named("mapGender")
    default Gender mapGender(String gender) {
        if (gender == null || gender.isBlank()) {
            return Gender.OTHER;
        }
        try {
            return Gender.valueOf(gender.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return Gender.OTHER;
        }
    }
}
