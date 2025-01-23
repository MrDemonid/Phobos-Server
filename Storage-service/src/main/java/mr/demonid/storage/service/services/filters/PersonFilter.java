package mr.demonid.storage.service.services.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.storage.service.domain.GenderType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonFilter {
    private Long tabNo;
    private String lastName;
    private String firstName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private GenderType gender;
}
