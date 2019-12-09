package org.fasttrackit.onlineshop.transfer;

import javax.validation.constraints.NotNull;

public class AddProductToCartRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Long costumerId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(Long costumerId) {
        this.costumerId = costumerId;
    }

    @Override
    public String toString() {
        return "AddProductToCartRequest{" +
                "productId=" + productId +
                ", costumerId=" + costumerId +
                '}';
    }
}
