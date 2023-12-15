package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.Item;
import de.ithoc.atmosphere.service.products.model.Pagination;
import de.ithoc.atmosphere.service.products.repository.*;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class FilterController implements FilterApi {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ConditionRepository conditionRepository;
    private final ModelMapper modelMapper;


    public FilterController(ItemRepository itemRepository,
                            CategoryRepository categoryRepository,
                            ConditionRepository conditionRepository,
                            ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.conditionRepository = conditionRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<Pagination> filterItems(
            @NotNull String category, @NotNull String condition,
            @NotNull BigDecimal fromPrice, @NotNull BigDecimal toPrice,
            Integer page, Integer size, String sortBy, String sortOrder
    ) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(category);
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(condition);
        if (conditionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<ItemEntity> productEntityPage =
                itemRepository.findByCategoryAndConditionAndPriceBetween(
                        ApiUtils.createPageable(page, size, sortBy, sortOrder),
                        categoryEntityOptional.get(), conditionOptional.get(),
                        fromPrice, toPrice);

        List<Item> items = productEntityPage.getContent().stream()
                .map(itemEntity -> modelMapper.map(itemEntity, Item.class)).toList();
        Pagination pagination =
                ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, items);

        return ResponseEntity.ok(pagination);
    }


    @Override
    public ResponseEntity<Pagination> filterItemsByCategory(
            @NotNull String category,
            Integer page, Integer size, String sortBy, String sortOrder
    ) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(category);
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ItemEntity> productEntityPage =
                itemRepository.findByCategory(pageable, categoryEntityOptional.get());
        List<Item> items = productEntityPage.getContent().stream()
                .map(itemEntity -> modelMapper.map(itemEntity, Item.class)).toList();
        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, items);

        return ResponseEntity.ok(pagination);
    }


    @Override
    public ResponseEntity<Pagination> filterItemsByCondition(
            @NotNull String condition,
            Integer page, Integer size, String sortBy, String sortOrder
    ) {

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(condition);
        if (conditionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ItemEntity> productEntityPage =
                itemRepository.findByCondition(pageable, conditionOptional.get());
        List<Item> items = productEntityPage.getContent().stream()
                .map(itemEntity -> modelMapper.map(itemEntity, Item.class)).toList();
        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, items);

        return ResponseEntity.ok(pagination);
    }


    @Override
    public ResponseEntity<Pagination> filterItemsByPriceRange(
            @NotNull BigDecimal fromPrice, @NotNull BigDecimal toPrice,
            Integer page, Integer size, String sortBy, String sortOrder
    ) {

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ItemEntity> productEntityPage = itemRepository.findByPriceBetween(pageable, fromPrice, toPrice);
        List<Item> items = productEntityPage.getContent().stream()
                .map(itemEntity -> modelMapper.map(itemEntity, Item.class)).toList();
        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, items);

        return ResponseEntity.ok(pagination);
    }

}
