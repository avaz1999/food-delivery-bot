package food.delivery.backend.repository;

import food.delivery.backend.entity.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Avaz Absamatov
 * Date: 12/27/2025
 */
public interface FileRepository extends JpaRepository<FileItem, Long> {
}
