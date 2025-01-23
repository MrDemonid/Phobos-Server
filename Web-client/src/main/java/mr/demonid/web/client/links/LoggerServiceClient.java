package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.PageDTO;
import mr.demonid.web.client.service.filters.LogFilter;
import mr.demonid.web.client.dto.LogOperationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;


//@FeignClient(name = "gateway-logger", url = "http://localhost:9010", configuration = FeignClientConfig.class)

@FeignClient(name = "LOGGER-SERVICE", configuration = FeignClientConfig.class)
public interface LoggerServiceClient {

    @PostMapping("/api/logger/read")
    ResponseEntity<PageDTO<LogOperationDTO>> getLog(@RequestBody LogFilter filter, Pageable pageable);
}
