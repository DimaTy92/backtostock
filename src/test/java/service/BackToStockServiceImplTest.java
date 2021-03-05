package service;

import com.example.entity.Product;
import com.example.entity.User;
import com.example.service.UserNotifierService;
import com.example.service.impl.BackToStockServiceImpl;
import com.example.type.ProductCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BackToStockServiceImplTest {

    @Captor
    ArgumentCaptor<User> userCapture;
    @Captor
    ArgumentCaptor<Product> productCapture;

    @Mock
    private UserNotifierService notifierService;

    @InjectMocks
    private BackToStockServiceImpl stockService;

    @Test
    void subscribe_WhenUserPassed_ThenUserIsAddedToCorrectSubscription() {
        User user = new User("Dima", true, 28);
        Product product = new Product("MacBook", ProductCategory.DIGITAL);
        stockService.subscribe(user, product);

        List<User> users = stockService.subscribedUsers(product);

        assertThat(users).hasSize(1)
                .containsOnly(user);
    }

    @Test
    void notifyProductAddedToStock_WhenUserWithPriorityExists_ThenUserWithPriorityWasCalled() {
        User sergey = new User("Sergey", true, 22);
        User randomUser = new User("User", false, 45);
        User oldUser = new User("Granny", false, 73);

        Product medicalProduct = new Product("solpadein", ProductCategory.MEDICAL);

        stockService.subscribe(sergey, medicalProduct);
        stockService.subscribe(oldUser, medicalProduct);
        stockService.subscribe(randomUser, medicalProduct);

        stockService.notifyProductAddedToStock(medicalProduct);

        Mockito.verify(notifierService).notifyUser(userCapture.capture(), productCapture.capture());

        assertThat(userCapture.getValue()).isEqualTo(sergey);
        assertThat(productCapture.getValue()).isEqualTo(medicalProduct);
    }

    @Test
    void notifyProductAddedToStock_WhenMultipleUserWithPriorityExists_ThenFirstUserWithPriorityWasCalled() {
        User sergey = new User("Sergey", true, 22);
        User randomUser = new User("User", true, 45);
        User oldUser = new User("Granny", false, 73);

        Product medicalProduct = new Product("solpadein", ProductCategory.MEDICAL);

        stockService.subscribe(sergey, medicalProduct);
        stockService.subscribe(oldUser, medicalProduct);
        stockService.subscribe(randomUser, medicalProduct);

        stockService.notifyProductAddedToStock(medicalProduct);

        Mockito.verify(notifierService).notifyUser(userCapture.capture(), productCapture.capture());

        assertThat(userCapture.getValue()).isEqualTo(sergey);
        assertThat(productCapture.getValue()).isEqualTo(medicalProduct);
    }

    @Test
    void notifyProductAddedToStock_WhenOldUser_ThenOldUserIsFirst() {
        User sergey = new User("Sergey", false, 22);
        User randomUser = new User("User", false, 45);
        User oldUser = new User("Granny", false, 73);

        Product medicalProduct = new Product("solpadein", ProductCategory.MEDICAL);

        stockService.subscribe(sergey, medicalProduct);
        stockService.subscribe(oldUser, medicalProduct);
        stockService.subscribe(randomUser, medicalProduct);

        stockService.notifyProductAddedToStock(medicalProduct);

        Mockito.verify(notifierService).notifyUser(userCapture.capture(), productCapture.capture());

        assertThat(userCapture.getValue()).isEqualTo(oldUser);
        assertThat(productCapture.getValue()).isEqualTo(medicalProduct);
    }

    @Test
    void notifyProductAddedToStock_WhenMultipleHighPriority_ThenNotifyByFifo() {
        User sergey = new User("Sergey", true, 22);
        User randomUser = new User("User", false, 45);
        User oldUser = new User("Granny", false, 73);
        User premiumUser = new User("premiumUser", true, 55);

        Product medicalProduct = new Product("solpadein", ProductCategory.MEDICAL);

        stockService.subscribe(sergey, medicalProduct);
        stockService.subscribe(oldUser, medicalProduct);
        stockService.subscribe(randomUser, medicalProduct);
        stockService.subscribe(premiumUser, medicalProduct);

        stockService.notifyProductAddedToStock(medicalProduct);
        stockService.notifyProductAddedToStock(medicalProduct);
        stockService.notifyProductAddedToStock(medicalProduct);
        stockService.notifyProductAddedToStock(medicalProduct);

        Mockito.verify(notifierService, times(4))
                .notifyUser(userCapture.capture(), productCapture.capture());

        assertThat(userCapture.getAllValues())
                .hasSize(4)
                .containsExactly(sergey, oldUser, premiumUser, randomUser);
    }

    @Test
    void notifyProductAddedToStock_WhenNoPremiumUsers_ThenFifoOrder() {
        User sergeyNonPremium = new User("Sergey", false, 22);
        User randomUserNonPremium = new User("User", false, 45);

        Product medicalProduct = new Product("solpadein", ProductCategory.MEDICAL);

        stockService.subscribe(sergeyNonPremium, medicalProduct);
        stockService.subscribe(randomUserNonPremium, medicalProduct);

        stockService.notifyProductAddedToStock(medicalProduct);
        stockService.notifyProductAddedToStock(medicalProduct);

        Mockito.verify(notifierService, times(2))
                .notifyUser(userCapture.capture(), productCapture.capture());

        assertThat(userCapture.getAllValues())
                .hasSize(2)
                .containsExactly(sergeyNonPremium, randomUserNonPremium);
    }

    @Test
    void notifyProductAddedToStock_WhenNoUser_NotifyNotCalled() {

        Product medicalProduct = new Product("solpadein", ProductCategory.MEDICAL);
        stockService.notifyProductAddedToStock(medicalProduct);

        Mockito.verify(notifierService, times(0))
                .notifyUser(ArgumentMatchers.any(User.class), ArgumentMatchers.any(Product.class));

    }

}
