package com.daengdaeng_eodiga.project.Global.Geo.Controller;
import com.daengdaeng_eodiga.project.Global.Geo.GeoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GeoController {
    private final GeoService geoService;

    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    @GetMapping("/api/location")
    public Map<String, String> getLocation(@RequestParam double latitude, @RequestParam double longitude) {
        return geoService.getRegionInfo(latitude, longitude);
    }
}
