package com.example.user;

import static springfox.documentation.builders.PathSelectors.any;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.example.user.domain.User;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "com.example" })
public class UserregistrationApplication {
	
	@Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("User").select()
                .apis(RequestHandlerSelectors.basePackage("com.example.user"))
                .paths(any()).build().apiInfo(new ApiInfo("User Registration Services",
                        "A set of services to provide data access to Users", "1.0.0", null,
                        new Contact("Ramakrishna", "@Dell", null),null, null));
    }


	public static void main(String[] args) {
		SpringApplication.run(UserregistrationApplication.class, args);
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setDefaultType(User.class);
        converter.setClassMapper(classMapper);
        return converter;
    }
}
