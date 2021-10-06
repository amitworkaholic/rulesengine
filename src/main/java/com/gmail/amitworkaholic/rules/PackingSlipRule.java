package com.gmail.amitworkaholic.rules;

import com.gmail.amitworkaholic.entity.LineItem;
import com.gmail.amitworkaholic.entity.Order;
import com.gmail.amitworkaholic.entity.Payment;
import com.gmail.amitworkaholic.entity.ProductType;
import com.gmail.amitworkaholic.service.PackingSlipService;
import com.gmail.amitworkaholic.service.RoyaltyService;
import com.gmail.amitworkaholic.service.ShippingService;

public class PackingSlipRule implements PaymentRule {
    private final PackingSlipService packingSlipService;
    private final ShippingService shippingService;
    private final RoyaltyService royaltyService;

    public PackingSlipRule(final ShippingService shippingService,
                           final RoyaltyService royaltyService,
                           final PackingSlipService packingSlipService) {
        this.shippingService = shippingService;
        this.royaltyService = royaltyService;
        this.packingSlipService = packingSlipService;
    }

    @Override
    public void makePayment(final Payment payment) {
        final Order order = payment.getOrder();
        final LineItem[] lineItems = order.getLineItems();

        Boolean generateForShipping = false;
        Boolean generateForRoyalty = false;

        for (final LineItem lineItem : lineItems) {
            if (lineItem.hasCategory(ProductType.Physical))
                generateForShipping = true;
            if (lineItem.hasCategory(ProductType.Books))
                generateForRoyalty = true;

            if (lineItem.getProductId() == "learning-to-ski")
                order.addGiftByProductId("first-aid");
        }

        packingSlipService.generate(order);

        if (generateForShipping)
            shippingService.generatePackingSlip(order);
        if (generateForRoyalty)
            royaltyService.generatePackingSlip(order);
    }
}
