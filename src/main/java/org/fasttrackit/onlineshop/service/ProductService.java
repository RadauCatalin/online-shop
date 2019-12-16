package org.fasttrackit.onlineshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.ProductRespone;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    //IoC - Inversion of Control
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    //Dependency injection
    @Autowired
    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public Product createProduct(SaveProductRequest request) {
        LOGGER.info("Creating product {}", request);
        Product product = objectMapper.convertValue(request, Product.class);

        // statements below would created the same object as the objectMapper did above
//        Product product = new Product();
//        product.setDescription(request.getDescription());
//        product.setName(request.getName());
//        product.setPrice(request.getPrice());
//        product.setQuantity(request.getQuantity());
//        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product);
    }

    public Product getProduct(long id) {

        LOGGER.info("Retrevieng product {}", id);
        //using optional
        return productRepository.findById(id)
                //lambada expression
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " does not exist."));
    }

    @Transactional
    public Page<ProductRespone> getProducts(GetProductsRequest request, Pageable pageable) {
        LOGGER.info("Retrieving products: {}", request);
        Page<Product> products;

        if (request != null && request.getPartialName() != null && request.getMinQuantity() != null) {
            products = productRepository.findByNameContainingAndQuantityGreaterThanEqual(request.getPartialName(),
                    request.getMinQuantity(), pageable);
        } else if (request != null && request.getPartialName() != null) {
            products = productRepository.findByNameContaining(request.getPartialName(), pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        List<ProductRespone> productRespones = new ArrayList<>();

        for (Product product : products.getContent()) {
            ProductRespone productRespone = new ProductRespone();
            productRespone.setId(product.getId());
            productRespone.setName(product.getName());
            productRespone.setDescription(product.getDescription());
            productRespone.setPrice(product.getPrice());
            productRespone.setQuantity(product.getQuantity());
            productRespone.setImageUrl(product.getImageUrl());

            productRespones.add(productRespone);
        }
        return new PageImpl<>(productRespones, pageable, products.getTotalElements());
    }

    public Product updateProduct(long id, SaveProductRequest request) {
        LOGGER.info("Updating product {}: {}", id, request);
        Product product = getProduct(id);
        BeanUtils.copyProperties(request, product);

        return productRepository.save(product);
    }

    public void deleteProduct(long id) {
        LOGGER.info("Deleting product {}", id);
        productRepository.deleteById(id);
        LOGGER.info("Deleted product {}", id);
    }
}