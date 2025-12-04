package com.example.stugbokningssystem.config;

import com.example.stugbokningssystem.entity.Cabin;
import com.example.stugbokningssystem.repository.CabinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final CabinRepository cabinRepository;

    @Override
    public void run(String... args) {
        // Lägg till några teststugor
        Cabin cabin1 = new Cabin(null, "Stugan till höger om Bolagsverket", "Östermalmsgatan 89, Sundsvall",
                4, 1200.0,
                "Modern stuga till höger om Bolagsverket");
        Cabin cabin2 = new Cabin(null, "Stugan till vänster om bolagsverket", "Östermalmsgatan 85, Sundsvall",
                6, 1500.0,
                "Rymlig stuga till vänster om Bolagsverket");
        Cabin cabin3 = new Cabin(null, "Stugan framför Bolagsverket", "Trädgårdsgatan 12, Sundsvall",
                8, 2500.0,
                "exklusiv stuga framför Bolagsverket");

        cabinRepository.save(cabin1);
        cabinRepository.save(cabin2);
        cabinRepository.save(cabin3);

        System.out.println("===========================================");
        System.out.println("Testdata har laddats: 3 stugor skapade");
        System.out.println("===========================================");
    }
}
