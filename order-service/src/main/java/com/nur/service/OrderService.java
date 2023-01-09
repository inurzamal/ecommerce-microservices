package com.nur.service;

import com.nur.dto.OrderLineItemsDto;
import com.nur.dto.OrderRequest;
import com.nur.model.Order;
import com.nur.model.OrderLineItems;
import com.nur.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

//    public OrderService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }

    public void placeOrder(OrderRequest orderRequest){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItemsDto> orderLineItemsDtoList = orderRequest.getOrderLineItemsDtoList();
        List<OrderLineItems> orderLineItems = orderLineItemsDtoList.stream().map(this::mapToOrderLineItems).toList();
        order.setOrderLineItemsList(orderLineItems);

        orderRepository.save(order);
    }
    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto itemDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(itemDto.getPrice());
        orderLineItems.setQuantity(itemDto.getQuantity());
        orderLineItems.setSkuCode(itemDto.getSkuCode());
        return orderLineItems;
    }
}
