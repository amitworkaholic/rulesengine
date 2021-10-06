package com.gmail.amitworkaholic.rules;

import com.gmail.amitworkaholic.entity.*;
import com.gmail.amitworkaholic.repository.MembershipRepository;
import com.gmail.amitworkaholic.service.NotificationService;

public class MembershipActivateRule implements PaymentRule {
    private final MembershipRepository membershipRepository;
    private final NotificationService notificationService;

    public MembershipActivateRule(final MembershipRepository membershipRepository, final NotificationService notificationService) {
        this.membershipRepository = membershipRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void makePayment(final Payment payment) {
        final Order order = payment.getOrder();
        final LineItem[] lineItems = order.getLineItems();
        final Customer customer = order.getCustomer();
        for (final LineItem lineItem : lineItems) {
            if (!lineItem.hasCategory(ProductType.Membership))
                continue;

            final Membership membership = membershipRepository.findByProductId(lineItem.getProductId());
            if (membership != null)
                customer.addMembership(membership, notificationService);
        }
    }

}