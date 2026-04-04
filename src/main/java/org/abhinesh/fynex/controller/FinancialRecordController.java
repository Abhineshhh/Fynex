package org.abhinesh.fynex.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.abhinesh.fynex.dto.ApiResponse;
import org.abhinesh.fynex.dto.FinancialRecordRequest;
import org.abhinesh.fynex.dto.FinancialRecordResponse;
import org.abhinesh.fynex.enums.TransactionType;
import org.abhinesh.fynex.services.FinancialRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService recordService;

    // ADMIN only - create record
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FinancialRecordResponse>> createRecord(
            @Valid @RequestBody FinancialRecordRequest request
            ){
        return ResponseEntity.status(201).body(ApiResponse.success("Record created successfully",recordService.createRecord(request)));

    }

    // ADMIN, ANALYST, VIEWER — get all with optional filter
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public ResponseEntity<ApiResponse<List<FinancialRecordResponse>>> getAllRecords(
            @RequestParam(required = false)TransactionType type,
            @RequestParam(required = false)String category,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate
            ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Records fetched successfully",
                recordService.getAllRecords(type,category,startDate,endDate)
                )
        );
    }

    // ADMIN, ANALYST, VIEWER — get single record
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public ResponseEntity<ApiResponse<FinancialRecordResponse>> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Record fetched successfully", recordService.getRecordById(id)));
    }

    // ADMIN only — update record
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FinancialRecordResponse>> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody FinancialRecordRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Record updated successfully", recordService.updateRecord(id, request)));
    }

    // ADMIN only — delete record
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Records deleted successfully"));
    }


}
