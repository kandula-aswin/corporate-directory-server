package com.github.bordertech.flux.wc.view.smart.list;

import com.github.bordertech.flux.view.ViewEventType;
import com.github.bordertech.flux.wc.common.TemplateConstants;
import com.github.bordertech.flux.wc.view.dumb.ListView;
import com.github.bordertech.flux.wc.view.dumb.SearchView;
import com.github.bordertech.flux.wc.view.dumb.ToolbarView;
import com.github.bordertech.flux.wc.view.dumb.toolbar.DefaultToolbarView;
import com.github.bordertech.flux.wc.view.event.base.SearchBaseEventType;

/**
 * Collection View with a Search View.
 *
 * @author jonathan
 * @param <S> the search type
 * @param <T> the item type
 */
public class DefaultListWithSearchView<S, T> extends AbstractListSmartView<S, T> {

	// Toolbar - Defaults to just the Reset Button and Reset Event is handled AUTO
	private final ToolbarView toolbarView = new DefaultToolbarView("vw_toolbar");

	private final SearchView<S> searchView;

	public DefaultListWithSearchView(final String viewId, final SearchView<S> searchView, final ListView<T> listView) {
		super(viewId, listView);

		// Views
		this.searchView = searchView;
		addComponentToTemplate(TemplateConstants.TAG_VW_TOOLBAR, toolbarView);

		// Default visibility
		listView.setContentVisible(false);
		listView.addHtmlClass("wc-margin-n-sm");

	}

	public SearchView<S> getSearchView() {
		return searchView;
	}

	public ToolbarView getToolbarView() {
		return toolbarView;
	}

	@Override
	protected void handleViewEvent(final String viewId, final ViewEventType event, final Object data) {
		super.handleViewEvent(viewId, event, data);
		if (event instanceof SearchBaseEventType) {
			handleSearchBaseEvents((SearchBaseEventType) event, data);
		}
	}

	protected void handleSearchBaseEvents(final SearchBaseEventType type, final Object data) {
		switch (type) {
			case SEARCH:
				handleSearchEvent((S) data);
				break;
			case SEARCH_VALIDATING:
				handleSearchValidatingEvent();
				break;
		}
	}

	protected void handleSearchEvent(final S criteria) {
		setStoreCriteria(criteria);
		doManualStart();
	}

	protected void handleSearchValidatingEvent() {
		getListView().reset();
		getPollingView().reset();
	}

}
