package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoLoginRequest;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoRegisterUserRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoAuthLoginResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoUserResponse;

public interface AuthenticationService {
    public DtoUserResponse register(DtoRegisterUserRequest dtoRegisterUserRequest);
    public DtoAuthLoginResponse login(DtoLoginRequest dtoLoginRequest);
    public String refreshToken(String refreshToken);
    public String logout(String authHeader);
}
