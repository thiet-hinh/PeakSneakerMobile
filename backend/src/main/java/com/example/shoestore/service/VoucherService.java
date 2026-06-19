package com.example.shoestore.service;

import com.example.shoestore.entity.Voucher;
import com.example.shoestore.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;


    public Voucher findById(Integer id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found with id: " + id));
    }

    public Voucher findByCode(String code) {
        return voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Voucher not found with code: " + code));
    }

    public boolean existsByCode(String code) {
        return voucherRepository.existsByCode(code);
    }

    @Transactional
    public Voucher save(Voucher voucher) {

        if (voucherRepository.existsByCode(voucher.getCode())) {
            throw new RuntimeException("Voucher code already exists: " + voucher.getCode());
        }

        return voucherRepository.save(voucher);
    }

    @Transactional
    public Voucher update(Integer id, Voucher updated) {

        Voucher existing = findById(id);

        if (!existing.getCode().equals(updated.getCode())
                && voucherRepository.existsByCode(updated.getCode())) {
            throw new RuntimeException("Voucher code already exists: " + updated.getCode());
        }

        existing.setCode(updated.getCode());
        existing.setDiscountType(updated.getDiscountType());
        existing.setDiscountValue(updated.getDiscountValue());
        existing.setExpireAt(updated.getExpireAt());
        existing.setIsActive(updated.getIsActive());
        existing.setMaxDiscountAmount(updated.getMaxDiscountAmount());
        existing.setMinOrderAmount(updated.getMinOrderAmount());
        existing.setStartAt(updated.getStartAt());
        existing.setUsageLimit(updated.getUsageLimit());

        return voucherRepository.save(existing);
    }

    @Transactional
    public Voucher incrementUsedCount(Integer voucherId) {

        Voucher voucher = findById(voucherId);

        voucher.setUsedCount(voucher.getUsedCount() + 1);

        return voucherRepository.save(voucher);
    }

    @Transactional
    public Voucher decrementUsedCount(Integer voucherId) {

        Voucher voucher = findById(voucherId);

        if (voucher.getUsedCount() > 0) {
            voucher.setUsedCount(voucher.getUsedCount() - 1);
        }

        return voucherRepository.save(voucher);
    }

    @Transactional
    public void setActive(Integer voucherId, Boolean isActive) {

        Voucher voucher = findById(voucherId);

        voucher.setIsActive(isActive);

        voucherRepository.save(voucher);
    }

    public boolean isValid(Voucher voucher) {

        LocalDateTime now = LocalDateTime.now();

        if (!Boolean.TRUE.equals(voucher.getIsActive())) {
            return false;
        }
        if (now.isBefore(voucher.getStartAt())) {
            return false;
        }
        if (now.isAfter(voucher.getExpireAt())) {
            return false;
        }
        if (voucher.getUsageLimit() > 0
                && voucher.getUsedCount() >= voucher.getUsageLimit()) {
            return false;
        }
        return true;
    }

}
