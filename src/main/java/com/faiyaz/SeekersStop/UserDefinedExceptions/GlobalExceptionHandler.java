package com.faiyaz.SeekersStop.UserDefinedExceptions;

import com.faiyaz.SeekersStop.Dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDto> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(HttpStatus.CONFLICT.value(),e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponseDto> handleBadCredentialsException(BadCredentialsException e){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponseDto);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleResourceNotFoundException(ResourceNotFoundException e){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(HttpStatus.NOT_FOUND.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponseDto);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionResponseDto> handleDuplicateResourceException(DuplicateResourceException e){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(HttpStatus.CONFLICT.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponseDto);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalStateException(IllegalStateException e){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponseDto> handleForbiddenException(ForbiddenException e){
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(HttpStatus.FORBIDDEN.value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponseDto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String msg = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(HttpStatus.BAD_REQUEST.value(),msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponseDto);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleException(Exception e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto
                (HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Something went wrong. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionResponseDto);

    }

}
