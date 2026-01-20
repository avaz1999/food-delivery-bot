package food.delivery.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 1/16/2026
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableDTO<T> implements Serializable {
    private List<T> items = new ArrayList<>();
    private long total;
}
