package com.github.bordertech.wcomponents.lib.app.ctrl;

import com.github.bordertech.wcomponents.lib.app.event.CollectionEventType;
import com.github.bordertech.wcomponents.lib.app.view.ListView;
import com.github.bordertech.wcomponents.lib.flux.Event;
import com.github.bordertech.wcomponents.lib.flux.Listener;
import com.github.bordertech.wcomponents.lib.mvc.impl.DefaultController;
import com.github.bordertech.wcomponents.lib.mvc.msg.MessageEventType;
import java.util.List;

/**
 * Controller for a List View.
 *
 * @author jonathan
 * @param <T> the result type
 */
public class ListMainCtrl<T> extends DefaultController {

	@Override
	public void setupController() {
		super.setupController();

		// LIST Listeners
		for (CollectionEventType type : CollectionEventType.values()) {
			Listener listener = new Listener() {
				@Override
				public void handleEvent(final Event event) {
					handleListEvents(event);
				}
			};
			registerListener(type, listener);
		}

	}

	@Override
	public void checkConfig() {
		super.checkConfig();
		if (getListView() == null) {
			throw new IllegalStateException("A list view has not been set.");
		}
	}

	public final ListView<T> getListView() {
		return getComponentModel().listView;
	}

	public final void setListView(final ListView<T> listView) {
		getOrCreateComponentModel().listView = listView;
		addView(listView);
	}

	protected void handleListEvents(final Event event) {
		CollectionEventType type = (CollectionEventType) event.getQualifier().getEventType();
		switch (type) {
			case RESET_COLLECTION:
				handleResetListEvent();
				break;
			case LOAD_ITEMS:
				List<T> items = (List<T>) event.getData();
				handleLoadList(items);
				break;
			case ADD_ITEM:
				handleAddItemEvent((T) event.getData());
				break;
			case REMOVE_ITEM:
				handleRemoveItemEvent((T) event.getData());
				break;
			case UPDATE_ITEM:
				handleUpdateItemEvent((T) event.getData());
				break;
		}

	}

	protected void handleResetListEvent() {
		getListView().resetView();
	}

	protected void handleAddItemEvent(final T item) {
		getListView().addItem(item);
		getListView().showList(true);
	}

	protected void handleRemoveItemEvent(final T item) {
		ListView<T> listView = getListView();
		listView.removeItem(item);
		if (listView.getViewBean().isEmpty()) {
			listView.showList(false);
		}
	}

	protected void handleUpdateItemEvent(final T item) {
		getListView().updateItem(item);
	}

	protected void handleLoadList(final List<T> items) {
		if (items == null || items.isEmpty()) {
			dispatchMessage(MessageEventType.INFO, "No records found");
		} else {
			ListView<T> listView = getListView();
			listView.setViewBean(items);
			listView.setContentVisible(true);
		}
	}

	@Override
	protected ListActionModel newComponentModel() {
		return new ListActionModel();
	}

	@Override
	protected ListActionModel getComponentModel() {
		return (ListActionModel) super.getComponentModel();
	}

	@Override
	protected ListActionModel getOrCreateComponentModel() {
		return (ListActionModel) super.getOrCreateComponentModel();
	}

	/**
	 * Holds the extrinsic state information of the view.
	 */
	public static class ListActionModel<T> extends CtrlModel {

		private ListView<T> listView;
	}

}
