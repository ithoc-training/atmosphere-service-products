package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.Pagination;
import de.ithoc.atmosphere.service.products.model.Product;
import de.ithoc.atmosphere.service.products.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/filter")
public class FilterController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ConditionRepository conditionRepository;
    private final ModelMapper modelMapper;

    public FilterController(ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            ConditionRepository conditionRepository,
                            ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.conditionRepository = conditionRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping(params = "category")
    public ResponseEntity<Pagination> filterProductsByCategory(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false, defaultValue = "price") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String category) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(category);
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ProductEntity> productEntityPage =
                productRepository.findByCategory(pageable, categoryEntityOptional.get());
        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();
        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
    }

    @GetMapping(params = {"fromPrice", "toPrice"})
    public ResponseEntity<Pagination> filterProductsByPriceRange(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false, defaultValue = "price") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) double fromPrice,
            @RequestParam(required = false) double toPrice) {

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ProductEntity> productEntityPage = productRepository.findByPriceBetween(pageable, fromPrice, toPrice);
        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();
        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
    }

    @GetMapping(params = "condition")
    public ResponseEntity<Pagination> filterProductsByCondition(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false, defaultValue = "price") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String condition) {

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(condition);
        if (conditionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ProductEntity> productEntityPage =
                productRepository.findByCondition(pageable, conditionOptional.get());
        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();
        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
    }

}
