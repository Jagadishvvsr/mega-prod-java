package com.example.megaservice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

  @Bean
  @ConditionalOnMissingBean(StorageService.class)
  public StorageService storageService(
      @Value("${app.storage.root:#{systemProperties['java.io.tmpdir']+'/uploads'}}") String root
  ) throws java.io.IOException {
    return new LocalStorageService(root);
  }
}
