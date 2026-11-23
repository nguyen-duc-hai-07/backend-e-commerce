package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ReturnRqImageRequest;
import org.oplearn.project.dto.request.ReturnRqRequest;
import org.oplearn.project.dto.request.ReturnRqUpdateRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ReturnRequestImageResponse;
import org.oplearn.project.dto.response.ReturnRequestResponse;
import org.oplearn.project.entity.ReturnRequest;
import org.oplearn.project.entity.ReturnRequestImage;
import org.oplearn.project.enums.ReturnRequestStatus;
import org.oplearn.project.enums.ReturnResolutionType;
import org.oplearn.project.exception.ReturnRequestCannotBeDeletedException;
import org.oplearn.project.exception.ReturnRequestImageLimitExceededException;
import org.oplearn.project.exception.ReturnRequestNotFoundException;
import org.oplearn.project.repository.ReturnRqImageRepository;
import org.oplearn.project.repository.ReturnRqRepository;
import org.oplearn.project.service.ReturnRqService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReturnRqServiceImpl implements ReturnRqService {
  private static final int MAX_IMAGES = 5;

  private final ReturnRqRepository repository;
  private final ReturnRqImageRepository imageRepository;

  @Override
  @Transactional
  public ReturnRequestResponse create(ReturnRqRequest request) {
    log.info("(service) create return request: {}", request);

    if (request.getImages() != null && request.getImages().size() > MAX_IMAGES) {
      log.error("(service) images exceed max limit {}", MAX_IMAGES);
      throw new ReturnRequestImageLimitExceededException();
    }

    ReturnRequest rq = ReturnRequest.builder()
      .orderId(request.getOrderId())
      .reason(request.getReason())
      .description(request.getDescription())
      .resolutionType(request.getResolutionType())
      .status(ReturnRequestStatus.PENDING)
      .build();

    ReturnRequest savedRq = repository.save(rq);

    List<ReturnRequestImageResponse> imageResponses = List.of();
    if (request.getImages() != null && !request.getImages().isEmpty()) {
      List<ReturnRqImageRequest> imgRequests = request.getImages();

      List<ReturnRequestImage> images = IntStream.range(0, imgRequests.size())
        .mapToObj(i -> ReturnRequestImage.builder()
          .returnRequestId(savedRq.getId())
          .imageUrl(imgRequests.get(i).getImageUrl())
          .displayOrder(i)
          .build())
        .toList();

      List<ReturnRequestImage> savedImages = imageRepository.saveAll(images);

      imageResponses = savedImages.stream()
        .map(ReturnRequestImageResponse::from)
        .sorted(Comparator.comparing(ReturnRequestImageResponse::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
        .toList();
    }

    return ReturnRequestResponse.from(savedRq, imageResponses);
  }

  @Override
  public ReturnRequestResponse detail(Long id) {
    log.info("(service) detail return request: {}", id);

    ReturnRequest rq = repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(ReturnRequestNotFoundException::new);

    List<ReturnRequestImageResponse> images = imageRepository.findByReturnRequestIdAndIsDeletedFalseOrderByDisplayOrderAsc(rq.getId()).stream()
      .map(ReturnRequestImageResponse::from)
      .toList();

    return ReturnRequestResponse.from(rq, images);
  }

  @Override
  @Transactional
  public void adminUpdate(ReturnRqUpdateRequest request, Long id) {
    log.info("(service) admin update return request: {}, request: {}", id, request);

    ReturnRequest rq = this.available(id);

    rq.setStatus(request.getStatus());
    if (request.getStatus() == ReturnRequestStatus.REJECTED) {
      rq.setRejectReason(request.getRejectReason());
    }

    repository.save(rq);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("(service) delete return request: {}", id);

    ReturnRequest rq = this.available(id);

    if (rq.getStatus() != ReturnRequestStatus.PENDING) {
      log.warn("(delete) cannot delete return request {} with status {}", id, rq.getStatus());
      throw new ReturnRequestCannotBeDeletedException();
    }

    repository.softDeleteById(id);

    List<ReturnRequestImage> images = imageRepository.findByReturnRequestIdAndIsDeletedFalseOrderByDisplayOrderAsc(id);
    if (!images.isEmpty()) {
      images.forEach(img -> img.setIsDeleted(true));
      imageRepository.saveAll(images);
    }
  }

  @Override
  public PageResponse<ReturnRequestResponse> filter(
    ReturnRequestStatus status,
    ReturnResolutionType type,
    int size,
    int page
  ) {
    log.info("(service) list return request: status: {}, type: {}, size: {}, page: {}", status, type, size, page);

    Pageable pageable = PageRequest.of(page, size);

    Page<ReturnRequest> rq = repository.filter(status, type, pageable);

    List<Long> rqIds = rq.getContent().stream()
      .map(ReturnRequest::getId)
      .toList();

    Map<Long, List<ReturnRequestImageResponse>> imagesByRqIds = rqIds.isEmpty()
      ? Map.of()
      : imageRepository.findByReturnRequestIdInAndIsDeletedFalse(rqIds).stream()
      .map(ReturnRequestImageResponse::from)
      .collect(Collectors.groupingBy(ReturnRequestImageResponse::getReturnRequestId));

    Page<ReturnRequestResponse> pagedRq = rq.map(
      rqItem -> ReturnRequestResponse.from(rqItem, imagesByRqIds.get(rqItem.getId()))
    );

    return PageResponse.of(pagedRq);
  }

  @Override
  public PageResponse<ReturnRequestResponse> filterByUserId(Long userId, int size, int page) {
    log.info("(service) list return request: userId: {}, size: {}, page: {}", userId, size, page);

    Pageable pageable = PageRequest.of(page, size);

    Page<ReturnRequest> rq = repository.filterByUserId(userId , pageable);

    List<Long> rqIds = rq.getContent().stream()
      .map(ReturnRequest::getId)
      .toList();

    Map<Long, List<ReturnRequestImageResponse>> imagesByRqIds = rqIds.isEmpty()
      ? Map.of()
      : imageRepository.findByReturnRequestIdInAndIsDeletedFalse(rqIds).stream()
      .map(ReturnRequestImageResponse::from)
      .collect(Collectors.groupingBy(ReturnRequestImageResponse::getReturnRequestId));

    Page<ReturnRequestResponse> pagedRq = rq.map(
      rqItem -> ReturnRequestResponse.from(rqItem, imagesByRqIds.get(rqItem.getId()))
    );

    return PageResponse.of(pagedRq);
  }

  @Override
  public boolean hasPendingRequest(Long orderId) {
    return repository.existsByOrderIdAndStatusAndIsDeletedFalse(orderId, ReturnRequestStatus.PENDING);
  }

  @Override
  public boolean hasApprovedRequest(Long orderId) {
    return repository.existsByOrderIdAndStatusAndIsDeletedFalse(orderId, ReturnRequestStatus.APPROVED);
  }

  @Override
  public int countByOrderId(Long orderId) {
    return repository.countByOrderIdAndIsDeletedFalse(orderId);
  }

  private ReturnRequest available(Long id) {
    return repository.findByIdAndIsDeletedFalse(id)
      .orElseThrow(ReturnRequestNotFoundException::new);
  }
}
