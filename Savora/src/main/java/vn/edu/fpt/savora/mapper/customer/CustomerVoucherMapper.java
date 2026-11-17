package vn.edu.fpt.savora.mapper.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.savora.dto.customer.response.CustomerVoucherResponse;
import vn.edu.fpt.savora.entity.Voucher;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface CustomerVoucherMapper extends BaseMapper<Voucher, CustomerVoucherResponse> {

    @Override
    @Mapping(
            target = "status",
            expression = "java(entity.getStatus() != null ? entity.getStatus().name() : \"ACTIVE\")"
    )
    CustomerVoucherResponse toResponse(Voucher entity);
}
