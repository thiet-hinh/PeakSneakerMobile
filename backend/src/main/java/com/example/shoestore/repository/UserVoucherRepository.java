package com.example.shoestore.repository;

import com.example.shoestore.entity.UserVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Integer> {

    List<UserVoucher> findByUserIdAndIsUsedFalse(Integer userId);

    Optional<UserVoucher> findByUserIdAndVoucherId(Integer userId, Integer voucherId);

    boolean existsByUserIdAndVoucherIdAndIsUsedFalse(Integer userId, Integer voucherId);

    boolean existsByUserIdAndVoucherIdAndIsUsedTrue(Integer userId, Integer voucherId);
}
