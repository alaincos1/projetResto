package fr.ul.miage.resto.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DateDto {
    private String startDate;
    private String endDate;
    private String displayDate;
}
