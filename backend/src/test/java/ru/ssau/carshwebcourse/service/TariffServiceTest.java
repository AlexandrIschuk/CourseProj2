package ru.ssau.carshwebcourse.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.carshwebcourse.dto.TariffDto;
import ru.ssau.carshwebcourse.dto.UserDto;
import ru.ssau.carshwebcourse.entity.*;
import ru.ssau.carshwebcourse.exceptionHandler.NotFoundException;
import ru.ssau.carshwebcourse.mapping.DriveMappingUtils;
import ru.ssau.carshwebcourse.mapping.TariffMappingUtils;
import ru.ssau.carshwebcourse.mapping.UserMappingUtils;
import ru.ssau.carshwebcourse.repository.CarRepository;
import ru.ssau.carshwebcourse.repository.DriveRepository;
import ru.ssau.carshwebcourse.repository.TariffRepository;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class TariffServiceTest {

    private TariffRepository tariffRepository;
    private TariffMappingUtils tariffMappingUtils;
    private TariffService tariffService;
    @BeforeEach
    void setUp() {
        tariffRepository = mock(TariffRepository.class);
        tariffMappingUtils = mock(TariffMappingUtils.class);
        tariffService = new TariffService(tariffRepository, tariffMappingUtils);

    }
    @Test
    void testGetTariffById_NotFound() {
        when(tariffRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            tariffService.getTariffById(999L);
        });
    }
    @Test
    void testUpdateTariff() {
        Long tariffId = 1L;

        Tariff existingTariff = new Tariff();
        existingTariff.setTariffId(tariffId);
        existingTariff.setName("Old Name");
        existingTariff.setPrice(100L);
        existingTariff.setTariffType(TariffType.MINUTE);

        TariffDto updateDto = new TariffDto();
        updateDto.setName("New Name");
        updateDto.setPrice(200L);
        updateDto.setTariffType(TariffType.KILOMETER);

        when(tariffRepository.getTariffByTariffId(tariffId)).thenReturn(existingTariff);
        when(tariffRepository.save(existingTariff)).thenReturn(existingTariff);
        when(tariffMappingUtils.mapTariffToDto(existingTariff)).thenReturn(new TariffDto());

        TariffDto result = tariffService.updateTariff(tariffId, updateDto);

        assertNotNull(result);
        assertEquals("New Name", existingTariff.getName());
        assertEquals(200L, existingTariff.getPrice());
        assertEquals(TariffType.KILOMETER, existingTariff.getTariffType());

    }

}
