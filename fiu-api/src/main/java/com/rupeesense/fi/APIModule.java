package com.rupeesense.fi;

import com.rupeesense.fi.filter.RequestAuthFilter;
import io.netty.channel.ChannelHandler.Sharable;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.netty.LogbookClientHandler;

@Configuration
public class APIModule {

  @Bean
  public FilterRegistrationBean<RequestAuthFilter> loggingFilter() {
    FilterRegistrationBean<RequestAuthFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new RequestAuthFilter());
    registrationBean.addUrlPatterns("/v1/fiu/consent/initiate"); // Set the URL pattern here; /* would mean all endpoints

    return registrationBean;
  }

  @Bean
  public Logbook logbook() {
    return Logbook.builder()
        .build();
  }

  @Bean
  public LogbookClientHandler logbookClientHandler() {
    return new LogbookClientHandler(logbook());
  }
}
