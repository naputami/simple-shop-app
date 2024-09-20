package com.naputami.simple_shop_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.naputami.simple_shop_api.dto.request.ItemFormDTO;
import com.naputami.simple_shop_api.dto.response.ItemDetailDTO;
import com.naputami.simple_shop_api.dto.response.ItemListItemDTO;
import com.naputami.simple_shop_api.dto.response.PaginatedResponseDTO;
import com.naputami.simple_shop_api.dto.response.StandardResponseDTO;
import com.naputami.simple_shop_api.model.Item;
import com.naputami.simple_shop_api.repository.ItemRespository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemService {
    private final MinioService minioService;
    private final CodeGeneratorService codeGeneratorService;
    private final MessageSource messageSource;
    private final ItemRespository itemRespository;

    @Transactional
    public ResponseEntity<StandardResponseDTO> addNewItem(ItemFormDTO request){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.add", new Object[]{"item"}, null);
        String savedFileName = null;
        Boolean isAvailable = true;

         try {

            if(request.getImgFile() != null){
                savedFileName = minioService.uploadFile(request.getImgFile(), request.getName());
            }

            if(request.getStock() == 0){
                isAvailable = false;
            }

            Item newItem = Item.builder()
                                .name(request.getName())
                                .code(codeGeneratorService.generateItemCode())
                                .price(request.getPrice())
                                .stock(request.getStock())
                                .pic(savedFileName)
                                .desc(request.getDesc())
                                .lastRestock(request.getLastRestockDate())
                                .isAvailable(isAvailable)
                                .build();
            
            itemRespository.save(newItem);
            
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
    public ResponseEntity<StandardResponseDTO> editItem(ItemFormDTO request, String id) {
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.edit", new Object[]{"item", id}, null);
        
        try {

            Optional<Item> foundItem = itemRespository.findById(id);

            if(foundItem.isEmpty()){
                status = HttpStatus.NOT_FOUND;
                message = messageSource.getMessage("api.error.not-found", new Object[]{"item", id}, null);
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
                
            }

            Item editedItem = foundItem.get();

            String imageFileName = editedItem.getPic();
            Boolean isAvailable = editedItem.getIsAvailable();

            if(request.getImgFile() != null){
                minioService.deleteFile(imageFileName);
                imageFileName = minioService.uploadFile(request.getImgFile(), request.getName());
            }

            if(request.getStock() != editedItem.getStock() && request.getStock() == 0){
                isAvailable = false;
            }

            editedItem.setName(request.getName());
            editedItem.setPrice(request.getPrice());
            editedItem.setStock(request.getStock());
            editedItem.setDesc(request.getDesc());
            editedItem.setLastRestock(request.getLastRestockDate());
            editedItem.setIsAvailable(isAvailable);
            editedItem.setPic(imageFileName);

            itemRespository.save(editedItem);
            
            
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
    public ResponseEntity<StandardResponseDTO> deleteItem(String id){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.delete", new Object[]{"item", id}, null);


        try {

            Optional<Item> foundItem = itemRespository.findById(id);

            if(foundItem.isEmpty()){
                status = HttpStatus.NOT_FOUND;
                message = messageSource.getMessage("api.error.not-found", new Object[]{"item", id}, null);
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
                
            }

            Item choosenItem = foundItem.get();
            if(choosenItem.getPic() != null){
                minioService.deleteFile(choosenItem.getPic());
            }

            itemRespository.delete(choosenItem);
          
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


    public ResponseEntity<StandardResponseDTO> getItemDetail(String id){
        StandardResponseDTO result = new StandardResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.load", new Object[]{"item"}, null);
        String imageUrl = null;

        try {

            Optional<Item> foundItem = itemRespository.findById(id);

            if(foundItem.isEmpty()){
                status = HttpStatus.NOT_FOUND;
                message = messageSource.getMessage("api.error.not-found", new Object[]{"item", id}, null);
                result.setCode(status.value());
                result.setStatus(status.name());
                result.setMessage(message);
    
                return ResponseEntity.status(status.value()).body(result);
                
            }

            Item choosenItem = foundItem.get();
            if(choosenItem.getPic() != null){
                imageUrl = minioService.getFilePublicUrl(choosenItem.getPic());
            }

            ItemDetailDTO itemData = ItemDetailDTO.builder()
                                                  .name(choosenItem.getName())
                                                  .itemCode(choosenItem.getCode())
                                                  .price(choosenItem.getPrice())
                                                  .stock(choosenItem.getStock())
                                                  .desc(choosenItem.getDesc())
                                                  .lastRestockDate(choosenItem.getLastRestock())
                                                  .isAvailable(choosenItem.getIsAvailable())
                                                  .imgUrl(imageUrl)
                                                  .build();
           
            
            result.setCode(status.value());
            result.setMessage(message);
            result.setStatus(status.name());
            result.setData(itemData);
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

     public ResponseEntity<PaginatedResponseDTO> getAllItems(Specification<Item> spec, Pageable page){
        PaginatedResponseDTO result = new PaginatedResponseDTO();
        HttpStatus status = HttpStatus.OK;
        String message = messageSource.getMessage("api.success.load", new Object[]{"item"}, null);

        try {

        Page<Item> itemPage = itemRespository.findAll(spec, page);

        List<Item> itemPageContent = itemPage.getContent();

        List<ItemListItemDTO> items = new ArrayList<>();

        for (Item item : itemPageContent) {

            ItemListItemDTO itemResult = ItemListItemDTO.builder()
                                                        .id(item.getId())
                                                        .name(item.getName())
                                                        .code(item.getCode())
                                                        .price(item.getPrice())
                                                        .stock(item.getStock())
                                                        .isAvailable(item.getIsAvailable())
                                                        .lastRestockDate(item.getLastRestock())
                                                        .build();
            items.add(itemResult);

        }

        result.setCode(status.value());
        result.setMessage(message);
        result.setStatus(status.name());
        result.setData(items);
        result.setPageNo(itemPage.getNumber() + 1);
        result.setPageSize(itemPage.getSize());
        result.setTotalPages(itemPage.getTotalPages());
        result.setTotalElements(itemPage.getTotalElements());

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
