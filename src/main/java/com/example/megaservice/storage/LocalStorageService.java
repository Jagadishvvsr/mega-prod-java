package com.example.megaservice.storage;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.*;

public class LocalStorageService implements StorageService {

  private final Path root;

  public LocalStorageService(String root) throws IOException {
    this.root = Path.of(root);
    Files.createDirectories(this.root);
  }

  @Override
  public String store(String keyPrefix, String filename, String contentType, long size, InputStream data) throws IOException {
    String key = (keyPrefix == null ? "" : keyPrefix + "/") + java.util.UUID.randomUUID() + "-" + filename;
    Path dest = this.root.resolve(key);
    Files.createDirectories(dest.getParent());
    Files.copy(data, dest, StandardCopyOption.REPLACE_EXISTING);
    return key;
  }

  @Override
  public InputStream load(String storageKey) throws IOException {
    return Files.newInputStream(this.root.resolve(storageKey));
  }

  @Override
  public void delete(String storageKey) throws IOException {
    Files.deleteIfExists(this.root.resolve(storageKey));
  }
}
