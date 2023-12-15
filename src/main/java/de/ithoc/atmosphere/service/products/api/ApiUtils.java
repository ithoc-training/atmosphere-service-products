package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.Item;
import de.ithoc.atmosphere.service.products.model.Pagination;
import de.ithoc.atmosphere.service.products.model.Product;
import de.ithoc.atmosphere.service.products.repository.ItemEntity;
import de.ithoc.atmosphere.service.products.repository.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


public class ApiUtils {


    public static Pagination createPaginationProduct(
            Integer page, Integer size, String sortBy, String sortOrder,
            Page<ProductEntity> productEntityPage, List<Product> products) {

        Pagination pagination = new Pagination();

        pagination.setContent(products);
        pagination.setPageNumber(page);
        pagination.setPageSize(size);
        pagination.setTotalElements(productEntityPage.getTotalElements());
        pagination.setTotalPages(productEntityPage.getTotalPages());
        pagination.setSortBy(sortBy);
        pagination.setSortOrder(sortOrder);

        return pagination;
    }


    public static Pageable createPageable(Integer page, Integer size, String sortBy, String sortOrder) {

        Sort sort = Sort.by(sortBy);
        if("DESC".equals(sortOrder)) {
            sort = sort.descending();
        }

        return PageRequest.of(page, size, sort);
    }


    public static Pagination createPagination(
            Integer page, Integer size, String sortBy, String sortOrder,
            Page<ItemEntity> productEntityPage,
            List<Item> items) {

        Pagination pagination = new Pagination();

        pagination.setContent(items);
        pagination.setPageNumber(page);
        pagination.setPageSize(size);
        pagination.setTotalElements(productEntityPage.getTotalElements());
        pagination.setTotalPages(productEntityPage.getTotalPages());
        pagination.setSortBy(sortBy);
        pagination.setSortOrder(sortOrder);

        return pagination;
    }

}
