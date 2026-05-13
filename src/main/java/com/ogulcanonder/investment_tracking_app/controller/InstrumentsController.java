package com.ogulcanonder.investment_tracking_app.controller;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoInstrumentsRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoInstrumentsResponse;
import com.ogulcanonder.investment_tracking_app.service.InstrumentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;

@RestController
@RequestMapping("/api/instruments/v1")
@RequiredArgsConstructor
public class InstrumentsController {
    private final InstrumentsService instrumentsService;

    @PostMapping
    public ResponseEntity<DtoInstrumentsResponse> createInstruments(
            @Valid @RequestBody DtoInstrumentsRequest dtoInstrumentsRequest) {
        DtoInstrumentsResponse dtoInstrumentsResponse = instrumentsService.create(dtoInstrumentsRequest);
        return new ResponseEntity<>(dtoInstrumentsResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DtoInstrumentsResponse>> getAllInstruments() {
        List<DtoInstrumentsResponse> dtoInstrumentsResponse = instrumentsService.getAll();
        return ResponseEntity.ok(dtoInstrumentsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoInstrumentsResponse> getInstrumentsById(@PathVariable Long id) {
        return ResponseEntity.ok(instrumentsService.getInstrumentsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateInstrumentsById(@PathVariable Long id,
                                                      @Valid @RequestBody DtoInstrumentsRequest dtoInstrumentsRequest) {
        instrumentsService.updateById(id, dtoInstrumentsRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstrumentsById(@PathVariable Long id) {
        instrumentsService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
