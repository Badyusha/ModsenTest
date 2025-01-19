package com.modsen.userstorageservice.services;

import com.modsen.commonmodels.exceptions.NoSuchUserException;
import com.modsen.commonmodels.exceptions.ValidationException;
import com.modsen.userstorageservice.enums.Role;
import com.modsen.userstorageservice.mappers.UserDTOMapper;
import com.modsen.userstorageservice.mappers.UserMapper;
import com.modsen.userstorageservice.models.dtos.UserDTO;
import com.modsen.userstorageservice.models.entities.User;
import com.modsen.userstorageservice.repositories.UserRepository;
import com.modsen.userstorageservice.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final UserMapper userMapper;
    private final UserDTOMapper userDTOMapper;
    private final UserRepository userRepository;

    public UserDTO authorizeUser(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = EncryptionUtil.hashString(userDTO.getPassword());

        return userRepository.findByUsernameAndPassword(username, password)
                .map(userMapper::toBookDTO)
                .orElseThrow(() ->
                            new NoSuchUserException("Unable to authorize user: username or password are incorrect"));
    }

    public UserDTO registerUser(UserDTO userDTO) throws ValidationException {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        Role role = userDTO.getRole();
        if(username.isBlank() || password.isBlank() || role == null) {
            throw new ValidationException("Unable to register user: some fields are blank");
        }

        userDTO.setPassword(EncryptionUtil.hashString(password));
        User user = userDTOMapper.toBook(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toBookDTO(savedUser);
    }

}
