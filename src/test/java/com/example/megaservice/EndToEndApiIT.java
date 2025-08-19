package com.example.megaservice;

import com.example.megaservice.incident.Incident;
import com.example.megaservice.user.AuthRequest;
import com.example.megaservice.user.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EndToEndApiIT {

    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;
    String base(){ return "http://localhost:"+port; }

    @Test
    void signup_login_then_create_incident_and_health() {
        // signup
        var signup = rest.postForEntity(
                base()+"/api/v1/auth/signup",
                new AuthRequest("user@example.com","pw"),
                AuthResponse.class);
        assertEquals(HttpStatus.CREATED, signup.getStatusCode());
        String token = signup.getBody().token();

        // bearer header
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(token);

        // create incident
        var created = rest.exchange(
                base()+"/api/v1/incidents",
                HttpMethod.POST,
                new HttpEntity<>(new com.example.megaservice.incident.IncidentCreateRequest("X","Y", Incident.Severity.MEDIUM), h),
                Incident.class);
        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        Long id = created.getBody().getId();
        assertNotNull(id);

        // acknowledge (use POST to avoid PATCH limitation)
        var ack = rest.exchange(
                base()+"/api/v1/incidents/{id}/acknowledge",
                HttpMethod.POST,
                new HttpEntity<>(null, h),
                Incident.class,
                id);
        assertEquals(Incident.Status.ACKNOWLEDGED, ack.getBody().getStatus());

        // resolve (use POST)
        var res = rest.exchange(
                base()+"/api/v1/incidents/{id}/resolve",
                HttpMethod.POST,
                new HttpEntity<>(null, h),
                Incident.class,
                id);
        assertEquals(Incident.Status.RESOLVED, res.getBody().getStatus());

        // health
        var health = rest.getForEntity(base()+"/actuator/health/liveness", String.class);
        assertEquals(HttpStatus.OK, health.getStatusCode());
        assertTrue(health.getBody().contains("\"status\":\"UP\""));
    }
}
