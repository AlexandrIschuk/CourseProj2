package ru.ssau.carshwebcourse.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import ru.ssau.carshwebcourse.dto.TariffDto;
import ru.ssau.carshwebcourse.entity.Tariff;
import ru.ssau.carshwebcourse.mapping.TariffMappingUtils;
import ru.ssau.carshwebcourse.repository.TariffRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffService {
    private final TariffRepository tariffRepository;
    private final TariffMappingUtils tariffMappingUtils;
    private final ApplicationArguments applicationArguments;

    public TariffService(TariffRepository tariffRepository, TariffMappingUtils tariffMappingUtils, ApplicationArguments applicationArguments){
        this.tariffRepository = tariffRepository;
        this.tariffMappingUtils = tariffMappingUtils;
        this.applicationArguments = applicationArguments;
    }
    public TariffDto newTariff(TariffDto tariffDto){
        return tariffMappingUtils.mapTariffToDto(tariffRepository.save(tariffMappingUtils.mapTariffToEntity(tariffDto)));
    }

    public  TariffDto getTariffById(Long id){
        return tariffMappingUtils.mapTariffToDto(tariffRepository.getTariffByTariffId(id));
    }

    public List<TariffDto> findAllTariffs(){
        List<Tariff> tariffs = tariffRepository.findAll();
        return tariffs.stream()
                .map(tariffMappingUtils::mapTariffToDto).collect(Collectors.toList());

    }



    public void deleteTariff(Long id){
        tariffRepository.deleteById(id);
    }
}
