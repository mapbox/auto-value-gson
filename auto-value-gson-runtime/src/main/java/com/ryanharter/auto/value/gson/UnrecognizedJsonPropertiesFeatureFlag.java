package com.ryanharter.auto.value.gson;

public final class UnrecognizedJsonPropertiesFeatureFlag {

  private static volatile boolean enabled = false;

  public static void enable() {
    enabled = true;
  }

  public static void disable() {
    enabled = false;
  }

  public static boolean isEnabled() {
    return enabled;
  }
}
