package com.levimartines.springboot2essentials.exception;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StandardError {

    private String message;
    private int status;
    private LocalDateTime timestamp;

    public StandardError(String message, int status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }
}
