package com.gmail.amitworkaholic.service;

import com.gmail.amitworkaholic.entity.Order;
import com.gmail.amitworkaholic.entity.PackingSlip;

public interface PackingSlipService {
    PackingSlip generate(Order order);
}
