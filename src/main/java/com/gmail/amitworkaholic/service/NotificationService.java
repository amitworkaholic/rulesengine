package com.gmail.amitworkaholic.service;

import com.gmail.amitworkaholic.entity.Customer;
import com.gmail.amitworkaholic.entity.Membership;

public interface NotificationService {
    void notify(Customer customer, Membership membership);
}