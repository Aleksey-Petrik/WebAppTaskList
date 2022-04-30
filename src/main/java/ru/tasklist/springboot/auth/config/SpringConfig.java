package ru.tasklist.springboot.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tasklist.springboot.auth.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity(debug = true)
// Указывает Spring контейнеру, чтобы находил файл конфигурации в классе. debug = true - для просмотра лога какие бины были созданы, в production нужно ставить false
public class SpringConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //Односторонний кодировщик паролей (хеширование) BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // порядок следования настроек внутри метода - неважен

        /* Если используется другая клиентская технология (не SpringMVC, а например Angular, React и пр.),
            то выключаем встроенную Spring-защиту от CSRF атак,
            иначе запросы от клиента не будут обрабатываться, т.к. Spring Security будет пытаться в каждом входящем запроcе искать спец. токен для защиты от CSRF
        */

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//SessionCreationPolicy.STATELESS - для того чтобы сессии не создавались

        http.csrf().disable(); // На время разработки проекта не будет ошибок (для POST, PUT и др. запросов) - недоступен и т.д.

        http.formLogin().disable(); // отключаем, т.к. форма авторизации создается не на Spring технологии (например, Spring MVC + JSP), а на любой другой клиентской технологии
        http.httpBasic().disable(); // отключаем стандартную браузерную форму авторизации

        http.requiresChannel()
                .anyRequest()
                .requiresSecure(); // обязательное исп. HTTPS

    }

}
