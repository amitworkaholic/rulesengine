package com.gmail.amitworkaholic.repository;

import com.gmail.amitworkaholic.entity.Membership;

public interface MembershipRepository {
    Membership findByProductId(String productId);
}
