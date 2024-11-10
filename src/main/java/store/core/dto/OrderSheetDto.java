package store.core.dto;

import java.util.List;

public record OrderSheetDto(
        List<OrderSheetItemDto> items
) {
    public record OrderSheetItemDto(
            String name,
            Long quantity
    ) {}
}
