package com.lab1.isthesiteup.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.entities.Server;
import com.lab1.isthesiteup.repositories.CheckRepository;
import com.lab1.isthesiteup.repositories.ServerRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerService serverService;

    @Mock
    private CheckRepository checkRepository;
    

    @Test
    void testAddServer() {
        Server server = new Server();
        server.setUrl("http://example.com");

        when(serverRepository.findByUrl(server.getUrl())).thenReturn(Optional.empty());
        when(serverRepository.save(server)).thenReturn(server);

        Server result = serverService.addServer(server);

        assertNotNull(result);
        assertEquals(server.getUrl(), result.getUrl());
        verify(serverRepository, times(1)).findByUrl(server.getUrl());
        verify(serverRepository, times(1)).save(server);
    }

    @Test
    void testAddServer_ServerAlreadyExists() {
        Server server = new Server();
        server.setUrl("http://example.com");
    
        when(serverRepository.findByUrl(server.getUrl())).thenReturn(Optional.of(server));
    
        assertThrows(IllegalArgumentException.class, () -> {
            serverService.addServer(server);
        }, "A server with this URL already exists.");
    
        verify(serverRepository, times(1)).findByUrl(server.getUrl());
        verify(serverRepository, never()).save(any(Server.class));
    }

    @Test
    void testUpdateServerUrl() {
        // Prepare test data
        Long serverId = 1L;
        String newUrl = "http://newexample.com";
        String currentUrl = "http://example.com";
        Server server = new Server();
        server.setId(serverId);
        server.setUrl(currentUrl);
        List<Check> checksToRemove = Arrays.asList(new Check(), new Check());
    
        // Mock serverRepository to return the server by ID
        when(serverRepository.findById(serverId)).thenReturn(Optional.of(server));
        // Mock checkRepository to return checks for the server's current URL
        when(checkRepository.findByServerUrl(currentUrl)).thenReturn(checksToRemove);
        // Mock serverRepository to indicate that the new URL does not exist
        when(serverRepository.findByUrl(newUrl)).thenReturn(Optional.empty());
        // Mock serverRepository to return the updated server
        when(serverRepository.save(any(Server.class))).thenReturn(server);
    
        // Call the method under test
        Server updatedServer = serverService.updateServerUrl(serverId, newUrl);
    
        // Verify interactions
        verify(serverRepository, times(1)).findById(serverId);
        verify(checkRepository, times(1)).findByServerUrl(currentUrl); // Adjusted to expect the current URL
        verify(checkRepository, times(1)).deleteAll(checksToRemove);
        verify(serverRepository, times(1)).findByUrl(newUrl);
        verify(serverRepository, times(1)).save(any(Server.class));
    
        // Assert that the server's URL was updated
        assertEquals(newUrl, updatedServer.getUrl());
    }
    
    
    
    @Test
    void testDeleteServer() {
        Long id = 1L;
    
        doNothing().when(serverRepository).deleteById(id);
    
        serverService.deleteServer(id);
    
        verify(serverRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllServers() {
        List<Server> servers = new ArrayList<>();
        Server server1 = new Server();
        server1.setUrl("http://example1.com");
        servers.add(server1);
    
        when(serverRepository.findAll()).thenReturn(servers);
    
        List<Server> result = serverService.getAllServers();
    
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(server1.getUrl(), result.get(0).getUrl());
        verify(serverRepository, times(1)).findAll();
    }

    @Test
    void testFindServersByCheckStatus() {
        List<Server> servers = new ArrayList<>();
        Server server1 = new Server();
        server1.setUrl("http://example1.com");
        servers.add(server1);
    
        when(serverRepository.findServersByCheckStatus("Site is up")).thenReturn(servers);
    
        List<Server> result = serverService.findServersByCheckStatus("Site is up");
    
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(server1.getUrl(), result.get(0).getUrl());
        verify(serverRepository, times(1)).findServersByCheckStatus("Site is up");
    }
    
}
