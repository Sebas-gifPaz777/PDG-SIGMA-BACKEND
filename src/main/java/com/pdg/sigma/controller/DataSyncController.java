package com.pdg.sigma.controller;

import com.pdg.sigma.service.DataSyncService;
import com.pdg.sigma.dto.UpdateRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://pdg-sigma.vercel.app/")
//@CrossOrigin(origins = {"http://localhost:3000", "https://pdg-sigma.vercel.app/"})
//@CrossOrigin(origins = {"http://200.3.193.229:3000", "https://pdg-sigma.vercel.app/"})

@RequestMapping("/api/sync")
public class DataSyncController {

    private static final Logger logger = LoggerFactory.getLogger(DataSyncController.class);
    private final DataSyncService dataSyncService;

    public DataSyncController(DataSyncService dataSyncService) {
        this.dataSyncService = dataSyncService;
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateSystem(@RequestBody UpdateRequestDTO request) {

        try {
            String result = dataSyncService.syncData(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error en la actualización: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error en la actualización: " + e.getMessage());
        }
    }
}
