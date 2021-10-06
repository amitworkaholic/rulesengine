package com.gmail.amitworkaholic;

import com.gmail.amitworkaholic.entity.*;
import com.gmail.amitworkaholic.repository.MembershipRepository;
import com.gmail.amitworkaholic.rules.*;
import com.gmail.amitworkaholic.service.NotificationService;
import com.gmail.amitworkaholic.service.PackingSlipService;
import com.gmail.amitworkaholic.service.RoyaltyService;
import com.gmail.amitworkaholic.service.ShippingService;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class RulesEngineTest {
    /**
     * Test 1 : If the payment is for a physical product, generate a packing slip for shipping.
     */
    @Test
    public void testPackingSlipGeneratedForPhysicalProduct() {
        LineItem[] lineItems = new LineItem[]{
                new LineItem("item1", "item1", new ProductType[]{
                        ProductType.Physical
                })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);

        PaymentRule sut = new PackingSlipRule(shippingService, royaltyService, packingSlipService);
        sut.makePayment(payment);

        verify(shippingService, times(1)).generatePackingSlip(order);
    }

    /**
     * Test 2 : If the payment is for a book, create a duplicate packing slip for the royalty department.
     */
    @Test
    public void testDuplicatePackingSlipForRoyaltyDepartment() {
        LineItem[] lineItems = new LineItem[]{
                new LineItem("item1", "item1", new ProductType[]{
                        ProductType.Books
                }),
                new LineItem("item2", "item2", new ProductType[]{
                        ProductType.Membership,
                })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);

        PaymentRule sut = new PackingSlipRule(shippingService, royaltyService, packingSlipService);
        sut.makePayment(payment);

        verify(royaltyService, times(1)).generatePackingSlip(order);
    }

    /**
     * Test 3 : If the payment is for a membership, activate that membership.
     */
    @Test
    public void testActivateMembership() {
        LineItem[] lineItems = new LineItem[]{
                new LineItem("item1", "item1", new ProductType[]{
                        ProductType.Membership
                })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        Membership membership = new Membership("item1", null);
        when(repo.findByProductId("item1")).thenReturn(membership);

        NotificationService notificationService = mock(NotificationService.class);

        PaymentRule sut = new MembershipActivateRule(repo, notificationService);
        sut.makePayment(payment);

        verify(customer, times(1)).addMembership(membership, notificationService);
    }

    /**
     * Test 4 : If the payment is an upgrade to a membership, apply the upgrade.
     */
    @Test
    public void testUpgradeMembership() {
        Membership membershipBasic = new Membership("Basic Plan", null);
        Membership membershipPro = new Membership("Pro plan", membershipBasic);

        LineItem[] lineItems = new LineItem[]{
                new LineItem(membershipPro.getProductId(), "pro", new ProductType[]{
                        ProductType.Membership
                })
        };
        Customer customer = mock(Customer.class);
        when(customer.hasMembership(membershipBasic)).thenReturn(true);

        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        when(repo.findByProductId(membershipPro.getProductId())).thenReturn(membershipPro);

        NotificationService notificationService = mock(NotificationService.class);

        PaymentRule sut = new MembershipUpgradeRule(repo, notificationService);
        sut.makePayment(payment);

        verify(customer, times(1)).addMembership(membershipPro, notificationService);
    }

    /**
     * Test 5 : If the payment is for a membership or upgrade, e-mail the owner and inform them of the activation/upgrade.
     */
    @Test
    public void testMembershipActivateNotify() {
        LineItem[] lineItems = new LineItem[]{
                new LineItem("item1", "item1", new ProductType[]{
                        ProductType.Membership
                })
        };
        Customer customer = spy(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        Membership membership = new Membership("item1", null);
        when(repo.findByProductId("item1")).thenReturn(membership);

        NotificationService notificationService = mock(NotificationService.class);

        PaymentRule sut = new MembershipActivateRule(repo, notificationService);
        sut.makePayment(payment);

        verify(notificationService, times(1)).notify(customer, membership);
    }

    @Test
    public void testMembershipUpgradeNotify() {
        Membership membershipBasic = new Membership("Basic Plan", null);
        Membership membershipPro = new Membership("Pro Plan", membershipBasic);

        LineItem[] lineItems = new LineItem[]{
                new LineItem(membershipPro.getProductId(), "pro", new ProductType[]{
                        ProductType.Membership
                })
        };
        Customer customer = spy(Customer.class);
        when(customer.hasMembership(membershipBasic)).thenReturn(true);

        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        MembershipRepository repo = mock(MembershipRepository.class);
        when(repo.findByProductId(membershipPro.getProductId())).thenReturn(membershipPro);

        NotificationService notificationService = mock(NotificationService.class);

        PaymentRule sut = new MembershipUpgradeRule(repo, notificationService);
        sut.makePayment(payment);

        verify(notificationService, times(1)).notify(customer, membershipPro);
    }

    /**
     * Test 6 : If the payment is for the video “Learning to Ski,” add a free “First Aid” video to the packing slip (the result of a court
     * decision in 1997).
     */
    @Test
    public void testAddGiftToOrderOnCourtDecision() {
        LineItem[] lineItems = new LineItem[]{
                new LineItem("learning-to-ski", "Learning to Ski", new ProductType[]{
                        ProductType.Videos
                })
        };
        Customer customer = mock(Customer.class);
        Order order = new Order(customer, lineItems, null);
        Payment payment = new Payment(order);

        PackingSlipService packingSlipService = mock(PackingSlipService.class);
        ShippingService shippingService = mock(ShippingService.class);
        RoyaltyService royaltyService = mock(RoyaltyService.class);

        PaymentRule sut = new PackingSlipRule(shippingService, royaltyService, packingSlipService);
        sut.makePayment(payment);

        String[] gifts = order.getGiftProductIds();
        assertNotNull(gifts);
        assertTrue(gifts.length == 1);
    }

    /**
     * Test 7 : If the payment is for a physical product or a book, generate a commission payment to the agent.
     */
    @Test
    public void testAgentCommissionForPhysicalOrBook() {
        LineItem[] lineItems = new LineItem[]{
                new LineItem("item", "item", new ProductType[]{
                        ProductType.Membership
                }),
                new LineItem("book1", "book1", new ProductType[]{
                        ProductType.Books
                })
        };
        Customer customer = mock(Customer.class);
        Agent agent = mock(Agent.class);
        Order order = new Order(customer, lineItems, agent);
        Payment payment = new Payment(order);

        PaymentRule sut = new AgentCommissionRule();
        sut.makePayment(payment);

        verify(agent, times(1)).generateCommission(any());
    }
}
