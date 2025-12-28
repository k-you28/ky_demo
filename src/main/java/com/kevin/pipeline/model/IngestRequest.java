package com.kevin.pipeline.model;

public class IngestRequest {

	private int payloadID;
	private String payload;

	public IngestRequest() {
		System.out.println("Def constructor request");
	}
	public IngestRequest(int id, String payload) {
		System.out.println("New Request made");
		this.payloadID = id;
		this.payload = payload;
	}

	public int getID() {
		return this.payloadID;
	}

	public String getPayload() {
		return this.payload;
	}

	public void setPayload(String newPayload) {
		this.payload = newPayload;
	}

}
