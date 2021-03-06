package com.github.bordertech.corpdir.jpa.v1.mapper;

import com.github.bordertech.corpdir.api.v1.model.OrgUnit;
import com.github.bordertech.corpdir.jpa.common.map.AbstractMapperVersionTree;
import com.github.bordertech.corpdir.jpa.entity.OrgUnitEntity;
import com.github.bordertech.corpdir.jpa.entity.PositionEntity;
import com.github.bordertech.corpdir.jpa.entity.UnitTypeEntity;
import com.github.bordertech.corpdir.jpa.entity.VersionCtrlEntity;
import com.github.bordertech.corpdir.jpa.entity.version.OrgUnitVersionEntity;
import com.github.bordertech.corpdir.jpa.util.MapperUtil;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Map {@link OrgUnit} and {@link OrgUnitEntity}.
 *
 * @author jonathan
 */
public class OrgUnitMapper extends AbstractMapperVersionTree<OrgUnit, OrgUnitVersionEntity, OrgUnitEntity> {

	@Override
	public void copyApiToEntity(final EntityManager em, final OrgUnit from, final OrgUnitEntity to, final Long versionId) {
		super.copyApiToEntity(em, from, to, versionId);
		// Type
		String origId = MapperUtil.convertEntityIdforApi(to.getType());
		String newId = MapperUtil.cleanApiKey(from.getTypeId());
		if (!MapperUtil.keyMatch(origId, newId)) {
			to.setType(getUnitTypeEntity(em, newId));
		}

	}

	@Override
	public void copyEntityToApi(final EntityManager em, final OrgUnitEntity from, final OrgUnit to, final Long versionId) {
		super.copyEntityToApi(em, from, to, versionId);
		to.setTypeId(MapperUtil.convertEntityIdforApi(from.getType()));
	}

	@Override
	protected OrgUnit createApiObject(final String id) {
		return new OrgUnit(id);
	}

	@Override
	protected OrgUnitEntity createEntityObject(final Long id) {
		return new OrgUnitEntity(id);
	}

	protected UnitTypeEntity getUnitTypeEntity(final EntityManager em, final String keyId) {
		return MapperUtil.getEntityByKeyId(em, keyId, UnitTypeEntity.class);
	}

	protected PositionEntity getPositionEntity(final EntityManager em, final String keyId) {
		return MapperUtil.getEntityByKeyId(em, keyId, PositionEntity.class);
	}

	@Override
	protected Class<OrgUnitEntity> getEntityClass() {
		return OrgUnitEntity.class;
	}

	@Override
	protected void handleVersionDataApiToEntity(final EntityManager em, final OrgUnit from, final OrgUnitEntity to, final VersionCtrlEntity ctrl) {

		// Get the links version for this entity
		OrgUnitVersionEntity links = to.getOrCreateVersion(ctrl);

		// Manager Position
		String origId = MapperUtil.convertEntityIdforApi(links.getManagerPosition());
		String newId = MapperUtil.cleanApiKey(from.getManagerPosId());
		if (!MapperUtil.keyMatch(origId, newId)) {
			links.setManagerPosition(null);
			// Remove from Orig Position
			if (origId != null) {
				PositionEntity pos = getPositionEntity(em, origId);
				pos.getOrCreateVersion(ctrl).removeManageOrgUnit(to);
			}
			// Add to New Position
			if (newId != null) {
				PositionEntity pos = getPositionEntity(em, newId);
				pos.getOrCreateVersion(ctrl).addManageOrgUnit(to);
			}
		}

		// Positions
		List<String> origIds = MapperUtil.convertEntitiesToApiKeys(links.getPositions());
		List<String> newIds = MapperUtil.cleanApiKeys(from.getPositionIds());
		if (!MapperUtil.keysMatch(origIds, newIds)) {
			// Removed
			for (String id : MapperUtil.keysRemoved(origIds, newIds)) {
				PositionEntity pos = getPositionEntity(em, id);
				links.removePosition(pos);
			}
			// Added
			for (String id : MapperUtil.keysAdded(origIds, newIds)) {
				PositionEntity pos = getPositionEntity(em, id);
				links.addPosition(pos);
			}
		}
	}

	@Override
	protected void handleVersionDataEntityToApi(final EntityManager em, final OrgUnitEntity from, final OrgUnit to, final Long versionId) {
		// Get the tree version for this entity
		OrgUnitVersionEntity links = from.getVersion(versionId);
		if (links != null) {
			to.setManagerPosId(MapperUtil.convertEntityIdforApi(links.getManagerPosition()));
			to.setPositionIds(MapperUtil.convertEntitiesToApiKeys(links.getPositions()));
		}
	}
}
