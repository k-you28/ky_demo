package com.kevin.pipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookRequest {

	@JsonProperty("requestKey")
	private String requestKey;

	private String payloadType;
	private String payload;

	public WebhookRequest() {}
	public WebhookRequest(String id, String payload) {
		this.requestKey = id;
		this.payload = payload;
		this.payloadType = null;
	}

	public WebhookRequest(String id, String payload, String payloadType) {
		this.requestKey = id;
		this.payload = payload;
		this.payloadType = payloadType;
	}

	public String getrequestKey() {
		return this.requestKey;
	}
	public void setrequestKey(String newID) { this.requestKey = newID; }

	public String getPayload() {
		return this.payload;
	}
	public void setPayload(String newPayload) {
		this.payload = newPayload;
	}

	public String getPayloadType() { return this.payloadType; }
	public void setPayloadType(String newPayloadType) { this.payloadType = newPayloadType; }
}
