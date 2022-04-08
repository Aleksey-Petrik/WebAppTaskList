package ru.tasklist.springboot.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity(debug = true)//Указывает Spring контейнеру, чтобы находил файл конфигурации в классе. debug = true - для просмотра какие бины были созданы, в production нужно ставить false
public class SpringConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable();//Отключаем т.к. форма авторизации создается не на Spring технологии(например, Spring MVC + JSP), а на любой другой клиентской технологии
        http.httpBasic().disable();//Отключаем стандартную браузерную форму авторизации

        http.requiresChannel().anyRequest().requiresSecure();//Обязательно использовать HTTPS
    }
}
