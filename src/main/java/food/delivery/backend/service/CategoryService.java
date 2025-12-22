package food.delivery.backend.service;

import food.delivery.backend.entity.Category;
import food.delivery.backend.exception.BadRequestException;
import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.model.request.CategoryCreateRequest;
import food.delivery.backend.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    @Transactional
    public ResponseCodes create(CategoryCreateRequest request) {
        if (repository.existsByNameUz(request.getNameUz()))
            throw new BadRequestException(ResponseCodes.CATEGORY_ALREADY_EXISTS);

        if (repository.existsByNameRu(request.getNameRu()))
            throw new BadRequestException(ResponseCodes.CATEGORY_ALREADY_EXISTS);

        Category category = Category.builder()
                .nameUz(request.getNameUz())
                .nameRu(request.getNameRu())
                .descriptionUz(request.getDescriptionUz())
                .descriptionRu(request.getDescriptionRu())
                .build();
        if (request.getParentId() != null) {
            Category parent = repository.findById(request.getParentId()).orElse(null);
            if (parent == null) throw new BadRequestException(ResponseCodes.CATEGORY_PARENT_NOT_FOUND);
            category.setParent(parent);
        }
        repository.save(category);
        return ResponseCodes.SUCCESS;

    }
}
