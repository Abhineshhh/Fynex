package org.abhinesh.fynex.services;


import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.FinancialRecordRequest;
import org.abhinesh.fynex.dto.FinancialRecordResponse;
import org.abhinesh.fynex.entity.FinancialRecord;
import org.abhinesh.fynex.entity.User;
import org.abhinesh.fynex.enums.TransactionType;
import org.abhinesh.fynex.exception.ResourceNotFoundException;
import org.abhinesh.fynex.repository.FinancialRecordRepository;
import org.abhinesh.fynex.repository.UserRepository;
import org.abhinesh.fynex.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    // Create Record
    public FinancialRecordResponse createRecord(FinancialRecordRequest request){
        String email = SecurityUtils.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));


        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(currentUser)
                .deleted(false)
                .build();
        return mapToResponse(recordRepository.save(record));
    }

    // Get All records (with optional filters)
    public List<FinancialRecordResponse> getAllRecords(
            TransactionType type,
            String category,
            LocalDate startDate,
            LocalDate endDate
    ){
        List<FinancialRecord> records;

        if (type != null && startDate != null && endDate != null) {
            records = recordRepository.findByTypeAndDateBetweenAndDeletedFalse(type,startDate,endDate);
        } else if (type != null) {
            records = recordRepository.findByTypeAndDeletedFalse(type);
        } else if (category != null) {
            records = recordRepository.findByCategoryAndDeletedFalse(category);
        } else if (startDate != null && endDate != null) {
            records = recordRepository.findByDateBetweenAndDeletedFalse(startDate,endDate);
        } else {
            records = recordRepository.findByDeletedFalse();
        }

        return records.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Get by id
    public FinancialRecordResponse getRecordById(Long id){
        FinancialRecord record = findActiveRecord(id);
        return mapToResponse(record);
    }

    // Update records
    public FinancialRecordResponse updateRecord(Long id, FinancialRecordRequest request){
        FinancialRecord record = findActiveRecord(id);

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        return mapToResponse(recordRepository.save(record));
    }

    // Soft delete
    public String deleteRecord(Long id) {
        FinancialRecord record = findActiveRecord(id);
        record.setDeleted(true);
        recordRepository.save(record);
        return "Record deleted successfully";
    }

    // Private helpers
    private FinancialRecord findActiveRecord(Long id){
        return recordRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id : " + id));
    }

    private FinancialRecordResponse mapToResponse(FinancialRecord record){
        return FinancialRecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .notes(record.getNotes())
                .createdBy(record.getCreatedBy() != null
                        ? record.getCreatedBy().getUsername() : null)
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
