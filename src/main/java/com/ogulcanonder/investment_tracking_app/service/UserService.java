package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoUpdatePasswordRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoProfileResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoUserResponse;
import com.ogulcanonder.investment_tracking_app.entity.User;

import java.util.Optional;

public interface UserService {
    public void create(User user);

    public Optional<User> findByEmail(String email);

    public void resetPassword(User user, String newPassword);

    public void updatePassword(String email, DtoUpdatePasswordRequest dtoUpdatePasswordRequest);

    public User getUserByEmail(String email);

    public User getCurrentUser();

    public DtoProfileResponse getUserProfile();
}
