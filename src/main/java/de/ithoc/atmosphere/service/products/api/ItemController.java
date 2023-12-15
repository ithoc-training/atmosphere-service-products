package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.Item;
import de.ithoc.atmosphere.service.products.model.Pagination;
import de.ithoc.atmosphere.service.products.repository.ItemEntity;
import de.ithoc.atmosphere.service.products.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*")
public class ItemController implements ItemsApi {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    public ItemController(
            ItemRepository itemRepository,
            ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<Void> createItem(Item item) {

        ItemEntity itemEntity = modelMapper.map(item, ItemEntity.class);
        itemRepository.save(itemEntity);

        return ResponseEntity.status(201).build();
    }


    @Override
    public ResponseEntity<Pagination> getItems(
            @RequestParam(value = "searchcriteria", required = false) String searchcriteria,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "price") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") String sortOrder
    ) {

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);

        Page<ItemEntity> itemEntityPage;
        if (searchcriteria == null || searchcriteria.isEmpty()) {
            itemEntityPage = itemRepository.findAll(pageable);
        } else {
            itemEntityPage = itemRepository.findByNameContainingOrDescriptionContaining(
                    pageable, searchcriteria, searchcriteria);
        }

        List<Item> items = itemEntityPage.getContent().stream()
                .map(itemEntity -> modelMapper.map(itemEntity, Item.class)).toList();

        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, itemEntityPage, items);

        return ResponseEntity.ok(pagination);
    }


    @Override
    public ResponseEntity<Item> getItemById(String id) {

        UUID uuid = UUID.fromString(id);
        ItemEntity itemEntity = itemRepository.findById(uuid).orElse(null);
        if (itemEntity == null) {
            return ResponseEntity.notFound().build();
        }

        Item item = modelMapper.map(itemEntity, Item.class);

        return ResponseEntity.ok(item);
    }

}
