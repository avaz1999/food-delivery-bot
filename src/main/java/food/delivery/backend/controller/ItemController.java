package food.delivery.backend.controller;

import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.model.request.ItemCreateRequest;
import food.delivery.backend.model.response.GenericResponse;
import food.delivery.backend.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static food.delivery.backend.utils.ApiConstants.BASE_URL;

/**
 * Created by Avaz Absamatov
 * Date: 12/27/2025
 */
@Slf4j
@RestController
@RequestMapping(BASE_URL + "items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping("create")
    @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN', 'KITCHEN_MANAGER')")
    public GenericResponse<ResponseCodes> create(@ModelAttribute ItemCreateRequest request) throws IOException {
        return GenericResponse.of(service.create(request));
    }
}
