package com.ogulcanonder.investment_tracking_app.mapper;

import com.ogulcanonder.investment_tracking_app.dto.response.DtoProfileResponse;
import com.ogulcanonder.investment_tracking_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "username", expression = " java(user.getRealUsername())")
    DtoProfileResponse toDto(User user);
}
