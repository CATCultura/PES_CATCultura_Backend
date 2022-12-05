package cat.cultura.backend.interceptors;

import cat.cultura.backend.entity.Role;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String EVENTENDPOINT = "/events*";
    public static final String CONCRETEEVENT = "/events/*";
    @Autowired
    private UserJpaRepository userJpaRepository;

    private String organizer = Role.ORGANIZER.toString();

    private String service = Role.SERVICE.toString();


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable()
                .authorizeHttpRequests()
                        .antMatchers(HttpMethod.GET, EVENTENDPOINT).permitAll().and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.GET, CONCRETEEVENT).permitAll().and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.GET, "/tags").permitAll().and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.POST,"/users").permitAll().and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.POST, EVENTENDPOINT).hasAuthority(organizer).and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.PUT, CONCRETEEVENT).hasAuthority(organizer).and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.DELETE, CONCRETEEVENT).hasAuthority(organizer).and().authorizeHttpRequests()
//                .antMatchers(HttpMethod.DELETE, "/events/2").hasAuthority(organizer).and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.GET, "/allevents").hasAuthority(service).and().authorizeHttpRequests()
                        .antMatchers(HttpMethod.POST,"/insert").hasAuthority(service).and().authorizeHttpRequests()
                        .antMatchers("/auth").permitAll().and().authorizeHttpRequests()
                        .anyRequest().authenticated().and().httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl(userJpaRepository);
    }


}
