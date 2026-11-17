package vn.edu.fpt.savora.service.customer;

import vn.edu.fpt.savora.dto.customer.request.ChangePasswordRequest;
import vn.edu.fpt.savora.dto.customer.request.CreateOrderRequest;
import vn.edu.fpt.savora.dto.customer.request.CreateReservationRequest;
import vn.edu.fpt.savora.dto.customer.request.CreateReviewRequest;
import vn.edu.fpt.savora.dto.customer.request.UpdateProfileRequest;
import vn.edu.fpt.savora.dto.customer.response.CustomerOrderResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerProfileResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerReservationResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerReviewResponse;
import vn.edu.fpt.savora.dto.customer.response.CustomerVoucherResponse;
import vn.edu.fpt.savora.entity.DiningTable;
import vn.edu.fpt.savora.entity.Dish;

import java.util.List;

public interface CustomerPortalService {

    CustomerProfileResponse getProfile(Integer userId);

    CustomerProfileResponse updateProfile(Integer userId, UpdateProfileRequest request);

    void changePassword(Integer userId, ChangePasswordRequest request);

    List<CustomerVoucherResponse> getActiveVouchers();

    List<DiningTable> getBookableTables();

    List<CustomerReservationResponse> getCustomerReservations(Integer userId);

    CustomerReservationResponse createReservation(Integer userId, CreateReservationRequest request);

    void cancelReservation(Integer userId, Integer reservationId);

    List<Dish> getAvailableDishes();

    List<CustomerOrderResponse> getCustomerOrders(Integer userId);

    CustomerOrderResponse createOrder(Integer userId, CreateOrderRequest request);

    List<CustomerReviewResponse> getCustomerReviews(Integer userId);

    CustomerReviewResponse createReview(Integer userId, CreateReviewRequest request);
}
