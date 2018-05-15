package com.amx.jax.postman.model;

public class Notipy extends Message {

	public static enum Color {
		DANGER("danger"), WARNING("warning"), SUCCESS("#36a64f"), INFO("info");

		public static final Color DEFAULT = INFO;

		String code;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		Color(String code) {
			this.code = code;
		}
	}

	public static enum Workspace {
		ALMEX, MODEX
	}

	public static enum Channel {
		NOTIPY("C9AK11W2K"), DEPLOYER("C8L3GL92A"), GENERAL("C7F823MLJ"), INQUIRY("CAQ4WUNAZ", Workspace.MODEX);

		String code;
		Workspace workspace;

		public static final Channel DEFAULT = GENERAL;

		Channel(String code) {
			this.code = code;
			this.workspace = Workspace.ALMEX;
		}

		Channel(String code, Workspace workspace) {
			this.code = code;
			this.workspace = workspace;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Workspace getWorkspace() {
			return workspace;
		}

		public void setWorkspace(Workspace workspace) {
			this.workspace = workspace;
		}
	}

	protected Channel channel = Channel.DEFAULT;
	protected Color color = Color.DEFAULT;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
