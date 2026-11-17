package vn.edu.fpt.savora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.savora.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByCustomer_UserIdOrderByCreatedAtDesc(Integer userId);
}
