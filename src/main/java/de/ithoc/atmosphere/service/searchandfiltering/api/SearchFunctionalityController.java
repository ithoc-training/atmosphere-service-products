package de.ithoc.atmosphere.service.searchandfiltering.api;

import de.ithoc.atmosphere.service.searchandfiltering.model.Product;
import de.ithoc.atmosphere.service.searchandfiltering.repository.CategoryEntity;
import de.ithoc.atmosphere.service.searchandfiltering.repository.CategoryRepository;
import de.ithoc.atmosphere.service.searchandfiltering.repository.ProductEntity;
import de.ithoc.atmosphere.service.searchandfiltering.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/search")
public class SearchFunctionalityController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public SearchFunctionalityController(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        List<ProductEntity> productEntities = productRepository.findAll();

        List<Product> products = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class))
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping(params = "search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String search) {

        List<ProductEntity> productEntities =
                productRepository.findByNameContainingOrDescriptionContaining(search, search);

        List<Product> products = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class))
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {

        UUID uuid = UUID.fromString(id);
        ProductEntity productEntity = productRepository.findById(uuid).orElse(null);
        if (productEntity == null) {
            return ResponseEntity.notFound().build();
        }

        Product product = modelMapper.map(productEntity, Product.class);

        return ResponseEntity.ok(product);
    }

    @GetMapping(params = "category")
    public ResponseEntity<List<Product>> searchProductsByCategory(@RequestParam String category) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(category);
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ProductEntity> productEntities = productRepository.findByCategory(categoryEntityOptional.get());

        List<Product> products = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class))
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping(params = {"fromPrice", "toPrice"})
    public ResponseEntity<List<Product>> searchProductsByPriceRange(
            @RequestParam double fromPrice, @RequestParam double toPrice) {

        List<ProductEntity> productEntities = productRepository.findByPriceBetween(fromPrice, toPrice);

        List<Product> products = productEntities.stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class))
                .toList();

        return ResponseEntity.ok(products);
    }

}
