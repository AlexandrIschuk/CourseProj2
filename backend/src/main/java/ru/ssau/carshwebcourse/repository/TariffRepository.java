package ru.ssau.carshwebcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ssau.carshwebcourse.entity.Tariff;

public interface TariffRepository extends JpaRepository<Tariff,Long> {
    Tariff getTariffByTariffId(Long tariffId);
}
