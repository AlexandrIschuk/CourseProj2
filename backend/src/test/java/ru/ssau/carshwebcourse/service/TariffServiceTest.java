package ru.ssau.carshwebcourse.service;
import org.junit.jupiter.api.Test;
import ru.ssau.carshwebcourse.exceptionHandler.NotFoundException;
import ru.ssau.carshwebcourse.mapping.TariffMappingUtils;
import ru.ssau.carshwebcourse.repository.TariffRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class TariffServiceTest {

    @Test
    void testGetTariffById_NotFound() {
        TariffRepository tariffRepository = mock(TariffRepository.class);
        TariffMappingUtils tariffMappingUtils = mock(TariffMappingUtils.class);

        when(tariffRepository.findById(999L)).thenReturn(Optional.empty());

        TariffService service = new TariffService(tariffRepository, tariffMappingUtils);

        assertThrows(NotFoundException.class, () -> {
            service.getTariffById(999L);
        });
    }

}
