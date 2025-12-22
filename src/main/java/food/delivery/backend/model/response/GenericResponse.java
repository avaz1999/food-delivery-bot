package food.delivery.backend.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import food.delivery.backend.exception.BadRequestException;
import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.utils.MessageHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Data
@NoArgsConstructor
@JsonPropertyOrder(value = {"code", "message", "timestamp", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenericResponse<T> implements Serializable {
    private static final ObjectMapper mapper = new ObjectMapper();
    private Integer code = ResponseCodes.SUCCESS.getCode();
    private String message = MessageHelper.get("success.message");
    @JsonProperty("timestamp")
    private Long timestamp = System.currentTimeMillis();
    private T data;


    public GenericResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public GenericResponse(T data, String message) {
        this.data = data;
        this.code = ResponseCodes.SUCCESS.getCode();
        this.message = message;
    }

    public GenericResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> GenericResponse<T> of(T data, String message) {
        return new GenericResponse<>(data, message);
    }

    public static <T> GenericResponse<T> of(ResponseCodes responseCode) {
        return new GenericResponse<>(responseCode.getCode(), responseCode.getMessage());
    }

    public static <T> GenericResponse<T> of(Integer code, String message) {
        return new GenericResponse<>(code, message);
    }

    public static <T> GenericResponse<T> of(Integer code, T data, String message) {
        return new GenericResponse<>(code, data, message);
    }

    public static GenericResponse<String> error(Integer code, String message) {
        return new GenericResponse<>(code, message);
    }

    public static GenericResponse<String> fail(ResponseCodes responseCodes) {
        return new GenericResponse<>(responseCodes.getCode(), responseCodes.getMessage());
    }

    public String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(ResponseCodes.FAIL);
        }
    }
}
