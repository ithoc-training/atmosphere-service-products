package de.ithoc.atmosphere.service.products.aggregates.item;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the aggregate root entity of an item.
 */
public class ItemRoot {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final ItemID id;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final UserID owner;

    @Getter
    private String name;

    @Getter
    private String description;
    private PriceVO price;
    private final List<PictureVO> pictures;
    private final List<CategoryEntity> categories;

    @Getter
    private final ConditionEntity condition;

    public ItemRoot(
            String owner, String name, String description,
            BigDecimal price, String condition) {
        this.id = new ItemID();
        this.owner = new UserID(owner);
        this.name = name;
        this.description = description;
        this.price = new PriceVO(price);
        this.pictures = new ArrayList<>(); // to be uploaded later
        this.categories = new ArrayList<>(); // to be defined later
        this.condition = new ConditionEntity(condition); // supposed to be fixed (either new or used)
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changePrice(BigDecimal price) {
        this.price = new PriceVO(price);
    }

    public void uploadPicture(String filename, String link) {
        PictureVO picture = new PictureVO(filename, link);
        this.pictures.add(picture);
    }

    public void defineCategory(String categoryName) {
        CategoryEntity category = new CategoryEntity(categoryName);
        this.categories.add(category);
    }


    public BigDecimal getPrice() {
        return price.amount;
    }

    public List<String> getPictures() {
        return this.pictures.stream().map(PictureVO::getUrl).toList();
    }

    public List<String> getCategories() {
        return categories.stream().map(categoryEntity -> categoryEntity.name).toList();
    }

}
