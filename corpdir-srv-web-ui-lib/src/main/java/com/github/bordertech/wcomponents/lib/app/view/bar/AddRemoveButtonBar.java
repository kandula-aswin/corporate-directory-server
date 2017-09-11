package com.github.bordertech.wcomponents.lib.app.view.bar;

import com.github.bordertech.wcomponents.Action;
import com.github.bordertech.wcomponents.ActionEvent;
import com.github.bordertech.wcomponents.AjaxTarget;
import com.github.bordertech.wcomponents.WAjaxControl;
import com.github.bordertech.wcomponents.WButton;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.lib.app.common.AppAjaxControl;
import com.github.bordertech.wcomponents.lib.app.event.ToolbarEventType;
import com.github.bordertech.wcomponents.lib.app.view.FormUpdateable;
import com.github.bordertech.wcomponents.lib.div.WDiv;
import com.github.bordertech.wcomponents.lib.flux.EventType;
import com.github.bordertech.wcomponents.lib.mvc.impl.DefaultView;

/**
 * ADD and REMOVE Toolbar.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class AddRemoveButtonBar<T> extends DefaultView<T> implements FormUpdateable {

	private final WButton btnAdd = new WButton("Add");
	private final WButton btnRemove = new WButton("Remove");

	private final WAjaxControl ajaxAdd = new AppAjaxControl(btnAdd);
	private final WAjaxControl ajaxRemove = new AppAjaxControl(btnRemove);

	private final WDiv ajaxPanel = new WDiv() {
		@Override
		public boolean isHidden() {
			return true;
		}

	};

	public AddRemoveButtonBar() {

		WContainer content = getContent();
		content.add(btnAdd);
		content.add(btnRemove);

		// Actions
		btnAdd.setAction(new Action() {
			@Override
			public void execute(ActionEvent event) {
				dispatchEvent(ToolbarEventType.ADD);
			}
		});
		btnRemove.setAction(new Action() {
			@Override
			public void execute(ActionEvent event) {
				dispatchEvent(ToolbarEventType.DELETE);
			}
		});

		// AJAX
		content.add(ajaxPanel);
		ajaxPanel.add(ajaxAdd);
		ajaxPanel.add(ajaxRemove);
		ajaxAdd.addTarget(this);
		ajaxRemove.addTarget(this);

		// Defaults
		btnRemove.setVisible(false);
	}

	public void showRemoveButton(final boolean show) {
		btnRemove.setVisible(show);
	}

	@Override
	public void addEventTarget(final AjaxTarget target, final EventType... eventType) {
		super.addEventTarget(target, eventType);
		ajaxAdd.addTarget(target);
		ajaxRemove.addTarget(target);
	}

	@Override
	public void doMakeFormReadonly(final boolean readonly) {
		setContentVisible(!readonly);
	}

}
