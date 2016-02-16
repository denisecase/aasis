package edu.ksu.cis.macr.aasis.agent.cc_message.custom;

/**
 * Performatives indicate a primary characteristic of the message and may be used to route behavior in plans.
 */
public enum Performative {
    /**
     * The agent is operating within the provided guidelines and (later) expects to remain within bounds throughout the
     * planning horizon.
     */
    GOOD,
    /**
     * The agent is operating (or expects to operate) in violation of one or more of its specified guidelines.
     */
    BAD
}
