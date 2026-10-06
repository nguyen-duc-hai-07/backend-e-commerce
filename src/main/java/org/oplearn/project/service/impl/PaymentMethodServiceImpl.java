package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.PaymentMethodRequest;
import org.oplearn.project.dto.response.PaymentMethodResponse;
import org.oplearn.project.entity.PaymentMethod;
import org.oplearn.project.exception.PaymentMethodAlreadyExistedException;
import org.oplearn.project.exception.PaymentMethodNotFoundException;
import org.oplearn.project.repository.PaymentMethodRepository;
import org.oplearn.project.service.PaymentMethodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

  private final PaymentMethodRepository repository;

  @Override
  @Transactional
  public PaymentMethodResponse create(PaymentMethodRequest request) {
    log.info("(create) paymentMethod request: {}", request);

    if (repository.existsByCodeAndIsDeletedFalse(request.getCode())) {
      log.error("(create) paymentMethod code already exists: {}", request.getCode());
      throw new PaymentMethodAlreadyExistedException();
    }

    PaymentMethod paymentMethod = new PaymentMethod();
    setPaymentMethodValues(paymentMethod, request);

    PaymentMethod saved = repository.save(paymentMethod);
    return PaymentMethodResponse.from(saved);
  }

  @Override
  @Transactional
  public PaymentMethodResponse update(PaymentMethodRequest request, Long id) {
    log.info("(update) paymentMethod id: {}, request: {}", id, request);

    PaymentMethod paymentMethod = findByIdOrThrow(id);

    if (repository.existsByCodeAndIdNotAndIsDeletedFalse(request.getCode(), id)) {
      log.error("(update) paymentMethod code already exists: {}", request.getCode());
      throw new PaymentMethodAlreadyExistedException();
    }

    setPaymentMethodValues(paymentMethod, request);
    PaymentMethod saved = repository.save(paymentMethod);

    return PaymentMethodResponse.from(saved);
  }

  @Override
  public PaymentMethodResponse detail(Long id) {
    log.info("(detail) paymentMethod id: {}", id);
    return PaymentMethodResponse.from(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(delete) paymentMethod id: {}", id);
    findByIdOrThrow(id);
    repository.softDeleteById(id);
  }

  @Override
  public List<PaymentMethodResponse> findAll() {
    log.info("(findAll) paymentMethods");
    return repository.findAllByIsDeletedFalse()
        .stream()
        .map(PaymentMethodResponse::from)
        .toList();
  }

  @Override
  public void checkPaymentMethodExist(Long id) {
    log.info("(checkPaymentMethodExist) id: {}", id);
    if (!repository.existsByIdAndIsDeletedFalse(id)) {
      log.error("(checkPaymentMethodExist) paymentMethod not found with id: {}", id);
      throw new PaymentMethodNotFoundException();
    }
  }

  private PaymentMethod findByIdOrThrow(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(PaymentMethodNotFoundException::new);
  }

  private void setPaymentMethodValues(PaymentMethod target, PaymentMethodRequest source) {
    target.setName(source.getName());
    target.setCode(source.getCode());
  }
}
