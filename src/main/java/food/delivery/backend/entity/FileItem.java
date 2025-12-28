package food.delivery.backend.entity;

import food.delivery.backend.enums.FileType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Entity
@Table(name = "files", indexes = {
        @Index(name = "idx_hash_id", columnList = "hash_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileItem extends GenericEntity {
    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(name = "size")
    private Long size;

    @Column(name = "hash_id", unique = true, nullable = false)
    private String hashId;

    @Column(name = "content")
    private byte[] content;
}
