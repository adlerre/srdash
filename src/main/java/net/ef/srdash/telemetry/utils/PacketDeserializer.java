package net.ef.srdash.telemetry.utils;

import java.util.ArrayList;
import java.util.List;

import net.ef.srdash.telemetry.data.Packet;
import net.ef.srdash.telemetry.data.PacketCarDamageData;
import net.ef.srdash.telemetry.data.PacketCarSetupData;
import net.ef.srdash.telemetry.data.PacketCarStatusData;
import net.ef.srdash.telemetry.data.PacketCarTelemetryData;
import net.ef.srdash.telemetry.data.PacketEventData;
import net.ef.srdash.telemetry.data.PacketLapData;
import net.ef.srdash.telemetry.data.PacketMotionData;
import net.ef.srdash.telemetry.data.PacketParticipantsData;
import net.ef.srdash.telemetry.data.PacketSessionData;
import net.ef.srdash.telemetry.data.PacketSessionHistoryData;
import net.ef.srdash.telemetry.data.elements.ButtonStatus;
import net.ef.srdash.telemetry.data.elements.CarDamageData;
import net.ef.srdash.telemetry.data.elements.CarMotionData;
import net.ef.srdash.telemetry.data.elements.CarSetupData;
import net.ef.srdash.telemetry.data.elements.CarStatusData;
import net.ef.srdash.telemetry.data.elements.CarTelemetryData;
import net.ef.srdash.telemetry.data.elements.DriverStatus;
import net.ef.srdash.telemetry.data.elements.Era;
import net.ef.srdash.telemetry.data.elements.Header;
import net.ef.srdash.telemetry.data.elements.LapData;
import net.ef.srdash.telemetry.data.elements.LapHistoryData;
import net.ef.srdash.telemetry.data.elements.MarshalZone;
import net.ef.srdash.telemetry.data.elements.ParticipantData;
import net.ef.srdash.telemetry.data.elements.PitStatus;
import net.ef.srdash.telemetry.data.elements.ResultStatus;
import net.ef.srdash.telemetry.data.elements.SafetyCarStatus;
import net.ef.srdash.telemetry.data.elements.SessionType;
import net.ef.srdash.telemetry.data.elements.TyreStintHistoryData;
import net.ef.srdash.telemetry.data.elements.Weather;
import net.ef.srdash.telemetry.data.elements.WeatherForecast;
import net.ef.srdash.telemetry.data.elements.WheelData;
import net.ef.srdash.telemetry.data.elements.ZoneFlag;

/**
 * F1 2018/2019 PacketDeserializer is the main class for deserializing the incoming
 * UDP packets and building Packet POJOs from the byte arrays
 * 
 * This class was created based on the documented UDP Specification located on
 * the Codemasters forums.
 * 
 * @author eh7n
 * @author Ren\u00E9 Adler (eagle)
 * 
 * @see https://forums.codemasters.com/discussion/136948/f1-2018-udp-specification
 * @see https://f1-2019-telemetry.readthedocs.io/en/latest/telemetry-specification.html
 * @see https://forums.codemasters.com/topic/50942-f1-2020-udp-specification/
 * @see https://github.com/raweceek-temeletry/f1-2021-udp
 *
 */
public class PacketDeserializer {

    public static final int F1_AUTO = -1;
    public static final int F1_2018 = 2018;
    public static final int F1_2019 = 2019;
    public static final int F1_2020 = 2020;
    public static final int F1_2021 = 2021;

    public static final int MAX_PACKET_SIZE_F1_2018 = 1341;
    public static final int MAX_PACKET_SIZE_F1_2019 = 1347;
    public static final int MAX_PACKET_SIZE_F1_2020 = 1464; // don't know yet
    public static final int MAX_PACKET_SIZE_F1_2021 = 2000; // don't know yet

    private int version;

    private PacketBuffer buffer;

    private PacketDeserializer(byte[] data, int version) {
        buffer = PacketBuffer.wrap(data);
        this.version = version;
    }

    /**
     * Read the packet data from a byte array
     * 
     * @param data : a F1 2018/2019 UDP packet
     * @return a Packet POJO
     */
    public static Packet read(byte[] data) {
        return read(data, F1_AUTO);
    }

    /**
     * Read the packet data from a byte array
     * 
     * @param data : a F1 2018/2019 UDP packet
     * @return a Packet POJO
     */
    public static Packet read(byte[] data, int version) {
        return new PacketDeserializer(data, version).buildPacket();
    }

    public static int maxPacketSize(int version) {
        switch (version) {
        case F1_2019:
            return MAX_PACKET_SIZE_F1_2019;
        case F1_2018:
            return MAX_PACKET_SIZE_F1_2018;
        case F1_2020:
            return MAX_PACKET_SIZE_F1_2020;
        case F1_2021:
            return MAX_PACKET_SIZE_F1_2021;
        default:
            return 2000;
        }
    }

    private int numberOfCars() {
        switch (version) {
        case F1_2020:
            return 22;
        case F1_2021:
            return 22;
        default:
            return 20;
        }

    }

    private Packet buildPacket() {

        Header header = buildHeader();

        switch (header.getPacketId()) {
        case 0:
            return buildPacketMotionData(header);
        case 1:
            return buildPacketSessionData(header);
        case 2:
            return buildPacketLapData(header);
        case 3:
            return buildPacketEventData(header);
        case 4:
            return buildPacketParticipantsData(header);
        case 5:
            return buildPacketCarSetupData(header);
        case 6:
            return buildPacketCarTelemetryData(header);
        case 7:
            return buildPacketCarStatusData(header);
        case 10:
            return buildPacketCarDamageData(header);
        case 11:
            return buildPacketSessionHistoryData(header);
        }

        return null;
    }

    /**
     * HEADER
     * 
     * Each packet has the following header
     * 
     * <pre>
     * {@code 
     	struct PacketHeader
    	{
    		uint16    m_packetFormat;             // 2018
    		uint8     m_gameMajorVersion;         // Game major version - "X.00"
            uint8     m_gameMinorVersion;         // Game minor version - "1.XX"
    		uint8     m_packetVersion;            // Version of this packet type, all start from 1
    		uint8     m_packetId;                 // Identifier for the packet type, see below
    		uint64    m_sessionUID;               // Unique identifier for the session
    		float     m_sessionTime;              // Session timestamp
    		uint32    m_frameIdentifier;          // Identifier for the frame the data was retrieved on
    		uint8     m_playerCarIndex;           // Index of player's car in the array
    		uint8     m_secondaryPlayerCarIndex;  // Index of secondary player's car in
                                                  // the array (split-screen)
                                                  // 255 if no second player
    	};
     * }
     * </pre>
     * 
     * @return the Header pojo
     */
    private Header buildHeader() {

        Header header = new Header();

        header.setPacketFormat(buffer.getNextUInt16AsInt()); // 2

        if (version == -1 || version != header.getPacketFormat()) {
            version = header.getPacketFormat();
        }

        if (version >= F1_2019) {
            header.setGameMajorVersion(buffer.getNextUInt8AsInt()); // 1
            header.setGameMinorVersion(buffer.getNextUInt8AsInt()); // 1
        }
        header.setPacketVersion(buffer.getNextUInt8AsInt()); // 1
        header.setPacketId(buffer.getNextUInt8AsInt()); // 1
        header.setSessionUID(buffer.getNextUInt64AsBigInteger()); // 8
        header.setSessionTime(buffer.getNextFloat());// 4
        header.setFrameIdentifier(buffer.getNextUIntAsLong());// 4
        header.setPlayerCarIndex(buffer.getNextUInt8AsInt()); // 1
        if (version >= F1_2020) {
            header.setSecondaryPlayerCarIndex(buffer.getNextUInt8AsInt()); // 1
        }

        return header;
    }

    private float msToSeconds(int ms) {
        return ms != 0 ? (float) ms / (float) 1000 : 0;
    }

    private float msToSeconds(long ms) {
        return ms != 0 ? (float) ms / (float) 1000 : 0;
    }

    /**
     * LAP DATA PACKET
     * 
     * The lap data packet gives details of all the cars in the session.
     * 
     * Frequency: Rate as specified in menus
     * 
     * Size: 841 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketLapData
    	{
    		PacketHeader    m_header;              // Header
    		LapData         m_lapData[20];         // Lap data for all cars on track
    	};
     * }
     * </pre>
     * 
     * @return the PacketLapData pojo
     */
    private PacketLapData buildPacketLapData(Header header) {
        PacketLapData packetData = new PacketLapData();
        List<LapData> lapDataList = new ArrayList<>();

        int playersIndex = header.getPlayerCarIndex();
        for (int i = 0; i < numberOfCars(); i++) {
            lapDataList.add(buildLapData(i, i == playersIndex));
        }

        packetData.setHeader(header);
        packetData.setLapDataList(lapDataList);
        return packetData;
    }

    /**
     * LAP DATA
     * 
     * <pre>
     * {@code 
    	struct LapData
    	{
    	    // <= F1 2019
    	    float       m_lastLapTime;                 // Last lap time in seconds
    	    float       m_currentLapTime;              // Current time around the lap in seconds
    	    float       m_bestLapTime;                 // Best lap time of the session in seconds
    	    float       m_sector1Time;                 // Sector 1 time in seconds
    	    float       m_sector2Time;                 // Sector 2 time in seconds
    	    
    	    // == F1 2020
    	    float    m_lastLapTime;                    // Last lap time in seconds
            float    m_currentLapTime;                 // Current time around the lap in seconds
            uint16   m_sector1TimeInMS;                // Sector 1 time in milliseconds
            uint16   m_sector2TimeInMS;                // Sector 2 time in milliseconds
            float    m_bestLapTime;                    // Best lap time of the session in seconds
            uint8    m_bestLapNum;                     // Lap number best time achieved on
            uint16   m_bestLapSector1TimeInMS;         // Sector 1 time of best lap in the session in milliseconds
            uint16   m_bestLapSector2TimeInMS;         // Sector 2 time of best lap in the session in milliseconds
            uint16   m_bestLapSector3TimeInMS;         // Sector 3 time of best lap in the session in milliseconds
            uint16   m_bestOverallSector1TimeInMS;     // Best overall sector 1 time of the session in milliseconds
            uint8    m_bestOverallSector1LapNum;       // Lap number best overall sector 1 time achieved on
            uint16   m_bestOverallSector2TimeInMS;     // Best overall sector 2 time of the session in milliseconds
            uint8    m_bestOverallSector2LapNum;       // Lap number best overall sector 2 time achieved on
            uint16   m_bestOverallSector3TimeInMS;     // Best overall sector 3 time of the session in milliseconds
            uint8    m_bestOverallSector3LapNum;       // Lap number best overall sector 3 time achieved on
    	    
    	    // >= F1 2021
    	    uint32      m_lastLapTimeInMS;             // Last lap time in milliseconds
            uint32      m_currentLapTimeInMS;          // Current time around the lap in milliseconds
            uint16      m_sector1TimeInMS;             // Sector 1 time in milliseconds
            uint16      m_sector2TimeInMS;             // Sector 2 time in milliseconds
    	    
    	    float       m_lapDistance;                 // Distance vehicle is around current lap in metres – could
    	                                               // be negative if line hasn’t been crossed yet
    	    float       m_totalDistance;               // Total distance travelled in session in metres – could
    	                                               // be negative if line hasn’t been crossed yet
    	    float       m_safetyCarDelta;              // Delta in seconds for safety car
    	    uint8       m_carPosition;                 // Car race position
    	    uint8       m_currentLapNum;               // Current lap number
    	    uint8       m_pitStatus;                   // 0 = none, 1 = pitting, 2 = in pit area
    	    
    	    // >= F1 2020 ?
    	    uint8       m_numPitStops;                 // Number of pit stops taken in this race
    	    
    	    uint8       m_sector;                      // 0 = sector1, 1 = sector2, 2 = sector3
    	    uint8       m_currentLapInvalid;           // Current lap invalid - 0 = valid, 1 = invalid
    	    uint8       m_penalties;                   // Accumulated time penalties in seconds to be added
    	    
    	    // >= F1 2020 ?
    	    uint8       m_warnings;                    // Accumulated number of warnings issued
            uint8       m_numUnservedDriveThroughPens; // Num drive through pens left to serve
            uint8       m_numUnservedStopGoPens;       // Num stop go pens left to serve
    	    
    	    uint8       m_gridPosition;                // Grid position the vehicle started the race in
    	    uint8       m_driverStatus;                // Status of driver - 0 = in garage, 1 = flying lap
    	                                               // 2 = in lap, 3 = out lap, 4 = on track
    	    uint8       m_resultStatus;                // Result status - 0 = invalid, 1 = inactive, 2 = active
    	                                               // 3 = finished, 4 = disqualified, 5 = not classified
    	                                               // 6 = retyred
    	                   
            // >= F1 2020 ?
    	    uint8       m_pitLaneTimerActive;          // Pit lane timing, 0 = inactive, 1 = active
            uint16      m_pitLaneTimeInLaneInMS;       // If active, the current time spent in the pit lane in ms
            uint16      m_pitStopTimerInMS;            // Time of the actual pit stop in ms
            uint8       m_pitStopShouldServePen;       // Whether the car should serve a penalty at this stop
    	};
     * }
     * </pre>
     */
    private LapData buildLapData(int carIndex, boolean playersCar) {
        LapData lapData = new LapData();

        lapData.setCarIndex(carIndex);
        lapData.setPlayersCar(playersCar);

        if (version <= F1_2019) {
            lapData.setLastLapTime(buffer.getNextFloat());
            lapData.setCurrentLapTime(buffer.getNextFloat());
            lapData.setBestLapTime(buffer.getNextFloat());
            lapData.setSector1Time(buffer.getNextFloat());
            lapData.setSector2Time(buffer.getNextFloat());
        } else if (version == F1_2020) {
            lapData.setLastLapTime(buffer.getNextFloat());
            lapData.setCurrentLapTime(buffer.getNextFloat());
            lapData.setSector1Time(msToSeconds(buffer.getNextUInt16AsInt()));
            lapData.setSector2Time(msToSeconds(buffer.getNextUInt16AsInt()));
            lapData.setBestLapTime(buffer.getNextFloat());
            lapData.setBestLapNum(buffer.getNextUInt8AsInt());
            lapData.setBestLapSector1TimeInMS(buffer.getNextUInt16AsInt());
            lapData.setBestLapSector2TimeInMS(buffer.getNextUInt16AsInt());
            lapData.setBestLapSector3TimeInMS(buffer.getNextUInt16AsInt());
            lapData.setBestOverallSector1LapNum(buffer.getNextUInt16AsInt());
            lapData.setBestOverallSector1LapNum(buffer.getNextUInt8AsInt());
            lapData.setBestOverallSector2LapNum(buffer.getNextUInt16AsInt());
            lapData.setBestOverallSector2LapNum(buffer.getNextUInt8AsInt());
            lapData.setBestOverallSector3LapNum(buffer.getNextUInt16AsInt());
            lapData.setBestOverallSector3LapNum(buffer.getNextUInt8AsInt());
        } else if (version >= F1_2021) {
            lapData.setLastLapTime(msToSeconds(buffer.getNextUIntAsLong()));
            lapData.setCurrentLapTime(msToSeconds(buffer.getNextUIntAsLong()));
            lapData.setSector1Time(msToSeconds(buffer.getNextUInt16AsInt()));
            lapData.setSector2Time(msToSeconds(buffer.getNextUInt16AsInt()));
        }
        lapData.setLapDistance(buffer.getNextFloat());
        lapData.setTotalDistance(buffer.getNextFloat());
        lapData.setSafetyCarDelta(buffer.getNextFloat());
        lapData.setCarPosition(buffer.getNextUInt8AsInt());
        lapData.setCurrentLapNum(buffer.getNextUInt8AsInt());
        lapData.setPitStatus(PitStatus.fromInt(buffer.getNextUInt8AsInt()));
        if (version >= F1_2021) {
            lapData.setNumPitStops(buffer.getNextUInt8AsInt());
        }
        lapData.setSector(buffer.getNextUInt8AsInt() + 1);
        lapData.setCurrentLapInvalid(buffer.getNextUInt8AsInt() == 1);
        lapData.setPenalties(buffer.getNextUInt8AsInt());
        if (version >= F1_2021) {
            lapData.setWarnings(buffer.getNextUInt8AsInt());
            lapData.setNumUnservedDriveThroughPens(buffer.getNextUInt8AsInt());
            lapData.setNumUnservedStopGoPens(buffer.getNextUInt8AsInt());
        }
        lapData.setGridPosition(buffer.getNextUInt8AsInt());
        lapData.setDriverStatus(DriverStatus.fromInt(buffer.getNextUInt8AsInt()));
        lapData.setResultStatus(ResultStatus.fromInt(buffer.getNextUInt8AsInt()));
        if (version >= F1_2021) {
            lapData.setPitLaneTimerActive(buffer.getNextUInt8AsInt() == 1);
            lapData.setPitLaneTimeInLane(msToSeconds(buffer.getNextUInt16AsInt()));
            lapData.setPitStopTimer(msToSeconds(buffer.getNextUInt16AsInt()));
            lapData.setPitStopShouldServePen(buffer.getNextUInt8AsInt());
        }

        return lapData;
    }

    /**
     * MOTION PACKET
     * 
     * The motion packet gives physics data for all the cars being driven. There is
     * additional data for the car being driven with the goal of being able to drive
     * a motion platform setup.
     * 
     * Frequency: Rate as specified in menus
     * 
     * Size: 1341 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketMotionData
    	{
    	    PacketHeader    m_header;               	// Header
    	
    	    CarMotionData   m_carMotionData[2?];		// Data for all cars on track
    	
    	    // Extra player car ONLY data
    	    float         m_suspensionPosition[4];       // Note: All wheel arrays have the following order:
    	    float         m_suspensionVelocity[4];       // RL, RR, FL, FR
    	    float         m_suspensionAcceleration[4];   // RL, RR, FL, FR
    	    float         m_wheelSpeed[4];               // Speed of each wheel
    	    float         m_wheelSlip[4];                // Slip ratio for each wheel
    	    float         m_localVelocityX;              // Velocity in local space
    	    float         m_localVelocityY;              // Velocity in local space
    	    float         m_localVelocityZ;              // Velocity in local space
    	    float         m_angularVelocityX;            // Angular velocity x-component
    	    float         m_angularVelocityY;            // Angular velocity y-component
    	    float         m_angularVelocityZ;            // Angular velocity z-component
    	    float         m_angularAccelerationX;        // Angular velocity x-component
    	    float         m_angularAccelerationY;        // Angular velocity y-component
    	    float         m_angularAccelerationZ;        // Angular velocity z-component
    	    float         m_frontWheelsAngle;            // Current front wheels angle in radians
    	};
     * }
     * </pre>
     * 
     * @return the PacketMotionData pojo
     */
    private PacketMotionData buildPacketMotionData(Header header) {

        PacketMotionData packetMotionData = new PacketMotionData();

        packetMotionData.setHeader(header);
        List<CarMotionData> carMotionDataList = new ArrayList<>();

        int playersCarIndex = packetMotionData.getHeader().getPlayerCarIndex();
        for (int carIndex = 0; carIndex < numberOfCars(); carIndex++) {
            carMotionDataList.add(buildCarMotionData(carIndex, carIndex == playersCarIndex));
        }
        packetMotionData.setCarMotionDataList(carMotionDataList);
        packetMotionData.setSuspensionPosition(new WheelData<Float>(buffer.getNextFloatArray(4)));
        packetMotionData.setSuspensionVelocity(new WheelData<Float>(buffer.getNextFloatArray(4)));
        packetMotionData.setSuspensionAcceleration(new WheelData<Float>(buffer.getNextFloatArray(4)));
        packetMotionData.setWheelSpeed(new WheelData<Float>(buffer.getNextFloatArray(4)));
        packetMotionData.setWheelSlip(new WheelData<Float>(buffer.getNextFloatArray(4)));
        packetMotionData.setLocalVelocityX(buffer.getNextFloat());
        packetMotionData.setLocalVelocityY(buffer.getNextFloat());
        packetMotionData.setLocalVelocityZ(buffer.getNextFloat());
        packetMotionData.setAngularVelocityX(buffer.getNextFloat());
        packetMotionData.setAngularVelocityY(buffer.getNextFloat());
        packetMotionData.setAngularVelocityZ(buffer.getNextFloat());
        packetMotionData.setAngularAccelerationX(buffer.getNextFloat());
        packetMotionData.setAngularAccelerationY(buffer.getNextFloat());
        packetMotionData.setAngularAccelerationZ(buffer.getNextFloat());
        packetMotionData.setFrontWheelsAngle(buffer.getNextFloat());

        return packetMotionData;
    }

    /**
     * CAR MOTION DATA
     * 
     * N.B. For the normalised vectors below, to convert to float values divide by
     * 32767.0f. 16-bit signed values are used to pack the data and on the
     * assumption that direction values are always between -1.0f and 1.0f.
     * 
     * <pre>
     * {@code 
    	struct CarMotionData
    	{
    	    float         m_worldPositionX;           // World space X position
    	    float         m_worldPositionY;           // World space Y position
    	    float         m_worldPositionZ;           // World space Z position
    	    float         m_worldVelocityX;           // Velocity in world space X
    	    float         m_worldVelocityY;           // Velocity in world space Y
    	    float         m_worldVelocityZ;           // Velocity in world space Z
    	    int16         m_worldForwardDirX;         // World space forward X direction (normalised)
    	    int16         m_worldForwardDirY;         // World space forward Y direction (normalised)
    	    int16         m_worldForwardDirZ;         // World space forward Z direction (normalised)
    	    int16         m_worldRightDirX;           // World space right X direction (normalised)
    	    int16         m_worldRightDirY;           // World space right Y direction (normalised)
    	    int16         m_worldRightDirZ;           // World space right Z direction (normalised)
    	    float         m_gForceLateral;            // Lateral G-Force component
    	    float         m_gForceLongitudinal;       // Longitudinal G-Force component
    	    float         m_gForceVertical;           // Vertical G-Force component
    	    float         m_yaw;                      // Yaw angle in radians
    	    float         m_pitch;                    // Pitch angle in radians
    	    float         m_roll;                     // Roll angle in radians
    	};
     * }
     * </pre>
     */
    private CarMotionData buildCarMotionData(int carIndex, boolean playersCar) {

        final float denormalizer = 32767.0f;

        CarMotionData carMotionData = new CarMotionData();

        carMotionData.setCarIndex(carIndex);
        carMotionData.setPlayersCar(playersCar);
        carMotionData.setWorldPositionX(buffer.getNextFloat());
        carMotionData.setWorldPositionY(buffer.getNextFloat());
        carMotionData.setWorldPositionZ(buffer.getNextFloat());
        carMotionData.setWorldVelocityX(buffer.getNextFloat());
        carMotionData.setWorldVelocityY(buffer.getNextFloat());
        carMotionData.setWorldVelocityZ(buffer.getNextFloat());
        carMotionData.setWorldForwardDirX(buffer.getNextUInt16AsInt() / denormalizer);
        carMotionData.setWorldForwardDirY(buffer.getNextUInt16AsInt() / denormalizer);
        carMotionData.setWorldForwardDirZ(buffer.getNextUInt16AsInt() / denormalizer);
        carMotionData.setWorldRightDirX(buffer.getNextUInt16AsInt() / denormalizer);
        carMotionData.setWorldRightDirY(buffer.getNextUInt16AsInt() / denormalizer);
        carMotionData.setWorldRightDirZ(buffer.getNextUInt16AsInt() / denormalizer);
        carMotionData.setgForceLateral(buffer.getNextFloat());
        carMotionData.setgForceLongitudinal(buffer.getNextFloat());
        carMotionData.setgForceVertical(buffer.getNextFloat());
        carMotionData.setYaw(buffer.getNextFloat());
        carMotionData.setPitch(buffer.getNextFloat());
        carMotionData.setRoll(buffer.getNextFloat());

        return carMotionData;

    }

    /**
     * SESSION PACKET
     * 
     * The session packet includes details about the current session in progress.
     * 
     * Frequency: 2 per second
     * 
     * Size: 147 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketSessionData
    	{
    	    PacketHeader    m_header;               // Header
    	
    	    uint8           m_weather;              // Weather - 0 = clear, 1 = light cloud, 2 = overcast
    	                                            // 3 = light rain, 4 = heavy rain, 5 = storm
    	    int8	    	m_trackTemperature;    	// Track temp. in degrees celsius
    	    int8	    	m_airTemperature;      	// Air temp. in degrees celsius
    	    uint8           m_totalLaps;           	// Total number of laps in this race
    	    uint16          m_trackLength;          // Track length in metres
    	    uint8           m_sessionType;         	// 0 = unknown, 1 = P1, 2 = P2, 3 = P3, 4 = Short P
    	                                            // 5 = Q1, 6 = Q2, 7 = Q3, 8 = Short Q, 9 = OSQ
    	                                            // 10 = R, 11 = R2, 12 = Time Trial
    	    int8            m_trackId;         		// -1 for unknown, 0-21 for tracks, see appendix
    	    uint8           m_era;                  // Era, 0 = modern, 1 = classic
    	    uint16          m_sessionTimeLeft;    	// Time left in session in seconds
    	    uint16          m_sessionDuration;     	// Session duration in seconds
    	    uint8           m_pitSpeedLimit;      	// Pit speed limit in kilometres per hour
    	    uint8           m_gamePaused;           // Whether the game is paused
    	    uint8           m_isSpectating;        	// Whether the player is spectating
    	    uint8           m_spectatorCarIndex;  	// Index of the car being spectated
    	    uint8           m_sliProNativeSupport;	// SLI Pro support, 0 = inactive, 1 = active
    	    uint8           m_numMarshalZones;      // Number of marshal zones to follow
    	    MarshalZone     m_marshalZones[21];     // List of marshal zones – max 21
    	    uint8           m_safetyCarStatus;      // 0 = no safety car, 1 = full safety car
    	                                            // 2 = virtual safety car
    	    uint8          m_networkGame;           // 0 = offline, 1 = online
    	    
    	    // >= F1 2020 ?
    	    uint8           m_numWeatherForecastSamples; // Number of weather samples to follow
            WeatherForecastSample m_weatherForecastSamples[56];   // Array of weather forecast samples
            uint8           m_forecastAccuracy;          // 0 = Perfect, 1 = Approximate
            uint8           m_aiDifficulty;              // AI Difficulty rating – 0-110
            uint32          m_seasonLinkIdentifier;      // Identifier for season - persists across saves
            uint32          m_weekendLinkIdentifier;     // Identifier for weekend - persists across saves
            uint32          m_sessionLinkIdentifier;     // Identifier for session - persists across saves
            uint8           m_pitStopWindowIdealLap;     // Ideal lap to pit on for current strategy (player)
            uint8           m_pitStopWindowLatestLap;    // Latest lap to pit on for current strategy (player)
            uint8           m_pitStopRejoinPosition;     // Predicted position to rejoin at (player)
            uint8           m_steeringAssist;            // 0 = off, 1 = on
            uint8           m_brakingAssist;             // 0 = off, 1 = low, 2 = medium, 3 = high
            uint8           m_gearboxAssist;             // 1 = manual, 2 = manual & suggested gear, 3 = auto
            uint8           m_pitAssist;                 // 0 = off, 1 = on
            uint8           m_pitReleaseAssist;          // 0 = off, 1 = on
            uint8           m_ERSAssist;                 // 0 = off, 1 = on
            uint8           m_DRSAssist;                 // 0 = off, 1 = on
            uint8           m_dynamicRacingLine;         // 0 = off, 1 = corners only, 2 = full
            uint8           m_dynamicRacingLineType;     // 0 = 2D, 1 = 3D
    	};
     * }
     * </pre>
     */
    private PacketSessionData buildPacketSessionData(Header header) {

        PacketSessionData sessionData = new PacketSessionData();

        sessionData.setHeader(header);
        sessionData.setWeather(Weather.fromInt(buffer.getNextUInt8AsInt()));
        sessionData.setTrackTemperature(buffer.getNextInt8AsInt());
        sessionData.setAirTemperature(buffer.getNextInt8AsInt());
        sessionData.setTotalLaps(buffer.getNextUInt8AsInt());
        sessionData.setTrackLength(buffer.getNextUInt16AsInt());
        sessionData.setSessionType(SessionType.fromInt(buffer.getNextUInt8AsInt()));
        sessionData.setTrackId(buffer.getNextInt8AsInt());
        sessionData.setEra(Era.fromInt(buffer.getNextInt8AsInt()));
        sessionData.setSessionTimeLeft(buffer.getNextUInt16AsInt());
        sessionData.setSessionDuration(buffer.getNextUInt16AsInt());
        sessionData.setPitSpeedLimit(buffer.getNextUInt8AsInt());
        sessionData.setGamePaused(buffer.getNextUInt8AsBoolean());
        sessionData.setSpectating(buffer.getNextUInt8AsBoolean());
        sessionData.setSliProNativeSupport(buffer.getNextUInt8AsBoolean());
        sessionData.setNumMarshalZones(buffer.getNextInt8AsInt());
        sessionData.setMarshalZones(buildMarshalZones(sessionData.getNumMarshalZones()));
        sessionData.setSafetyCarStatus(SafetyCarStatus.fromInt(buffer.getNextUInt8AsInt()));
        sessionData.setNetworkGame(buffer.getNextUInt8AsBoolean());

        if (version >= F1_2020) {
            sessionData.setNumWeatherForecastSamples(buffer.getNextInt8AsInt());
            sessionData.setWeatherForecast(buildWeatherForecasts(sessionData.getNumWeatherForecastSamples()));
            if (version >= F1_2021) {
                sessionData.setForecastAccuracy(buffer.getNextUInt8AsInt());
                sessionData.setAiDifficulty(buffer.getNextUInt8AsInt());
                sessionData.setSeasonLinkIdentifier(buffer.getNextUIntAsLong());
                sessionData.setWeekendLinkIdentifier(buffer.getNextUIntAsLong());
                sessionData.setSessionLinkIdentifier(buffer.getNextUIntAsLong());
                sessionData.setPitStopWindowIdealLap(buffer.getNextUInt8AsInt());
                sessionData.setPitStopWindowLatestLap(buffer.getNextUInt8AsInt());
                sessionData.setPitStopRejoinPosition(buffer.getNextUInt8AsInt());
                sessionData.setSteeringAssist(buffer.getNextUInt8AsBoolean());
                sessionData.setBrakingAssist(buffer.getNextUInt8AsBoolean());
                sessionData.setGearboxAssist(buffer.getNextUInt8AsBoolean());
                sessionData.setPitAssist(buffer.getNextUInt8AsBoolean());
                sessionData.setPitReleaseAssist(buffer.getNextUInt8AsBoolean());
                sessionData.setERSAssist(buffer.getNextUInt8AsBoolean());
                sessionData.setDRSAssist(buffer.getNextUInt8AsBoolean());
                sessionData.setDynamicRacingLine(buffer.getNextUInt8AsInt());
                sessionData.setDynamicRacingLineType(buffer.getNextUInt8AsInt());
            }
        }

        return sessionData;
    }

    /**
     * MARSHAL ZONE
     * 
     * <pre>
     * {@code 
    	struct MarshalZone
    	{
    	    float  m_zoneStart;   // Fraction (0..1) of way through the lap the marshal zone starts
    	    int8   m_zoneFlag;    // -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red
    	};
     * }
     * </pre>
     */
    private List<MarshalZone> buildMarshalZones(int numZones) {
        List<MarshalZone> marshalZones = new ArrayList<>();
        for (int k = 0; k < numZones; k++) {
            MarshalZone marshalZone = new MarshalZone();
            marshalZone.setZoneStart(buffer.getNextFloat());
            marshalZone.setZoneFlag(ZoneFlag.fromInt(buffer.getNextInt8AsInt()));
            marshalZones.add(marshalZone);
        }
        return marshalZones;
    }

    /**
     * <pre>
     * {@code
     *  struct WeatherForecastSample
        {
            uint8     m_sessionType;              // 0 = unknown, 1 = P1, 2 = P2, 3 = P3, 4 = Short P, 5 = Q1
                                                  // 6 = Q2, 7 = Q3, 8 = Short Q, 9 = OSQ, 10 = R, 11 = R2
                                                  // 12 = Time Trial
            uint8     m_timeOffset;               // Time in minutes the forecast is for
            uint8     m_weather;                  // Weather - 0 = clear, 1 = light cloud, 2 = overcast
                                                  // 3 = light rain, 4 = heavy rain, 5 = storm
            int8      m_trackTemperature;         // Track temp. in degrees Celsius
            int8      m_trackTemperatureChange;   // Track temp. change – 0 = up, 1 = down, 2 = no change
            int8      m_airTemperature;           // Air temp. in degrees celsius
            int8      m_airTemperatureChange;     // Air temp. change – 0 = up, 1 = down, 2 = no change
            uint8     m_rainPercentage;           // Rain percentage (0-100)
        };
     * }
     * </pre>
     */
    private List<WeatherForecast> buildWeatherForecasts(int num) {
        num = num == 255 ? 56 : Integer.max(num, 56);
        List<WeatherForecast> weatherForecasts = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            WeatherForecast weatherForecast = new WeatherForecast();
            weatherForecast.setSessionType(SessionType.fromInt(buffer.getNextUInt8AsInt()));
            weatherForecast.setTimeOffset(buffer.getNextUInt8AsInt());
            weatherForecast.setWeather(buffer.getNextUInt8AsInt());
            weatherForecast.setTrackTemperature(buffer.getNextInt8AsInt());
            if (version >= F1_2021) {
                weatherForecast.setTrackTemperatureChange(buffer.getNextInt8AsInt());
            }
            weatherForecast.setAirTemperature(buffer.getNextInt8AsInt());
            if (version >= F1_2021) {
                weatherForecast.setAirTemperatureChange(buffer.getNextInt8AsInt());
                weatherForecast.setRainPercentage(buffer.getNextUInt8AsInt());
            }
        }
        return weatherForecasts;
    }

    /**
     * EVENT PACKET
     * 
     * This packet gives details of events that happen during the course of the
     * race.
     * 
     * Frequency: When the event occurs
     * 
     * Size: 25 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketEventData
    	{
    	    PacketHeader    m_header;               // Header
    	    
    	    uint8           m_eventStringCode[4];   // Event string code, see above
    	    EventDataDetails m_eventDetails;        // Event details - should be interpreted differently
                                                    // for each type
    	};
     * }
     * </pre>
     * 
     * @param header
     * @return the EventData packet
     */
    private PacketEventData buildPacketEventData(Header header) {
        PacketEventData eventData = new PacketEventData();
        eventData.setHeader(header);
        eventData.setEventCode(buffer.getNextCharArrayAsString(4));

        return eventData;
    }

    /**
     * PARTICIPANTS PACKET
     * 
     * This is a list of participants in the race. If the vehicle is controlled by
     * AI, then the name will be the driver name. If this is a multiplayer game, the
     * names will be the Steam Id on PC, or the LAN name if appropriate. On Xbox
     * One, the names will always be the driver name, on PS4 the name will be the
     * LAN name if playing a LAN game, otherwise it will be the driver name.
     * 
     * Frequency: Every 5 seconds
     * 
     * Size: 1082 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketParticipantsData
    	{
    	    PacketHeader    m_header;            // Header
    	
    	    uint8           m_numCars;           // Number of cars in the data
    	    ParticipantData m_participants[20];
    	};	 
     * }
     * </pre>
     * 
     * @param header
     * @return a PacketParticipantsData pojo
     * 
     */
    private PacketParticipantsData buildPacketParticipantsData(Header header) {
        PacketParticipantsData participantsData = new PacketParticipantsData();

        participantsData.setHeader(header);
        participantsData.setNumCars(buffer.getNextUInt8AsInt());

        int numCars = participantsData.getNumCars() == 255 ? numberOfCars() : participantsData.getNumCars();
        List<ParticipantData> participants = new ArrayList<>();
        for (int k = 0; k < numCars; k++) {
            participants.add(buildParticipantData());
        }
        participantsData.setParticipants(participants);

        // Ignore the rest of the data in the buffer
        return participantsData;
    }

    /**
     * PARTICIPANT DATA
     * 
     * <pre>
     * {@code
    	struct ParticipantData
    	{
    	    uint8      m_aiControlled;           // Whether the vehicle is AI (1) or Human (0) controlled
    	    uint8      m_driverId;               // Driver id - see appendix
    	    uint8      m_networkId;              // Network id – unique identifier for network players, >= 2020
    	    uint8      m_teamId;                 // Team id - see appendix
    	    uint8      m_myTeam;                 // My team flag – 1 = My Team, 0 = otherwise, >= 2020
    	    uint8      m_raceNumber;             // Race number of the car
    	    uint8      m_nationality;            // Nationality of the driver
    	    char       m_name[48];               // Name of participant in UTF-8 format – null terminated
    	                                         // Will be truncated with … (U+2026) if too long
    	    uint8_t    m_yourTelemetry;          // The player's UDP setting, 0 = restricted, 1 = public
     * }; }
     * 
     * @return a ParticipantData pojo
     */
    private ParticipantData buildParticipantData() {
        ParticipantData participant = new ParticipantData();

        participant.setAiControlled(buffer.getNextUInt8AsBoolean());
        participant.setDriverId(buffer.getNextUInt8AsInt());
        if (version >= F1_2021) {
            participant.setNetworkId(buffer.getNextUInt8AsInt());
        }
        participant.setTeamId(buffer.getNextUInt8AsInt());
        if (version >= F1_2021) {
            participant.setMyTeam(buffer.getNextUInt8AsInt());
        }
        participant.setRaceNumber(buffer.getNextUInt8AsInt());
        participant.setNationality(buffer.getNextUInt8AsInt());
        participant.setName(buffer.getNextCharArrayAsString(48));
        if (version >= F1_2019) {
            participant.setYourTelemetry(buffer.getNextUInt8AsBoolean());
        }

        return participant;
    }

    /**
     * CAR SETUPS PACKET
     * 
     * This packet details the car setups for each vehicle in the session. Note that
     * in multiplayer games, other player cars will appear as blank, you will only
     * be able to see your car setup and AI cars.
     * 
     * Frequency: Every 5 seconds
     * 
     * Size: 841 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketCarSetupData
    	{
    	    PacketHeader    m_header;            // Header
    	
    	    CarSetupData    m_carSetups[20];
    	};
     * }
     * </pre>
     * 
     * @param header
     * @return
     */
    private PacketCarSetupData buildPacketCarSetupData(Header header) {
        PacketCarSetupData carSetupData = new PacketCarSetupData();
        carSetupData.setHeader(header);
        List<CarSetupData> carSetups = new ArrayList<>();
        for (int k = 0; k < numberOfCars(); k++) {
            carSetups.add(buildCarSetupData());
        }
        carSetupData.setCarSetups(carSetups);
        return carSetupData;
    }

    /**
     * CAR SETUP DATA
     * 
     * <pre>
     * {@code 
    	struct CarSetupData
    	{
    		uint8     m_frontWing;                // Front wing aero
    		uint8     m_rearWing;                 // Rear wing aero
    		uint8     m_onThrottle;               // Differential adjustment on throttle (percentage)
    		uint8     m_offThrottle;              // Differential adjustment off throttle (percentage)
    		float     m_frontCamber;              // Front camber angle (suspension geometry)
    		float     m_rearCamber;               // Rear camber angle (suspension geometry)
    		float     m_frontToe;                 // Front toe angle (suspension geometry)
    		float     m_rearToe;                  // Rear toe angle (suspension geometry)
    		uint8     m_frontSuspension;          // Front suspension
    		uint8     m_rearSuspension;           // Rear suspension
    		uint8     m_frontAntiRollBar;         // Front anti-roll bar
    		uint8     m_rearAntiRollBar;          // Front anti-roll bar
    		uint8     m_frontSuspensionHeight;    // Front ride height
    		uint8     m_rearSuspensionHeight;     // Rear ride height
    		uint8     m_brakePressure;            // Brake pressure (percentage)
    		uint8     m_brakeBias;                // Brake bias (percentage)
    		
    		// >= F1 2019
    		float     m_frontTyrePressure;        // Front tyre pressure (PSI)
    		float     m_rearTyrePressure;         // Rear tyre pressure (PSI)
    		
    		// >= F1 2020 ?
    		float     m_rearLeftTyrePressure;     // Rear left tyre pressure (PSI)
            float     m_rearRightTyrePressure;    // Rear right tyre pressure (PSI)
            float     m_frontLeftTyrePressure;    // Front left tyre pressure (PSI)
            float     m_frontRightTyrePressure;   // Front right tyre pressure (PSI)
    		
    		uint8     m_ballast;                  // Ballast
    		float     m_fuelLoad;                 // Fuel load
    	};
     * }
     * </pre>
     * 
     * @return a CarSetupData pojo
     */
    private CarSetupData buildCarSetupData() {
        CarSetupData setupData = new CarSetupData();
        setupData.setFrontWing(buffer.getNextUInt8AsInt()); // 1*
        setupData.setRearWing(buffer.getNextUInt8AsInt()); // 2*
        setupData.setOnThrottle(buffer.getNextUInt8AsInt()); // 3*
        setupData.setOffThrottle(buffer.getNextUInt8AsInt()); // 4*
        setupData.setFrontCamber(buffer.getNextFloat()); // 8 *
        setupData.setRearCamber(buffer.getNextFloat()); // 16*
        setupData.setFrontToe(buffer.getNextFloat()); // 24*
        setupData.setRearToe(buffer.getNextFloat()); // 32*
        setupData.setFrontSuspension(buffer.getNextUInt8AsInt()); // 33*
        setupData.setRearSuspension(buffer.getNextUInt8AsInt()); // 34*
        setupData.setFrontAntiRollBar(buffer.getNextUInt8AsInt()); // 35*
        setupData.setRearAntiRollBar(buffer.getNextUInt8AsInt()); // 36*
        setupData.setFrontSuspensionHeight(buffer.getNextUInt8AsInt()); // 37*
        setupData.setRearSuspensionHeight(buffer.getNextUInt8AsInt()); // 38*
        setupData.setBrakePressure(buffer.getNextUInt8AsInt());
        setupData.setBrakeBias(buffer.getNextUInt8AsInt()); // 39
        if (version <= F1_2019) {
            setupData.setFrontTyrePressure(buffer.getNextFloat()); // 47
            setupData.setRearTyrePressure(buffer.getNextFloat()); // 55
        } else if (version >= F1_2020) {
            setupData.setRearLeftTyrePressure(buffer.getNextFloat());
            setupData.setRearRightTyrePressure(buffer.getNextFloat());
            setupData.setFrontLeftTyrePressure(buffer.getNextFloat());
            setupData.setFrontRightTyrePressure(buffer.getNextFloat());
        }
        setupData.setBallast(buffer.getNextUInt8AsInt()); // 56
        setupData.setFuelLoad(buffer.getNextFloat()); // 40
        return setupData;
    }

    /**
     * CAR TELEMETRY PACKET
     * 
     * This packet details telemetry for all the cars in the race. It details
     * various values that would be recorded on the car such as speed, throttle
     * application, DRS etc.
     * 
     * Frequency: Rate as specified in menus
     * 
     * Size: 1085 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketCarTelemetryData
    	{
    	    PacketHeader        m_header;                // Header
    	
    	    CarTelemetryData    m_carTelemetryData[20];
    	
    	    // F1 2019
    	    uint32              m_buttonStatus;         // Bit flags specifying which buttons are being
    	                                                // pressed currently - see appendices
    
            // F1 2021
            uint8               m_mfdPanelIndex;       // Index of MFD panel open - 255 = MFD closed
                                                       // Single player, race – 0 = Car setup, 1 = Pits
                                                       // 2 = Damage, 3 =  Engine, 4 = Temperatures
                                                       // May vary depending on game mode
            uint8               m_mfdPanelIndexSecondaryPlayer;   // See above
            int8                m_suggestedGear;       // Suggested gear for the player (1-8)
                                                       // 0 if no gear suggested
    	};
     * }
     * </pre>
     * 
     * @param header
     * @return a PacketCarTelemetryData pojo
     */
    private PacketCarTelemetryData buildPacketCarTelemetryData(Header header) {
        PacketCarTelemetryData packetCarTelemetry = new PacketCarTelemetryData();

        packetCarTelemetry.setHeader(header);
        List<CarTelemetryData> carsTelemetry = new ArrayList<>();
        for (int k = 0; k < numberOfCars(); k++) {
            carsTelemetry.add(buildCarTelemetryData());
        }
        if (version <= F1_2020) {
            packetCarTelemetry.setButtonStatus(buildButtonStatus());
        }
        if (version >= F1_2020) {
            packetCarTelemetry.setMfdPanelIndex(buffer.getNextUInt8AsInt());
            packetCarTelemetry.setMfdPanelIndexSecondaryPlayer(buffer.getNextUInt8AsInt());
            packetCarTelemetry.setSuggestedGear(buffer.getNextInt8AsInt());
        }
        packetCarTelemetry.setCarTelemetryData(carsTelemetry);
        return packetCarTelemetry;
    }

    /**
     * CAR TELEMETRY DATA
     * 
     * <pre>
     * {@code 
    	struct CarTelemetryData
    	{
    	    uint16    m_speed;                      // Speed of car in kilometres per hour
    	    uint8     m_throttle;                   // Amount of throttle applied (0 to 100)
    	    int8      m_steer;                      // Steering (-100 (full lock left) to 100 (full lock right))
    	    uint8     m_brake;                      // Amount of brake applied (0 to 100)
    	    uint8     m_clutch;                     // Amount of clutch applied (0 to 100)
    	    int8      m_gear;                       // Gear selected (1-8, N=0, R=-1)
    	    uint16    m_engineRPM;                  // Engine RPM
    	    uint8     m_drs;                        // 0 = off, 1 = on
    	    uint8     m_revLightsPercent;           // Rev lights indicator (percentage)
    	    uint16    m_revLightsBitValue;          // Rev lights (bit 0 = leftmost LED, bit 14 = rightmost LED) > F1 2019
    	    uint16    m_brakesTemperature[4];       // Brakes temperature (celsius)
    	    uint16    m_tyresSurfaceTemperature[4]; // Tyres surface temperature (celsius)
    	    uint16    m_tyresInnerTemperature[4];   // Tyres inner temperature (celsius)
    	    uint16    m_engineTemperature;          // Engine temperature (celsius)
    	    float     m_tyresPressure[4];           // Tyres pressure (PSI)
    	    uint8_t   m_surfaceType[4];             // Driving surface, see appendices
    	};
     * }
     * </pre>
     * 
     * @return a CarTelemetryData pojo
     */
    private CarTelemetryData buildCarTelemetryData() {

        CarTelemetryData carTelemetry = new CarTelemetryData();

        carTelemetry.setSpeed(buffer.getNextUInt16AsInt());
        if (version == F1_2018) {
            carTelemetry.setThrottle(buffer.getNextUInt8AsInt());
            carTelemetry.setSteer(buffer.getNextInt8AsInt());
            carTelemetry.setBrake(buffer.getNextUInt8AsInt());
        } else if (version >= F1_2019) {
            carTelemetry.setThrottle(Math.round(buffer.getNextFloat() * 100));
            carTelemetry.setSteer(Math.round(buffer.getNextFloat() * 100));
            carTelemetry.setBrake(Math.round(buffer.getNextFloat() * 100));
        }
        carTelemetry.setClutch(buffer.getNextUInt8AsInt());
        carTelemetry.setGear(buffer.getNextInt8AsInt());
        carTelemetry.setEngineRpm(buffer.getNextUInt16AsInt());
        carTelemetry.setDrs(buffer.getNextUInt8AsBoolean());
        carTelemetry.setRevLightsPercent(buffer.getNextUInt8AsInt());
        if (version >= F1_2021) {
            carTelemetry.setRevLightsBitValue(buffer.getNextUInt16AsInt());
        }
        carTelemetry.setBrakeTemperature(new WheelData<Integer>(buffer.getNextUInt16ArrayAsIntegerArray(4)));
        if (version <= F1_2019) {
            carTelemetry.setTyreSurfaceTemperature(new WheelData<Integer>(buffer.getNextUInt16ArrayAsIntegerArray(4)));
            carTelemetry.setTyreInnerTemperature(new WheelData<Integer>(buffer.getNextUInt16ArrayAsIntegerArray(4)));
        } else if (version >= F1_2020) {
            carTelemetry.setTyreSurfaceTemperature(new WheelData<Integer>(buffer.getNextUInt8ArrayAsIntegerArray(4)));
            carTelemetry.setTyreInnerTemperature(new WheelData<Integer>(buffer.getNextUInt8ArrayAsIntegerArray(4)));
        }
        carTelemetry.setEngineTemperature(buffer.getNextUInt16AsInt());
        carTelemetry.setTyrePressure(new WheelData<Float>(buffer.getNextFloatArray(4)));
        if (version >= F1_2019) {
            carTelemetry.setSurfaceType(new WheelData<Integer>(buffer.getNextUInt8ArrayAsIntegerArray(4)));
        }

        return carTelemetry;
    }

    /**
     * BUTTON FLAGS
     * 
     * These flags are used in the telemetry packet to determine if any buttons are
     * being held on the controlling device. If the value below logical ANDed with
     * the button status is set then the corresponding button is being held
     * 
     * @return the ButtonStatus pojo
     */
    private ButtonStatus buildButtonStatus() {

        long flags = buffer.getNextUIntAsLong();

        ButtonStatus controller = new ButtonStatus();
        controller.setCrossAPressed((flags & 0x0001) == 1);
        controller.setTriangleYPressed((flags & 0x0002) == 1);
        controller.setCircleBPressed((flags & 0x0004) == 1);
        controller.setSquareXPressed((flags & 0x0008) == 1);
        controller.setDpadLeftPressed((flags & 0x0010) == 1);
        controller.setDpadRightPressed((flags & 0x0020) == 1);
        controller.setDpadUpPressed((flags & 0x0040) == 1);
        controller.setDpadDownPressed((flags & 0x0080) == 1);
        controller.setOptionsMenuPressed((flags & 0x0100) == 1);
        controller.setL1LBPressed((flags & 0x0200) == 1);
        controller.setR1RBPressed((flags & 0x0400) == 1);
        controller.setL2LTPressed((flags & 0x0800) == 1);
        controller.setR2RTPressed((flags & 0x1000) == 1);
        controller.setLeftStickPressed((flags & 0x2000) == 1);
        controller.setRightStickPressed((flags & 0x4000) == 1);

        return controller;
    }

    /**
     * CAR STATUS PACKET
     * 
     * This packet details car statuses for all the cars in the race. It includes
     * values such as the damage readings on the car.
     * 
     * Frequency: 2 per second
     * 
     * Size: 1061 bytes
     * 
     * <pre>
     * {@code 
    	struct PacketCarStatusData
    	{
    	    PacketHeader        m_header;            // Header
    	
    	    CarStatusData       m_carStatusData[20];
    	};
     * }
     * </pre>
     * 
     * @param header
     * @return a PacketCarStatusData packet
     */
    private PacketCarStatusData buildPacketCarStatusData(Header header) {

        PacketCarStatusData packetCarStatus = new PacketCarStatusData();

        packetCarStatus.setHeader(header);
        List<CarStatusData> carStatuses = new ArrayList<>();
        for (int k = 0; k < numberOfCars(); k++) {
            carStatuses.add(buildCarStatusData());
        }
        packetCarStatus.setCarStatuses(carStatuses);

        return packetCarStatus;
    }

    /**
     * CAR STATUS DATA
     * 
     * <pre>
     * {@code 
    	struct CarStatusData
    	{
    	    uint8       m_tractionControl;          // 0 (off) - 2 (high)
    	    uint8       m_antiLockBrakes;           // 0 (off) - 1 (on)
    	    uint8       m_fuelMix;                  // Fuel mix - 0 = lean, 1 = standard, 2 = rich, 3 = max
    	    uint8       m_frontBrakeBias;           // Front brake bias (percentage)
    	    uint8       m_pitLimiterStatus;         // Pit limiter status - 0 = off, 1 = on
    	    float       m_fuelInTank;               // Current fuel mass
    	    float       m_fuelCapacity;             // Fuel capacity
    	    float       m_fuelRemainingLaps;        // Fuel remaining in terms of laps (value on MFD)
    	    uint16      m_maxRPM;                   // Cars max RPM, point of rev limiter
    	    uint16      m_idleRPM;                  // Cars idle RPM
    	    uint8       m_maxGears;                 // Maximum number of gears
    	    uint8       m_drsAllowed;               // 0 = not allowed, 1 = allowed, -1 = unknown
    	    
    	    // >= F1 2020
            uint16      m_drsActivationDistance;    // 0 = DRS not available, non-zero - DRS will be available
                                                    // in [X] metres
    
            // <= F1 2019
    	    uint8       m_tyresWear[4];             // Tyre wear percentage
    	    
    	    uint8       m_tyreCompound;             // Modern - 0 = hyper soft, 1 = ultra soft
    	                                            // 2 = super soft, 3 = soft, 4 = medium, 5 = hard
    	                                            // 6 = super hard, 7 = inter, 8 = wet
    	                                            // Classic - 0-6 = dry, 7-8 = wet
            uint8_t     m_tyreVisualCompound;       // F1 visual (can be different from actual compound)
                                                    // 16 = soft, 17 = medium, 18 = hard, 7 = inter, 8 = wet
                                                    // F1 Classic – same as above
                                                    // F2 – same as above
                                                     
                                                    
            // <= F1 2019
    	    uint8       m_tyresDamage[4];           // Tyre damage (percentage)
    	    uint8       m_frontLeftWingDamage;      // Front left wing damage (percentage)
    	    uint8       m_frontRightWingDamage;     // Front right wing damage (percentage)
    	    uint8       m_rearWingDamage;           // Rear wing damage (percentage)
    	    uint8       m_engineDamage;             // Engine damage (percentage)
    	    uint8       m_gearBoxDamage;            // Gear box damage (percentage)
    	    uint8       m_exhaustDamage;            // Exhaust damage (percentage)
    	    
    	    // >= F1 2020
    	    uint8       m_tyresAgeLaps;             // Age in laps of the current set of tyres
    	    
    	    int8        m_vehicleFiaFlags;          // -1 = invalid/unknown, 0 = none, 1 = green
    	                                            // 2 = blue, 3 = yellow, 4 = red
    	    float       m_ersStoreEnergy;           // ERS energy store in Joules
    	    uint8       m_ersDeployMode;            // ERS deployment mode, 0 = none, 1 = low, 2 = medium
    	                                            // 3 = high, 4 = overtake, 5 = hotlap
    	    float       m_ersHarvestedThisLapMGUK;  // ERS energy harvested this lap by MGU-K
    	    float       m_ersHarvestedThisLapMGUH;  // ERS energy harvested this lap by MGU-H
    	    float       m_ersDeployedThisLap;       // ERS energy deployed this lap
    	    
    	    // >= F1 2020
    	    uint8       m_networkPaused;            // Whether the car is paused in a network game
    	};
     * }
     * </pre>
     * 
     * @return a CarStatusData pojo
     */
    private CarStatusData buildCarStatusData() {

        CarStatusData carStatus = new CarStatusData();

        carStatus.setTractionControl(buffer.getNextUInt8AsInt());
        carStatus.setAntiLockBrakes(buffer.getNextUInt8AsBoolean());
        carStatus.setFuelMix(buffer.getNextUInt8AsInt());
        carStatus.setFrontBrakeBias(buffer.getNextUInt8AsInt());
        carStatus.setPitLimiterOn(buffer.getNextUInt8AsBoolean());
        carStatus.setFuelInTank(buffer.getNextFloat());
        carStatus.setFuelCapacity(buffer.getNextFloat());
        if (version >= F1_2019) {
            carStatus.setFuelRemainingLaps(buffer.getNextFloat());
        }
        carStatus.setMaxRpm(buffer.getNextUInt16AsInt());
        carStatus.setIdleRpm(buffer.getNextUInt16AsInt());
        carStatus.setMaxGears(buffer.getNextUInt8AsInt());
        carStatus.setDrsAllowed(buffer.getNextUInt8AsInt());
        if (version >= F1_2020) {
            carStatus.setDrsActivationDistance(buffer.getNextUInt16AsInt());
        }
        if (version <= F1_2020) {
            carStatus.setTyresWear(new WheelData<Integer>(buffer.getNextUInt8ArrayAsIntegerArray(4)));
        }
        carStatus.setTyreCompound(buffer.getNextUInt8AsInt());
        if (version >= F1_2019) {
            carStatus.setTyreVisualCompound(buffer.getNextUInt8AsInt());
        }
        if (version <= F1_2020) {
            if (version == F1_2020) {
                carStatus.setTyresAgeLaps(buffer.getNextUInt8AsInt());
            }
            carStatus.setTyresDamage(new WheelData<Integer>(buffer.getNextUInt8ArrayAsIntegerArray(4)));
            carStatus.setFrontLeftWingDamage(buffer.getNextUInt8AsInt());
            carStatus.setFrontRightWingDamage(buffer.getNextUInt8AsInt());
            carStatus.setRearWingDamage(buffer.getNextUInt8AsInt());
            if (version == F1_2020) {
                carStatus.setDrsFault(buffer.getNextUInt8AsInt());
            }
            carStatus.setEngineDamage(buffer.getNextUInt8AsInt());
            carStatus.setGearBoxDamage(buffer.getNextUInt8AsInt());
            if (version == F1_2018) {
                carStatus.setExhaustDamage(buffer.getNextUInt8AsInt());
            }
        }
        if (version >= F1_2021) {
            carStatus.setTyresAgeLaps(buffer.getNextUInt8AsInt());
        }
        carStatus.setVehicleFiaFlags(buffer.getNextInt8AsInt());
        carStatus.setErsStoreEngery(buffer.getNextFloat());
        carStatus.setErsDeployMode(buffer.getNextUInt8AsInt());
        carStatus.setErsHarvestedThisLapMGUK(buffer.getNextFloat());
        carStatus.setErsHarvestedThisLapMGUH(buffer.getNextFloat());
        carStatus.setErsDeployedThisLap(buffer.getNextFloat());

        if (version >= F1_2021) {
            carStatus.setNetworkPaused(buffer.getNextUInt8AsInt());
        }

        return carStatus;
    }

    /**
     * 
     * <pre>
     * {@code
     *  struct PacketCarDamageData
        {
            PacketHeader    m_header;               // Header
    
            CarDamageData   m_carDamageData[22];
        };
     * }
     * </pre>
     * @param header
     * @return
     */
    private PacketCarDamageData buildPacketCarDamageData(Header header) {
        PacketCarDamageData packetCarStatus = new PacketCarDamageData();

        packetCarStatus.setHeader(header);
        List<CarDamageData> carDamages = new ArrayList<>();
        for (int k = 0; k < numberOfCars(); k++) {
            carDamages.add(buildCarDamageData());
        }
        packetCarStatus.setCarDamages(carDamages);

        return packetCarStatus;
    }

    /**
     * 
     * <pre>
     * {@code
     *   struct CarDamageData
         {
            float     m_tyresWear[4];                     // Tyre wear (percentage)
            uint8     m_tyresDamage[4];                   // Tyre damage (percentage)
            uint8     m_brakesDamage[4];                  // Brakes damage (percentage)
            uint8     m_frontLeftWingDamage;              // Front left wing damage (percentage)
            uint8     m_frontRightWingDamage;             // Front right wing damage (percentage)
            uint8     m_rearWingDamage;                   // Rear wing damage (percentage)
            uint8     m_floorDamage;                      // Floor damage (percentage)
            uint8     m_diffuserDamage;                   // Diffuser damage (percentage)
            uint8     m_sidepodDamage;                    // Sidepod damage (percentage)
            uint8     m_drsFault;                         // Indicator for DRS fault, 0 = OK, 1 = fault
            uint8     m_gearBoxDamage;                    // Gear box damage (percentage)
            uint8     m_engineDamage;                     // Engine damage (percentage)
            uint8     m_engineMGUHWear;                   // Engine wear MGU-H (percentage)
            uint8     m_engineESWear;                     // Engine wear ES (percentage)
            uint8     m_engineCEWear;                     // Engine wear CE (percentage)
            uint8     m_engineICEWear;                    // Engine wear ICE (percentage)
            uint8     m_engineMGUKWear;                   // Engine wear MGU-K (percentage)
            uint8     m_engineTCWear;                     // Engine wear TC (percentage)
        }
     * }
     */
    private CarDamageData buildCarDamageData() {
        CarDamageData carDamage = new CarDamageData();

        carDamage.setTyresWear(new WheelData<Float>(buffer.getNextFloatArray(4)));
        carDamage.setTyresDamage(new WheelData<Integer>(buffer.getNextUInt8ArrayAsIntegerArray(4)));
        carDamage.setBrakesDamage(new WheelData<Integer>(buffer.getNextUInt8ArrayAsIntegerArray(4)));
        carDamage.setFrontLeftWingDamage(buffer.getNextInt8AsInt());
        carDamage.setFrontRightWingDamage(buffer.getNextInt8AsInt());
        carDamage.setRearWingDamage(buffer.getNextInt8AsInt());
        carDamage.setFloorDamage(buffer.getNextInt8AsInt());
        carDamage.setDiffuserDamage(buffer.getNextInt8AsInt());
        carDamage.setSidepodDamage(buffer.getNextInt8AsInt());
        carDamage.setDrsFault(buffer.getNextInt8AsInt());
        carDamage.setGearBoxDamage(buffer.getNextInt8AsInt());
        carDamage.setEngineDamage(buffer.getNextInt8AsInt());
        carDamage.setEngineMGUHWear(buffer.getNextInt8AsInt());
        carDamage.setEngineESWear(buffer.getNextInt8AsInt());
        carDamage.setEngineCEWear(buffer.getNextInt8AsInt());
        carDamage.setEngineICEWear(buffer.getNextInt8AsInt());
        carDamage.setEngineMGUKWear(buffer.getNextInt8AsInt());
        carDamage.setEngineTCWear(buffer.getNextInt8AsInt());

        return carDamage;
    }

    /**
     * 
     * <pre>
     * {@code
     *  struct PacketSessionHistoryData
        {
            PacketHeader  m_header;                   // Header
    
            uint8         m_carIdx;                   // Index of the car this lap data relates to
            uint8         m_numLaps;                  // Num laps in the data (including current partial lap)
            uint8         m_numTyreStints;            // Number of tyre stints in the data
    
            uint8         m_bestLapTimeLapNum;        // Lap the best lap time was achieved on
            uint8         m_bestSector1LapNum;        // Lap the best Sector 1 time was achieved on
            uint8         m_bestSector2LapNum;        // Lap the best Sector 2 time was achieved on
            uint8         m_bestSector3LapNum;        // Lap the best Sector 3 time was achieved on
    
            LapHistoryData          m_lapHistoryData[100];  // 100 laps of data max
            TyreStintHistoryData    m_tyreStintsHistoryData[8];
        };
     * }
     * </pre>
     * 
     * @param header
     * @return
     */
    private PacketSessionHistoryData buildPacketSessionHistoryData(Header header) {
        PacketSessionHistoryData packetSessionHistory = new PacketSessionHistoryData();

        packetSessionHistory.setHeader(header);
        packetSessionHistory.setCarIdx(buffer.getNextUInt8AsInt());
        packetSessionHistory.setNumLaps(buffer.getNextUInt8AsInt());
        packetSessionHistory.setNumTyreStints(buffer.getNextUInt8AsInt());
        packetSessionHistory.setBestLapTimeLapNum(buffer.getNextUInt8AsInt());
        packetSessionHistory.setBestSector1LapNum(buffer.getNextUInt8AsInt());
        packetSessionHistory.setBestSector2LapNum(buffer.getNextUInt8AsInt());
        packetSessionHistory.setBestSector3LapNum(buffer.getNextUInt8AsInt());

        int numLaps = packetSessionHistory.getNumLaps() == 0 ? 100 : packetSessionHistory.getNumLaps();
        List<LapHistoryData> lapHistoryData = new ArrayList<>();
        for (int i = 0; i < numLaps; i++) {
            lapHistoryData.add(buildLapHistoryData());
        }
        packetSessionHistory.setLapHistoryData(lapHistoryData);

        int numStints = packetSessionHistory.getNumTyreStints() == 0 ? 8 : packetSessionHistory.getNumTyreStints();
        List<TyreStintHistoryData> tyreStintHistoryData = new ArrayList<>();
        for (int i = 0; i < numStints; i++) {
            tyreStintHistoryData.add(buildTyreStintsHistoryData());
        }
        packetSessionHistory.setTyreStintsHistoryData(tyreStintHistoryData);

        return packetSessionHistory;
    }

    /**
     * 
     * <pre>
     * {@code
     *  struct LapHistoryData
        {
            uint32    m_lapTimeInMS;           // Lap time in milliseconds
            uint16    m_sector1TimeInMS;       // Sector 1 time in milliseconds
            uint16    m_sector2TimeInMS;       // Sector 2 time in milliseconds
            uint16    m_sector3TimeInMS;       // Sector 3 time in milliseconds
            uint8     m_lapValidBitFlags;      // 0x01 bit set-lap valid,      0x02 bit set-sector 1 valid
                                               // 0x04 bit set-sector 2 valid, 0x08 bit set-sector 3 valid
        };
     * }
     * </pre>
     * @return
     */
    private LapHistoryData buildLapHistoryData() {
        LapHistoryData lapHistory = new LapHistoryData();

        lapHistory.setLapTime(msToSeconds(buffer.getNextUIntAsLong()));
        lapHistory.setSector1Time(msToSeconds(buffer.getNextUInt16AsInt()));
        lapHistory.setSector2Time(msToSeconds(buffer.getNextUInt16AsInt()));
        lapHistory.setSector3Time(msToSeconds(buffer.getNextUInt16AsInt()));
        lapHistory.setLapValidBitFlags(buffer.getNextUInt8AsInt());

        return lapHistory;
    }

    /**
     * 
     * <pre>
     * {@code
     *  struct TyreStintHistoryData
        {
            uint8     m_endLap;                // Lap the tyre usage ends on (255 of current tyre)
            uint8     m_tyreActualCompound;    // Actual tyres used by this driver
            uint8     m_tyreVisualCompound;    // Visual tyres used by this driver
        };
     * }
     * </pre>
     * @return
     */
    private TyreStintHistoryData buildTyreStintsHistoryData() {
        TyreStintHistoryData tyreStintHistory = new TyreStintHistoryData();

        tyreStintHistory.setEndLap(buffer.getNextUInt8AsInt());
        tyreStintHistory.setTyreActualCompound(buffer.getNextUInt8AsInt());
        tyreStintHistory.setTyreVisualCompound(buffer.getNextUInt8AsInt());

        return tyreStintHistory;
    }

}
