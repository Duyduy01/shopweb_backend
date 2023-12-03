package com.clothes.websitequanao.event;

import com.clothes.websitequanao.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class EventStart {
    private final CampaignService campaignService;

    @Async
    @Scheduled(cron = "0 50 21 * * ?")
    public void writeCurrentTime() {
        try {
            log.info("run event");
            boolean check = campaignService.workerCampaign();
            if (!check)
                throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Lỗi sự kiện");
        }

    }
}
