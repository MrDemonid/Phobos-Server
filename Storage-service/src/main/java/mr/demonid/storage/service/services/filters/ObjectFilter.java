package mr.demonid.storage.service.services.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectFilter {
    private Long fromId;
    private Long toId;
}
