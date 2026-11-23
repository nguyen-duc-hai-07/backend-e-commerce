package org.oplearn.project.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.event.auth.UserRegisteredEvent;
import org.oplearn.project.service.EmailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEmailEventListener {

  private final EmailService emailService;

  @Async("mailTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
  public void handleUserRegistered(UserRegisteredEvent event) {
    if (event.email() == null || event.email().isBlank()) {
      log.info("(handleUserRegistered) user {} has no email, skip welcome email", event.userId());
      return;
    }

    log.info("(handleUserRegistered) sending welcome email to: {}", event.email());

    try {
      String recipientName = StringUtils.hasText(event.fullName()) ? event.fullName() : event.username();
      Map<String, Object> variables = Map.of(
          "recipientName", recipientName
      );

      emailService.sendHtmlEmail(
          event.email(),
          "Chào mừng bạn gia nhập cộng đồng mua sắm! 🎉",
          "mail/welcome-email",
          variables
      );
    } catch (Exception e) {
      log.error("(handleUserRegistered) failed to send welcome email to: {}", event.email(), e);
    }
  }
}
