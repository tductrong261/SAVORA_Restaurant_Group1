package vn.edu.fpt.savora.mapper.admin;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.savora.dto.admin.response.AdminTableResponse;
import vn.edu.fpt.savora.entity.DiningTable;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface AdminTableMapper extends BaseMapper<DiningTable, AdminTableResponse> {

    @Override
    @Mapping(
            target = "status",
            expression = "java(entity.getStatus() != null ? entity.getStatus().name() : \"AVAILABLE\")"
    )
    AdminTableResponse toResponse(DiningTable entity);
}
