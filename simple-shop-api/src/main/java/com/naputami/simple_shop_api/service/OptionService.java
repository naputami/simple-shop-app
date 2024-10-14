package com.naputami.simple_shop_api.service;

import org.springframework.stereotype.Service;

import com.naputami.simple_shop_api.dto.response.OptionItemDTO;
import com.naputami.simple_shop_api.dto.response.OptionItemListDTO;
import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.repository.CustomerRepository;
import com.naputami.simple_shop_api.repository.ItemRespository;
import com.naputami.simple_shop_api.model.Customer;
import com.naputami.simple_shop_api.model.Item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OptionService {
    private final CustomerRepository customerRepository;
    private final ItemRespository itemRespository;
    private final MessageSource messageSource;


    public ResponseEntity<StandardResponseDTO> getCustomerOptions(){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.load", new Object[]{"customer"}, null);

        try {
            List<Customer> customers = customerRepository.findActiveCustomers();
            List<OptionItemDTO> data = new ArrayList<>();

            for(Customer item: customers){
                OptionItemDTO itemDTO = OptionItemDTO.builder()
                                                     .id(item.getId())
                                                     .name(item.getName())
                                                     .build();
                data.add(itemDTO);
            }

            result.setCode(status.value());
            result.setMessage(message);
            result.setStatus(status.getReasonPhrase());
            result.setData(data);
            return ResponseEntity.ok().body(result);


        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message= messageSource.getMessage("api.error.server", null, null);

            result.setCode(status.value());
            result.setStatus(status.getReasonPhrase());
            result.setMessage(message);

            return ResponseEntity.internalServerError().body(result);
        }


    }

    public ResponseEntity<StandardResponseDTO> getItemOptions(){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.load", new Object[]{"item"}, null);

        try {
            List<Item> items = itemRespository.findAvailableItems();
            List<OptionItemListDTO> data = new ArrayList<>();

            for(Item item: items){
               OptionItemListDTO itemDTO = OptionItemListDTO.builder()
                                                            .id(item.getId())
                                                            .name(item.getName())
                                                            .price(item.getPrice())
                                                            .build();
                data.add(itemDTO);
            }

            result.setCode(status.value());
            result.setMessage(message);
            result.setStatus(status.getReasonPhrase());
            result.setData(data);
            return ResponseEntity.ok().body(result);


        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message= messageSource.getMessage("api.error.server", null, null);

            result.setCode(status.value());
            result.setStatus(status.getReasonPhrase());
            result.setMessage(message);

            return ResponseEntity.internalServerError().body(result);
        }


    }
}
