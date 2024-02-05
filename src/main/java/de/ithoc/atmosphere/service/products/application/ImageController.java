package de.ithoc.atmosphere.service.products.application;

import de.ithoc.atmosphere.service.products.model.pexels.search.Response;
import de.ithoc.atmosphere.service.products.repository.ItemEntity;
import de.ithoc.atmosphere.service.products.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final ItemRepository itemRepository;
    private final RestTemplate pexelsApiClient;
    private final Map<String, String> pexelsApiParams;

    public ImageController(
            ItemRepository itemRepository,
            RestTemplate pexelsApiClient, Map<String, String> pexelsApiParams) {
        this.itemRepository = itemRepository;
        this.pexelsApiClient = pexelsApiClient;
        this.pexelsApiParams = pexelsApiParams;
    }

    @PatchMapping
    public ResponseEntity<Void> patchImages() {
        log.trace("updateImage");

        int page = 0; // pagination starts with 0
        log.debug("Fetching page {}.", page + 1);
        Pageable pageable = ApiUtils.createPageable(page, 20, "name", "ASC");
        Page<ItemEntity> itemEntityPageRoot = itemRepository.findAll(pageable);
        int totalPages = itemEntityPageRoot.getTotalPages();
        log.debug("Total pages: {}", totalPages);

        for (int i = 1; i < totalPages; i++) {
            log.debug("Fetching page {}.", page + 1);
            Page<ItemEntity> itemEntityPage = itemRepository.findAll(
                    ApiUtils.createPageable(++page, 20, "name", "ASC"));

            itemEntityPage.getContent().forEach(itemEntity -> {
                String query = itemEntity.getName() + " " + itemEntity.getDescription();
                log.debug("Query: '{}'.", query);

                replacePicture(itemEntity, query);
            });
            log.debug("Fetched page {}.", page + 1);
        }
        log.debug("Images updated for all pages.");

        return ResponseEntity.status(202).build();
    }

    private void replacePicture(ItemEntity itemEntity, String query) {
        pexelsApiParams.put("query", query);
        ResponseEntity<Response> responseEntity =
                pexelsApiClient.getForEntity("", Response.class, pexelsApiParams);
        Response response = responseEntity.getBody();
        if(response != null && !response.getPhotos().isEmpty()) {
            String original = response.getPhotos().getFirst().getSrc().getTiny();
            log.debug("Image original: '{}'.", original);
            String tiny = response.getPhotos().getFirst().getSrc().getTiny();
            log.debug("Image tiny: '{}'.", tiny);

            log.debug("Replacing image {} by {}.", itemEntity.getImage(), tiny);
            itemEntity.setImage(tiny);
            itemRepository.save(itemEntity);
        }
    }

}
