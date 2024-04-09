package com.lab1.isthesiteup.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.lab1.isthesiteup.config.CacheConfig;
import com.lab1.isthesiteup.entities.Check;
import com.lab1.isthesiteup.repositories.CheckRepository;
import com.lab1.isthesiteup.repositories.ServerRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {CheckService.class, CacheConfig.class})
class CheckServiceTest {

    @Mock
    private CheckRepository checkRepository;

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private CheckService checkService;

    // @Test
    // public void testGetServerStatus() {
    //     String url = "http://example.com";
    //     Check check = new Check();
    //     check.setUrl(url);
    //     check.setStatus("Site is up");

    //     when(serverRepository.findByUrl(url)).thenReturn(Optional.empty());
    //     when(checkRepository.save(any(Check.class))).thenReturn(check);

    //     Check result = checkService.getServerStatus(url);

    //     assertNotNull(result);
    //     assertEquals(check.getStatus(), result.getStatus());
    //     verify(serverRepository, times(1)).findByUrl(url);
    //     verify(checkRepository, times(1)).save(any(Check.class));
    // }

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

    // @Test
    // public void testUpdateCheck() {
    //     Long id = 1L;
    //     Check check = new Check();
    //     check.setId(id);
    //     check.setUrl("http://example.com");
    //     check.setStatus("Site is up");
    
    //     when(checkRepository.findById(id)).thenReturn(Optional.of(check));
    //     when(checkRepository.save(any(Check.class))).thenReturn(check);
    
    //     checkService.updateCheck(id, check);
    
    //     verify(checkRepository, times(1)).findById(id);
    //     verify(checkRepository, times(1)).save(any(Check.class));
    // }

    // @Test
    // public void testDeleteCheck() {
    //     Long id = 1L;
    
    //     doNothing().when(checkRepository).deleteById(id);
    
    //     checkService.deleteCheck(id);
    
    //     verify(checkRepository, times(1)).deleteById(id);
    // }
    
    // Добавьте другие тесты для методов getServerStatus, saveCheck, updateCheck, deleteCheck и т.д.
}
