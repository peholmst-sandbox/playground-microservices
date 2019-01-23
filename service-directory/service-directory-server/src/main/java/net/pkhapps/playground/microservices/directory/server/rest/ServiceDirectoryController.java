package net.pkhapps.playground.microservices.directory.server.rest;

import net.pkhapps.playground.microservices.directory.api.*;
import net.pkhapps.playground.microservices.directory.server.application.ServiceDirectoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
class ServiceDirectoryController {

    private final ServiceDirectoryService serviceDirectoryService;

    ServiceDirectoryController(ServiceDirectoryService serviceDirectoryService) {
        this.serviceDirectoryService = serviceDirectoryService;
    }

    @PostMapping("/service")
    public void registerService(@RequestBody ServiceRegistration registration) {
        serviceDirectoryService.registerService(registration);
    }

    @PostMapping("/frontend")
    public void registerFrontend(@RequestBody FrontendRegistration registration) {
        serviceDirectoryService.registerFrontend(registration);
    }

    @PostMapping("/service/instances")
    public ResponseEntity<Void> registerInstance(@RequestBody ServiceInstanceRegistration registration) {
        return registerInstance(() -> serviceDirectoryService.registerInstance(registration));
    }

    @PostMapping("/frontend/instances")
    public ResponseEntity<Void> registerInstance(@RequestBody FrontendInstanceRegistration registration) {
        return registerInstance(() -> serviceDirectoryService.registerInstance(registration));
    }

    private ResponseEntity<Void> registerInstance(Runnable method) {
        try {
            method.run();
            return ResponseEntity.ok().build();
        } catch (NoSuchResourceException ex) {
            return ResponseEntity.notFound().build();
        } catch (InvalidSignatureException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/service/status")
    public List<ServiceStatus> getServiceStatus() {
        return serviceDirectoryService.getServiceStatus();
    }

    @GetMapping("/frontend/status")
    public List<FrontendStatus> getFrontendStatus() {
        return serviceDirectoryService.getFrontendStatus();
    }
}
