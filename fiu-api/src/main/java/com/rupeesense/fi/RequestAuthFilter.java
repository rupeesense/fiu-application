package com.rupeesense.fi;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class RequestAuthFilter implements Filter {

  public RequestAuthFilter() {
    FirebaseApp.initializeApp();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String idToken = httpRequest.getHeader("Authorization");

    if (idToken == null || idToken.isEmpty()) {
        response.getWriter().write("Authentication failed.");
        ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }

    try {
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
      String uid = decodedToken.getUid();
      // You can now use the uid to identify the user in your system
      chain.doFilter(request, response);
    } catch (FirebaseAuthException e) {
      // Token is invalid, respond with an error status or handle as required
      response.getWriter().write("Authentication failed.");
      ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  @Override
  public void destroy() {
  }
}

