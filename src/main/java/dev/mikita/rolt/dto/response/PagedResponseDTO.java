package dev.mikita.rolt.dto.response;

import java.util.List;

public record PagedResponseDTO<T>(
    List<T> content,
    int currentPage,
    int pageSize,
    long totalItems,
    int totalPages
) {}
