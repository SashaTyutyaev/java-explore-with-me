package ru.practicum.admin.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.admin.events.StateActionAdmin;
import ru.practicum.users.events.model.Location;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @Length(min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @Length(min = 20, max = 7000)
    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateActionAdmin stateAction;

    @Length(min = 3, max = 120)
    private String title;

}
