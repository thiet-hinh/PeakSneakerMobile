package com.example.shoestore.thirdparty.ghn;

import com.example.shoestore.dto.response.AddressItemResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GHNService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ghn.api.token}")
    private String apiToken;

    @Value("${ghn.api.shopId}")
    private String shopId;

    @Value("${ghn.api.url}")
    private String feeUrl;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", apiToken);
        headers.set("ShopId", shopId);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public List<AddressItemResponse> getProvinceList() {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
        List<AddressItemResponse> result = new ArrayList<>();
        for (Map<String, Object> item : data) {
            result.add(new AddressItemResponse(item.get("ProvinceID").toString(), item.get("ProvinceName").toString()));
        }
        return result;
    }

    public List<AddressItemResponse> getDistrictList(Integer provinceId) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=" + provinceId;
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
        List<AddressItemResponse> result = new ArrayList<>();
        for (Map<String, Object> item : data) {
            result.add(new AddressItemResponse(item.get("DistrictID").toString(), item.get("DistrictName").toString()));
        }
        return result;
    }

    public List<AddressItemResponse> getWardList(Integer districtId) {
        String url = "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtId;
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
        List<AddressItemResponse> result = new ArrayList<>();
        for (Map<String, Object> item : data) {
            result.add(new AddressItemResponse(item.get("WardCode").toString(), item.get("WardName").toString()));
        }
        return result;
    }

    public Object calculateFee(int toDistrictId, String toWardCode) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("service_type_id", 2); // E-commerce delivery
        requestBody.put("to_district_id", toDistrictId);
        requestBody.put("to_ward_code", toWardCode);
        requestBody.put("height", 15);
        requestBody.put("length", 30);
        requestBody.put("width", 20);
        requestBody.put("weight", 1000); // Default 1kg for a pair of shoes

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, createHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(feeUrl, HttpMethod.POST, entity, Object.class);
        return response.getBody();
    }

}
