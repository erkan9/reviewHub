package erkamber.services.interfaces;


import erkamber.dtos.VenueDto;
import erkamber.requests.VenueRequestDto;

import java.util.List;

public interface VenueService {

    VenueDto saveVenue(VenueRequestDto newVenue);

    VenueDto findVenueByVenueName(String venueName);

    VenueDto findVenueByVenueName(int venueID);

    List<VenueDto> findVenueByVenueCity(String city);

    List<VenueDto> findVenuesByCategoryID(int categoryID);

    List<VenueDto> findVenueByVenueCityOrderByAverageRatingAsc(String city);

    List<VenueDto> findVenueByVenueCityOrderByAverageRatingDesc(String city);

    List<VenueDto> findVenueByVenueCityAndCategoryCategory(String venueCity, String category);

    VenueDto findVenueByPhoneNumber(String phoneNumber);

    VenueDto findVenueByEmail(String email);

    List<VenueDto> findAllByOrderByAverageRatingAsc();

    List<VenueDto> findAllByOrderByAverageRatingDesc();

    List<VenueDto> findAllVenues();

    void deleteByVenueID(int venueID);

    int updateVenue(int venueID, String phone, String address, String city, String description);
}