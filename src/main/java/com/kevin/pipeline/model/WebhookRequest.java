package com.kevin.pipeline.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookRequest {

	@JsonProperty("eventId")
	private String eventID;

	private String payloadType;
	private String payload;

	public WebhookRequest() {}
	public WebhookRequest(String id, String payload) {
		this.eventID = id;
		this.payload = payload;
		this.payloadType = null;
	}

	public WebhookRequest(String id, String payload, String payloadType) {
		this.eventID = id;
		this.payload = payload;
		this.payloadType = payloadType;
	}

	public String getEventID() {
		return this.eventID;
	}
	public void setEventID(String newID) { this.eventID = newID; }

	public String getPayload() {
		return this.payload;
	}
	public void setPayload(String newPayload) {
		this.payload = newPayload;
	}

	public String getPayloadType() { return this.payloadType; }
	public void setPayloadType(String newPayloadType) { this.payloadType = newPayloadType; }
}
