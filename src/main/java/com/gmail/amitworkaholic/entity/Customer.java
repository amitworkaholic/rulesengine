package com.gmail.amitworkaholic.entity;

import com.gmail.amitworkaholic.service.NotificationService;

public class Customer {
    public void addMembership(Membership membership, NotificationService notificationService) {
        notificationService.notify(this, membership);
    }

    public boolean hasMembership(Membership membership) {
        return false;
    }
}
