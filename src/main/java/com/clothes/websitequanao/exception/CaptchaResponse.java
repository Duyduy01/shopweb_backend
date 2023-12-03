package com.clothes.websitequanao.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaResponse {
    private boolean success;
    @JsonProperty("challenge_ts")
    private LocalDateTime challengeTs;
    private String hostName;
    @JsonProperty("error-codes")
    private List<String> errorCodes;
}