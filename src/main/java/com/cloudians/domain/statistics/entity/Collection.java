package com.cloudians.domain.statistics.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cloudians.domain.statistics.dto.response.CollectionResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Collection {
   
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="collection_id")
    private Long collectionId;
    
    @Column(name="user_email")
    private String userEmail;
    
  @Column(name="monthly_id")
  private Long monthlyId;
    
    @Column(name="date")
    private LocalDate date;
    
    @Column(name="most_color_and_element")
    private String mostColorAndElement;
    
    public CollectionResponse toDto() {
	return CollectionResponse.builder()
		.date(date)
		.userEmail(userEmail)
		.mostColorAndElement(mostColorAndElement)
		.build();
    }
    
}
