package com.mycompany.location.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TIMINGS_INFO")
public class TimingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TIMINGS_ID")
    private Long id;
    @JsonFormat(pattern = "h:mm a")
    @Column(name = "MON_TO_FRI_OPEN_TIME")
    private LocalTime monToFriOpenTime;
    @JsonFormat(pattern = "h:mm a")
    @Column(name = "MON_TO_FRI_CLOSE_TIME")
    private LocalTime monToFriCloseTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    @Column(name = "SAT_OPEN_TIME")
    private LocalTime satOpenTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    @Column(name = "SAT_CLOSE_TIME")
    private LocalTime satCloseTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    @Column(name = "SUN_OPEN_TIME")
    private LocalTime sunOpenTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    @Column(name = "SUN_CLOSE_TIME")
    private LocalTime sunCloseTime;
}
