package ru.practicum.shareit.commentTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTest {

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private CommentMapper commentMapper;

    private Comment comment;
    private CommentDto commentDto;
    private CommentCreateDto commentCreateDto;

    @BeforeEach
    void setUp() {
        commentCreateDto = new CommentCreateDto(1L, "commentDes", LocalDateTime.now());
        comment = new Comment(1L, "commentDes", null, null, commentCreateDto.getCreated());
        commentDto = new CommentDto(1L, "commentDes", "user1", comment.getCreated());
    }

    @Test
    void toCommentTest() {
        Comment actualComment = commentMapper.toComment(commentCreateDto);
        assertEquals(comment, actualComment);
    }

    @Test
    void toCommentDtoTest() {
        Request request = new Request(1L, "requestDes", new User(), LocalDateTime.now());
        Comment comment = new Comment(1L, "commentText", new Item(1L, "item1", "item1Des",
                true, new User(), request),
                new User(1L, "user1", "user1@gmail.com"), LocalDateTime.now());
        CommentDto expectedDto = new CommentDto(1L, "commentText", "user1", comment.getCreated());

        CommentDto actualDto = commentMapper.toCommentDto(comment);

        assertEquals(expectedDto, actualDto);
    }


}