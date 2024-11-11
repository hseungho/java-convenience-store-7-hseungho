package store.core.view;

import java.util.Arrays;
import java.util.List;
import store.commons.util.Command;
import store.core.dto.OrderSheetDto;
import store.core.dto.OrderSheetDto.OrderSheetItemDto;

public class OrderSheetInputView implements InputView<String, OrderSheetDto> {

    @Override
    public void display(String content) {
        System.out.println(content);
    }

    @Override
    public OrderSheetDto displayWithInput(String content) {
        display(content);
        String read = Command.read();
        List<String> inputItems = parseToItems(read);
        List<OrderSheetItemDto> items = parseToEachItem(inputItems);
        return new OrderSheetDto(items);
    }

    private List<String> parseToItems(String read) {
        return Arrays.stream(read.split(",")).toList();
    }

    private List<OrderSheetItemDto> parseToEachItem(List<String> inputItems) {
        return inputItems.stream()
                .map(this::mapToEachItem)
                .map(this::mapToOrderSheetItem)
                .toList();
    }

    private String mapToEachItem(String inputEachItem) {
        if (inputEachItem == null || inputEachItem.isBlank()) {
            throw new IllegalArgumentException("Required items");
        }
        if (!inputEachItem.startsWith("[") || !inputEachItem.endsWith("]")) {
            throw new IllegalArgumentException("Invalid items");
        }
        return inputEachItem.substring(1, inputEachItem.length() - 1);
    }

    private OrderSheetItemDto mapToOrderSheetItem(String inputOrderSheetItem) {
        String[] itemStrArr = inputOrderSheetItem.split("-");
        validateItemStrArray(itemStrArr);
        String name = mapToItemName(itemStrArr[0]);
        Long quantity = mapToItemQuantity(itemStrArr[1]);
        return new OrderSheetItemDto(name, quantity);
    }

    private void validateItemStrArray(String[] itemStrArray) {
        if (itemStrArray.length != 2) {
            throw new IllegalArgumentException("Invalid item array");
        }
    }

    private String mapToItemName(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Required item name");
        }
        return itemName;
    }

    private Long mapToItemQuantity(String itemQuantity) {
        if (itemQuantity == null || itemQuantity.isBlank()) {
            throw new IllegalArgumentException("Required item quantity");
        }
        try {
            return Long.parseLong(itemQuantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid item quantity");
        }
    }
}
