package com.naputami.simple_shop_api.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@Entity
@Builder
public class Customer {
    @Id
    @GeneratedValue(generator = "cuid")
    @GenericGenerator(name="cuid", strategy="com.naputami.simple_shop_api.model.CuidGenerator" )
    @Column(name = "customer_id", updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "customer_address")
    private String address;

    @Column(name = "customer_code")
    private String code;

    @Column(name = "customer_phone")
    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_order_date")
    private Timestamp lastOrderDate;

    @Column(name = "pic")
    private String pic;
}
