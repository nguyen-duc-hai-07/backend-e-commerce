package org.oplearn.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.AddressRequest;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.facade.AddressFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.CREATED_MESSAGE;
import static org.oplearn.project.constants.OpLearnConstants.CommonConstants.SUCCESS_MESSAGE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

  private final AddressFacadeService facade;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<AddressResponse> create(@Valid @RequestBody AddressRequest request) {
    log.info("(create) request: {}", request);
    return ResponseGeneral.ofCreated(CREATED_MESSAGE, facade.create(request));
  }

  @PutMapping("/{id}")
  public ResponseGeneral<AddressResponse> update(
      @PathVariable("id") Long id,
      @Valid @RequestBody AddressRequest request
  ) {
    log.info("(update) id: {}, request: {}", id, request);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.update(request, id));
  }

  @GetMapping("/{id}")
  public ResponseGeneral<AddressResponse> detail(@PathVariable("id") Long id) {
    log.info("(detail) id: {}", id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.detail(id));
  }

  @DeleteMapping("/{id}")
  public ResponseGeneral<Void> delete(@PathVariable("id") Long id) {
    log.info("(delete) id: {}", id);
    facade.delete(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @PutMapping("/{id}/default")
  public ResponseGeneral<Void> setDefault(@PathVariable("id") Long id) {
    log.info("(setDefault) id: {}", id);
    facade.setDefault(id);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE);
  }

  @GetMapping("/{userId}/list")
  public ResponseGeneral<PageResponse<AddressResponse>> list(@PathVariable("userId") Long userId) {
    log.info("(list) userId: {}", userId);
    return ResponseGeneral.ofSuccess(SUCCESS_MESSAGE, facade.list(userId));
  }
}
