package mr.demonid.web.client.service.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import mr.demonid.web.client.dto.PhoneType;

/**
 * Фильтр для выборки телефонов из БД микросервиса Storage.
 */
@Data
@AllArgsConstructor
public class PhoneFilter {
    private String number;
    private PhoneType type;
}
