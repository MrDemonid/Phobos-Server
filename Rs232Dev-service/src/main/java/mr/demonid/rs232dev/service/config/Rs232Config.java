package mr.demonid.rs232dev.service.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.RepeaterRs232;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Создаем бин класса работы с COM-портом.
 */
@Configuration
@Log4j2
@AllArgsConstructor
public class Rs232Config {

    private final AppProperties appProperties;


    @Bean
    public RepeaterRs232 repeaterRs232() {
        log.info("Init on port '{}', with {} repeaters", appProperties.getAuxPort(), appProperties.getNumRepeaters());
        RepeaterRs232 repeater = new RepeaterRs232(appProperties.getAuxPort(), appProperties.getNumRepeaters());
        if (repeater.open()) {
            log.info("Init RS232 is OK!");
        } else {
            log.error("Init RS232 is NOT OK!");
        }
        return repeater;
    }


}
