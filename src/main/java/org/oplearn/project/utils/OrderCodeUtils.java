package org.oplearn.project.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class OrderCodeUtils {

  private static final String DEFAULT_PREFIX = "HAZI";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");
  // Loại bỏ các ký tự dễ nhầm lẫn như 0, O, 1, I
  private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

  private OrderCodeUtils() {
  }

  /**
   * Sinh mã đơn hàng mặc định với prefix "HAZI", ví dụ: HAZI261002203730K8M2
   */
  public static String generateOrderCode() {
    return generateOrderCode(DEFAULT_PREFIX);
  }

  /**
   * Sinh mã đơn hàng với prefix tùy chỉnh
   * @param prefix tiền tố của mã đơn hàng (ví dụ: "ORD", "EXC", "REF")
   */
  public static String generateOrderCode(String prefix) {
    String timestamp = LocalDateTime.now().format(FORMATTER);
    String randomSuffix = generateRandomString(4);
    return String.format("%s%s%s", prefix, timestamp, randomSuffix);
  }

  private static String generateRandomString(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int index = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(index));
    }
    return sb.toString();
  }
}
