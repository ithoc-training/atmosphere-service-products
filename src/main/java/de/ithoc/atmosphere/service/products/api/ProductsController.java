package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.Product;
import de.ithoc.atmosphere.service.products.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class ProductsController implements ProductsApi {

    private final CategoryRepository categoryRepository;
    private final ConditionRepository conditionRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public ProductsController(
            CategoryRepository categoryRepository, ConditionRepository conditionRepository,
            ProductRepository productRepository, ModelMapper modelMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.conditionRepository = conditionRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<Void> createProduct(Product product) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(product.getCategory().getName());
        if (categoryEntityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(product.getCondition().getName());
        if (conditionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        productEntity.setCategory(categoryEntityOptional.get());
        productEntity.setCondition(conditionOptional.get());
        productRepository.save(productEntity);

        return ResponseEntity.status(201).build();
    }

}
