package ru.practicum.shareit.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.Storage.CommentRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getAllByItemId(Long itemId) {
        return commentRepository.getAllByItemId(itemId);
    }

    public List<Comment> findAllByItemIn(List<Item> items) {
        return commentRepository.findAllByItemIn(items);
    }

}
