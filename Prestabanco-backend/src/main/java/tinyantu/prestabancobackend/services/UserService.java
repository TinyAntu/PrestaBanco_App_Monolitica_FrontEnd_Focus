package tinyantu.prestabancobackend.services;

import tinyantu.prestabancobackend.entities.UserEntity;
import tinyantu.prestabancobackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tinyantu.prestabancobackend.exceptions.BadRequestException;



import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserEntity findUserById(Long Id){
        return userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    public UserEntity saveUser(UserEntity user) {
        // Validar que el email no esté vacío y tenga formato correcto
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BadRequestException("El formato del correo es inválido.");
        }

        if(AgeInYears(user.getBirthdate()) < 18){
            throw new BadRequestException("Tiene que ser mayor de edad.");
        }
        // Validar que la contraseña no esté vacía y tenga un mínimo de 8 caracteres
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new BadRequestException("La contraseña debe tener al menos 8 caracteres.");
        }
        if(user.getRut() == null || user.getRut().isEmpty()){
            throw new BadRequestException("El RUT no puede ser nulo.");
        }

        if(!isValidRutFormat(user.getRut())){
            throw new BadRequestException("El rut no puede tener contener mas caracteres aparte de K ");
        }

        // Validar que el RUT no esté duplicado
        if (userRepository.findByRut(user.getRut()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RUT ya está registrado.");
        }



        // Validar otros campos obligatorios
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new BadRequestException("El nombre es obligatorio.");
        }

        // Si pasa todas las validaciones, guarda el usuario
        return userRepository.save(user);
    }


    public UserEntity authenticateUser(String Rut, String Password) {
        Optional<UserEntity> user = userRepository.findByRut(Rut);

        if (user.isPresent()) {
            if (user.get().getPassword().equals(Password)) {
                return user.get();
            } else {

                System.out.println("Incorrect password for user with Rut: " + Rut);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
            }
        } else {

            System.out.println("User not found for Rut: " + Rut);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public int AgeInYears(Date birthdate) {
        LocalDate Local_birthdate;
        if (birthdate instanceof java.sql.Date) {
            Local_birthdate = ((java.sql.Date) birthdate).toLocalDate();
        } else {
            Local_birthdate = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        LocalDate Actual_date = LocalDate.now();
        return Period.between(Local_birthdate, Actual_date).getYears();
    }

    public static boolean isValidRutFormat(String rut) {
        if (rut == null || rut.isEmpty()) {
            return false; // El RUT no puede ser nulo o vacío.
        }

        // Expresión regular:
        // Acepta números separados opcionalmente por puntos (máximo 8 dígitos), un guion opcional y un dígito/K
        return rut.matches("\\d{1,3}(\\.\\d{3}){0,2}-?[\\dkK]?");
    }
}
