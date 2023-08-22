package com.rupeesense.fi.filter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;

public class RequestAuthFilter implements Filter {

  public RequestAuthFilter() {
    try {
      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.getApplicationDefault())
          .setProjectId("ascendant-woods-395319")
          .build();
      FirebaseApp.initializeApp(options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String idToken = httpRequest.getHeader("Authorization");

    if (HttpMethod.OPTIONS.name().equals(httpRequest.getMethod())) {
      chain.doFilter(request, response);
      return;
    }

    if (idToken == null || idToken.isEmpty()) {
      response.getWriter().write("Authentication failed.");
      ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    try {
      idToken = idToken.replace("Bearer ", "");
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
      String uid = decodedToken.getUid();
      // You can now use the uid to identify the user in your system
      chain.doFilter(request, response);
    } catch (FirebaseAuthException e) {
      // Token is invalid, respond with an error status or handle as required
      response.getWriter().write("Authentication failed.");
      ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}

