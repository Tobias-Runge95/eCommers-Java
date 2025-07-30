package com.ShopingCart.dreamshops.service.User;

import com.ShopingCart.dreamshops.DTO.Image.UserDto;
import com.ShopingCart.dreamshops.model.User;
import com.ShopingCart.dreamshops.request.User.CreateUserRequest;
import com.ShopingCart.dreamshops.request.User.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
