package pl.gov.coi.pomocua.ads.accomodations;

import pl.gov.coi.pomocua.ads.Location;

import java.util.List;

public class AccommodationsTestDataGenerator {
    public static AccommodationOffer sampleOffer() {
        AccommodationOffer request = new AccommodationOffer();
        request.title = "sample work";
        request.location = new Location("Mazowieckie", "Warszawa");
        request.hostLanguage = List.of(AccommodationOffer.Language.PL, AccommodationOffer.Language.UA);
        request.description = "description";
        request.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_2;
        request.guests = 5;
        return request;
    }

    public static AccommodationOfferDefinitionDTO sampleUpdateJson() {
        var updateJson = new AccommodationOfferDefinitionDTO();
        updateJson.title = "new title";
        updateJson.description = "new description";
        updateJson.location = new Location("Pomorskie", "Gdańsk");
        updateJson.guests = 14;
        updateJson.lengthOfStay = AccommodationOffer.LengthOfStay.MONTH_3;
        updateJson.hostLanguage = List.of(AccommodationOffer.Language.UA);
        return updateJson;
    }
}
