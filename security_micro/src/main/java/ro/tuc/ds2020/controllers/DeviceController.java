package ro.tuc.ds2020.controllers;


import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.services.DeviceService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private RestTemplate restTemplate;

    private final String authServiceUrl = "http://localhost:8081/device";

    @RequestMapping(method = RequestMethod.GET, value = "/auth/all")
    @ResponseBody
    public List<Device> getAllDevices() {
        String url = authServiceUrl + "/all";
        ResponseEntity<Device[]> response = restTemplate.exchange(url, HttpMethod.GET, null, Device[].class);
        return Arrays.asList(response.getBody());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth/getdevices/{personId}")
    @ResponseBody
    public List<Device> devicesForUser(@PathVariable("personId") Integer personId) {
        String url = authServiceUrl + "/getdevices/" + personId;
        ResponseEntity<Device[]> response = restTemplate.exchange(url, HttpMethod.GET, null, Device[].class);
        return Arrays.asList(response.getBody());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth/getById")
    @ResponseBody
    public Device getById(@RequestParam(name = "id") Integer id) {
        String url = authServiceUrl + "/getById?id=" + id;
        ResponseEntity<Device> response = restTemplate.exchange(url, HttpMethod.GET, null, Device.class);
        return response.getBody();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/auth/deleteById")
    @ResponseBody
    public ResponseEntity<String> deleteDeviceById(@RequestParam(name = "id") Integer id) {
        String url = authServiceUrl + "/deleteById?id=" + id;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        return ResponseEntity.ok("Device deleted successfully.");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/auth/deleteByPersonId/{personId}")
    @ResponseBody
    public ResponseEntity<String> deleteDevicesByPersonId(@PathVariable("personId") int personId) {
        String url = authServiceUrl + "/deleteByPersonId/" + personId;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        return ResponseEntity.ok("Devices for person " + personId + " deleted successfully.");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/auth/save")
    @ResponseBody
    public Device saveDevice(@RequestBody Device device) {
        String url = authServiceUrl + "/save";
        ResponseEntity<Device> response = restTemplate.postForEntity(url, device, Device.class);
        return response.getBody();
    }
}
