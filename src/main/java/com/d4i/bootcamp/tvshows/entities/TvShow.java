package com.d4i.bootcamp.tvshows.entities;

import java.io.Serializable;
import java.time.Year;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TV_SHOWS")
@NamedQuery(name = "TvShow.findByCategoryId",
		query = "SELECT ts FROM TvShow ts JOIN ts.categories c WHERE c.id = :categoryId")
public class TvShow implements Serializable {

	private static final long serialVersionUID = 4916713904971425156L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "SHORT_DESC", nullable = true)
	private String shortDescription;

	@Column(name = "LONG_DESC", nullable = true)
	private String longDescription;

	@Column(name = "YEAR")
	private Year year;

	@Column(name = "RECOMMENDED_AGE")
	private byte recommendedAge;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "CATEGORIES_TV_SHOWS", joinColumns = {
			@JoinColumn(name = "TV_SHOW_ID")}, inverseJoinColumns = {
			@JoinColumn(name = "CATEGORY_ID")})
	private List<Category> categories;

	@Column(name = "ADVERTISING", nullable = true)
	private String advertising;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tvShow")
	private List<Season> seasons;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "tvShows")
	private List<Award> awards;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public byte getRecommendedAge() {
		return recommendedAge;
	}

	public void setRecommendedAge(byte recommendedAge) {
		this.recommendedAge = recommendedAge;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getAdvertising() {
		return advertising;
	}

	public void setAdvertising(String advertising) {
		this.advertising = advertising;
	}

	public List<Season> getSeasons() {
		return seasons;
	}

	public void setSeasons(List<Season> seasons) {
		this.seasons = seasons;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}
}
