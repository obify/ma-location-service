package com.mycompany.location.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LOCATION_SERVICES")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "LOCATION_ID")
    private Long locationId;
    @Column(name = "SERVICE_ID")
    private Integer serviceId;
    //@Column(name = "SERVICE_NAME")
    //private String title;
    //@ManyToMany(cascade = CascadeType.ALL, mappedBy = "services")
    //private Set<LocationEntity> locations = new HashSet<>();
}
