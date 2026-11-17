package vn.edu.fpt.savora.mapper.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.savora.dto.customer.response.CustomerOrderItemResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerOrderResponse;
import vn.edu.fpt.savora.entity.OrderItem;
import vn.edu.fpt.savora.entity.RestaurantOrder;
import vn.edu.fpt.savora.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface CustomerOrderMapper extends BaseMapper<RestaurantOrder, CustomerOrderResponse> {

    @Override
    @Mapping(
            target = "orderType",
            expression = "java(entity.getOrderType() != null ? entity.getOrderType().name() : \"DINE_IN\")"
    )
    @Mapping(target = "tableCode", source = "diningTable.tableCode")
    @Mapping(target = "reservationCode", source = "reservation.reservationCode")
    @Mapping(target = "voucherCode", source = "voucher.voucherCode")
    @Mapping(
            target = "status",
            expression = "java(entity.getStatus() != null ? entity.getStatus().name() : \"OPEN\")"
    )
    CustomerOrderResponse toResponse(RestaurantOrder entity);

    @Mapping(target = "dishId", source = "dish.dishId")
    @Mapping(target = "dishName", source = "dish.dishName")
    @Mapping(
            target = "lineTotal",
            expression = "java(item.getUnitPrice() != null && item.getQuantity() != null ? item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())) : java.math.BigDecimal.ZERO)"
    )
    @Mapping(
            target = "itemStatus",
            expression = "java(item.getItemStatus() != null ? item.getItemStatus().name() : \"PENDING\")"
    )
    CustomerOrderItemResponse toItemResponse(OrderItem item);
}
