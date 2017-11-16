package com.github.bordertech.wcomponents.lib.polling;

import com.github.bordertech.wcomponents.Action;
import com.github.bordertech.wcomponents.ActionEvent;
import com.github.bordertech.wcomponents.AjaxHelper;
import com.github.bordertech.wcomponents.AjaxTarget;
import com.github.bordertech.wcomponents.Margin;
import com.github.bordertech.wcomponents.Request;
import com.github.bordertech.wcomponents.WAjaxControl;
import com.github.bordertech.wcomponents.WButton;
import com.github.bordertech.wcomponents.WContainer;
import com.github.bordertech.wcomponents.WDiv;
import com.github.bordertech.wcomponents.WMessages;
import com.github.bordertech.wcomponents.WProgressBar;
import com.github.bordertech.wcomponents.WText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This panel is used to poll via AJAX and also do a Reload of Views via AJAX.
 *
 * @author Jonathan Austin
 * @since 1.0.0
 */
public class PollingPanel extends WDiv implements Pollable {

	/**
	 * Start polling manually button.
	 */
	private final WButton startButton = new WButton("Start");

	private final WDiv holder = new WDiv() {
		@Override
		protected void preparePaintComponent(final Request request) {
			super.preparePaintComponent(request);
			if (!isInitialised()) {
				handleInitContent(request);
				setInitialised(true);
			}
		}
	};
	/**
	 * Messages.
	 */
	private final WMessages messages = new WMessages(true);

	/**
	 * Retry load.
	 */
	private final WButton retryButton = new WButton("Retry");

	/**
	 * The container that holds the AJAX poller.
	 */
	private final WContainer pollingContainer = new WContainer();

	private final WText pollingText = new WText() {
		@Override
		public String getText() {
			return getPollingText();
		}
	};

	private final WProgressBar pollingProgressBar = new WProgressBar(100);

	private final WText progressBarScript = new WText() {
		@Override
		protected void preparePaintComponent(final Request request) {
			if (!isInitialised()) {
				setText(buildProgressBarScript());
				setInitialised(true);
			}
		}
	};

	/**
	 * The container that holds the AJAX control.
	 */
	private final WDiv ajaxPollingPanel = new WDiv() {
		@Override
		public boolean isHidden() {
			return true;
		}
	};

	/**
	 * AJAX poller.
	 */
	private final WAjaxControl ajaxPolling = new WAjaxControl(null, ajaxPollingPanel) {
		@Override
		public int getDelay() {
			return getPollingInterval();
		}

		@Override
		public void handleRequest(final Request request) {
			super.handleRequest(request);
			// Check if Polling
			if (isPollingTrigger() && checkForStopPolling()) {
				doPanelReload();
			}
		}

	};

	/**
	 * AJAX control to reload whole view and any other views.
	 */
	private final WAjaxControl ajaxReload = new WAjaxControl(null, this) {
		@Override
		public void handleRequest(Request request) {
			super.handleRequest(request);
			// Reloading
			if (AjaxHelper.isCurrentAjaxTrigger(this)) {
				pollingContainer.reset();
				handleStoppedPolling();
			}
		}
	};

	public PollingPanel() {
		this(174);
	}

	/**
	 * Construct polling panel.
	 *
	 * @param delay the delay for polling
	 */
	public PollingPanel(final int delay) {

		add(holder);
		holder.setSearchAncestors(false);

		messages.setMargin(new Margin(0, 0, 3, 0));
		holder.add(messages);
		holder.add(startButton);
		holder.add(retryButton);

		// Manual Start load
		startButton.setAjaxTarget(this);
		startButton.setAction(new Action() {
			@Override
			public void execute(final ActionEvent event) {
				doStartPolling();
				startButton.setVisible(false);
			}
		});

		// Retry load
		retryButton.setAjaxTarget(this);
		retryButton.setAction(new Action() {
			@Override
			public void execute(final ActionEvent event) {
				doRetry();
			}
		});

		// Set default visibility
		// AJAX polling details
		setPollingInterval(delay);
		ajaxPolling.setLoadOnce(true);
		ajaxReload.setLoadOnce(true);
		ajaxReload.setDelay(10);
		progressBarScript.setEncodeText(false);

		// Polling container is outside AJAX panel so it does not pulse)
		holder.add(pollingContainer);
		pollingContainer.add(pollingText);
		pollingContainer.add(pollingProgressBar);
		pollingContainer.add(progressBarScript);
		pollingContainer.add(ajaxPollingPanel);
		ajaxPollingPanel.add(ajaxPolling);
		ajaxPollingPanel.add(ajaxReload);

		// Set default visibility
		pollingContainer.setVisible(false);
		ajaxPolling.setVisible(false);
		ajaxReload.setVisible(false);
		retryButton.setVisible(false);
		startButton.setVisible(false);
	}

	public final WDiv getHolder() {
		return holder;
	}

	protected void handleInitContent(final Request request) {
		getStartButton().setVisible(isManualStart());
	}

	/**
	 * @return the AJAX polling interval in milli seconds
	 */
	@Override
	public int getPollingInterval() {
		return getComponentModel().pollingInterval;
	}

	/**
	 *
	 * @param interval the AJAX polling interval in milli seconds
	 */
	@Override
	public final void setPollingInterval(final int interval) {
		getOrCreateComponentModel().pollingInterval = interval;
	}

	/**
	 * @param text the text displayed while polling
	 */
	@Override
	public void setPollingText(final String text) {
		getOrCreateComponentModel().pollingText = text;
	}

	/**
	 * @return the text displayed while polling
	 */
	@Override
	public String getPollingText() {
		return getComponentModel().pollingText;
	}

	/**
	 * @param panelStatus the panel status
	 */
	@Override
	public void setPollingStatus(final PollingStatus panelStatus) {
		getOrCreateComponentModel().serviceStatus = panelStatus;
	}

	/**
	 * @return the panel status
	 */
	@Override
	public PollingStatus getPollingStatus() {
		return getComponentModel().serviceStatus;
	}

	/**
	 * Start AJAX polling.
	 */
	@Override
	public void doStartPolling() {
		// Make sure start buttonis not visible
		getStartButton().setVisible(false);

		// Start AJAX polling
		setPollingStatus(PollingStatus.PROCESSING);
		pollingContainer.reset();
		pollingContainer.setVisible(true);
		ajaxPolling.setVisible(true);
		handleStartedPolling();
	}

	/**
	 * Retry the polling action.
	 */
	@Override
	public void doRetry() {
		doRefreshContent();
		doStartPolling();
	}

	/**
	 * Reset to start load again.
	 */
	@Override
	public void doRefreshContent() {
		getHolder().reset();
		setPollingStatus(PollingStatus.NOT_STARTED);
	}

	/**
	 * Do AJAX Reload.
	 */
	@Override
	public void doPanelReload() {
		boolean alreadyPolling = isPolling();
		pollingContainer.reset();
		List<AjaxTarget> targets = getAjaxTargets();
		if (targets != null && !targets.isEmpty()) {
			ajaxReload.addTargets(targets);
		}
		pollingContainer.setVisible(true);
		ajaxReload.setVisible(true);
		if (!alreadyPolling) {
			handleStartedPolling();
		}
	}

	@Override
	public List<AjaxTarget> getAjaxTargets() {
		return getComponentModel().extraTargets;
	}

	@Override
	public void addAjaxTarget(final AjaxTarget target) {
		PollingModel model = getOrCreateComponentModel();
		if (model.extraTargets == null) {
			model.extraTargets = new ArrayList();
		}
		if (!model.extraTargets.contains(target)) {
			model.extraTargets.add(target);
		}
	}

	/**
	 * @return true if start polling manually with the start button.
	 */
	@Override
	public boolean isManualStart() {
		return getComponentModel().manualStart;
	}

	/**
	 *
	 * @param manualStart true if start polling manually with the start button
	 */
	@Override
	public void setManualStart(final boolean manualStart) {
		getOrCreateComponentModel().manualStart = manualStart;
	}

	/**
	 * The messages for the panel.
	 *
	 * @return the messages for the panel
	 */
	protected WMessages getMessages() {
		return messages;
	}

	protected void handleErrorMessage(final String msg) {
		handleErrorMessage(Arrays.asList(msg));
	}

	protected void handleErrorMessage(final List<String> msgs) {
		for (String msg : msgs) {
			getMessages().error(msg);
		}
	}

	/**
	 * @return the retry button.
	 */
	@Override
	public WButton getRetryButton() {
		return retryButton;
	}

	/**
	 * @return the start button
	 */
	@Override
	public WButton getStartButton() {
		return startButton;
	}

	/**
	 *
	 * @return true if need to stop polling
	 */
	protected boolean checkForStopPolling() {
		PollingStatus status = getPollingStatus();
		return status != PollingStatus.PROCESSING;
	}

	/**
	 * Start polling.
	 */
	protected void handleStartedPolling() {
		// Do Nothing
	}

	/**
	 * Stopped polling and panel has been reloaded.
	 */
	protected void handleStoppedPolling() {
		// Do Nothing
	}

	/**
	 * @return true if polling and is the current AJAX trigger.
	 */
	protected boolean isPollingTrigger() {
		return isPolling() && AjaxHelper.isCurrentAjaxTrigger(ajaxPolling);
	}

	/**
	 * @return true if currently polling
	 */
	protected boolean isPolling() {
		return pollingContainer.isVisible();
	}

	/**
	 * @return the script to step the progress bar
	 */
	protected String buildProgressBarScript() {
		StringBuilder script = new StringBuilder();
		script.append("<script type='text/javascript'>");
		script.append("function startStepProgressBar() {");
		script.append("  var elem = document.getElementById('").append(pollingProgressBar.getId()).append("');");
		script.append("  window.setInterval(stepProgressBar, 250, elem);");
		script.append("}");
		script.append("function stepProgressBar(bar) {");
		script.append("   if (bar.value > 99) { bar.value = 0; }");
		script.append("   bar.value++;");
		script.append("}");
		script.append("window.setTimeout(startStepProgressBar, 1000);");
		script.append("</script>");
		return script.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PollingModel newComponentModel() {
		return new PollingModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PollingModel getOrCreateComponentModel() {
		return (PollingModel) super.getOrCreateComponentModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PollingModel getComponentModel() {
		return (PollingModel) super.getComponentModel();
	}

	/**
	 * This model holds the state information.
	 */
	public static class PollingModel extends DivModel {

		/**
		 * Service status.
		 */
		private PollingStatus serviceStatus = PollingStatus.NOT_STARTED;

		/**
		 * Polling text.
		 */
		private String pollingText = "Loading....";

		/**
		 * The polling interval in milli seconds.
		 */
		private int pollingInterval;

		/**
		 * Extra AJAX targets when polling stops.
		 */
		private List<AjaxTarget> extraTargets;

		/**
		 * Flag if start polling manually with the start button.
		 */
		private boolean manualStart;

	}

}
