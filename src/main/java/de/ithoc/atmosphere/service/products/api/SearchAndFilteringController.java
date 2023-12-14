package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class SearchAndFilteringController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ConditionRepository conditionRepository;

    public SearchAndFilteringController(ProductRepository productRepository, CategoryRepository categoryRepository, ConditionRepository conditionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.conditionRepository = conditionRepository;
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Page<ProductEntity>> getAllProducts(Pageable pageable) {

        Page<ProductEntity> productEntityPage = productRepository.findAll(pageable);

        return ResponseEntity.ok(productEntityPage);
    }

    @GetMapping(value = "/search", params = "search")
    public ResponseEntity<Page<ProductEntity>> searchProducts(Pageable pageable, @RequestParam String search) {

        Page<ProductEntity> productEntityPage =
                productRepository.findByNameContainingOrDescriptionContaining(pageable, search, search);

        return ResponseEntity.ok(productEntityPage);
    }

    @GetMapping(value = "/search/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable String id) {

        UUID uuid = UUID.fromString(id);
        ProductEntity productEntity = productRepository.findById(uuid).orElse(null);
        if (productEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productEntity);
    }

    @GetMapping(value = "/filter", params = "category")
    public ResponseEntity<Page<ProductEntity>> searchProductsByCategory(
            Pageable pageable, @RequestParam String category) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(category);
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<ProductEntity> productEntityPage =
                productRepository.findByCategory(pageable, categoryEntityOptional.get());

        return ResponseEntity.ok(productEntityPage);
    }

    @GetMapping(value = "/filter", params = {"fromPrice", "toPrice"})
    public ResponseEntity<Page<ProductEntity>> searchProductsByPriceRange(
            Pageable pageable, @RequestParam double fromPrice, @RequestParam double toPrice) {

        Page<ProductEntity> productEntityPage = productRepository.findByPriceBetween(pageable, fromPrice, toPrice);

        return ResponseEntity.ok(productEntityPage);
    }

    @GetMapping(value = "/filter", params = "condition")
    public ResponseEntity<Page<ProductEntity>> filterProductsByCondition(
            Pageable pageable, @RequestParam String condition) {

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(condition);
        if (conditionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Page<ProductEntity> productEntityPage =
                productRepository.findByCondition(pageable, conditionOptional.get());

        return ResponseEntity.ok(productEntityPage);
    }

}
