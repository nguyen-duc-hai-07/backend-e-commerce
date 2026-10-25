package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.NotificationRequest;
import org.oplearn.project.dto.response.NotificationResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.facade.NotificationFacadeService;
import org.oplearn.project.repository.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constants.OpLearnConstants.VariableConstant.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

  private final NotificationFacadeService facade;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<NotificationResponse> create(@Valid @RequestBody NotificationRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @GetMapping("/feed")
  public ResponseGeneral<PageResponse<NotificationRepository.NotificationFeedProjection>> getFeed(
      @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
      @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size
  ) {
    log.info("(getFeed) page: {}, size: {}", page, size);
    size = Math.min(size, MAX_PAGE_SIZE);
    Pageable pageable = PageRequest.of(page, size);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, PageResponse.of(facade.getFeed(pageable)));
  }

  @GetMapping("/unread-count")
  public ResponseGeneral<Long> getUnreadCount() {
    log.info("(getUnreadCount)");
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.getUnreadCount());
  }

  @GetMapping("/{id}")
  public ResponseGeneral<NotificationResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @PatchMapping("/{id}/read")
  public ResponseGeneral<Boolean> markAsRead(@PathVariable("id") Long id) {
    log.info("(markAsRead) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.markAsRead(id));
  }

  @PatchMapping("/read-all")
  public ResponseGeneral<Integer> markAllAsRead() {
    log.info("(markAllAsRead)");
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.markAllAsRead());
  }
}
