package com.example.shoestore.service;

import com.example.shoestore.dto.request.ApplyVoucherRequest;
import com.example.shoestore.dto.response.ApplyVoucherResponse;
import com.example.shoestore.entity.Voucher;
import com.example.shoestore.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final UserVoucherService userVoucherService;

    public Voucher findById(Integer id) {
        return voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher not found with id: " + id));
    }

    public Voucher findByCode(String code) {
        return voucherRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Voucher not found with code: " + code));
    }

    public ApplyVoucherResponse applyVoucher(ApplyVoucherRequest request) {
        Voucher voucher = voucherRepository.findByCode(request.getVoucherCode()).orElse(null);
        if (voucher == null) return fail("Voucher không tồn tại");
        if (!Boolean.TRUE.equals(voucher.getIsActive())) return fail("Voucher đã bị khóa");
        if (voucher.isNotStarted()) return fail("Voucher chưa đến thời gian sử dụng");
        if (voucher.isExpired()) return fail("Voucher đã hết hạn");
        if (voucher.isUsageLimitExceeded()) return fail("Voucher đã hết lượt sử dụng");
        if (!voucher.isMinOrderSatisfied(request.getOrderAmount())) return fail("Đơn hàng tối thiểu " + voucher.getMinOrderAmount());
        if (userVoucherService.hasUsedVoucher(request.getUserId(), voucher.getId())) return fail("Bạn đã sử dụng voucher này");

        BigDecimal discount = voucher.calculateDiscount(request.getOrderAmount());
        return ApplyVoucherResponse.builder()
                .success(true)
                .code(voucher.getCode())
                .discountAmount(discount)
                .message("Áp dụng voucher thành công")
                .build();
    }

    @Transactional
    public Voucher incrementUsedCount(Integer voucherId) {
        Voucher voucher = findById(voucherId);
        voucher.setUsedCount(voucher.getUsedCount() + 1);
        return voucherRepository.save(voucher);
    }

    private ApplyVoucherResponse fail(String message) {
        return ApplyVoucherResponse.builder()
                .success(false)
                .code("")
                .discountAmount(BigDecimal.ZERO)
                .message(message)
                .build();
    }
}
