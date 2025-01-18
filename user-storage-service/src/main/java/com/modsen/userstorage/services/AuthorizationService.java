package com.modsen.userstorage.services;

import com.modsen.commonmodels.exceptions.NoSuchUserException;
import com.modsen.commonmodels.exceptions.ValidationException;
import com.modsen.userstorage.enums.Role;
import com.modsen.userstorage.mappers.UserDTOMapper;
import com.modsen.userstorage.mappers.UserMapper;
import com.modsen.userstorage.models.dtos.UserDTO;
import com.modsen.userstorage.models.entities.User;
import com.modsen.userstorage.repositories.UserRepository;
import com.modsen.userstorage.utils.EncryptionUtil;
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
