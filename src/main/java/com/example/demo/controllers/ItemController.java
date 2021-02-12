package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.services.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@Slf4j
public class ItemController {


	private final ItemService itemService;


	public ItemController(ItemService itemService) {

		this.itemService = itemService;
	}

	@GetMapping
	public ResponseEntity<List<Item>> getItems() {

		return ResponseEntity.ok(itemService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemService.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemService.findByName(name);
		log.info("searching order for {} item ",name);
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}
	
}
