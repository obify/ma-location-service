package com.mycompany.location.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimingsRequestModel {

    @JsonFormat(pattern = "h:mm a")
    private LocalTime monToFriOpenTime;
    @JsonFormat(pattern = "h:mm a")
    private LocalTime monToFriCloseTime;
    @JsonFormat(pattern = "h:mm a")
    private LocalTime satOpenTime;
    @JsonFormat(pattern = "h:mm a")
    private LocalTime satCloseTime;
    @JsonFormat(pattern = "h:mm a")
    private LocalTime sunOpenTime;
    @JsonFormat(pattern = "h:mm a")
    private LocalTime sunCloseTime;
}
