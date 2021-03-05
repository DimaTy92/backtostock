package entity;

import com.example.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void callConstructor_WhenAllParameterPassed_ThenObjectIsCorrectlyInitialized() {
        String name = "Dima";
        boolean premium = true;
        int age = 28;
        User user = new User(name, premium, age);

        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getAge()).isEqualTo(age);
        assertThat(user.isPremium()).isEqualTo(premium);
    }

}
