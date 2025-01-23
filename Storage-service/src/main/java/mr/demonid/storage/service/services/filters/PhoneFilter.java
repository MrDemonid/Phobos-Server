package mr.demonid.storage.service.services.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mr.demonid.storage.service.domain.PhoneType;

/**
 * Данные для фильтра выборки из БД Phones
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneFilter {
    private String number;      // null - не используется
    private PhoneType type;     // null - не используется
}
