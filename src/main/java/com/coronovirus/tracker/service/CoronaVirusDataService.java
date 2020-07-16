package com.coronovirus.tracker.service;

import com.coronovirus.tracker.domain.LocationStats;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class CoronaVirusDataService {

    @Value("${application.suggestionUrlTemplate}")
    private String suggestionUrl;


    private final RestTemplate restTemplate;

    private List<LocationStats> allStats = new ArrayList<>();


    @PostConstruct
    @Scheduled(cron = "5 * * * * *")
    public void fletchDataCSV() throws IOException {
        String result = restTemplate.getForObject(suggestionUrl, String.class);

        StringReader csvBodyReader = new StringReader(result);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);


        List<LocationStats> newStats = new ArrayList<>();

        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(locationStat);
            System.out.println(locationStat.getCountry());
        }


        this.allStats = newStats;
    }

}
