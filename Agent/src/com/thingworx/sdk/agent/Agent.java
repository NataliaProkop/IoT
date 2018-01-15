package com.thingworx.sdk.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.communications.common.SecurityClaims;

/**
 *
 * @author Natalia
 */
//Refer to the "Delivery Truck Example" section of the documentation
//for a detailed explanation of this example's operation 
public class Agent extends ConnectedThingClient {

    private static final Logger LOG = LoggerFactory.getLogger(Agent.class);

    public Agent(ClientConfigurator config) throws Exception {
        super(config);
    }

    // Test example
    public static void main(String[] args) throws Exception {
        //URI Example ws://localhost:80/Thingworx/WS
        //If not working, change the port
        //Based on Tomcat configuration, ie ws://localhost:8080/Thingworx/WS
        String uri = "ws://localhost:80/Thingworx/WS";

        //The keyId found in the default_key that was
        //imported into the ThingWorx Composer
        //Updated based on that value
        String appKey = "56d47017-06f5-4a3e-b68c-1005e417745a";

        // Set the required configuration information
        ClientConfigurator config = new ClientConfigurator();
        // The uri for connecting to ThingWorx
        config.setUri(uri);

        // This will allow us to test against a server using a self-signed certificate.
        // This should be removed for production systems.
        config.ignoreSSLErrors(true); // All self signed certs

        // Set the security using an Application Key
        //Login can be performed using a username/password combo
        //Belonging to a user that has been added within the ThingWorx Composer
        //This is not recommended
        //SecurityClaims claims = SecurityClaims.fromCredentials("default_user", "admin");
        SecurityClaims claims = SecurityClaims.fromAppKey(appKey);
        config.setSecurityClaims(claims);

        // Create the client passing in the configuration from above
        
        Agent client = new Agent(config);
        RoomThing room6 = new RoomThing("Room6", "Room6 in ThingWorx composer", client);
        client.bindThing(room6);
        RoomThing room5 = new RoomThing("Room5", "Room5 in ThingWorx composer", client);
        client.bindThing(room5);
        RoomThing room1 = new RoomThing("Room1", "Room1 in ThingWorx composer", client);
        client.bindThing(room1);
        RoomThing room2 = new RoomThing("Room2", "Room2 in ThingWorx composer", client);
        client.bindThing(room2);
        RoomThing room3 = new RoomThing("Room3", "Room3 in ThingWorx composer", client);
        client.bindThing(room3);
        RoomThing room4 = new RoomThing("Room4", "Room4 in ThingWorx composer", client);
        client.bindThing(room4);
        
        try {
            // Start the client. The client will connect to the server and 
            // authenticate, using the Application Key specified above.
            client.start();

            LOG.info("The client is now connected.");

            // As long as the client has not been shutdown, continue
            while (!client.isShutdown()) {
                // Only process the Virtual Things if the client is connected
                if (client.isConnected()) {
                    // Loop over all the Virtual Things and process them
                    for (VirtualThing thing : client.getThings().values()) {
                        try {
                            thing.processScanRequest();
                        } catch (Exception eProcessing) {
                            System.out.println("Error Processing Scan Request for [" + thing.getName() + "] : " + eProcessing.getMessage());
                        }
                    }
                }

                // Suspend processing at the scan rate interval
                Thread.sleep(1000);
            }
        } catch (Exception eStart) {
            System.out.println("Initial Start Failed : " + eStart.getMessage());
        }
    }

}
