package pl.kflorczyk.onlineshopbackend.validators;

import pl.kflorczyk.onlineshopbackend.dto.CompanyAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.PersonAddressDTO;
import pl.kflorczyk.onlineshopbackend.dto.UserAddressDTO;

public class UserAddressValidator {

    public boolean validate(UserAddressDTO userAddressDTO) {
        if(userAddressDTO.getCity() == null || userAddressDTO.getCity().isEmpty()) return false;
        if(userAddressDTO.getHouseNumber() <= 0) return false;
        if(userAddressDTO.getZipCode() == null) return false; // it's just a simple CRUD app so this enough

        if(userAddressDTO instanceof PersonAddressDTO) {
            PersonAddressDTO dto = (PersonAddressDTO) userAddressDTO;

            if(dto.getFirstName() == null || dto.getFirstName().isEmpty()) return false;
            if(dto.getLastName() == null || dto.getLastName().isEmpty()) return false;
        } else if(userAddressDTO instanceof CompanyAddressDTO) {
            CompanyAddressDTO dto = (CompanyAddressDTO) userAddressDTO;
            if(dto.getName() == null || dto.getName().isEmpty()) return false;
            if(dto.getNip() == null || dto.getNip().length() != 10) return false;
        }

        return true;
    }
}
