package com.github.bordertech.corpdir.web.ui.shell;

/**
 * Entity Criteria View.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 *
 */
public interface CriteriaView<T> extends BasicEventView<CriteriaView<T>, CriteriaEvent> {

	T getCriteria();

}
