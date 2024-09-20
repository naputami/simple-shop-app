package com.naputami.simple_shop_api.service;

import org.springframework.stereotype.Service;

import com.naputami.simple_shop_api.dto.request.OrderFormDTO;
import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.repository.CustomerRepository;
import com.naputami.simple_shop_api.repository.ItemRespository;
import com.naputami.simple_shop_api.repository.OrderRepository;

import jakarta.transaction.Transactional;

import com.naputami.simple_shop_api.model.Item;
import com.naputami.simple_shop_api.model.Order;
import com.naputami.simple_shop_api.model.Customer;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRespository itemRespository;
    private final MessageSource messageSource;
    private final CodeGeneratorService codeGeneratorService;


    @Transactional
    public ResponseEntity<StandardResponseDTO> addNewOrder(OrderFormDTO request){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.add", new Object[]{"order"}, null);

        try {
            Optional<Item> foundItem = itemRespository.findById(request.getItemId());
            Optional<Customer> foundCustomer = customerRepository.findById(request.getCustomerId());

            if(foundItem.isEmpty() || foundCustomer.isEmpty()){
                status = HttpStatus.NOT_FOUND;
                message = "Item atau customer tidak dapat ditemukan!";
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
            }

            Item orderedItem = foundItem.get();
            Customer customer = foundCustomer.get();

            if(orderedItem.getStock() < request.getQty()){
                status = HttpStatus.BAD_REQUEST;
                message = "Quantitas yang dipesan melebihi stock yang tersedia";
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
            }

            Double totalPrice = orderedItem.getPrice() * request.getQty();

            Order newOrder = Order.builder()
                                  .code(codeGeneratorService.generateOrderCode())
                                  .customer(customer)
                                  .item(orderedItem)
                                  .totalPrice(totalPrice)
                                  .qty(request.getQty())
                                  .order_date(request.getOrderDate())
                                  .build();
            
            orderRepository.save(newOrder);

            Integer newStock = orderedItem.getStock() - request.getQty();
            Boolean isAvailable = newStock == 0? false: true;
            LocalDate updatedLastOrderDate = customer.getLastOrderDate();


            if(updatedLastOrderDate == null){
                updatedLastOrderDate = request.getOrderDate();
            } else {
                int comparison = updatedLastOrderDate.compareTo(request.getOrderDate());

                if(comparison < 0){
                    updatedLastOrderDate = request.getOrderDate();
                }
            }

            orderedItem.setStock(newStock);
            orderedItem.setIsAvailable(isAvailable);

            itemRespository.save(orderedItem);

            customer.setLastOrderDate(updatedLastOrderDate);

            customerRepository.save(customer);

            result.setCode(status.value());
            result.setMessage(message);
            result.setStatus(status.name());
            return ResponseEntity.ok().body(result);




        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message= messageSource.getMessage("api.error.server", null, null);

            result.setCode(status.value());
            result.setStatus(status.name());
            result.setMessage(message);

            return ResponseEntity.internalServerError().body(result);
        }
    }

}
