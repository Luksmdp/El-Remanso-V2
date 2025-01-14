package com.muebleselremanso.elremansov2.exception;

import com.muebleselremanso.elremansov2.model.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String requiredType;
        try {
            requiredType = ex.getRequiredType().getSimpleName();
        } catch (NullPointerException e) {
            requiredType = "unknown type";
        }
        String message = String.format("The parameter '%s' must be of type '%s'.", parameterName, requiredType);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return ResponseEntity.badRequest().body("The parameter '" + name + "' is required and was not provided.");
    }

    @ExceptionHandler(DirectoryCreationException.class)
    public ResponseEntity<ApiResponse<String>> handleDirectoryCreationException(DirectoryCreationException ex) {
        ApiResponse<String> response = new ApiResponse<>("Error creating product folder", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotFoundException(ProductNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NoProductsFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNoProductsFoundException(NoProductsFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NoCategoriesFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNoCategoriesFoundException(NoCategoriesFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extraer el mensaje de error de las restricciones fallidas
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        ApiResponse<String> response = new ApiResponse<>(errorMessage, null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>("An unexpected error occurred: " + ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
