package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.controllers.MovieController;
import com.devsuperior.dsmovie.dto.MovieGenreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;

	@Transactional(readOnly = true)
	public Page<MovieDTO> findAll(String title, Pageable pageable) {
		Page<MovieEntity> result = repository.searchByTitle(title, pageable);
		return result.map(x -> new MovieDTO(x)
				.add(
					linkTo(methodOn(MovieController.class).findAll(x.getTitle(), pageable))
						.withSelfRel())
				.add(
					linkTo(methodOn(MovieController.class).findById(x.getId()))
						.withRel("GET movie by id")
				)
			);
	}

	@Transactional(readOnly = true)
	public Page<MovieGenreDTO> findAllMovieGenre(String title, Pageable pageable) {
		Page<MovieEntity> result = repository.searchByTitle(title, pageable);
		return result.map(x -> new MovieGenreDTO(x)
				.add(
					linkTo(methodOn(MovieController.class).findAllV1(x.getTitle(), pageable))
						.withSelfRel())
				.add(
					linkTo(methodOn(MovieController.class).findByIdV1(x.getId()))
						.withRel("GET movie by id")
				)
		);
	}

	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		MovieEntity result = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new MovieDTO(result)
                .add(linkTo(methodOn(MovieController.class).findById(id)).withSelfRel())
                .add(linkTo(methodOn(MovieController.class).findAll(result.getTitle(), null))
                        .withRel("All movies"));
	}

	@Transactional(readOnly = true)
	public MovieGenreDTO findByIdMovieGenre(Long id) {
		MovieEntity result = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new MovieGenreDTO(result)
				.add(linkTo(methodOn(MovieController.class).findById(id)).withSelfRel())
				.add(linkTo(methodOn(MovieController.class).findAll(result.getTitle(), null))
						.withRel("All movies"))
				.add(linkTo(methodOn(MovieController.class).update(id, new MovieDTO(result)))
						.withRel("Update movie"))
				.add(linkTo(methodOn(MovieController.class).delete(id))
						.withRel("Delete movie"));
	}

	@Transactional
	public MovieDTO insert(MovieDTO dto) {
		MovieEntity entity = new MovieEntity();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new MovieDTO(entity)
				.add(linkTo(methodOn(MovieController.class).findById(entity.getId())).withSelfRel());
	}

	@Transactional
	public MovieDTO update(Long id, MovieDTO dto) {
		try {
			MovieEntity entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new MovieDTO(entity)
					.add(linkTo(methodOn(MovieController.class).findById(id)).withSelfRel());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	public void delete(Long id) {
		if (!repository.existsById(id))
			throw new ResourceNotFoundException("Recurso não encontrado");
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	private void copyDtoToEntity(MovieDTO dto, MovieEntity entity) {
		entity.setTitle(dto.getTitle());
		entity.setScore(dto.getScore());
		entity.setCount(dto.getCount());
		entity.setImage(dto.getImage());
	}
}