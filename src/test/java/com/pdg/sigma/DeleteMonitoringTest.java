package com.pdg.sigma;

import com.pdg.sigma.domain.Monitoring;
import com.pdg.sigma.domain.MonitoringMonitor;
import com.pdg.sigma.repository.MonitoringMonitorRepository;
import com.pdg.sigma.repository.MonitoringRepository;
import com.pdg.sigma.service.MonitoringServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class DeleteMonitoringTest {

    @Mock
    private MonitoringRepository monitoringRepository;

    @Mock
    private MonitoringMonitorRepository monitoringMonitorRepository;

    @InjectMocks
    private MonitoringServiceImpl monitoringService;

    @Test
    void deleteMonitoring_shouldReturnFalse_whenMonitoringNotFound() {
        long id = 1L;
        Mockito.when(monitoringRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = monitoringService.deleteMonitoring(id);

        assertFalse(result);
        Mockito.verify(monitoringRepository, never()).delete(any());
    }

    @Test
    void deleteMonitoring_shouldReturnFalse_whenMonitoringHasMonitors() {
        long id = 1L;
        Monitoring monitoring = new Monitoring();
        Mockito.when(monitoringRepository.findById(id)).thenReturn(Optional.of(monitoring));
        Mockito.when(monitoringMonitorRepository.findByMonitoring(monitoring))
                .thenReturn(List.of(new MonitoringMonitor()));

        boolean result = monitoringService.deleteMonitoring(id);

        assertFalse(result);
        Mockito.verify(monitoringRepository, never()).delete(any());
    }

    @Test
    void deleteMonitoring_shouldReturnTrue_whenMonitoringExistsAndHasNoMonitors() {
        long id = 1L;
        Monitoring monitoring = new Monitoring();
        Mockito.when(monitoringRepository.findById(id)).thenReturn(Optional.of(monitoring));
        Mockito.when(monitoringMonitorRepository.findByMonitoring(monitoring))
                .thenReturn(Collections.emptyList());

        boolean result = monitoringService.deleteMonitoring(id);

        assertTrue(result);
        Mockito.verify(monitoringRepository).delete(monitoring);
    }
}

