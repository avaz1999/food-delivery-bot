package food.delivery.backend.entity;

import food.delivery.backend.enums.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Entity
@Table(name = "files", indexes = {
        @Index(name = "idx_file_id", columnList = "file_id"),
        @Index(name = "idx_hash_id", columnList = "hash_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileItem extends GenericEntity {
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "file_unique_id")
    private String fileUniqueId;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(name = "size")
    private Long size;

    @Column(name = "hash_id", unique = true, nullable = false)
    private String hashId;
}
