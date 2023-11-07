package cn.niit.shop_online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class ShopOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopOnlineApplication.class, args);
    }

}
