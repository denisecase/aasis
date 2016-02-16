/** * A general class for connections or other classes shared between different organizations in * this simulation, for example, connections and connection guidelines use the same RabbitMQ enabled classes for both the * grid control holorachy and the power sales auctioning hierarchy. * * Guidelines are provided as parameters to goals. * Connection guidelines provide information about the other agent and the organization in which they will be working, * including who should start as the default master and where the organization specification that * drives the organization behavior can be found. * */
package edu.ksu.cis.macr.aasis.common;