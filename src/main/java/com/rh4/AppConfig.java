package com.rh4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.rh4.entities.*;
import com.rh4.models.MyUserDetails;
@Configuration
public class AppConfig {

	@Bean
    public GroupEntity group() {
        return new GroupEntity();
    }
	@Bean
    public MyUser user() {
        return new MyUser();
    }
	@Bean
    public MyUserDetails myUserDetails() {
        return new MyUserDetails();
    }
	@Bean
    public Cancelled cancelled() {
        return new Cancelled();
    }
}
