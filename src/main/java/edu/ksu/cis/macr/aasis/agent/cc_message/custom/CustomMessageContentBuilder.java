package edu.ksu.cis.macr.aasis.agent.cc_message.custom;

public class CustomMessageContentBuilder {
    private String sampleText;
    private int sampleValue;
    private EquipmentStatus equipmentStatus;


    public CustomMessageContent createCustomMessageContent() {
        return new CustomMessageContent(sampleText, sampleValue, equipmentStatus);
    }

    public CustomMessageContentBuilder setEquipmentStatus(EquipmentStatus equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
        return this;
    }

    public CustomMessageContentBuilder setSampleText(String sampleText) {
        this.sampleText = sampleText;
        return this;
    }

    public CustomMessageContentBuilder setSampleValue(int sampleValue) {
        this.sampleValue = sampleValue;
        return this;
    }
}