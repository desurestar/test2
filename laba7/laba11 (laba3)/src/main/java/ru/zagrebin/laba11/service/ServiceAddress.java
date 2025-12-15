package ru.zagrebin.laba11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zagrebin.laba11.dto.AddressDto;
import ru.zagrebin.laba11.entity.Address;
import ru.zagrebin.laba11.repository.AddressRepository;

import java.util.List;

@Service
public class ServiceAddress {
    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findAllAddress() {
        return (List<Address>) addressRepository.findAll();
    }

    public Address findAddressById(Long id) {
        return addressRepository.findById(id).get();
    }

    public void SaveAddress(Address address) {
        addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}