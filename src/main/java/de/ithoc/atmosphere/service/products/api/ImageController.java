package de.ithoc.atmosphere.service.products.api;

import de.ithoc.atmosphere.service.products.model.serpapi.SearchResponse;
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
    private final RestTemplate serpApiClient;
    private final Map<String, String> serpApiParams;

    public ImageController(ItemRepository itemRepository, RestTemplate serpApiClient, Map<String, String> serpApiParams) {
        this.itemRepository = itemRepository;
        this.serpApiClient = serpApiClient;
        this.serpApiParams = serpApiParams;
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

                serpApiParams.put("q", query);
                ResponseEntity<SearchResponse> responseEntity =
                        serpApiClient.getForEntity("", SearchResponse.class, serpApiParams);
                SearchResponse searchResponse = responseEntity.getBody();
                if(searchResponse != null && !searchResponse.getImagesResults().isEmpty()) {
                    // Pick first image for convenience for now
                    String original = searchResponse.getImagesResults().getFirst().getOriginal();
                    log.debug("Image original: '{}'.", original);
                    String thumbnail = searchResponse.getImagesResults().getFirst().getThumbnail();
                    log.debug("Image thumbnail: '{}'.", thumbnail);

                    log.debug("Replacing image {} by {}.", itemEntity.getImage(), thumbnail);
                    itemEntity.setImage(thumbnail);
                    itemRepository.save(itemEntity);
                }
            });
            log.debug("Fetched page {}.", page + 1);
        }
        log.debug("Images updated for all pages.");

        return ResponseEntity.status(202).build();
    }

}
