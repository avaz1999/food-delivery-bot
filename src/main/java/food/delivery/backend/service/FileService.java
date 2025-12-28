package food.delivery.backend.service;

import food.delivery.backend.entity.FileItem;
import food.delivery.backend.enums.FileType;
import food.delivery.backend.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static food.delivery.backend.utils.HashIdGeneratorUtils.generateHashId;

/**
 * Created by Avaz Absamatov
 * Date: 12/27/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository repository;

    public FileItem uploadFile(MultipartFile file) throws IOException {
        FileItem fileItem = FileItem.builder()
                .fileName(file.getOriginalFilename())
                .fileType(FileType.IMAGE)
                .size(file.getSize())
                .hashId(generateHashId())
                .content(file.getBytes())
                .build();
        return repository.save(fileItem);

    }

}
