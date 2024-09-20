package com.naputami.simple_shop_api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
@Entity
@Builder
public class Item {
    @Id
    @GeneratedValue(generator = "cuid")
    @GenericGenerator(name="cuid", strategy="com.naputami.simple_shop_api.model.CuidGenerator" )
    @Column(name = "item_id", updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_code")
    private String code;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price", columnDefinition = "Decimal(10,2)")
    private Double price;

    @Column(name = "last_restock")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastRestock;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "description", columnDefinition = "TEXT")
    private String desc;

    @Column(name= "pic")
    private String pic;
}
