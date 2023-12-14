package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.Pagination;
import de.ithoc.atmosphere.service.products.model.Product;
import de.ithoc.atmosphere.service.products.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
public class SearchController implements SearchApi {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public SearchController(
            ProductRepository productRepository,
            ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping(params = "search")
    public ResponseEntity<Pagination> getProducts(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false, defaultValue = "price") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String search) {

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);

        Page<ProductEntity> productEntityPage;
        if (search == null || search.isEmpty()) {
            productEntityPage = productRepository.findAll(pageable);
        } else {
            productEntityPage = productRepository.findByNameContainingOrDescriptionContaining(pageable, search, search);
        }

        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();

        Pagination pagination = ApiUtils.createPagination(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
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

}
