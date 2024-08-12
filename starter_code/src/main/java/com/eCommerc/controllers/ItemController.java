package com.eCommerc.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eCommerc.model.persistence.Item;
import com.eCommerc.model.persistence.repositories.ItemRepository;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private static final Logger logger = LogManager.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		logger.info("Received request to get all items");
		List<Item> items = itemRepository.findAll();
		logger.info("Returning {} items", items.size());
		return ResponseEntity.ok(items);
	}

	@GetMapping("{id}/")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		logger.info("Received request to get item with id: {}", id);
		return ResponseEntity.of(itemRepository.findById(id));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		logger.info("Received request to get items with name: {}", name);
		List<Item> items = itemRepository.findByName(name);
		if (items == null || items.isEmpty()) {
			logger.warn("No items found with name: {}", name);
			return ResponseEntity.notFound().build();
		}
		logger.info("Returning {} items with name: {}", items.size(), name);
		return ResponseEntity.ok(items);
	}
}