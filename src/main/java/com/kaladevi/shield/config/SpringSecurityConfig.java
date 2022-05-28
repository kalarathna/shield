package com.kaladevi.shield.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
        import org.springframework.security.core.userdetails.User;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
        import org.springframework.web.cors.CorsConfiguration;
        import org.springframework.web.cors.CorsConfigurationSource;

        import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

    @Value("${BASICAUTH.USERNAME}")
    private String userName;
    @Value("${BASICAUTH.PASSWORD}")
    private String password;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        return new CorsConfiguration().applyPermitDefaultValues();
                    }
                }).and().csrf().disable().authorizeRequests()
                .antMatchers("/shield/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated().and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .headers()
                .xssProtection();;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configure DB authentication provider for user accounts
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username(userName)
                        .password(password)
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

}
