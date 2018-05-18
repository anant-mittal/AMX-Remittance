package com.amx.jax.postman;

public final class PostManUrls {

	public static final String SEND_SMS = "/postman/sms/send";
	public static final String SEND_EMAIL = "/postman/email/send";
	public static final String SEND_EMAIL_OLD = "/email/api/send/transaction/email";
	public static final String SEND_EMAIL_SUPPORT = "/postman/email/support";
	public static final String NOTIFY_SLACK = "/postman/slack/notify";
	public static final String NOTIFY_PUSH = "/postman/push/notify";
	public static final String NOTIFY_PUSH_SUBSCRIBE = "/postman/subscribe/{topic}";
	public static final String NOTIFY_SLACK_EXCEP = "/postman/slack/exception";
	public static final String PROCESS_TEMPLATE = "/postman/template/process";
	public static final String PROCESS_TEMPLATE_FILE = "/postman/template/file";
	public static final String GEO_LOC = "/geo/location";

	public static final String LIST_TENANT = "/postman/list/tenant";
	public static final String LIST_NATIONS = "/postman/list/nations";
	public static final String LIST_BRANCHES = "/postman/list/branches";

}
