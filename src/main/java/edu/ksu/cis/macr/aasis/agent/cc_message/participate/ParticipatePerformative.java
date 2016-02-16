package edu.ksu.cis.macr.aasis.agent.cc_message.participate;

/**
 * Performatives indicate a primary characteristic of the message and may be used to route behavior in plans.
 */
public enum ParticipatePerformative {
    /**
     *
     */
    ASSIGNMENT, /* directed */
    ASSIGNMENT_RECEIVED, /* directed */
    DEASSIGNMENT, /* directed */
    DEASSIGNMENT_RECEIVED, /* directed */
    GOAL_MODIFICATION, /* directed */
    GOAL_MODIFICATION_RECEIVED, /* directed */
    BROADCASTING_AGENT_REGISTRATION, /* broadcast */
    AGENT_REGISTRATION_CONFIRMATION, /* directed */
    AGENT_REGISTRATION_CONFIRMATION_RECEIVED, /* directed */
    AGENT_UPDATE_INFORMATION, /* directed */
    AGENT_UPDATE_INFORMATION_RECEIVED, /* directed */
    GOAL_MODEL_EVENT, /* directed */
    EVENT_RECEIVED, /* directed */
    AGENT_FAILURE
}
