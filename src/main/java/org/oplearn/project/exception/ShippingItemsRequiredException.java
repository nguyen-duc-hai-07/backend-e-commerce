package org.oplearn.project.exception;

import org.oplearn.project.exception.base.BadRequestException;

public class ShippingItemsRequiredException extends BadRequestException {

  public ShippingItemsRequiredException() {
    super("org.oplearn.project.exception.ShippingItemsRequiredException");
  }
}
