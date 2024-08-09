package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsHappyPath() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setPrice(BigDecimal.valueOf(10.00));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item2");
        item2.setPrice(BigDecimal.valueOf(20.00));

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void getItemByIdHappyPath() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setPrice(BigDecimal.valueOf(10.00));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item responseItem = response.getBody();
        assertNotNull(responseItem);
        assertEquals("Item1", responseItem.getName());
    }

    @Test
    public void getItemByIdNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameHappyPath() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("ItemName");
        item1.setPrice(BigDecimal.valueOf(10.00));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("ItemName");
        item2.setPrice(BigDecimal.valueOf(20.00));

        when(itemRepository.findByName("ItemName")).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("ItemName");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void getItemsByNameNotFound() {
        when(itemRepository.findByName("NonExistentName")).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("NonExistentName");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}