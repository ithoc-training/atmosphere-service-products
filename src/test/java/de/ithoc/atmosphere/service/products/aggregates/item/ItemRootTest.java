package de.ithoc.atmosphere.service.products.aggregates.item;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemRootTest {

    private ItemRoot createItemRoot() {
        return new ItemRoot(
                "Owner",
                "Name",
                "Description",
                BigDecimal.valueOf(13.37),
                "New"
        );
    }


    @Test
    void changeName() {
        // Given
        ItemRoot itemRoot = createItemRoot();

        // When
        itemRoot.changeName("New Name");

        // Then
        assertEquals("New Name", itemRoot.getName());
    }


    @Test
    void changeDescription() {
        // Given
        ItemRoot itemRoot = createItemRoot();

        // When
        itemRoot.changeDescription("New Description");

        // Then
        assertEquals("New Description", itemRoot.getDescription());
    }


    @Test
    void changePrice() {
        // Given
        ItemRoot itemRoot = createItemRoot();

        // When
        itemRoot.changePrice(BigDecimal.valueOf(42.42));

        // Then
        assertEquals(BigDecimal.valueOf(42.42), itemRoot.getPrice());
    }


    @Test
    void uploadPicture() {
        // Given
        ItemRoot itemRoot = createItemRoot();

        // When
        itemRoot.uploadPicture("Filename", "Link");

        // Then
        assertEquals(1, itemRoot.getPictures().size());
        //noinspection SequencedCollectionMethodCanBeUsed
        assertEquals("Filename/Link", itemRoot.getPictures().get(0));
    }


    @Test
    void defineCategory() {
        // Given
        ItemRoot itemRoot = createItemRoot();

        // When
        itemRoot.defineCategory("Category");

        // Then
        assertEquals(1, itemRoot.getCategories().size());
        //noinspection SequencedCollectionMethodCanBeUsed
        assertEquals("Category", itemRoot.getCategories().get(0));
    }

}