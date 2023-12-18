package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.CategoryEnum;
import de.ithoc.atmosphere.service.products.model.Error;
import de.ithoc.atmosphere.service.products.model.PostResponse;
import de.ithoc.atmosphere.service.products.model.Product;
import de.ithoc.atmosphere.service.products.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ResponseEntity<PostResponse> createProduct(Product product) {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findByName(product.getCategory().getName());
        if (categoryEntityOptional.isEmpty()) {
            String message = String.format("Category with name '%s' not found", product.getCategory().getName());
            message += String.format(" (available categories: %s)", List.of(CategoryEnum.values()));

            return ResponseEntity.status(404).body(createResponse(message));
        }

        Optional<ConditionEntity> conditionOptional = conditionRepository.findByName(product.getCondition().getName());
        if (conditionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        productEntity.setCategory(categoryEntityOptional.get());
        productEntity.setCondition(conditionOptional.get());
        productRepository.save(productEntity);

        return ResponseEntity.status(201).body(new PostResponse().errors(List.of()));
    }


    private PostResponse createResponse(String message) {

        Error error = new Error().message(message);
        PostResponse postResponse = new PostResponse();
        postResponse.addErrorsItem(error);

        return postResponse;
    }

}
