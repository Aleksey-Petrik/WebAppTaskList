package ru.tasklist.springboot.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.web.session.SessionManagementFilter;
import ru.tasklist.springboot.auth.filter.AuthTokenFilter;
import ru.tasklist.springboot.auth.filter.ExceptionHandlerFilter;
import ru.tasklist.springboot.auth.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity(debug = true)
// Указывает Spring контейнеру, чтобы находил файл конфигурации в классе. debug = true - для просмотра лога какие бины были созданы, в production нужно ставить false
public class SpringConfig extends WebSecurityConfigurerAdapter {

    // для получения пользователя из БД
    private UserDetailsServiceImpl userDetailsService;

    // перехватывает все выходящие запросы (проверяет jwt если необходимо, автоматически логинит пользователя)
    private AuthTokenFilter authTokenFilter;// его нужно зарегистрировать в filterchain
    private ExceptionHandlerFilter exceptionHandlerFilter; // самый верхний фильтр, который отлавливает ошибки во всех следующих фильтрах и отправляет клиенту в формате JSON

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {// внедряем наш компонент Spring @Service
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setExceptionHandlerFilter(ExceptionHandlerFilter exceptionHandlerFilter) {
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Autowired
    public void setAuthTokenFilter(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;// внедряем фильтр
    }

    // кодировщик паролей односторонним алгоритмом хеширования BCrypt https://ru.bitcoinwiki.org/wiki/Bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // используем стандартный готовый authenticationManager из Spring контейнера (используется для проверки логина-пароля)
    // эти методы доступны в документации Spring Security - оттуда их можно копировать, чтобы не писать вручную
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // указываем наш сервис userDetailsService для проверки пользователя в БД и кодировщик паролей
    // эти методы доступны в документации Spring Security - оттуда их можно копировать, чтобы не писать вручную
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {// настройки AuthenticationManager для правильной проверки логин-пароль
        authenticationManagerBuilder.userDetailsService(userDetailsService)// использовать наш сервис для загрузки User из БД
                .passwordEncoder(passwordEncoder());// указываем, что используется кодировщик пароля (для корректной проверки пароля)
    }

    // нужно отключить вызов фильтра AuthTokenFilter для сервлет контейнера (чтобы фильтр вызывался не 2 раза, а только один раз из Spring контейнера)
    // https://stackoverflow.com/questions/39314176/filter-invoke-twice-when-register-as-spring-bean
    @Bean
    public FilterRegistrationBean registration(AuthTokenFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);// FilterRegistrationBean - регистратор фильтров для сервлет контейнера
        registration.setEnabled(false);// отключить исп-е фильтра для сервлет контейнера
        return registration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // порядок следования настроек внутри метода - неважен, можно в любой последовательности

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

        // authTokenFilter - валидация JWT, до того, как запрос попадет в контроллер
        http.addFilterBefore(authTokenFilter, SessionManagementFilter.class);// добавляем наш фильтр в securityfilterchain

        // отлавливает ошибки последующих фильтром и отправляет их клиенту в формате JSON
        http.addFilterBefore(exceptionHandlerFilter, AuthTokenFilter.class); // этот фильтр должен обязательно находиться перед всеми нашими остальными фильтрами
    }

}
