package de.ithoc.atmosphere.service.searchandfiltering.api;

import de.ithoc.atmosphere.service.searchandfiltering.repository.CategoryEntity;
import de.ithoc.atmosphere.service.searchandfiltering.repository.CategoryRepository;
import de.ithoc.atmosphere.service.searchandfiltering.repository.ProductEntity;
import de.ithoc.atmosphere.service.searchandfiltering.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/search")
public class SearchFunctionalityController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public SearchFunctionalityController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<Page<ProductEntity>> getAllProducts(Pageable pageable) {

        Page<ProductEntity> productEntityPage = productRepository.findAll(pageable);

        return ResponseEntity.ok(productEntityPage);
    }

    @GetMapping(params = "search")
    public ResponseEntity<Page<ProductEntity>> searchProducts(Pageable pageable, @RequestParam String search) {

        Page<ProductEntity> productEntityPage =
                productRepository.findByNameContainingOrDescriptionContaining(pageable, search, search);

        return ResponseEntity.ok(productEntityPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable String id) {

        UUID uuid = UUID.fromString(id);
        ProductEntity productEntity = productRepository.findById(uuid).orElse(null);
        if (productEntity == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productEntity);
    }

    @GetMapping(params = "category")
    public ResponseEntity<Page<ProductEntity>> searchProductsByCategory(Pageable pageable, @RequestParam String category) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(category);
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<ProductEntity> productEntityPage = productRepository.findByCategory(pageable, categoryEntityOptional.get());

        return ResponseEntity.ok(productEntityPage);
    }

    @GetMapping(params = {"fromPrice", "toPrice"})
    public ResponseEntity<Page<ProductEntity>> searchProductsByPriceRange(
            Pageable pageable, @RequestParam double fromPrice, @RequestParam double toPrice) {

        Page<ProductEntity> productEntityPage = productRepository.findByPriceBetween(pageable, fromPrice, toPrice);

        return ResponseEntity.ok(productEntityPage);
    }

}
