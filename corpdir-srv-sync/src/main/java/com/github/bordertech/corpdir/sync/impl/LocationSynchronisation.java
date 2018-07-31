package com.github.bordertech.corpdir.sync.impl;

import com.github.bordertech.corpdir.api.v1.LocationReadOnlyService;
import com.github.bordertech.corpdir.api.v1.LocationService;
import com.github.bordertech.corpdir.api.v1.model.Location;
import com.github.bordertech.corpdir.sync.common.AbstractSynchronisation;
import javax.inject.Inject;

/**
 *
 * @author aswinkandula
 */
public class LocationSynchronisation extends AbstractSynchronisation<LocationReadOnlyService, LocationService, Location> {

	@Inject
	public LocationSynchronisation(LocationReadOnlyService sourceService, LocationService destinationService) {
		super(sourceService, destinationService);
	}

}