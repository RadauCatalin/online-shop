package org.fasttrackit.onlineshop.steps;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@Component
public class ProductSteps {
    @Autowired
    ProductService productService;

    public Product createProduct() {
        SaveProductRequest request = new SaveProductRequest();
        request.setName("Banana " + System.currentTimeMillis());
        request.setPrice(5.00);
        request.setQuantity(100);
        request.setDescription("Healty Food");

        Product createdProduct = productService.createProduct(request);

        assertThat(createdProduct, notNullValue());
        assertThat(createdProduct.getId(), notNullValue());
        assertThat(createdProduct.getId(), greaterThan(0L));
        assertThat(createdProduct.getName(), is(request.getName()));
        assertThat(createdProduct.getPrice(), is(request.getPrice()));
        assertThat(createdProduct.getQuantity(), is(request.getQuantity()));
        assertThat(createdProduct.getDescription(), is(request.getDescription()));

        return createdProduct;
    }
}
