package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.mapper.DateMapper;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface CommentMapper {

    @Mapping(source = "author.name", target = "authorName")
    CommentDto mapToCommentDto(Comment comment);
}