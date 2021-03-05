package entity;

import com.example.entity.Product;
import com.example.type.ProductCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    public void callConstructor_WhenAllParameterPassed_ThenObjectIsCorrectlyInitialized() {
        String id = "Analgin";
        ProductCategory category = ProductCategory.MEDICAL;
        Product product = new Product(id, category);
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    public void callEquals_WhenFieldsAreEquals_ThenReturnTrue() {
        String id = "Analgin";
        ProductCategory category = ProductCategory.MEDICAL;
        Product firstProduct = new Product(id, category);
        Product secondProduct = new Product(id, category);

        assertThat(firstProduct.equals(secondProduct)).isTrue();
    }

    @Test
    public void callEquals_WhenFieldsAreNotEquals_ThenReturnFalse() {
        String id = "Analgin";
        Product firstProduct = new Product(id, ProductCategory.MEDICAL);
        Product secondProduct = new Product(id, ProductCategory.BOOKS);

        assertThat(firstProduct.equals(secondProduct)).isFalse();
    }


}
