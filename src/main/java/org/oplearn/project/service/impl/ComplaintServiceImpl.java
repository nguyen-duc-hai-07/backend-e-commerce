package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ComplaintRequest;
import org.oplearn.project.dto.request.ComplaintUpdateRequest;
import org.oplearn.project.dto.response.ComplaintResponse;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.entity.Complaint;
import org.oplearn.project.enums.ComplaintStatus;
import org.oplearn.project.exception.ComplaintAlreadyResolvedException;
import org.oplearn.project.exception.ComplaintCannotBeDeletedException;
import org.oplearn.project.exception.ComplaintLimitExceededException;
import org.oplearn.project.exception.ComplaintNotFoundException;
import org.oplearn.project.exception.InvalidComplaintStatusException;
import org.oplearn.project.repository.ComplaintRepository;
import org.oplearn.project.service.ComplaintService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

  private static final int MAX_COMPLAINTS = 3;

  private final ComplaintRepository repository;

  @Override
  @Transactional
  public ComplaintResponse create(ComplaintRequest request) {
    log.info("(service) create complaint");

    this.validateCreate(request);

    Complaint complaint = Complaint.builder()
        .userId(request.getUserId())
        .orderId(request.getOrderId())
        .content(request.getContent())
        .status(ComplaintStatus.PENDING)
        .build();

    Complaint saved = repository.save(complaint);
    return ComplaintResponse.from(saved);
  }

  @Override
  @Transactional
  public ComplaintResponse update(ComplaintUpdateRequest request, Long id) {
    log.info("(service) update complaint: {}", id);

    if (request.getStatus() == null || request.getStatus() == ComplaintStatus.PENDING) {
      log.warn("(update) invalid status: {}", request.getStatus());
      throw new InvalidComplaintStatusException();
    }

    Complaint complaint = this.findById(id);

    complaint.setStatus(request.getStatus());

    if (request.getAdminReply() != null) {
      complaint.setAdminReply(request.getAdminReply());
    }

    Complaint updated = repository.save(complaint);
    return ComplaintResponse.from(updated);
  }

  @Override
  public ComplaintResponse detail(Long id) {
    log.info("(service) detail complaint: {}", id);
    return ComplaintResponse.from(this.findById(id));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(service) delete complaint: {}", id);

    Complaint complaint = this.findById(id);

    if (complaint.getStatus() != ComplaintStatus.PENDING) {
      log.warn("(delete) cannot delete complaint: {}", id);
      throw new ComplaintCannotBeDeletedException();
    }

    repository.softDeleteById(id);
  }

  @Override
  public PageResponse<ComplaintResponse> filterByUserId(Long userId, ComplaintStatus status, int size, int page) {
    log.info("(service) list complaints by userId: {}", userId);

    Pageable pageable = PageRequest.of(page, size);
    Page<Complaint> complaints = repository.filterByUserId(userId, status, pageable);
    return PageResponse.of(complaints.map(ComplaintResponse::from));
  }

  @Override
  public PageResponse<ComplaintResponse> filter(ComplaintStatus status, int size, int page) {
    log.info("(service) list complaints");

    Pageable pageable = PageRequest.of(page, size);
    Page<Complaint> complaints = repository.filter(status, pageable);
    return PageResponse.of(complaints.map(ComplaintResponse::from));
  }

  private Complaint findById(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(ComplaintNotFoundException::new);
  }

  private void validateCreate(ComplaintRequest request) {
    if (repository.existsByUserIdAndOrderIdAndStatusAndIsDeletedFalse(request.getUserId(), request.getOrderId(), ComplaintStatus.RESOLVED)) {
      log.warn("(create) already resolved");
      throw new ComplaintAlreadyResolvedException();
    }

    if (repository.countByUserIdAndOrderIdAndIsDeletedFalse(request.getUserId(), request.getOrderId()) >= MAX_COMPLAINTS) {
      log.warn("(create) limit exceeded");
      throw new ComplaintLimitExceededException();
    }
  }
}
