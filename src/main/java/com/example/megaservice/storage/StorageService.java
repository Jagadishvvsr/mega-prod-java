package com.example.megaservice.storage;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
  String store(String keyPrefix, String filename, String contentType, long size, InputStream data) throws IOException;
  InputStream load(String storageKey) throws IOException;
  void delete(String storageKey) throws IOException;
}
