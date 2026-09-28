package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReviewReplyRequest;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.ReviewReplyResponse;
import org.oplearn.project.facade.ReplyFacadeService;
import org.oplearn.project.service.ReviewReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.CREATED_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/review-replies")
public class ReviewReplyController {

  private final ReplyFacadeService facade;
  private final ReviewReplyService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ReviewReplyResponse> create(@Valid @RequestBody ReviewReplyRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<ReviewReplyResponse> update(
      @PathVariable("id") Long id,
      @Valid @RequestBody ReviewReplyRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.update(request.getContent(), id));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<ReviewReplyResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    service.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/review/{reviewId}")
  public ResponseGeneral<ReviewReplyResponse> findByReviewId(@PathVariable("reviewId") Long reviewId) {
    log.info("(findByReviewId) reviewId: {}", reviewId);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.findByReviewId(reviewId));
  }
}
