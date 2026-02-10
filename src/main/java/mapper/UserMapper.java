package mapper;

import dto.UserCredentialDTO;
import dto.UserDTO;
import entity.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    // converting dto to entity
    public static UserDTO toUserDTO(User user) {

        if (user == null) {
            return null;
        }

        return new UserDTO.UserDTOBuilder()
                .userId(Integer.parseInt(user.getUserId()))
                .username(user.getUsername())
               // .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contactNumber(user.getContactNumber())
                .role(user.getRole())
                .address(user.getAddress())
                .build();
    }

    // converting from entity to dto now
    public static User toUser(UserDTO userDTO, UserCredentialDTO userCredentialDTO, String hashedPassword) {
        if (userDTO == null) {
            return null;
        }

        return new User.UserBuilder()
                .userId(String.valueOf(userDTO.getUserId()))
                .username(userCredentialDTO.getUsername())
                .password(hashedPassword)
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .address(userDTO.getAddress())
                .contactNumber(userDTO.getContactNumber())
                .role(userDTO.getRole())
                .build();
    }

    // converting list of Users to list of UserDTos
    public static List<UserDTO> toDTOList(List<User> users) {
            if (users == null) {
                return null;
            }
            return users.stream()
                    .map(UserMapper::toUserDTO)
                    .collect(Collectors.toList());
        }

    public static User toUpdatedUser(User existingUser, UserDTO updatedDTO) {

        if (existingUser == null || updatedDTO == null) {
            return null;
        }

        return new User.UserBuilder()
                .userId(existingUser.getUserId())
                .firstName(updatedDTO.getFirstName())
                .lastName(updatedDTO.getLastName())
                .address(updatedDTO.getAddress())
                .contactNumber(updatedDTO.getContactNumber())
                .build();
    }

}

