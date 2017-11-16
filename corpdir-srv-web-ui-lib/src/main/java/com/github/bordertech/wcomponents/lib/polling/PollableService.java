package com.github.bordertech.wcomponents.lib.polling;

import com.github.bordertech.taskmanager.service.ServiceAction;
import java.io.Serializable;

/**
 * Polling component that creates a new thread to call a service and polls for the result.
 *
 * @author Jonathan Austin
 * @param <S> the criteria type
 * @param <T> the result type
 * @since 1.0.0
 */
public interface PollableService<S extends Serializable, T extends Serializable> extends Pollable {

	/**
	 * Manually set the criteria and the result.
	 *
	 * @param criteria the criteria
	 * @param result the result
	 */
	void doManuallyLoadResult(final S criteria, final T result);

	/**
	 * @return the id for the record
	 */
	S getPollingCriteria();

	/**
	 * @param criteria the id for the record
	 */
	void setPollingCriteria(final S criteria);

	/**
	 * @return the polling result, or null if not processed successfully yet
	 */
	T getPollingResult();

	/**
	 *
	 * @return the service action to use for polling
	 */
	ServiceAction<S, T> getServiceAction();

	/**
	 *
	 * @param serviceAction the service action to use for polling
	 */
	void setServiceAction(final ServiceAction<S, T> serviceAction);

}
