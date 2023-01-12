package com.nur.service;

import com.nur.dto.InventoryResponse;
import com.nur.model.Inventory;
import com.nur.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {

        log.info("waiting started--forcefully by sleep method");
        Thread.sleep(7000);
        log.info("Waiting ended");

        List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCode);

        return inventoryList.stream().map(inventory -> InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity()>0)
                .build()).toList();
    }
}
