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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LOCATION_INFO")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LOCATION_ID")
    private Long id;
    @Column(name = "LOCATION_TYPE")
    private String locationType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS_ID")
    private AddressEntity address;

    @Column(name = "USER_MODIFIED")
    private Long userid;

    @Column(name = "STATUS")
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TIMINGS_ID")
    private TimingsEntity timings;

    /*
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "LOCATION_SERVICE", joinColumns = {@JoinColumn(name = "LOCATION_ID")},
    inverseJoinColumns = {@JoinColumn(name = "SERVICE_ID")})
    private Set<ServiceEntity> services = new HashSet<>();
     */
}
