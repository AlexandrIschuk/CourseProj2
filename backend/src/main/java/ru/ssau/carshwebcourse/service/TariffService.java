package ru.ssau.carshwebcourse.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import ru.ssau.carshwebcourse.dto.TariffDto;
import ru.ssau.carshwebcourse.entity.Tariff;
import ru.ssau.carshwebcourse.exceptionHandler.NotFoundException;
import ru.ssau.carshwebcourse.mapping.TariffMappingUtils;
import ru.ssau.carshwebcourse.repository.TariffRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffService {
    private final TariffRepository tariffRepository;
    private final TariffMappingUtils tariffMappingUtils;


    public TariffService(TariffRepository tariffRepository, TariffMappingUtils tariffMappingUtils){
        this.tariffRepository = tariffRepository;
        this.tariffMappingUtils = tariffMappingUtils;

    }
    public TariffDto newTariff(TariffDto tariffDto){
        return tariffMappingUtils.mapTariffToDto(tariffRepository.save(tariffMappingUtils.mapTariffToEntity(tariffDto)));
    }

    public TariffDto updateTariff(Long id, TariffDto tariffDto){
        Tariff tariff = tariffRepository.getTariffByTariffId(id);
        tariff.setName(tariffDto.getName());
        tariff.setPrice(tariffDto.getPrice());
        tariff.setTariffType(tariffDto.getTariffType());
        tariffRepository.save(tariff);
        return tariffMappingUtils.mapTariffToDto(tariff);

    }

    public  TariffDto getTariffById(Long id){
        return tariffMappingUtils.mapTariffToDto(tariffRepository.findById(id).orElseThrow(() -> new NotFoundException("Tariff not found")));
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
