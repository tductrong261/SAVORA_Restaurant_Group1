package vn.edu.fpt.savora.mapper.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.savora.dto.customer.response.CustomerReservationResponse;
import vn.edu.fpt.savora.entity.Reservation;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface CustomerReservationMapper extends BaseMapper<Reservation, CustomerReservationResponse> {

    @Override
    @Mapping(target = "tableId", source = "diningTable.tableId")
    @Mapping(target = "tableCode", source = "diningTable.tableCode")
    @Mapping(target = "tableName", source = "diningTable.tableName")
    @Mapping(target = "area", source = "diningTable.area")
    @Mapping(target = "tableCapacity", source = "diningTable.capacity")
    @Mapping(
            target = "status",
            expression = "java(entity.getStatus() != null ? entity.getStatus().name() : \"PENDING\")"
    )
    CustomerReservationResponse toResponse(Reservation entity);
}
