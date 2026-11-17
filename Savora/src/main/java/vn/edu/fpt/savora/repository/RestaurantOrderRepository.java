package vn.edu.fpt.savora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.savora.entity.RestaurantOrder;

import java.util.List;

@Repository
public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Integer> {
    List<RestaurantOrder> findByCustomer_UserIdOrderByCreatedAtDesc(Integer userId);
    List<RestaurantOrder> findTop6ByOrderByCreatedAtDesc();
}
