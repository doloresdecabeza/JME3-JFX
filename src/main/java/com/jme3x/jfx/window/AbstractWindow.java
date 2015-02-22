package com.jme3x.jfx.window;

import java.net.URL;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.Region;

import com.jme3x.jfx.AbstractHud;

public abstract class AbstractWindow extends AbstractHud {
	private Region					inner;
	private Region					outer;

	private boolean					init;

	private SimpleBooleanProperty	externalized	= new SimpleBooleanProperty();
	private SimpleBooleanProperty	externalizeAble	= new SimpleBooleanProperty(true);

	private SimpleBooleanProperty	moveAble		= new SimpleBooleanProperty(true);

	private SimpleBooleanProperty	minimizeAble	= new SimpleBooleanProperty(true);
	private SimpleBooleanProperty	minimized		= new SimpleBooleanProperty();
	private SimpleBooleanProperty	maximizeAble	= new SimpleBooleanProperty(true);
	private SimpleBooleanProperty	maximized		= new SimpleBooleanProperty();
	private SimpleBooleanProperty	modal			= new SimpleBooleanProperty();

	private SimpleBooleanProperty	resizable		= new SimpleBooleanProperty(true);
	private SimpleBooleanProperty	innerScroll		= new SimpleBooleanProperty(false);

	private SimpleStringProperty	title			= new SimpleStringProperty("Untitled Window");
	private WindowController		controller;

	public BooleanProperty minimizeAbleProperty() {
		return this.minimizeAble;
	}

	public StringProperty titleProperty() {
		return this.title;
	}

	public BooleanProperty innerScrollProperty() {
		return this.innerScroll;
	}

	public BooleanProperty resizableProperty() {
		return this.resizable;
	}

	public BooleanProperty externalized() {
		return this.externalized;
	}

	/**
	 * centers a window on the screen, using it's current width and height this method does not work correctly, for some reason
	 */
	@Deprecated
	public void center() {
		assert this.init : "Needs to be init to center";
		final double sceneWidth = this.getNode().getScene().getWidth();
		final double sceneHeight = this.getNode().getScene().getHeight();

		double windowWidth = this.inner.getWidth();
		if (windowWidth == 0) {
			windowWidth = Math.max(this.inner.getPrefWidth(), this.inner.getMinWidth());
		}

		double windowHeight = this.inner.getHeight();
		if (windowHeight == 0) {
			windowHeight = Math.max(this.inner.getPrefHeight(), this.inner.getMinHeight());
		}

		final double newPosx = (sceneWidth / 2) - (windowWidth / 2);
		final double newPosy = (sceneHeight / 2) - (windowHeight / 2);
		this.setLayoutX((int) newPosx);
		this.setLayoutY((int) newPosy);

	}

	public BooleanProperty externalizeAbleProperty() {
		return this.externalizeAble;
	}

	public BooleanProperty modalProperty() {
		return this.modal;
	}

	public Region getWindowContent() {
		return this.inner;
	}

	@Override
	protected Region doInit() {
		try {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			final URL location = Thread.currentThread().getContextClassLoader().getResource("com/jme3x/jfx/window/window.fxml");
			final URL css = Thread.currentThread().getContextClassLoader().getResource("com/jme3x/jfx/window/window.css");
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			this.outer = fxmlLoader.load(location.openStream());
			this.outer.getStylesheets().clear();
			this.outer.getStylesheets().add(css.toExternalForm());
			this.controller = fxmlLoader.getController();

			this.inner = this.innerInit();
			this.controller.setWindow(this);

			this.init = true;
			this.afterInit();
			return this.outer;
		} catch (final Throwable t) {
			this.setInnerError(t);
			return null;
		}
	}

	public void close() {
		if (!this.isAttached()) {
			return;
		}
		System.out.println("close request");
	}

	/**
	 * custom init code, eg for setting the enforced settings
	 */
	protected abstract void afterInit();

	/**
	 * can be called from any Thread, if not jfx thread is async
	 * 
	 * @param title
	 */
	public void setLayoutX(final double x) {
		this.outer.setLayoutX(x);
	}

	protected void setSize(final int i, final int j) {
		this.outer.setPrefSize(i, j);
	}

	/**
	 * can be called from any Thread, if not jfx thread is async
	 * 
	 * @param title
	 */
	public void setLayoutY(final double y) {
		this.outer.setLayoutY(y);
	}

	public Region getInnerWindow() {
		return this.outer;
	}

	public BooleanProperty moveAbleProperty() {
		return this.moveAble;
	}

}
