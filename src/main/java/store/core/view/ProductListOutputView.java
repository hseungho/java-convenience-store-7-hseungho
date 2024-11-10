package store.core.view;

import java.text.DecimalFormat;
import java.util.List;
import store.core.dto.ProductDto;

public class ProductListOutputView implements OutputView<List<ProductDto>> {

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    public void display(List<ProductDto> content) {
        displayIndex();
        displayProducts(content);
    }

    private void displayIndex() {
        String index = """
                안녕하세요. W편의점입니다.
                현재 보유하고 있는 상품입니다.
                """;
        System.out.println(index);
    }

    private void displayProducts(List<ProductDto> content) {
        StringBuilder builder = new StringBuilder();
        for (ProductDto product : content) {
            builder.append("- ");
            builder.append(product.name() + " ");
            builder.append(decimalFormat.format(product.price()) + "원 ");
            builder.append(product.quantity()+"개 ");
            builder.append(product.promotionName() + "\n");
        }
        System.out.println(builder);
    }
}
