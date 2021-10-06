package com.gmail.amitworkaholic.rules;

import com.gmail.amitworkaholic.entity.*;
import com.gmail.amitworkaholic.repository.MembershipRepository;
import com.gmail.amitworkaholic.service.NotificationService;

public class MembershipUpgradeRule implements PaymentRule {

    private final MembershipRepository membershipRepository;
    private final NotificationService notificationService;

    public MembershipUpgradeRule(final MembershipRepository repo, final NotificationService notificationService) {
        membershipRepository = repo;
        this.notificationService = notificationService;
    }

    @Override
    public void makePayment(Payment payment) {
        Order order = payment.getOrder();
        Customer customer = order.getCustomer();
        LineItem[] lineItems = order.getLineItems();
        for (LineItem lineItem : lineItems) {
            if (!lineItem.hasCategory(ProductType.Membership))
                continue;

            Membership membership = membershipRepository.findByProductId(lineItem.getProductId());
            Membership previousLevel = membership.getPreviousLevel();
            if (customer.hasMembership(previousLevel))
                customer.addMembership(membership, notificationService);
        }
    }
}
