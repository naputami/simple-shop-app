package com.naputami.simple_shop_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.naputami.simple_shop_api.dto.request.CustomerFormDTO;
import com.naputami.simple_shop_api.dto.response.CustomerDetailDTO;
import com.naputami.simple_shop_api.dto.response.CustomerListItemDTO;
import com.naputami.simple_shop_api.dto.response.PaginatedResponseDTO;
import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.model.Customer;
import com.naputami.simple_shop_api.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final MinioService minioService;
    private final CodeGeneratorService codeGeneratorService;
    private final MessageSource messageSource;

    @Transactional
    public ResponseEntity<StandardResponseDTO> addNewCustomer(CustomerFormDTO request) {
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.add", new Object[]{"customer"}, null);

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

    @Transactional
    public ResponseEntity<StandardResponseDTO> editCustomer(CustomerFormDTO request, String id) {
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.edit", new Object[]{"customer", id}, null);

        try {

            Optional<Customer> foundCustomer = customerRepository.findById(id);

            if(foundCustomer.isEmpty()){
                status = HttpStatus.NOT_FOUND;
                message = messageSource.getMessage("api.error.not-found", new Object[]{"customer", id}, null);
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
                
            }

            Customer editedCustomer = foundCustomer.get();

            String imageFileName = editedCustomer.getPic();

            if(request.getImgFile() != null){
                minioService.deleteFile(imageFileName);
                imageFileName = minioService.uploadFile(request);
            }

            editedCustomer.setName(request.getName());
            editedCustomer.setAddress(request.getAddress());
            editedCustomer.setPhone(request.getPhoneNumber());
            editedCustomer.setIsActive(request.getIsActive());
            editedCustomer.setPic(imageFileName);


            customerRepository.save(editedCustomer);
            
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

    public ResponseEntity<StandardResponseDTO> getCustomerDetail(String id){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.load", new Object[]{"customer"}, null);
        String imageUrl = null;

        try {

            Optional<Customer> foundCustomer = customerRepository.findById(id);

            if(foundCustomer.isEmpty()){
                status = HttpStatus.NOT_FOUND;
                message = messageSource.getMessage("api.error.not-found", new Object[]{"customer", id}, null);
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
                
            }

            Customer choosenCustomer = foundCustomer.get();
            if(choosenCustomer.getPic() != null){
                imageUrl = minioService.getFilePublicUrl(choosenCustomer.getPic());
            }
            CustomerDetailDTO customerData = CustomerDetailDTO.builder()
                                                               .name(choosenCustomer.getName())
                                                               .address(choosenCustomer.getAddress())
                                                               .custCode(choosenCustomer.getCode())
                                                               .phoneNumber(choosenCustomer.getPhone())
                                                               .isActive(choosenCustomer.getIsActive())
                                                               .lastOrderDate(choosenCustomer.getLastOrderDate())
                                                               .imgUrl(imageUrl)
                                                               .build();

           
            
            result.setCode(status.value());
            result.setMessage(message);
            result.setStatus(status.name());
            result.setData(customerData);
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

    @Transactional
    public ResponseEntity<StandardResponseDTO> deleteCustomer(String id){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.delete", new Object[]{"customer", id}, null);


        try {

            Optional<Customer> foundCustomer = customerRepository.findById(id);

            if(foundCustomer.isEmpty()){
                status = HttpStatus.NOT_FOUND;
                message = messageSource.getMessage("api.error.not-found", new Object[]{"customer", id}, null);
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
                
            }

            Customer choosenCustomer = foundCustomer.get();
            if(choosenCustomer.getPic() != null){
                minioService.deleteFile(choosenCustomer.getPic());
            }

            customerRepository.delete(choosenCustomer);
          
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

    public ResponseEntity<PaginatedResponseDTO> getAllCustomers(Specification<Customer> spec, Pageable page){
        PaginatedResponseDTO result = new PaginatedResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.load", new Object[]{"customer"}, null);

        try {

        Page<Customer> customerPage = customerRepository.findAll(spec, page);

        List<Customer> customerPageContent = customerPage.getContent();

        List<CustomerListItemDTO> customers = new ArrayList<>();

        for (Customer item : customerPageContent) {
            String imageUrl = null;

            if(item.getPic() != null){
                imageUrl = minioService.getFilePublicUrl(item.getPic());
            }

            CustomerListItemDTO itemResult = CustomerListItemDTO.builder()
                                                                .id(item.getId())
                                                                .name(item.getName())
                                                                .address(item.getAddress())
                                                                .custCode(item.getCode())
                                                                .imgUrl(imageUrl)
                                                                .build();
            customers.add(itemResult);

        }

        result.setCode(status.value());
        result.setMessage(message);
        result.setStatus(status.name());
        result.setData(customers);
        result.setPageNo(customerPage.getNumber() + 1);
        result.setPageSize(customerPage.getSize());
        result.setTotalPages(customerPage.getTotalPages());
        result.setTotalElements(customerPage.getTotalElements());

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
