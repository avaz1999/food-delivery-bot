package food.delivery.backend.exception.header;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import food.delivery.backend.exception.BadRequestException;
import food.delivery.backend.exception.InternalServerException;
import food.delivery.backend.exception.ResponseCodes;
import food.delivery.backend.model.response.GenericResponse;
import food.delivery.backend.utils.MessageHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Avaz Absamatov
 * Date: 12/22/2025
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ======================== 400 BAD REQUEST — Validation & JSON Parse ======================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GenericResponse<String>> handleNoResourceFound(NoResourceFoundException ex) {

        log.warn("Endpoint not found: {} {}", ex.getHttpMethod(), ex.getResourcePath());

        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.NOT_FOUND.getCode(),
                "Endpoint not found"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericResponse<String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        log.warn("JSON parse error: {}", ex.getMessage());

        String message = "Invalid request data format";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            String field = ife.getPath().stream()
                    .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "[" + ref.getIndex() + "]")
                    .collect(Collectors.joining("."));

            String value = ife.getValue() != null ? ife.getValue().toString() : "null";
            String targetType = ife.getTargetType().getSimpleName();

            if (ife.getTargetType().isEnum()) {
                String validValues = Arrays.toString(ife.getTargetType().getEnumConstants());
                message = String.format("'%s' is not valid for field '%s'. Allowed values: %s", value, field, validValues);
            } else {
                message = String.format("Field '%s' must be a valid %s. Received: '%s'", field, targetType, value);
            }
        }

        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.INVALID_DATA_TYPE.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GenericResponse<String>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        String currentMethod = ex.getMethod();
        String requestUrl = request.getRequestURI();
        String query = request.getQueryString();
        if (query != null) {
            requestUrl += "?" + query;
        }

        Set<HttpMethod> supported = ex.getSupportedHttpMethods();
        String allowed = supported != null
                ? supported.stream().map(HttpMethod::name).collect(Collectors.joining(", "))
                : "NONE";

        String message = String.format(
                "%s %s is not supported for this endpoint",
                currentMethod, requestUrl
        );

        log.warn("405 Method Not Allowed → {} {}", currentMethod, requestUrl);

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header("Allow", allowed)
                .body(GenericResponse.error(
                        ResponseCodes.METHOD_NOT_ALLOWED.getCode(),
                        message
                ));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<GenericResponse<String>> handleInvalidFormat(InvalidFormatException ex) {
        log.warn("Invalid format in request: {}", ex.getMessage());

        String field = ex.getPath().stream()
                .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "[index]")
                .collect(Collectors.joining("."));

        String value = ex.getValue() != null ? ex.getValue().toString() : "null";
        String targetType = ex.getTargetType().getSimpleName();

        String message;
        if (ex.getTargetType().isEnum()) {
            String validValues = Arrays.toString(ex.getTargetType().getEnumConstants());
            message = String.format("Invalid value '%s' for '%s'. Valid values: %s", value, field, validValues);
        } else {
            message = String.format("Cannot convert '%s' to %s in field '%s'", value, targetType, field);
        }

        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.INVALID_DATA_TYPE.getCode(),
                message
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Custom BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericResponse<String>> handleBadRequestException(BadRequestException e) {
        log.warn("Bad request: {}", e.getMessage());
        String message = e.isI18n() ? e.getMessage() : MessageHelper.get(e.getMessage(), e.getArgs());

        GenericResponse<String> response = GenericResponse.of(e.getCode(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Unsupported Media Type (example, JSON  form-data )
    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ResponseEntity<GenericResponse<String>> handleUnsupportedMediaType(UnsupportedMediaTypeStatusException e) {
        log.warn("Unsupported media type: {}", e.getMessage());
        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.BAD_REQUEST.getCode(),
                "Unsupported content type. Expected application/json"
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<GenericResponse<String>> handleServerWebInputException(ServerWebInputException e) {
        log.warn("Invalid request input: {}", e.getMessage());
        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.BAD_REQUEST.getCode(),
                "Invalid request format"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /* ======================== 400/409 — Database Constraints ======================== */

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericResponse<String>> handleConstraintDataBaseViolation(ConstraintViolationException e) {
        log.error("Database constraint violation: {}", e.getMessage());
        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.CONSTRAINT_VIOLATION.getCode(),
                "Data violates database rules"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericResponse<String>> handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("Data integrity violation: {}", e.getMessage());
        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.ERROR_WHILE_SAVING.getCode(),
                "Cannot save data due to database conflict"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /* ======================== 500 INTERNAL SERVER ERROR ======================== */

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<GenericResponse<String>> handleInternalServerError(InternalServerException e) {
        log.error("Internal server error (custom): {}", e.getMessage(), e);
        String message = e.isI18n() ? e.getMessage() : MessageHelper.get(e.getMessage(), e.getArgs());

        GenericResponse<String> response = GenericResponse.of(e.getCode(), message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GenericResponse<String>> handleSQLException(SQLException e) {
        log.error("Database error: {}", e.getMessage(), e);
        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.DATABASE_ERROR.getCode(),
                "Database operation failed"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericResponse<String>> handleIllegalArgument(IllegalArgumentException e) {
        log.error("Illegal argument: {}", e.getMessage(), e);
        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.SYSTEM_ERROR.getCode(),
                "System error occurred"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<GenericResponse<String>> handleAllUncaughtException(Throwable e) {
        log.error("Unhandled exception occurred!", e);
        GenericResponse<String> response = GenericResponse.of(
                ResponseCodes.SYSTEM_ERROR.getCode(),
                "An unexpected error occurred"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
