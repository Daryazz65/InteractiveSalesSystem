package ru.cementpromo.service;

import ru.cementpromo.model.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CementPromoService {

    private final BigDecimal pricePerKg;
    private final BigDecimal startDiscount;
    private final BigDecimal discountStep;

    public CementPromoService(BigDecimal pricePerKg, BigDecimal startDiscount, BigDecimal discountStep) {
        this.pricePerKg = pricePerKg;
        this.startDiscount = startDiscount;
        this.discountStep = discountStep;
    }

    public Map<String, BigDecimal> calculate(List<Order> allOrders) {
        List<Order> sortedOrders = allOrders.stream()
                .sorted((o1, o2) -> o1.time().compareTo(o2.time()))
                .toList();
        return IntStream.range(0, sortedOrders.size())
                .mapToObj(i -> {
                    Order order = sortedOrders.get(i);

                    BigDecimal currentDiscount = getDiscountByIndex(i);

                    BigDecimal finalCost = calculateCost(order.kg(), currentDiscount);

                    return Map.entry(order.company(), finalCost);
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    private BigDecimal getDiscountByIndex(int index) {
        BigDecimal calculated = startDiscount
                .subtract(discountStep.multiply(BigDecimal.valueOf(index)));

        return calculated.max(BigDecimal.ZERO);
    }

    private BigDecimal calculateCost(int kg, BigDecimal discount) {
        BigDecimal basePrice = pricePerKg.multiply(BigDecimal.valueOf(kg));
        BigDecimal multiplier = BigDecimal.ONE.subtract(discount);

        return basePrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}