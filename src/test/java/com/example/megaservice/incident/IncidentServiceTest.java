package com.example.megaservice.incident;

import com.example.megaservice.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock IncidentRepository incidents;
    @Mock IncidentCommentRepository comments;
    @Mock AttachmentRepository attachments;
    @Mock StorageService storage;

    @InjectMocks IncidentService service;

    @Test
    void create_defaults_and_severity() {
        when(incidents.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Incident i = service.create("X", "Y", Incident.Severity.HIGH);
        assertEquals(Incident.Status.OPEN, i.getStatus());
        assertEquals(Incident.Severity.HIGH, i.getSeverity());
    }

    @Test
    void list_by_status() {
        when(incidents.findByStatus(eq(Incident.Status.OPEN), any())).thenReturn(new PageImpl<>(List.of(new Incident()), PageRequest.of(0,10), 1));
        assertEquals(1, service.list(Incident.Status.OPEN, PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    void add_attachment_uses_storage() throws Exception {
        when(incidents.findById(1L)).thenReturn(Optional.of(new Incident()));
        when(storage.store(any(), any(), any(), anyLong(), any())).thenReturn("k");
        // We don't use MultipartFile here in unit test; just ensure storage is called via service logic
        service.addAttachment(1L, new org.springframework.mock.web.MockMultipartFile("f","a.txt","text/plain", new byte[]{1,2,3}));
        verify(storage).store(startsWith("incidents/1"), eq("a.txt"), eq("text/plain"), anyLong(), any());
    }
}
