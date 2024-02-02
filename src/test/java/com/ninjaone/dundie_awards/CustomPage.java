package com.ninjaone.dundie_awards;

import java.util.List;

public record CustomPage<T>(
        int number,
        int size,
        int totalElements,
        List<T> content
) {
}
