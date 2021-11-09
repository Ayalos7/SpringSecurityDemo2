package com.jb.mySpringSecurity.security;


import com.jb.mySpringSecurity.JWT.JwtUserAndPassFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder PASSWORD_ENCODER;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder PASSWORD_ENCODER) {
        this.PASSWORD_ENCODER = PASSWORD_ENCODER;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUserAndPassFilter(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*", "/media/*", "/img/*").permitAll()
                .anyRequest()
                .authenticated();

    }

    protected void configure_old(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //.and()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*", "/media/*", "/img/*").permitAll()
                .antMatchers("/test/**").hasRole(ApplicationUserRole.ADMIN.name()) //ROLE_ADMIN
                //.antMatchers(HttpMethod.DELETE, "/company/**").hasAuthority(ApplicationUserPermission.COMPANY_WRITE.name())
                //.antMatchers(HttpMethod.POST, "/company/**").hasAuthority(ApplicationUserPermission.COMPANY_WRITE.name())
                //.antMatchers(HttpMethod.PUT, "/company/**").hasAuthority(ApplicationUserPermission.COMPANY_WRITE.name())
                //.antMatchers(HttpMethod.GET, "/company/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.SUPPORT.name())
                .anyRequest()
                .authenticated()
                .and()
                //.httpBasic();
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/mainpage", true)
                .and()
                .rememberMe() //will work for only 30 minutes
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(14))//will work now for 14 days
                .key("just_a_key")
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login")
        ;
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails myUser = User.builder()
                .username("ayal")
                .password(PASSWORD_ENCODER.encode("12345"))
                //.roles(ApplicationUserRole.ADMIN.name())  //admin role
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();
        UserDetails client = User.builder()
                .username("client")
                .password(PASSWORD_ENCODER.encode("12345"))
                .roles(ApplicationUserRole.CUSTOMER.name())
                .authorities(ApplicationUserRole.CUSTOMER.getGrantedAuthorities())
                .build();
        UserDetails support = User.builder()
                .username("support")
                .password(PASSWORD_ENCODER.encode("12345"))
                .roles(ApplicationUserRole.SUPPORT.name())
                .authorities(ApplicationUserRole.SUPPORT.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(myUser, client, support);
    }
}
