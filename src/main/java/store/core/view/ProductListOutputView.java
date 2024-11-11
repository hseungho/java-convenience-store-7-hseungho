package store.core.view;

import java.util.List;
import store.core.constants.Numbers;
import store.core.dto.ProductDto;

public class ProductListOutputView implements OutputView<List<ProductDto>> {

    @Override
    public void display(List<ProductDto> content) {
        displayIndex();
        displayProducts(content);
    }

    private void displayIndex() {
        String index = """
                \n안녕하세요. W편의점입니다.
                현재 보유하고 있는 상품입니다.
                """;
        System.out.println(index);
    }

    private void displayProducts(List<ProductDto> content) {
        StringBuilder builder = new StringBuilder();
        for (ProductDto product : content) {
            builder.append("- ");
            builder.append(product.name() + " ");
            builder.append(Numbers.formatDecimal(product.price()) + "원 ");
            buildQuantity(builder, product.quantity());
            builder.append(product.promotionName() + "\n");
        }
        System.out.println(builder);
    }

    private void buildQuantity(StringBuilder builder, Long quantity) {
        if (quantity == 0L) {
            builder.append("재고 없음 ");
            return;
        }
        builder.append(quantity + "개 ");
    }
}
