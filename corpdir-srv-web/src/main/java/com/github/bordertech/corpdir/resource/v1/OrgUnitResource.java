package com.github.bordertech.corpdir.resource.v1;

import com.github.bordertech.corpdir.api.response.BasicResponse;
import com.github.bordertech.corpdir.api.response.DataResponse;
import com.github.bordertech.corpdir.api.v1.OrgUnitService;
import com.github.bordertech.corpdir.api.v1.model.OrgUnit;
import com.github.bordertech.corpdir.api.v1.model.Position;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Organisation Unit REST Resource.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
@Path(value = "v1/orgunits")
public class OrgUnitResource implements OrgUnitService {

	private final OrgUnitService impl;

	/**
	 * @param impl the service implementation
	 */
	@Inject
	public OrgUnitResource(final OrgUnitService impl) {
		this.impl = impl;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<List<OrgUnit>> search(@QueryParam("search") final String search) {
		return impl.search(search);
	}

	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<OrgUnit> retrieve(@PathParam("key") final String orgUnitKeyId) {
		return impl.retrieve(orgUnitKeyId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<OrgUnit> create(final OrgUnit orgUnit) {
		return impl.create(orgUnit);
	}

	@PUT
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<OrgUnit> update(@PathParam("key") final String orgUnitKeyId, final OrgUnit orgUnit) {
		return impl.update(orgUnitKeyId, orgUnit);
	}

	@DELETE
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public BasicResponse delete(@PathParam("key") final String orgUnitKeyId) {
		return impl.delete(orgUnitKeyId);
	}

	@GET
	@Path("/{key}/orgunits")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<List<OrgUnit>> getSubs(@PathParam("key") final String orgUnitKeyId) {
		return impl.getSubs(orgUnitKeyId);
	}

	@PUT
	@Path("/{key}/orgunits/{subKey}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<OrgUnit> addSub(@PathParam("key") final String orgUnitKeyId, @PathParam("subKey") final String subOrgUnitKeyId) {
		return impl.addSub(orgUnitKeyId, subOrgUnitKeyId);
	}

	@DELETE
	@Path("/{key}/orgunits/{subKey}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<OrgUnit> removeSub(@PathParam("key") final String orgUnitKeyId, @PathParam("subKey") final String subOrgUnitKeyId) {
		return impl.removeSub(orgUnitKeyId, subOrgUnitKeyId);
	}

	@GET
	@Path("/{key}/positions")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<List<Position>> getPositions(@PathParam("key") final String orgUnitKeyId) {
		return impl.getPositions(orgUnitKeyId);
	}

	@PUT
	@Path("/{key}/positions/{positionKey}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<OrgUnit> addPosition(@PathParam("key") final String orgUnitKeyId, @PathParam("positionKey") final String positionKeyId) {
		return impl.addPosition(orgUnitKeyId, positionKeyId);
	}

	@DELETE
	@Path("/{key}/positions/{positionKey}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<OrgUnit> removePosition(@PathParam("key") final String orgUnitKeyId, @PathParam("positionKey") final String positionKeyId) {
		return impl.removePosition(orgUnitKeyId, positionKeyId);
	}

	@GET
	@Path("/{key}/managers")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public DataResponse<Position> getManagerPosition(@PathParam("key") final String orgUnitKeyId) {
		return impl.getManagerPosition(orgUnitKeyId);
	}

}
