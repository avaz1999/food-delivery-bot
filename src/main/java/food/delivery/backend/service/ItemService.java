package food.delivery.backend.service;

import food.delivery.backend.entity.Category;
import food.delivery.backend.entity.FileItem;
import food.delivery.backend.entity.Item;
import food.delivery.backend.enums.FoodStatus;
import food.delivery.backend.enums.Language;
import food.delivery.backend.exception.BadRequestException;
import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.model.dto.ItemDTO;
import food.delivery.backend.model.request.ItemCreateRequest;
import food.delivery.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 12/27/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;
    private final CategoryService categoryService;
    private final FileService fileService;

    public ResponseCodes create(ItemCreateRequest request) throws IOException {
        if (request.getImage() == null || request.getImage().isEmpty())
            throw new BadRequestException(ResponseCodes.IMAGE_CANNOT_BE_NULL);

        if (repository.existsByNameUz(request.getNameUz()))
            throw new BadRequestException(ResponseCodes.ITEM_ALREADY_EXISTS);

        if (repository.existsByNameRu(request.getNameRu()))
            throw new BadRequestException(ResponseCodes.ITEM_ALREADY_EXISTS);

        Item item = buildEntity(request);

        Category one = categoryService.findOne(request.getCategoryId());
        if (one == null)
            throw new BadRequestException(ResponseCodes.CATEGORY_NOT_FOUND);
        item.setCategory(one);

        FileItem fileItem = fileService.uploadFile(request.getImage());
        item.setImage(fileItem);

        repository.save(item);
        return ResponseCodes.SUCCESS;
    }

    private static Item buildEntity(ItemCreateRequest request) {
        return Item.builder()
                .nameUz(request.getNameUz())
                .nameRu(request.getNameRu())
                .descriptionUz(request.getDescriptionUz())
                .descriptionRu(request.getDescriptionRu())
                .price(request.getPrice())
                .discountPrice(request.getDiscountPrice())
                .status(request.getStatus())
                .build();
    }

    public ItemDTO getItemByCategoryId(Long categoryId, Language language) {
        Item item = repository.findByCategory_IdAndStatus(categoryId, FoodStatus.AVAILABLE);
        if (item == null) return null;
        return buildDTO(item, language);
    }

    private ItemDTO buildDTO(Item item, Language language) {
        boolean isUzbek = Objects.equals(Language.UZ, language);
        return ItemDTO.builder()
                .id(item.getId())
                .name(isUzbek ? item.getNameUz() : item.getNameRu())
                .description(isUzbek ? item.getDescriptionUz() : item.getDescriptionRu())
                .price(item.getPrice())
                .discountPrice(item.getDiscountPrice())
                .status(item.getStatus())
                .image(item.getImage().getContent())
                .build();
    }
}
