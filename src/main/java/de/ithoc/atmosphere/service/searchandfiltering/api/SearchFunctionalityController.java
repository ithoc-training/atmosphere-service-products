package de.ithoc.atmosphere.service.searchandfiltering.api;

import de.ithoc.atmosphere.service.searchandfiltering.model.Product;
import de.ithoc.atmosphere.service.searchandfiltering.repository.ProductEntity;
import de.ithoc.atmosphere.service.searchandfiltering.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/search")
public class SearchFunctionalityController {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public SearchFunctionalityController(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
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
}
