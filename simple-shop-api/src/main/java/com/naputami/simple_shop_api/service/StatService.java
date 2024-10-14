package com.naputami.simple_shop_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.dto.response.StatDTO;
import com.naputami.simple_shop_api.repository.CustomerRepository;
import com.naputami.simple_shop_api.repository.ItemRespository;
import com.naputami.simple_shop_api.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatService {
    private final CustomerRepository customerRepository;
    private final ItemRespository itemRespository;
    private final OrderRepository orderRepository;
    private final MessageSource messageSource;

    public ResponseEntity<StandardResponseDTO> getStatInfo(){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.load", new Object[]{"statistik"}, null);

        try {
            Long totalCustomer = customerRepository.count();
            Long totalItem = itemRespository.count();
            Long totalOrder = orderRepository.count();

            StatDTO data = StatDTO.builder()
                                  .totalCustomer(totalCustomer)
                                  .totalItem(totalItem)
                                  .totalOrder(totalOrder)
                                  .build();

            result.setCode(status.value());
            result.setStatus(status.getReasonPhrase());
            result.setMessage(message);
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
