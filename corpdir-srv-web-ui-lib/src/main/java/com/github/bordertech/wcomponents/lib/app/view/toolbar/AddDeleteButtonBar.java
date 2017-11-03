package com.github.bordertech.wcomponents.lib.app.view.toolbar;

import com.github.bordertech.flux.wc.view.DefaultDumbView;
import com.github.bordertech.wcomponents.Action;
import com.github.bordertech.wcomponents.ActionEvent;
import com.github.bordertech.wcomponents.WButton;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.WDiv;
import com.github.bordertech.wcomponents.lib.app.common.AppAjaxControl;
import com.github.bordertech.wcomponents.lib.app.view.event.base.ToolbarBaseViewEvent;
import com.github.bordertech.wcomponents.lib.app.view.form.FormUpdateable;
import com.github.bordertech.wcomponents.lib.icons.IconConstants;

/**
 * ADD and REMOVE Toolbar.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class AddDeleteButtonBar<T> extends DefaultDumbView<T> implements FormUpdateable {

	private final WButton btnAdd = new WButton("Add");
	private final WButton btnDelete = new WButton("Remove");

	private final AppAjaxControl ajaxAdd = new AppAjaxControl(btnAdd);
	private final AppAjaxControl ajaxDelete = new AppAjaxControl(btnDelete);

	private final WDiv ajaxPanel = new WDiv() {
		@Override
		public boolean isHidden() {
			return true;
		}

	};

	public AddDeleteButtonBar(final String viewId) {
		super(viewId);

		WContainer content = getContent();
		content.add(btnAdd);
		content.add(btnDelete);

		// Add details
		btnAdd.setImageUrl(IconConstants.EDIT_IMAGE);
		btnAdd.setRenderAsLink(false);
		btnAdd.setToolTip("Add");

		// Delete details
		btnDelete.setImageUrl(IconConstants.REMOVE_IMAGE);
		btnDelete.setRenderAsLink(false);
		btnDelete.setMessage("Do you want to remove this item?");
		btnDelete.setToolTip("Delete");

		// Actions
		btnAdd.setAction(new Action() {
			@Override
			public void execute(ActionEvent event) {
				dispatchViewEvent(ToolbarBaseViewEvent.ADD);
			}
		});
		btnDelete.setAction(new Action() {
			@Override
			public void execute(ActionEvent event) {
				dispatchViewEvent(ToolbarBaseViewEvent.DELETE);
			}
		});

		// AJAX
		content.add(ajaxPanel);
		ajaxPanel.add(ajaxAdd);
		ajaxPanel.add(ajaxDelete);
		ajaxAdd.addTarget(this);
		ajaxDelete.addTarget(this);

		// Defaults
		btnDelete.setVisible(false);

		registerEventAjaxControl(ToolbarBaseViewEvent.ADD, ajaxAdd);
		registerEventAjaxControl(ToolbarBaseViewEvent.DELETE, ajaxDelete);
	}

	public void showRemoveButton(final boolean show) {
		btnDelete.setVisible(show);
	}

	@Override
	public void doMakeFormReadonly(final boolean readonly) {
		setContentVisible(!readonly);
	}

}
