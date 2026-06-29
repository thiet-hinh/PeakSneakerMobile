package com.example.shoestore.service;

import com.example.shoestore.entity.Order;
import com.example.shoestore.entity.UserVoucher;
import com.example.shoestore.repository.UserVoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserVoucherService {

    private final UserVoucherRepository userVoucherRepository;

    public UserVoucher findById(Integer id) {
        return userVoucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserVoucher not found with id: " + id));
    }

    public UserVoucher findByUserIdAndVoucherId(Integer userId, Integer voucherId) {
        return userVoucherRepository.findByUserIdAndVoucherId(userId, voucherId)
                .orElseThrow(() -> new RuntimeException("UserVoucher not found with userId: " + userId + " and voucherId: " + voucherId));
    }


    public boolean hasUsedVoucher(Integer userId, Integer voucherId) {
        return userVoucherRepository.existsByUserIdAndVoucherIdAndIsUsedTrue(userId, voucherId);
    }

    @Transactional
    public UserVoucher save(UserVoucher userVoucher) {
        return userVoucherRepository.save(userVoucher);
    }

    @Transactional
    public UserVoucher update(Integer id, UserVoucher updated) {
        UserVoucher existing = findById(id);
        existing.setUser(updated.getUser());
        existing.setVoucher(updated.getVoucher());
        existing.setOrder(updated.getOrder());
        existing.setIsUsed(updated.getIsUsed());
        existing.setUsedAt(updated.getUsedAt());
        return userVoucherRepository.save(existing);
    }

    @Transactional
    public UserVoucher markAsUsed(Integer userVoucherId, Order order) {
        UserVoucher userVoucher = findById(userVoucherId);
        if (Boolean.TRUE.equals(userVoucher.getIsUsed())) {
            throw new RuntimeException("Voucher already used");
        }
        userVoucher.setIsUsed(true);
        userVoucher.setUsedAt(LocalDateTime.now());
        userVoucher.setOrder(order);
        return userVoucherRepository.save(userVoucher);
    }

    @Transactional
    public UserVoucher markAsUnused(Integer userVoucherId) {
        UserVoucher userVoucher = findById(userVoucherId);
        userVoucher.setIsUsed(false);
        userVoucher.setUsedAt(null);
        userVoucher.setOrder(null);
        return userVoucherRepository.save(userVoucher);
    }
}
