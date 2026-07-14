package com.ogulcanonder.investment_tracking_app.service;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoRegisterUserRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoUserResponse;

public interface AuthenticationService {
    public DtoUserResponse register(DtoRegisterUserRequest dtoRegisterUserRequest);
}
