package com.lab1.isthesiteup.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.lab1.isthesiteup.config.CacheConfig;
import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.entities.Server;
import com.lab1.isthesiteup.repositories.CheckRepository;
import com.lab1.isthesiteup.repositories.ServerRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {CheckService.class, CacheConfig.class})
class CheckServiceTest {

    @Mock
    private CheckRepository checkRepository;

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private CheckService checkService;

    @Mock
    private CacheConfig cacheConfig;
    

    
    @Test
    void testSaveCheck() {
        Check check = new Check();
        check.setUrl("http://example.com");
        check.setStatus("Site is up");
    
        when(checkRepository.save(any(Check.class))).thenReturn(check);
    
        Check savedCheck = checkService.saveCheck(check);
    
        assertNotNull(savedCheck);
        assertEquals(check.getStatus(), savedCheck.getStatus());
        verify(checkRepository, times(1)).save(any(Check.class));
    }

    @Test
    void testGetAllChecks() {
        List<Check> checks = new ArrayList<>();
        Check check1 = new Check();
        check1.setUrl("http://example1.com");
        check1.setStatus("Site is up");
        checks.add(check1);
    
        when(checkRepository.findAll()).thenReturn(checks);
    
        List<Check> result = checkService.getAllChecks();
    
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(check1.getStatus(), result.get(0).getStatus());
        verify(checkRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCheck() {
        Check check = new Check();
        check.setId(1L);
        check.setUrl("http://example.com");
        check.setStatus("Site is up");
    
        when(checkRepository.findById(1L)).thenReturn(Optional.of(check));
        when(serverRepository.findByUrl(anyString())).thenReturn(Optional.of(new Server()));
        when(checkRepository.save(any(Check.class))).thenReturn(check);
    
        checkService.updateCheck(1L, check);
    
        verify(checkRepository, times(1)).findById(1L);
        verify(serverRepository, times(2)).findByUrl(anyString());
        verify(checkRepository, times(2)).save(any(Check.class));
    }

    @Test
    void testUpdateCheck_CreatesNewServer() {
        // Prepare test data
        Long checkId = 1L;
        String newUrl = "http://newexample.com";
        Check check = new Check();
        check.setId(checkId);
        check.setUrl(newUrl);
    
        // Mock checkRepository to return the check for the given ID
        when(checkRepository.findById(checkId)).thenReturn(Optional.of(check));
    
        // Mock serverRepository to indicate that the server URL does not exist
        when(serverRepository.findByUrl(newUrl)).thenReturn(Optional.empty());
    
        // Mock serverRepository to return a new server when save is called
        Server newServer = new Server();
        newServer.setUrl(newUrl);
        when(serverRepository.save(any(Server.class))).thenReturn(newServer);
    
        // Call the method under test
        checkService.updateCheck(checkId, check);
    
        // Verify interactions
        verify(checkRepository, times(1)).findById(checkId);
        verify(serverRepository, times(2)).findByUrl(newUrl);
        verify(serverRepository, times(2)).save(any(Server.class));
        verify(cacheConfig, times(1)).remove(newUrl);
    }

    @Test
    void testUpdateCheck_CheckExists() {
        // Prepare test data
        Long checkId = 1L;
        String newUrl = "http://newexample.com";
        Check check = new Check();
        check.setId(checkId);
        check.setUrl(newUrl);
    
        // Mock checkRepository to return the check for the given ID
        when(checkRepository.findById(checkId)).thenReturn(Optional.of(check));
    
        // Mock serverRepository to indicate that the server URL does not exist
        when(serverRepository.findByUrl(newUrl)).thenReturn(Optional.empty());
    
        // Mock serverRepository to return a new server when save is called
        Server newServer = new Server();
        newServer.setUrl(newUrl);
        when(serverRepository.save(any(Server.class))).thenReturn(newServer);
    
        // Call the method under test
        checkService.updateCheck(checkId, check);
    
        // Verify interactions
        verify(checkRepository, times(1)).findById(checkId);
        verify(serverRepository, times(2)).findByUrl(newUrl);
        verify(serverRepository, times(2)).save(any(Server.class));
        verify(cacheConfig, times(1)).remove(newUrl);
    }
    
    
    @Test
    void testDeleteCheck() {
        Check check = new Check();
        check.setId(1L);
        check.setUrl("http://example.com");
        check.setStatus("Site is up");
    
        when(checkRepository.findById(1L)).thenReturn(Optional.of(check));
        doNothing().when(checkRepository).deleteById(1L);
    
        checkService.deleteCheck(1L);
    
        verify(checkRepository, times(1)).findById(1L);
        verify(checkRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCheck_CheckDoesNotExist() {
        // Prepare test data
        Long checkId = 1L;
    
        // Mock checkRepository to indicate that the check does not exist
        when(checkRepository.findById(checkId)).thenReturn(Optional.empty());
    
        // Call the method under test
        checkService.deleteCheck(checkId);
    
        // Verify interactions
        verify(checkRepository, times(1)).findById(checkId);
        verify(checkRepository, never()).deleteById(anyLong());
        verify(cacheConfig, never()).remove(anyString());
    }
    
 
    @Test
    void testBulkUpdateServerStatusThroughChecks() {
        // Prepare test data
        List<Long> serverIds = Arrays.asList(1L, 2L);
        String newStatus = "Site is down";
        Server server1 = new Server();
        server1.setId(1L);
        server1.setUrl("http://example1.com");
        Server server2 = new Server();
        server2.setId(2L);
        server2.setUrl("http://example2.com");
        List<Check> checks1 = Arrays.asList(new Check(), new Check());
        List<Check> checks2 = Arrays.asList(new Check(), new Check());
    
        // Mock serverRepository to return servers by ID
        when(serverRepository.findById(1L)).thenReturn(Optional.of(server1));
        when(serverRepository.findById(2L)).thenReturn(Optional.of(server2));
    
        // Mock checkRepository to return checks for each server URL
        when(checkRepository.findByServerUrl(server1.getUrl())).thenReturn(checks1);
        when(checkRepository.findByServerUrl(server2.getUrl())).thenReturn(checks2);
    
        // Call the method under test
        checkService.bulkUpdateServerStatusThroughChecks(serverIds, newStatus);
    
        // Verify interactions
        verify(serverRepository, times(2)).findById(anyLong());
        verify(checkRepository, times(2)).findByServerUrl(anyString());
        verify(checkRepository, times(4)).save(any(Check.class)); // 2 servers * 2 checks each
    }
    
}
