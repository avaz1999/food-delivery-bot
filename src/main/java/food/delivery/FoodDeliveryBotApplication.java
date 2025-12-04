package food.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("food.delivery.bot.config")
public class FoodDeliveryBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryBotApplication.class, args);
    }

}
