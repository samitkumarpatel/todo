package dev.efullstack.todo.routers;

import dev.efullstack.todo.models.Tag;
import dev.efullstack.todo.models.Task;
import dev.efullstack.todo.services.TagService;
import dev.efullstack.todo.services.TaskService;
import dev.efullstack.todo.services.TaskTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class TodoHandler {
    final TaskService taskService;
    final TagService tagService;
    final TaskTagService taskTagService;

    public Mono<ServerResponse> createTask(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        return request
                .bodyToMono(Task.class)
                .doOnNext(task -> log.info("Task: {}", task))
                .flatMap(task -> taskService.newTask(userId, task))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> newTags(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        return request
                .bodyToMono(Tag.class)
                .flatMap(tag -> tagService.newTag(userId, tag))
                .flatMap(tag -> ServerResponse.ok().bodyValue(tag));
    }

    public Mono<ServerResponse> patchTask(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var taskId = Long.valueOf(request.pathVariable("taskId"));
        return request
                .bodyToMono(Task.class)
                .flatMap(task -> taskService.patchTask(userId, taskId, task))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> tasks(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var filterParam = request.queryParam("filter").orElse("ALL");
        if (filterParam.equals("ALL")) {
            return taskService.allTasks(userId)
                    .flatMap(ServerResponse.ok()::bodyValue);
        } else {
            return taskService.allTasksByFilter(userId, filterParam)
                    .flatMap(ServerResponse.ok()::bodyValue);
        }

    }

    public Mono<ServerResponse> tagById(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var tagId = Long.valueOf(request.pathVariable("tagId"));
        return tagService.tagById(userId, tagId)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> patchTag(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var tagId = Long.valueOf(request.pathVariable("tagId"));
        return request
                .bodyToMono(Tag.class)
                .flatMap(tag -> tagService.patchTag(userId, tagId, tag))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findTaskById(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var taskId = Long.valueOf(request.pathVariable("taskId"));
        return taskService.findByTaskId(userId, taskId)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> tasksByTagId(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var tagId = Long.valueOf(request.pathVariable("tagId"));

        return taskTagService.findTagsByTagId(userId, tagId)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> tags(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        return tagService.allTag(userId)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteTask(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var taskId = Long.valueOf(request.pathVariable("taskId"));
        return taskService.deleteTask(userId, taskId)
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> updateTask(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var taskId = Long.valueOf(request.pathVariable("taskId"));
        return request
                .bodyToMono(Task.class)
                .flatMap(task -> taskService.updateTask(userId, taskId, task))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateTag(ServerRequest request) {
        var userId = Long.valueOf(request.pathVariable("userId"));
        var tagId = Long.valueOf(request.pathVariable("tagId"));
        return request
                .bodyToMono(Tag.class)
                .flatMap(tag -> tagService.updateTag(userId, tagId, tag))
                .flatMap(ServerResponse.ok()::bodyValue);
    }
}
