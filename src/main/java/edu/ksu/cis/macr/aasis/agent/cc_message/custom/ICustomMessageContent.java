package edu.ksu.cis.macr.aasis.agent.cc_message.custom;

import java.io.IOException;

/**
 * Provides an interface for defining the content in an a custom (application-specific message).
 */
public interface ICustomMessageContent {
    public static ICustomMessageContent create(String sampleText, int sampleValue, EquipmentStatus equipmentStatus) {
        return new CustomMessageContent(sampleText, sampleValue, equipmentStatus);
    }

    Object deserialize(byte[] bytes) throws Exception;

    public EquipmentStatus getEquipmentStatus();

    public void setEquipmentStatus(EquipmentStatus equipmentStatus);

    public String getSampleText();

    public void setSampleText(String sampleText);

    public int getSampleValue();

    public void setSampleValue(int sampleValue);

    byte[] serialize() throws IOException;
}
