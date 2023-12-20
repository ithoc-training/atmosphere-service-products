package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.*;
import de.ithoc.atmosphere.service.products.model.Error;
import de.ithoc.atmosphere.service.products.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*")
public class ItemController implements ItemsApi {

    private final CategoryRepository categoryRepository;
    private final ConditionRepository conditionRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    public ItemController(
            CategoryRepository categoryRepository, ConditionRepository conditionRepository, ItemRepository itemRepository,
            ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.conditionRepository = conditionRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<PostResponse> createItem(Item item) {

        List<Error> errors = new ArrayList<>();

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(item.getCategory().getName());
        if (categoryEntityOptional.isEmpty()) {
            String message = String.format("Category with name '%s' not found", item.getCategory().getName());
            message += String.format(" (available categories: %s)", List.of(CategoryEnum.values()));
            errors.add(new Error().message(message));
        }

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(item.getCondition().getName());
        if (conditionOptional.isEmpty()) {
            String message = String.format("Condition with name '%s' not found", item.getCondition().getName());
            message += String.format(" (available conditions: %s)", List.of(CondititionEnum.values()));
            errors.add(new Error().message(message));
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(400).body(new PostResponse().errors(errors));
        }

        ItemEntity itemEntity = modelMapper.map(item, ItemEntity.class);
        itemEntity.setCategory(categoryEntityOptional.get());
        itemEntity.setCondition(conditionOptional.get());
        itemRepository.save(itemEntity);

        return ResponseEntity.status(201).body(new PostResponse().errors(List.of()));
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
