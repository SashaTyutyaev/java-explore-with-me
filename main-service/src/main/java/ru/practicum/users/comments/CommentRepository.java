package ru.practicum.users.comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.admin.users.model.User;
import ru.practicum.users.comments.model.Comment;
import ru.practicum.users.events.model.Event;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByUser(User user, Pageable pageable);

    List<Comment> getCommentsByEvent(Event event, Pageable pageable);
}
