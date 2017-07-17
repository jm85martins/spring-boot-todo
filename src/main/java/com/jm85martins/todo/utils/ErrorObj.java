package com.jm85martins.todo.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by jorgemartins on 17/07/2017.
 */
@Getter
@Setter
@Builder
public class ErrorObj {
    private long timestamp = new Date().getTime();
    private String developerMessage;
    private String userMessage;
}
