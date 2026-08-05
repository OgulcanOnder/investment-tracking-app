package com.ogulcanonder.investment_tracking_app.exception;

public class PasswordResetTokenExpiredException extends RuntimeException {
  public PasswordResetTokenExpiredException(String message) {
    super(message);
  }
}
