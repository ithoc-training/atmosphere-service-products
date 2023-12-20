package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.Pagination;
import de.ithoc.atmosphere.service.products.model.Product;
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
@Deprecated(forRemoval = true)
public class FilterController implements FilterApi {

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


    @Override
    public ResponseEntity<Pagination> filterProducts(
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

        Page<ProductEntity> productEntityPage =
                productRepository.findByCategoryAndConditionAndPriceBetween(
                        ApiUtils.createPageable(page, size, sortBy, sortOrder),
                        categoryEntityOptional.get(), conditionOptional.get(),
                        fromPrice, toPrice);

        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();
        Pagination pagination =
                ApiUtils.createPaginationProduct(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
    }


    @Override
    public ResponseEntity<Pagination> filterProductsByCategory(
            @NotNull String category,
            Integer page, Integer size, String sortBy, String sortOrder
    ) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(category);
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ProductEntity> productEntityPage =
                productRepository.findByCategory(pageable, categoryEntityOptional.get());
        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();
        Pagination pagination = ApiUtils.createPaginationProduct(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
    }


    @Override
    public ResponseEntity<Pagination> filterProductsByCondition(
            @NotNull String condition,
            Integer page, Integer size, String sortBy, String sortOrder
    ) {

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(condition);
        if (conditionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ProductEntity> productEntityPage =
                productRepository.findByCondition(pageable, conditionOptional.get());
        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();
        Pagination pagination = ApiUtils.createPaginationProduct(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
    }


    @Override
    public ResponseEntity<Pagination> filterProductsByPriceRange(
            @NotNull BigDecimal fromPrice, @NotNull BigDecimal toPrice,
            Integer page, Integer size, String sortBy, String sortOrder
    ) {

        Pageable pageable = ApiUtils.createPageable(page, size, sortBy, sortOrder);
        Page<ProductEntity> productEntityPage = productRepository.findByPriceBetween(pageable, fromPrice, toPrice);
        List<Product> products = productEntityPage.getContent().stream()
                .map(productEntity -> modelMapper.map(productEntity, Product.class)).toList();
        Pagination pagination = ApiUtils.createPaginationProduct(page, size, sortBy, sortOrder, productEntityPage, products);

        return ResponseEntity.ok(pagination);
    }

}
