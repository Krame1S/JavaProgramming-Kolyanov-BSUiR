package com.lab1.isthesiteup.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.lab1.isthesiteup.entities.Server;
import com.lab1.isthesiteup.repositories.ServerRepository;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerService serverService;

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
    

    // @Test
    // public void testUpdateServerUrl() {
    //     Long id = 1L;
    //     String newUrl = "http://newurl.com";
    //     Server server = new Server();
    //     server.setId(id);
    //     server.setUrl("http://oldurl.com");
    
    //     when(serverRepository.findById(id)).thenReturn(Optional.of(server));
    //     when(serverRepository.findByUrl(newUrl)).thenReturn(Optional.empty());
    //     when(serverRepository.save(any(Server.class))).thenReturn(server);
    
    //     Server updatedServer = serverService.updateServerUrl(id, newUrl);
    
    //     assertNotNull(updatedServer);
    //     assertEquals(newUrl, updatedServer.getUrl());
    //     verify(serverRepository, times(1)).findById(id);
    //     verify(serverRepository, times(1)).findByUrl(newUrl);
    //     verify(serverRepository, times(1)).save(any(Server.class));
    // }
    
    @Test
    void testDeleteServer() {
        Long id = 1L;
    
        doNothing().when(serverRepository).deleteById(id);
    
        serverService.deleteServer(id);
    
        verify(serverRepository, times(1)).deleteById(id);
    }
    
    // Добавьте другие тесты для методов addServer, updateServerUrl, deleteServer и т.д.
}
