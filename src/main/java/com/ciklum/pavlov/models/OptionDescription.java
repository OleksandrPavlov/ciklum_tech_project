package com.ciklum.pavlov.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OptionDescription {
    public OptionDescription(String optionKey, String description) {
        this.optionKey = optionKey;
        this.description = description;
    }
    private String optionKey;
    private String description;
}
