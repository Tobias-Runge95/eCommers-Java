package com.ShopingCart.dreamshops.Controller;

import com.ShopingCart.dreamshops.DTO.Image.UserDto;
import com.ShopingCart.dreamshops.Response.ApiResponse;
import com.ShopingCart.dreamshops.exceptions.AlreadyExistsException;
import com.ShopingCart.dreamshops.exceptions.ResourceNotFoundException;
import com.ShopingCart.dreamshops.model.User;
import com.ShopingCart.dreamshops.request.User.CreateUserRequest;
import com.ShopingCart.dreamshops.request.User.UpdateUserRequest;
import com.ShopingCart.dreamshops.service.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/user")
public class UserController {
    private final IUserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest user) {
        try{
            User newUser = userService.createUser(user);
            return ResponseEntity.ok(new ApiResponse("User Created", newUser));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("User already exists", null));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        try{
            User user = userService.getUserById(id);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest user, @PathVariable Long userId) {
        try{
            User updateUser = userService.updateUser(user, userId);
            UserDto userDto = userService.convertUserToDto(updateUser);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
