package org.fasttrackit.onlineshop.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}
