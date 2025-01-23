package mr.demonid.web.client.service.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import mr.demonid.web.client.dto.GenderType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PersonFilter {
    private Long tabNo;
    private String lastName;
    private String firstName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private GenderType gender;
}
