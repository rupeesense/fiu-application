package com.rupeesense.fi;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIModule {

  @Bean
  public FilterRegistrationBean<RequestAuthFilter> loggingFilter() {
    FilterRegistrationBean<RequestAuthFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new RequestAuthFilter());
    registrationBean.addUrlPatterns("/*"); // Set the URL pattern here; /* would mean all endpoints

    return registrationBean;
  }


}
