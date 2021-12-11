package com.mycompany.location.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimingsResponseModel implements Serializable {

    private Long id;
    @JsonFormat(pattern = "h:mm a")
    private LocalTime monToFriOpenTime;
    @JsonFormat(pattern = "h:mm a")
    private LocalTime monToFriCloseTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    private LocalTime satOpenTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    private LocalTime satCloseTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    private LocalTime sunOpenTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    private LocalTime sunCloseTime;
}
