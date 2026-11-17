package vn.edu.fpt.savora.service.customer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import vn.edu.fpt.savora.entity.OrderItem;
import vn.edu.fpt.savora.entity.Reservation;
import vn.edu.fpt.savora.entity.RestaurantOrder;
import vn.edu.fpt.savora.entity.Review;
import vn.edu.fpt.savora.entity.User;
import vn.edu.fpt.savora.entity.Voucher;
import vn.edu.fpt.savora.enums.DishStatus;
import vn.edu.fpt.savora.enums.Gender;
import vn.edu.fpt.savora.enums.OrderItemStatus;
import vn.edu.fpt.savora.enums.OrderStatus;
import vn.edu.fpt.savora.enums.OrderType;
import vn.edu.fpt.savora.enums.ReservationStatus;
import vn.edu.fpt.savora.enums.TableStatus;
import vn.edu.fpt.savora.enums.VoucherStatus;
import vn.edu.fpt.savora.exception.AppException;
import vn.edu.fpt.savora.exception.ErrorCode;
import vn.edu.fpt.savora.mapper.customer.CustomerOrderMapper;
import vn.edu.fpt.savora.mapper.customer.CustomerProfileMapper;
import vn.edu.fpt.savora.mapper.customer.CustomerReservationMapper;
import vn.edu.fpt.savora.mapper.customer.CustomerReviewMapper;
import vn.edu.fpt.savora.mapper.customer.CustomerVoucherMapper;
import vn.edu.fpt.savora.repository.DiningTableRepository;
import vn.edu.fpt.savora.repository.DishRepository;
import vn.edu.fpt.savora.repository.ReservationRepository;
import vn.edu.fpt.savora.repository.RestaurantOrderRepository;
import vn.edu.fpt.savora.repository.ReviewRepository;
import vn.edu.fpt.savora.repository.UserRepository;
import vn.edu.fpt.savora.repository.VoucherRepository;
import vn.edu.fpt.savora.security.util.PasswordUtils;
import vn.edu.fpt.savora.service.customer.CustomerPortalService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CustomerPortalServiceImpl implements CustomerPortalService {

    private static final Set<ReservationStatus> ACTIVE_RESERVATION_STATUSES = EnumSet.of(
            ReservationStatus.PENDING,
            ReservationStatus.CONFIRMED,
            ReservationStatus.SEATED
    );

    private final UserRepository userRepository;
    private final DiningTableRepository diningTableRepository;
    private final ReservationRepository reservationRepository;
    private final DishRepository dishRepository;
    private final VoucherRepository voucherRepository;
    private final RestaurantOrderRepository restaurantOrderRepository;
    private final ReviewRepository reviewRepository;

    private final CustomerProfileMapper profileMapper;
    private final CustomerReservationMapper reservationMapper;
    private final CustomerOrderMapper orderMapper;
    private final CustomerVoucherMapper voucherMapper;
    private final CustomerReviewMapper reviewMapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerProfileResponse getProfile(Integer userId) {
        User user = getUserOrThrow(userId);
        return profileMapper.toResponse(user);
    }

    @Override
    @Transactional
    public CustomerProfileResponse updateProfile(Integer userId, UpdateProfileRequest request) {
        User user = getUserOrThrow(userId);
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPhone(request.getPhone().trim());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setAddress(request.getAddress() != null ? request.getAddress().trim() : null);

        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(request.getAvatarUrl().trim());
        }

        if (request.getGender() != null && !request.getGender().isBlank()) {
            try {
                user.setGender(Gender.valueOf(request.getGender().trim().toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                user.setGender(Gender.OTHER);
            }
        }

        User saved = userRepository.save(user);
        return profileMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void changePassword(Integer userId, ChangePasswordRequest request) {
        User user = getUserOrThrow(userId);

        if (!PasswordUtils.checkPassword(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        user.setPasswordHash(PasswordUtils.hashPassword(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerVoucherResponse> getActiveVouchers() {
        try {
            return voucherMapper.toResponseList(
                    voucherRepository.findByStatusOrderByVoucherIdAsc(VoucherStatus.ACTIVE)
            );
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiningTable> getBookableTables() {
        try {
            return diningTableRepository.findByStatusNotOrderByTableCodeAsc(TableStatus.MAINTENANCE);
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerReservationResponse> getCustomerReservations(Integer userId) {
        try {
            return reservationMapper.toResponseList(
                    reservationRepository.findByCustomer_UserIdOrderByReservationDateDescReservationTimeDesc(userId)
            );
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public CustomerReservationResponse createReservation(Integer userId, CreateReservationRequest request) {
        User customer = getUserOrThrow(userId);
        DiningTable table = diningTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new AppException(ErrorCode.TABLE_NOT_FOUND));

        if (request.getGuestCount() > table.getCapacity()) {
            throw new AppException(ErrorCode.TABLE_CAPACITY_EXCEEDED);
        }

        List<Reservation> existing = reservationRepository.findByDiningTable_TableIdAndReservationDateAndStatusIn(
                table.getTableId(),
                request.getReservationDate(),
                ACTIVE_RESERVATION_STATUSES
        );

        int duration = Reservation.DEFAULT_DURATION_MINUTES;
        for (Reservation r : existing) {
            if (r.overlapsWith(request.getReservationTime(), duration)) {
                throw new AppException(ErrorCode.TABLE_TIME_CONFLICT);
            }
        }

        String code = "RES-" + System.currentTimeMillis() % 1000000
                + "-" + ThreadLocalRandom.current().nextInt(10, 99);

        Reservation reservation = Reservation.builder()
                .reservationCode(code)
                .customer(customer)
                .customerName((customer.getFirstName() + " " + customer.getLastName()).trim())
                .customerPhone(request.getCustomerPhone().trim())
                .customerEmail(customer.getEmail())
                .diningTable(table)
                .reservationDate(request.getReservationDate())
                .reservationTime(request.getReservationTime())
                .durationMinutes(duration)
                .endTime(request.getReservationTime().plusMinutes(duration))
                .guestCount(request.getGuestCount())
                .specialRequest(request.getSpecialRequest() != null ? request.getSpecialRequest().trim() : null)
                .status(ReservationStatus.CONFIRMED)
                .build();

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void cancelReservation(Integer userId, Integer reservationId) {
        Reservation reservation = reservationRepository.findByReservationIdAndCustomer_UserId(reservationId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() == ReservationStatus.PENDING
                || reservation.getStatus() == ReservationStatus.CONFIRMED) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dish> getAvailableDishes() {
        try {
            return dishRepository.findByStatusOrderByDishIdAsc(DishStatus.AVAILABLE);
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrderResponse> getCustomerOrders(Integer userId) {
        try {
            return orderMapper.toResponseList(
                    restaurantOrderRepository.findByCustomer_UserIdOrderByCreatedAtDesc(userId)
            );
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public CustomerOrderResponse createOrder(Integer userId, CreateOrderRequest request) {
        User customer = getUserOrThrow(userId);
        Dish dish = dishRepository.findById(request.getDishId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST));

        OrderType orderType = OrderType.TAKEAWAY;
        if ("PRE_ORDER".equalsIgnoreCase(request.getOrderType())) {
            orderType = OrderType.PRE_ORDER;
        }

        Reservation linkedReservation = null;
        DiningTable linkedTable = null;
        if (orderType == OrderType.PRE_ORDER && request.getReservationId() != null) {
            linkedReservation = reservationRepository.findByReservationIdAndCustomer_UserId(request.getReservationId(), userId)
                    .orElse(null);
            if (linkedReservation != null) {
                linkedTable = linkedReservation.getDiningTable();
            }
        }

        BigDecimal unitPrice = dish.getDiscountPrice() != null ? dish.getDiscountPrice() : dish.getPrice();
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));
        BigDecimal discount = BigDecimal.ZERO;
        Voucher appliedVoucher = null;

        if (request.getVoucherCode() != null && !request.getVoucherCode().isBlank()) {
            Voucher voucher = voucherRepository.findByVoucherCodeIgnoreCase(request.getVoucherCode().trim())
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_INVALID));

            LocalDateTime now = LocalDateTime.now();
            if (voucher.getStatus() != VoucherStatus.ACTIVE
                    || voucher.getQuantity() <= 0
                    || (voucher.getEndDate() != null && voucher.getEndDate().isBefore(now))) {
                throw new AppException(ErrorCode.VOUCHER_INVALID);
            }

            if (voucher.getMinOrderValue() != null && subtotal.compareTo(voucher.getMinOrderValue()) < 0) {
                throw new AppException(ErrorCode.VOUCHER_MIN_ORDER);
            }

            if (voucher.getDiscountPercent() != null && voucher.getDiscountPercent() > 0) {
                discount = subtotal.multiply(BigDecimal.valueOf(voucher.getDiscountPercent()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                if (voucher.getMaxDiscountAmount() != null && discount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
                    discount = voucher.getMaxDiscountAmount();
                }
            }

            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);
            appliedVoucher = voucher;
        }

        BigDecimal taxable = subtotal.subtract(discount).max(BigDecimal.ZERO);
        BigDecimal tax = taxable.multiply(new BigDecimal("0.08")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = taxable.add(tax);

        String orderCode = "ORD-" + System.currentTimeMillis() % 1000000
                + "-" + ThreadLocalRandom.current().nextInt(10, 99);

        RestaurantOrder order = RestaurantOrder.builder()
                .orderCode(orderCode)
                .customer(customer)
                .diningTable(orderType == OrderType.TAKEAWAY ? null : linkedTable)
                .reservation(linkedReservation)
                .voucher(appliedVoucher)
                .orderType(orderType)
                .subtotalAmount(subtotal)
                .discountAmount(discount)
                .taxAmount(tax)
                .totalAmount(total)
                .status(OrderStatus.OPEN)
                .note(request.getNote() != null ? request.getNote().trim() : null)
                .items(new ArrayList<>())
                .build();

        OrderItem item = OrderItem.builder()
                .order(order)
                .dish(dish)
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .note(request.getNote() != null ? request.getNote().trim() : null)
                .itemStatus(OrderItemStatus.PENDING)
                .build();

        order.getItems().add(item);

        RestaurantOrder saved = restaurantOrderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerReviewResponse> getCustomerReviews(Integer userId) {
        try {
            return reviewMapper.toResponseList(
                    reviewRepository.findByCustomer_UserIdOrderByCreatedAtDesc(userId)
            );
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public CustomerReviewResponse createReview(Integer userId, CreateReviewRequest request) {
        User customer = getUserOrThrow(userId);
        Dish dish = request.getDishId() != null
                ? dishRepository.findById(request.getDishId()).orElse(null)
                : null;
        RestaurantOrder order = request.getOrderId() != null
                ? restaurantOrderRepository.findById(request.getOrderId()).orElse(null)
                : null;

        Review review = Review.builder()
                .customer(customer)
                .dish(dish)
                .order(order)
                .rating(request.getRating())
                .comment(request.getComment().trim())
                .build();

        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponse(saved);
    }

    private User getUserOrThrow(Integer userId) {
        if (userId == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
