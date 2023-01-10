package com.nur.controller;

import com.nur.dto.InventoryResponse;
import com.nur.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // @GetMapping("/{sku-code},{sku-code}")
    // public boolean isInStock(@PathVariable("sku-code") String skuCode){}
    //http://localhost:8082/api/inventory/hp-pavilion,hp-pavilion-red // multiple value using @PathVariable

    //http://localhost:8082/api/inventory?skuCode=hp-pavilion&skuCode=hp-pavilion-red //using @RequestParam (it uses query parameter)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }

}
