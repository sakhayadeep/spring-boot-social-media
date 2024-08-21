package com.sakhayadeep.social.social_media.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sakhayadeep.social.social_media.post.Post;
import com.sakhayadeep.social.social_media.post.PostNotFoundException;
import com.sakhayadeep.social.social_media.post.jpa.PostRepository;
import com.sakhayadeep.social.social_media.user.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jpa")
public class UserJpaResource {
	private UserRepository userRepository;
	private PostRepository postRepository;
	private MessageSource messageSource;

	public UserJpaResource(MessageSource messageSource, UserRepository userRepository, PostRepository postRepository) {
		this.messageSource = messageSource;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);
		EntityModel<User> entityModel = EntityModel.of(user.get());
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable Integer id) {
		userRepository.deleteById(id);
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@GetMapping(path = "/hello-world-i18n")
	public String helloWorldI18n() {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage("hello.world.message", null, "Default", locale);
	}

	@GetMapping("users/{id}/posts")
	public List<Post> retrieveAllPostsForUsers(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		return user.get().getPosts();
	}
	
	@GetMapping("users/{id}/posts/{postId}")
	public Post retrievePostForUsers(@PathVariable int id, @PathVariable int postId) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		Optional<Post> post = postRepository.findById(postId);
		if(post.isEmpty())
			throw new PostNotFoundException("postId:"+postId);
		return post.get();
	}
	
	@PostMapping("users/{id}/posts")
	public ResponseEntity<Post> createPostsForUsers(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty())
			throw new UserNotFoundException("id:" + id);

		post.setUser(user.get());
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{postId}").buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
}
