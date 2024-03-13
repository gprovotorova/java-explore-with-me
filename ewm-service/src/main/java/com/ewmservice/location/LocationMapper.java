package com.ewmservice.location;

import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return new Location(locationDto.getLat(), locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
