package org.fakechitor.cloudfilestorage.dto.mapper

import org.fakechitor.cloudfilestorage.dto.request.UserRequestDto
import org.fakechitor.cloudfilestorage.dto.response.UserResponseDto
import org.fakechitor.cloudfilestorage.model.User
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    fun convertToModel(userRequestDto: UserRequestDto): User

    fun convertToDto(user: User): UserResponseDto
}
