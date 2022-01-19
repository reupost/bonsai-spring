package com.bonsainet.bonsai.controller;

import com.bonsainet.bonsai.model.User;
import com.bonsainet.bonsai.service.IUserService;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class SecurityHelper {

  public static boolean isAuthenticatedUserAdmin() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Cognito_bonsaiAdmin_Role"))) {
      return true;
    } else {
      return false;
    }
  }

  public static boolean isUserTheAuthenticatedUser(User u) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      OAuth2User authUser = (OAuth2User) auth.getPrincipal();
      String authEmail = authUser.getAttribute("email");
      if (u.getEmail().equalsIgnoreCase(authEmail)) {
        return true;
      }
    }
    return false;
  }

  public static Optional<User> getAuthenticatedUserAsUser(IUserService userService) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      OAuth2User authUser = (OAuth2User) auth.getPrincipal();
      String authEmail = authUser.getAttribute("email");
      return userService.findByEmail(authEmail);
    }
    return Optional.empty();
  }
}
