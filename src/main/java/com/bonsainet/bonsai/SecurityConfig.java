package com.bonsainet.bonsai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//  @Autowired
//  public void configureGlobal1(AuthenticationManagerBuilder auth) throws Exception {
//    //try in memory auth with no users to support the case that this will allow for users that are logged in to go anywhere
//    //auth.inMemoryAuthentication();
//    auth.inMemoryAuthentication()
//        .withUser("user").password("password").roles("USER")
//        .and()
//        .withUser("admin").password("admin").roles("ADMIN");
//  }
//
////  @Override
////  protected void configure(HttpSecurity http) throws Exception {
////    http.httpBasic()
////        .disable()
////        .authorizeRequests()
////        .antMatchers(HttpMethod.GET, "/bonsai").permitAll()
////        .antMatchers(HttpMethod.GET, "/bonsai/*").permitAll()
////        .antMatchers(HttpMethod.PUT, "/bonsai").hasRole("ADMIN")
////        .antMatchers(HttpMethod.POST, "/bonsai").hasRole("ADMIN")
////        .antMatchers(HttpMethod.PUT, "/bonsai/*").hasRole("ADMIN")
////        .antMatchers(HttpMethod.POST, "/bonsai/*").hasRole("ADMIN")
////        .antMatchers(HttpMethod.PATCH, "/bonsai/*").hasRole("ADMIN")
////        .antMatchers(HttpMethod.DELETE, "/bonsai/*").hasRole("ADMIN")
////        // until we've got front-end sorted out
////        .antMatchers(HttpMethod.GET, "/*").permitAll()
////        .antMatchers(HttpMethod.GET, "/*/*").permitAll()
////        .antMatchers(HttpMethod.GET, "/*/*/*").permitAll()
////        .antMatchers(HttpMethod.PUT, "/*/*/*").permitAll()
////        .antMatchers(HttpMethod.POST, "/*/*/*").permitAll()
////        .antMatchers(HttpMethod.PATCH, "/*/*/*").permitAll()
////        .antMatchers(HttpMethod.DELETE, "/*/*/*").permitAll()
////        .antMatchers(HttpMethod.PUT, "/*/*").permitAll()
////        .antMatchers(HttpMethod.POST, "/*/*").permitAll()
////        .antMatchers(HttpMethod.PATCH, "/*/*").permitAll()
////        .antMatchers(HttpMethod.DELETE, "/*/*").permitAll()
////        .antMatchers(HttpMethod.PUT, "/*").permitAll()
////        .antMatchers(HttpMethod.POST, "/*").permitAll()
////        .antMatchers(HttpMethod.PATCH, "/*").permitAll()
////        .antMatchers(HttpMethod.DELETE, "/*").permitAll()
////        .anyRequest().authenticated()
////        .and()
////        .csrf()
////        .disable();
////  }
//
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.csrf()
//        .and()
//        .authorizeRequests(authz -> authz.mvcMatchers("/")
//            .permitAll()
//            .anyRequest()
//            .authenticated())
//        .oauth2Login()
//        .and()
//        .logout()
//        .logoutSuccessUrl("/");
//  }
//}

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final String clientId;
  private final String logoutUrl;

  public SecurityConfig(@Value("${spring.security.oauth2.client.registration.cognito.clientId}") String clientId,
      @Value("${cognito.logoutUrl}") String logoutUrl) {
    this.clientId = clientId;
    this.logoutUrl = logoutUrl;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf()
        .and()
        .authorizeRequests(authorize ->
            authorize.mvcMatchers("/").permitAll()
                .anyRequest().authenticated())
        .oauth2Login()
        .and()
        .logout()
        .logoutSuccessHandler(new CognitoOidcLogoutSuccessHandler(logoutUrl, clientId));
  }
}
