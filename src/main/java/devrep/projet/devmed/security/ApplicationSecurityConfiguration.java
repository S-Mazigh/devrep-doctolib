package devrep.projet.devmed.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration {

    @Autowired
    DataSource dataSource;
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
     
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/home/**", "/js/**", "/css/**","/signup/**").permitAll()
                                .antMatchers("/profile", "/profile/modify", "/getAppointment").authenticated()
                                .antMatchers("/profile/modify/**").hasAuthority("PRO");
        http.formLogin().usernameParameter("Email").passwordParameter("Password")
            .loginPage("/login").permitAll()
            .defaultSuccessUrl("/home")
            .failureUrl("/login-error")
            .and().logout().permitAll();
        http.cors().and().csrf().disable();
        /*
        http.authorizeRequests().anyRequest().permitAll()
            .and().formLogin().loginPage("/login");*/
            
        return http.build();
    }
}
