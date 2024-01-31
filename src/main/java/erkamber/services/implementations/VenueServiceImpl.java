package erkamber.services.implementations;

import erkamber.dtos.VenueDto;
import erkamber.entities.Category;
import erkamber.entities.Venue;
import erkamber.exceptions.ResourceNotFoundException;
import erkamber.repositories.VenueRepository;
import erkamber.requests.VenueRequestDto;
import erkamber.services.interfaces.VenueService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    private final CategoryServiceImpl categoryService;

    private final ModelMapper mapper;

    public VenueServiceImpl(VenueRepository venueRepository, CategoryServiceImpl categoryService, ModelMapper mapper) {
        this.venueRepository = venueRepository;
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @Override
    public VenueDto saveVenue(VenueRequestDto newVenue) {

        Venue venue = mapper.map(newVenue, Venue.class);

        Category category = categoryService.categoryByCategoryID(newVenue.getCategoryID());

        venue.setCategory(category);

        venueRepository.save(venue);

        return mapper.map(venue, VenueDto.class);
    }

    @Override
    public VenueDto findVenueByVenueName(String venueName) {

        Optional<Venue> searchedVenueOptional = venueRepository.findVenueByVenueName(venueName);

        Venue searchedVenue = searchedVenueOptional.orElseThrow(() ->
                new ResourceNotFoundException("Venue not Found", "Venue"));

        return mapper.map(searchedVenue, VenueDto.class);
    }

    @Override
    public VenueDto findVenueByVenueName(int venueID) {

        Optional<Venue> searchedVenueOptional = venueRepository.findById(venueID);

        Venue searchedVenue = searchedVenueOptional.orElseThrow(() ->
                new ResourceNotFoundException("Venue not Found", "Venue"));

        return mapper.map(searchedVenue, VenueDto.class);
    }

    @Override
    public List<VenueDto> findVenueByVenueCity(String city) {

        List<Venue> venuesByCity = venueRepository.findVenueByVenueCity(city);

        return mapListToVenueDto(venuesByCity);
    }

    @Override
    public List<VenueDto> findVenueByVenueCityOrderByAverageRatingAsc(String city) {

        List<Venue> orderedVenues = venueRepository.findVenueByVenueCityOrderByAverageRatingAsc(city);

        return mapListToVenueDto(orderedVenues);
    }

    @Override
    public List<VenueDto> findVenueByVenueCityOrderByAverageRatingDesc(String city) {

        List<Venue> orderedVenues = venueRepository.findVenueByVenueCityOrderByAverageRatingDesc(city);

        return mapListToVenueDto(orderedVenues);
    }

    @Override
    public List<VenueDto> findVenueByVenueCityAndCategoryCategory(String venueCity, String category) {

        List<Venue> venuesByCityAndCategory = venueRepository.findVenueByVenueCityAndCategoryCategory(venueCity, category);

        return mapListToVenueDto(venuesByCityAndCategory);
    }

    @Override
    public VenueDto findVenueByPhoneNumber(String phoneNumber) {

        Optional<Venue> searchedVenueOptional = venueRepository.findVenueByPhoneNumber(phoneNumber);

        Venue searchedVenue = searchedVenueOptional.orElseThrow(() ->
                new ResourceNotFoundException("Venue not Found", "Venue"));

        getCategoryOfVenue(searchedVenue);

        return mapper.map(searchedVenue, VenueDto.class);
    }

    @Override
    public VenueDto findVenueByEmail(String email) {

        Optional<Venue> searchedVenueOptional = venueRepository.findVenueByEmail(email);

        Venue searchedVenue = searchedVenueOptional.orElseThrow(() ->
                new ResourceNotFoundException("Venue not Found", "Venue"));

        getCategoryOfVenue(searchedVenue);

        return mapper.map(searchedVenue, VenueDto.class);
    }

    @Override
    public List<VenueDto> findAllByOrderByAverageRatingAsc() {

        List<Venue> venuesInAscOrder = venueRepository.findAllByOrderByAverageRatingDesc();

        return mapListToVenueDto(venuesInAscOrder);
    }

    @Override
    public List<VenueDto> findAllByOrderByAverageRatingDesc() {

        List<Venue> venuesInDescOrder = venueRepository.findAllByOrderByAverageRatingAsc();

        return mapListToVenueDto(venuesInDescOrder);
    }

    @Override
    public List<VenueDto> findAllVenues() {

        List<Venue> allVenues = venueRepository.findAll();

        return mapListToVenueDto(allVenues);
    }

    private List<VenueDto> mapListToVenueDto(List<Venue> listOfVenues) {

        listOfVenues.forEach(this::getCategoryOfVenue);

        return listOfVenues.stream()
                .map(venue -> mapper.map(venue, VenueDto.class))
                .collect(Collectors.toList());
    }

    private void getCategoryOfVenue(Venue venue) {

        Category category = categoryService.getCategoryOfVenue(venue.getVenueID());

        venue.setCategory(category);
    }

    Venue findVenueByID(int venueID) {

        Optional<Venue> searchedVenueOptional = venueRepository.findById(venueID);

        return searchedVenueOptional.orElseThrow(() ->
                new ResourceNotFoundException("Venue not Found", "Venue"));
    }

    void updateVenueRating(int newReviewRating, int venueID) {

        Optional<Venue> searchedVenueOptional = venueRepository.findById(venueID);

        Venue venue = searchedVenueOptional.orElseThrow(() ->
                new ResourceNotFoundException("Venue not Found", "Venue"));

        int reviewCount = venue.getReviewCount();
        double currentAverageRating = venue.getAverageRating();
        double newAverageRating = calculateNewAverageRating(currentAverageRating, reviewCount, newReviewRating);

        venue.setReviewCount(++reviewCount);
        venue.setAverageRating(newAverageRating);

        venueRepository.save(venue);
    }

    void updateVenueRatingOnDelete(int newReviewRating, int venueID) {

        Optional<Venue> searchedVenueOptional = venueRepository.findById(venueID);

        Venue venue = searchedVenueOptional.orElseThrow(() ->
                new ResourceNotFoundException("Venue not Found", "Venue"));

        int reviewCount = venue.getReviewCount();
        double currentAverageRating = venue.getAverageRating();
        double newAverageRating = calculateNewAverageRatingOnDelete(currentAverageRating, reviewCount, newReviewRating);

        venue.setReviewCount(--reviewCount);
        venue.setAverageRating(newAverageRating);

        venueRepository.save(venue);
    }

    double calculateNewAverageRating(double currentAverageRating, int reviewCount, int newReviewRating) {

        return ((currentAverageRating * reviewCount) + newReviewRating) / (reviewCount + 1.0);
    }

    public static double calculateNewAverageRatingOnDelete(double currentAverageRating, int reviewCount, int deletedReviewRating) {

        return ((currentAverageRating * reviewCount) - deletedReviewRating) / (reviewCount - 1.0);

    }
}
