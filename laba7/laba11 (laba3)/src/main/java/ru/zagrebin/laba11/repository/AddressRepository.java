package ru.zagrebin.laba11.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.zagrebin.laba11.entity.Address;

import java.util.List;

@Repository
public interface AddressRepository  extends CrudRepository<Address, Long> {
    List<Address> findByCityContainingIgnoreCase(String city);
    List<Address> findByStreetContainingIgnoreCase(String street);
}
