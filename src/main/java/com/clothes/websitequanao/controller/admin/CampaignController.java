package com.clothes.websitequanao.controller.admin;

import com.clothes.websitequanao.dto.request.campaign.CampaignDetailRequestDto;
import com.clothes.websitequanao.dto.request.campaign.CampaignRequestDto;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/admin/campaign")
@RequiredArgsConstructor
@RestController
public class CampaignController {
    private final CampaignService campaignService;

    // campaign
    @GetMapping
    public ResponseEntity<?> addCampaign() {
        ServiceResponse result = campaignService.getAllCampaignAdmin();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCampaign(@RequestBody CampaignRequestDto dto) {
        System.out.println(dto);
        ServiceResponse result = campaignService.addCampaign(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    // campaign
    // campaign detail
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getCampaignDetail(@PathVariable Long id) {
        ServiceResponse result = campaignService.getCampaignDetailById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/detail/add")
    public ResponseEntity<?> addCampaignDetail(@RequestBody CampaignDetailRequestDto dto) {
        ServiceResponse result = campaignService.addCampaignDetail(dto);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping({"/finish"})
    public ResponseEntity<?> finishCampaign(@RequestParam Long campaignId) {
        ServiceResponse result = campaignService.finishCampaign(campaignId);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    /// campaign detail
    @GetMapping({"/detail/product"})
    public ResponseEntity<?> getAllProduct() {
        ServiceResponse result = campaignService.campaignGetProduct();
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


}
