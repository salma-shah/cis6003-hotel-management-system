package mapper;

import dto.UserDTO;
import entity.User;

public class UserMapper {

    // converting dto to entity
    public static UserDTO toUserDTO(User user) {

        if (user == null) {
            return null;
        }

        return new UserDTO.UserDTOBuilder()
                .userId(user.getUserId())
                .username(user.getUsername())
               // .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contactNumber(user.getContactNumber())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    // converting from entity to dto now
    public static User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return new User.UserBuilder()
                .userId(userDTO.getUserDTOId())
                .username(userDTO.getUserDTOUsername())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .contactNumber(userDTO.getContactNumber())
                .role(userDTO.getRole())
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .deletedAt(userDTO.getDeletedAt())
                .build();
    }
}

