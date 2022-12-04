package devrep.projet.devmed.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import devrep.projet.devmed.entities.Etat;

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
        http.formLogin().usernameParameter("Email").passwordParameter("Password") // c'est name des input du form
            .loginPage("/login").permitAll()
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
                    Etat state = (Etat) request.getSession(false).getAttribute("etat");
                    if(state!=null) // sinon NullPointerException possible
                    {
                        state.setConnected(true);
                        state.setBadEmail(false);
                        state.setWrongPass(false);
                        state.setWho(userDetails.getUser());
                        state.setPro(state.getWho().getAuthority().equals("PRO"));
                        request.getSession().setAttribute("etat", state);
                    }
                    System.err.println("Authentication: "+state);
                    response.sendRedirect("/home");
                }
            })
            .failureUrl("/login-error") //  l'exception : (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            .and()
            .logout().logoutUrl("/logout-all").logoutSuccessUrl("/home");
        // Pour pouvoir faire des post. Faut voir si c'est possible de faire autrement.
        http.cors().and().csrf().disable();
        /*
        http.authorizeRequests().anyRequest().permitAll()
            .and().formLogin().loginPage("/login");*/
            
        return http.build();
    }
}
