package food.delivery.backend.controller;

import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.model.request.CategoryCreateRequest;
import food.delivery.backend.model.response.GenericResponse;
import food.delivery.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static food.delivery.backend.utils.ApiConstants.BASE_URL;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Slf4j
@RestController
@RequestMapping(BASE_URL + "categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;


    @PostMapping("create")
    @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN', 'KITCHEN_MANAGER')")
    public GenericResponse<ResponseCodes> create(@RequestBody CategoryCreateRequest request){
        return GenericResponse.of(service.create(request));
    }

}
