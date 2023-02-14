package com.example.jablonski.pwc.controller;

import com.example.jablonski.pwc.model.ErrorResponse;
import com.example.jablonski.pwc.model.RouteResponse;
import com.example.jablonski.pwc.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/routing", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Route", description = "This is used to find a route path between destination")
public class CountryController {

    private final CountryService countryService;


    @Operation(summary = "Find rout",
            description = "Find route between origin and destination",
            tags = "Route")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(implementation = RouteResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - When there is no path found between destinations",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - When given origin/destination value is invalid",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    @GetMapping("/{origin}/{destination}")
    public ResponseEntity<RouteResponse> findRoutingGpt(
            @Parameter(required = true, description = "Origin country code", example = "POL") @PathVariable(name = "origin") String origin,
            @Parameter(required = true, description = "Destination country code", example = "ESP") @PathVariable(name = "destination") String destination) {

        log.info("Find routing controller origin={}, destination={}", origin, destination);
        return ResponseEntity.ok(countryService.getPath(origin, destination));
    }
}
