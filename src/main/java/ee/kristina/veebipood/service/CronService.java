package ee.kristina.veebipood.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class CronService {

    // * sekundid
    //** minutid
    //*** tunnid
    //**** kuupaev
    //***** kuu
    //****** nadalapaev kus nii 0 kui ka 7 on pyhap

    /*@Scheduled(cron = "0 0 17 * * 1-5") // 17.00 every day between M-F
    public void runOnWorkDays() {
        System.out.println("cron service is running after every second");
    }*/

    //@Scheduled(cron = "*/2 30 10 * * *") // every 2 sec at 10:30
    //public void runAfterTwoSeconds() {
        //System.out.println("cron service is running after every 2 seconds");
    //}

    /*@Scheduled(cron = "0 0 * * * *") // every hour
    public void runEveryHour() {
        System.out.println("cron service is running every hour");
    }

    @Scheduled(cron = "0 0 8-20 * * 0-5") // every hour between 8 and 20 every day between Sunday and Friday
    public void run() {
        System.out.println("cron service is running every hour");
        //bookingRepository.findByCreatedBetween(Homme 24h parast, homme 25h parast);
        // sms/ email booking teavitusest
    }*/

    // kustuta koik maksmata tellimused nt

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "0 */10 * * * *")
    public void callUrlEveryTenMinutes() {
        String url = "https://veebipood-01-2026.onrender.com/products";
        restTemplate.getForObject(url, String.class);
        System.out.println(LocalDateTime.now() + " - Called " + url + " -> OK");
    }
}
