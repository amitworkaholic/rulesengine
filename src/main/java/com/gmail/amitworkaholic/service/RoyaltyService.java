package com.gmail.amitworkaholic.service;

import com.gmail.amitworkaholic.entity.Order;
import com.gmail.amitworkaholic.entity.PackingSlip;

public interface RoyaltyService {
    PackingSlip generatePackingSlip(Order order);
}