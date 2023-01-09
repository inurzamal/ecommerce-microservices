package com.nur.service;

import com.nur.dto.InventoryResponse;
import com.nur.model.Inventory;
import com.nur.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){

        List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCode);

        return inventoryList.stream().map(inventory -> InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity()>0)
                .build()).toList();
    }
}
