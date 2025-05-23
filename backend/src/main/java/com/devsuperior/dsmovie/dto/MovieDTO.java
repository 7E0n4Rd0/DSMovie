package com.devsuperior.dsmovie.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import com.devsuperior.dsmovie.entities.MovieEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;

public class MovieDTO extends RepresentationModel<MovieDTO> implements Serializable {

	private static final DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

	@Schema(description = "Database generated movie ID")
	private Long id;

	@NotBlank(message = "Required field")
	@Size(min = 5, max = 80, message = "Title must be between 5 and 80 characters")
	@Schema(description = "Movie title")
	private String title;
	
	@PositiveOrZero(message = "Score should be greater than or equal to zero")
	private Double score;
	
	@PositiveOrZero(message = "Count should be greater than or equal to zero")
	private Integer count;
	
	@NotBlank(message = "Required field")
	@URL(message = "Field must be a valid url")
	private String image;

	public MovieDTO(){}

	public MovieDTO(Long id, String title, Double score, Integer count, String image) {
		this.id = id;
		this.title = title;
		this.score = Double.valueOf(df.format(score));
		this.count = count;
		this.image = image;
	}

	public MovieDTO(MovieEntity movie) {
		id = movie.getId();
		title = movie.getTitle();
		score = movie.getScore();
		count = movie.getCount();
		image = movie.getImage();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Double getScore() {
		return score;
	}
	
	public Integer getCount() {
		return count;
	}

	public String getImage() {
		return image;
	}

	@Override
	public String toString() {
		return "MovieDTO [id=" + id + ", title=" + title + ", score=" + Double.valueOf(df.format(score)) + ", count=" + count + ", image=" + image
				+ "]";
	}
}
