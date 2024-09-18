package com.naputami.simple_shop_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.naputami.simple_shop_api.dto.request.CustomerFormDTO;
import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.model.Customer;
import com.naputami.simple_shop_api.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final MinioService minioService;
    private final CodeGeneratorService codeGeneratorService;

    public ResponseEntity<StandardResponseDTO> addNewCustomer(CustomerFormDTO request) {
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = "";

        try {

            String savedFileName = minioService.uploadFile(request);
            Customer newCustomer = Customer.builder()
                                          .name(request.getName())
                                          .address(request.getAddress())
                                          .phone(request.getPhoneNumber())
                                          .code(codeGeneratorService.generateCustomerCode())
                                          .isActive(true)
                                          .pic(savedFileName)
                                          .build();

            customerRepository.save(newCustomer);
            
            result.setCode(status.value());
            result.setMessage("Berhasil menyimpan customer baru");
            result.setStatus(status.name());
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message= "Tejadi kesalahan server";

            result.setCode(status.value());
            result.setStatus(status.name());
            result.setMessage(message);

            return ResponseEntity.internalServerError().body(result);

        }



    }

}
