package com.bonsainet.bonsai;

import static org.springframework.security.config.Customizer.withDefaults;

import com.nimbusds.jose.shaded.json.JSONArray;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

@EnableWebSecurity
@Order(101)
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .oauth2Login(withDefaults());
  }

  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

      authorities.forEach(authority -> {
        if (authority instanceof OidcUserAuthority) {
          OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;

          OidcIdToken idToken = oidcUserAuthority.getIdToken();
          OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

          // Map the claims found in idToken and/or userInfo
          // to one or more GrantedAuthority's and add it to mappedAuthorities

          mappedAuthorities.add(oidcUserAuthority);
          if (((OidcUserAuthority) authority).getAttributes().containsKey("cognito:roles")) {
            JSONArray roles = (JSONArray) ((OidcUserAuthority) authority).getAttributes().get("cognito:roles");
            roles.forEach(i -> {
              String cleanedAuth = i.toString().split("/")[1];
              OidcUserAuthority e = new OidcUserAuthority("ROLE_" + cleanedAuth, idToken, userInfo);
              mappedAuthorities.add(e);
            });
          }

        } else if (authority instanceof OAuth2UserAuthority) {
          OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;

          Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

          // Map the attributes found in userAttributes
          // to one or more GrantedAuthority's and add it to mappedAuthorities

        }
      });

      return mappedAuthorities;
    };
  }
}