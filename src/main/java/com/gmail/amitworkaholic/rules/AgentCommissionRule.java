package com.gmail.amitworkaholic.rules;

import com.gmail.amitworkaholic.entity.*;

public class AgentCommissionRule implements PaymentRule {

    @Override
    public void makePayment(Payment payment) {
        Order order = payment.getOrder();
        LineItem[] lineItems = order.getLineItems();

        Boolean addCommission = false;

        for (LineItem lineItem : lineItems) {
            if (lineItem.hasCategory(ProductType.Books) || lineItem.hasCategory(ProductType.Physical)) {
                addCommission = true;
                break;
            }
        }

        if (addCommission) {
            Agent agent = order.getAgent();
            agent.generateCommission(order);
        }
    }

}
