package edu.ksu.cis.macr.aasis.agent.cc_message.connect;

public interface IConnectMessageContent {
    @Override
    String toString();


    double getDelay();

    String getExpectedMasterAbbrev();

    String getMessage();

    String getOrganizationAbbrev();
}
