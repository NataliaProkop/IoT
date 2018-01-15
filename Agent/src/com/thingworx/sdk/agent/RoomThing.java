package com.thingworx.sdk.agent;

import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;

import org.slf4j.Logger;

import com.thingworx.metadata.annotations.ThingworxEventDefinition;
import com.thingworx.metadata.annotations.ThingworxEventDefinitions;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.metadata.collections.FieldDefinitionCollection;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.constants.CommonPropertyNames;
import com.thingworx.types.primitives.DatetimePrimitive;
import com.thingworx.types.primitives.LocationPrimitive;
import com.thingworx.types.primitives.NumberPrimitive;
import com.thingworx.types.primitives.StringPrimitive;
import com.thingworx.types.primitives.structs.Location;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.ServiceDefinition;
import java.util.Random;
import org.joda.time.DateTime;

/**
 *
 * @author Natalia
 */
@ThingworxPropertyDefinitions(properties = {
    @ThingworxPropertyDefinition(name = "id", description = "", baseType = "NUMBER",
            aspects = {"dataChangeType:VALUE",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:TRUE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:0"}),
    @ThingworxPropertyDefinition(name = "temperature", description = "", baseType = "NUMBER",
            aspects = {"dataChangeType:VALUE",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:TRUE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:0"}),
    @ThingworxPropertyDefinition(name = "occupancy", description = "", baseType = "BOOLEAN",
            aspects = {"dataChangeType:VALUE",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:TRUE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:false"}),
    @ThingworxPropertyDefinition(name = "humidity", description = "", baseType = "NUMBER",
            aspects = {"dataChangeType:VALUE",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:TRUE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:0"}),
    @ThingworxPropertyDefinition(name = "heating", description = "", baseType = "BOOLEAN",
            aspects = {"dataChangeType:VALUE",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:TRUE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:false"}),
    @ThingworxPropertyDefinition(name = "airConditioning", description = "", baseType = "BOOLEAN",
            aspects = {"dataChangeType:VALUE",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:TRUE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:false"}),
    @ThingworxPropertyDefinition(name = "light", description = "", baseType = "BOOLEAN",
            aspects = {"dataChangeType:VALUE",
                "dataChangeThreshold:0",
                "cacheTime:0",
                "isPersistent:TRUE",
                "isReadOnly:FALSE",
                "pushType:ALWAYS",
                "isFolded:FALSE",
                "defaultValue:false"}),})
public class RoomThing extends VirtualThing implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RoomThing.class);

    private final static String ID_FIELD = "id";
    private final static String TEMPERATURE_FIELD = "temperature";
    private final static String OCCUPANTY_FIELD = "occupancy";
    private final static String HUMIDITY_FIELD = "humidity";
    private final static String HEATING_FIELD = "heating";
    private final static String AIR_CONDITIONING_FIELD = "airConditioning";
    private final static String LIGHT_FIELD = "light";
    private Double id, temperature, humidity;
    private Boolean occupancy, heating, airConditioning, light;
    private String thingName = null;
    private Random rand = new Random();

    public RoomThing(String name, String description, ConnectedThingClient client) throws Exception {
        super(name, description, client);
        thingName = name;
        super.initializeFromAnnotations();
        this.init();
    }

    public void synchronizeState() {
        // Be sure to call the base class
        super.synchronizeState();
        // Send the property values to ThingWorx when a synchronization is required
        super.syncProperties();
    }

    private void init() {
        //get
        try {
            id = getId();
        } catch (Exception ex) {
            id = new Double(0);
        }
        if (id == null) {
            id = new Double(0);
        }
        temperature = rand.nextDouble();
        humidity = rand.nextDouble();
        occupancy = rand.nextBoolean();
        heating = rand.nextBoolean();
        airConditioning = rand.nextBoolean();
        light = rand.nextBoolean();

        try {
            setId();
            setTemperature();
            setHumidity();
            setOccupancy();
            setHeating();
            setAirConditioning();
            setLight();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }
    }

    @Override
    public void run() {
        try {
            // Delay for a period to verify that the Shutdown service will return
            Thread.sleep(1000);
            // Shutdown the client
            this.getClient().shutdown();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }
    }

    @Override
    public void processScanRequest() throws Exception {
        //get
        try {
            id = getId();
        } catch (Exception ex) {
            id = new Double(0);
        }
        if (id == null) {
            id = new Double(0);
        }
        id += 1;
        temperature = round(30 * rand.nextDouble(), 2);
        humidity = round(10 + (100 - 10) * rand.nextDouble(), 2);
        occupancy = rand.nextBoolean();
        heating = rand.nextBoolean();
        airConditioning = rand.nextBoolean();
        light = rand.nextBoolean();
        LOG.info("Sending " + thingName + " id " + id.doubleValue());
        addtoTable();
        try {
            setId();
            setTemperature();
            setHumidity();
            setOccupancy();
            setHeating();
            setAirConditioning();
            setLight();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }

        this.updateSubscribedProperties(5000);
    }

    public Double getId() {
        return (Double) getProperty(ID_FIELD).getValue().getValue();
    }

    public void setId() throws Exception {
        setProperty(ID_FIELD, this.id);
    }

    public void setTemperature() throws Exception {
        setProperty(TEMPERATURE_FIELD, this.temperature);
    }

    public void setHumidity() throws Exception {
        setProperty(HUMIDITY_FIELD, this.humidity);
    }

    public void setOccupancy() throws Exception {
        setProperty(OCCUPANTY_FIELD, this.occupancy);
    }

    public void setHeating() throws Exception {
        setProperty(HEATING_FIELD, this.heating);
    }

    public void setAirConditioning() throws Exception {
        setProperty(AIR_CONDITIONING_FIELD, this.airConditioning);
    }

    public void setLight() throws Exception {
        setProperty(LIGHT_FIELD, this.light);
    }

    public void addtoTable() throws Exception {
        ValueCollection params = new ValueCollection();
        params.put("Date", new DatetimePrimitive(DateTime.now()));
        this.getClient().invokeService(ThingworxEntityTypes.Things, this.thingName, "AddToTable", params, 10000);
        LOG.info("Adding to table " + thingName + " id " + id.doubleValue());
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
