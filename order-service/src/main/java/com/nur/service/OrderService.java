package com.nur.service;

import com.nur.dto.InventoryResponse;
import com.nur.dto.OrderLineItemsDto;
import com.nur.dto.OrderRequest;
import com.nur.event.OrderPlacedEvent;
import com.nur.model.Order;
import com.nur.model.OrderLineItems;
import com.nur.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

//    public OrderService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }

    public String placeOrder(OrderRequest orderRequest){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItemsDto> orderLineItemsDtoList = orderRequest.getOrderLineItemsDtoList();
        List<OrderLineItems> orderLineItems = orderLineItemsDtoList.stream().map(this::mapToOrderLineItems).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodeList = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        //call inventory-service to check if product is in stock or not
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);

        if(allProductsInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed successfully";
        }
        else {
            throw new IllegalArgumentException("Product not in stock");
        }

    }
    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto itemDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(itemDto.getPrice());
        orderLineItems.setQuantity(itemDto.getQuantity());
        orderLineItems.setSkuCode(itemDto.getSkuCode());
        return orderLineItems;
    }
}
