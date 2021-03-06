export interface Header {
    packetFormat?: number;
    packetVersion?: number;
    packetId?: number;
    sessionUID?: number;
    sessionTime?: number;
    frameIdentifier?: number;
    playerCarIndex?: number;
}

export interface Packet {
    header: Header;
    carTelemetryData?: Array<CarTelemetryData>;
    carStatuses?: Array<CarStatusData>;
    lapDataList?: Array<LapData>;
    carDamages?: Array<CarDamageData>;
    carSetups?: Array<CarSetupData>;
}

export interface WheelData<T> {
    frontLeft?: T;
    frontRight?: T;
    rearLeft?: T;
    rearRight?: T;
}

export interface CarTelemetryData {
    speed?: number;
    throttle?: number;
    steer?: number;
    brake?: number;
    clutch?: number;
    gear?: number;
    engineRpm?: number;
    drs?: boolean;
    revLightsPercent?: number;
    brakeTemperature?: WheelData<number>;
    tyreSurfaceTemperature?: WheelData<number>;
    tyreInnerTemperature?: WheelData<number>;
    engineTemperature?: number;
    tyrePressure?: WheelData<number>;
}

export interface CarStatusData {
    tractionControl?: number;
    antiLockBrakes?: boolean;
    fuelMix?: number;
    frontBrakeBias?: number;
    pitLimiterOn?: boolean;
    fuelInTank?: number;
    fuelCapacity?: number;
    fuelRemainingLaps?: number;
    maxRpm?: number;
    idleRpm?: number;
    maxGears?: number;
    drsAllowed?: number;
    drsActivationDistance?: number;
    tyresWear?: WheelData<number>;
    tyreCompound?: number;
    tyreVisualCompound?: number;
    tyresDamage?: WheelData<number>;
    frontLeftWingDamage?: number;
    frontRightWingDamage?: number;
    rearWingDamage?: number;
    engineDamage?: number;
    gearBoxDamage?: number;
    exhaustDamage?: number;
    vehicleFiaFlags?: number;
    ersStoreEngery?: number;
    ersDeployMode?: number;
    ersHarvestedThisLapMGUK?: number;
    ersHarvestedThisLapMGUH?: number;
    ersDeployedThisLap?: number;
}

export interface CarDamageData {
    tyresWear?: WheelData<number>;
    tyresDamage?: WheelData<number>;
    brakesDamage?: WheelData<number>;
    frontLeftWingDamage?: number;
    frontRightWingDamage?: number;
    rearWingDamage?: number;
    floorDamage?: number;
    diffuserDamage?: number;
    sidepodDamage?: number;
    drsFault?: number;
    gearBoxDamage?: number;
    engineDamage?: number;
    engineMGUHWear?: number;
    engineESWear?: number;
    engineCEWear?: number;
    engineICEWear?: number;
    engineMGUKWear?: number;
    engineTCWear?: number;
}

export interface CarSetupData {
    frontWing?: number;
    rearWing?: number;
    onThrottle?: number;
    offThrottle?: number;
    frontCamber?: number;
    rearCamber?: number;
    frontToe?: number;
    rearToe?: number;
    frontSuspension?: number;
    rearSuspension?: number;
    frontAntiRollBar?: number;
    rearAntiRollBar?: number;
    frontSuspensionHeight?: number;
    rearSuspensionHeight?: number;
    brakePressure?: number;
    brakeBias?: number;
    frontTyrePressure?: number;
    rearTyrePressure?: number;
    rearLeftTyrePressure?: number;
    rearRightTyrePressure?: number;
    frontLeftTyrePressure?: number;
    frontRightTyrePressure?: number;
    ballast?: number;
    fuelLoad?: number;
}

export enum PitStatus {
    NONE,
    PITTING,
    IN_PIT
}

export enum DriverStatus {
    IN_GARAGE,
    FLYING_LAP,
    IN_LAP,
    OUT_LAP,
    ON_TRACK
}

export enum ResultStatus {
    INVALID,
    INACTIVE,
    ACTIVE,
    FINISHED,
    DISQUALIFIED,
    NOT_CLASSIFIED,
    RETIRED
}

export interface LapData {
    carIndex?: number;
    playersCar?: boolean;
    lastLapTime?: number;
    currentLapTime?: number;
    bestLapTime?: number;
    sector1Time?: number;
    sector2Time?: number;
    lapDistance?: number;
    totalDistance?: number;
    safetyCarDelta?: number;
    carPosition?: number;
    currentLapNum?: number;
    pitStatus?: PitStatus;
    sector?: number;
    currentLapInvalid?: boolean;
    penalties?: number;
    gridPosition?: number;
    driverStatus?: DriverStatus;
    resultStatus?: ResultStatus;
}

export enum Weather {
    CLEAR,
    LIGHT_CLOUD,
    OVERCAST,
    LIGHT_RAIN,
    HEAVY_RAIN,
    STORM
}

export enum SessionType {
    UNKNOWN,
    P1,
    P2,
    P3,
    SHORT_P,
    Q1,
    Q2,
    Q3,
    SHORT_Q,
    OSQ,
    R,
    R2,
    TIME_TRIAL
}

export enum Era {
    MODERN,
    CLASSIC
}

export enum SafetyCarStatus {
    NO_SAFETY_CAR,
    FULL_SAFETY_CAR,
    VIRTUAL_SAFETY_CAR
}

export enum ZoneFlag {
    NONE,
    GREEN,
    BLUE,
    YELLOW,
    RED,
    UNKNOWN
}

export interface MarshalZone {
    zoneStart?: number;
    zoneFlag?: ZoneFlag;
}

export interface SessionData extends Packet {
    weather?: Weather;
    trackTemperature?: number;
    airTemperature?: number;
    totalLaps?: number;
    trackLength?: number;
    sessionType?: SessionType;
    trackId?: number;
    era?: Era;
    sessionTimeLeft?: number;
    sessionDuration?: number;
    pitSpeedLimit?: number;
    gamePaused?: boolean;
    spectating?: boolean;
    spectatorCarIndex?: number;
    sliProNativeSupport?: boolean;
    numMarshalZones?: number;
    marshalZones?: Array<MarshalZone>;
    safetyCarStatus?: SafetyCarStatus;
    networkGame?: boolean;
}

export interface LapHistoryData {
    lapTime?: number;
    sector1Time?: number;
    sector2Time?: number;
    sector3Time?: number;
    lapValidBitFlags?: number;
}

export interface TyreStintHistoryData {
    endLap?: number;
    tyreActualCompound?: number;
    tyreVisualCompound?: number;
}

export interface PacketSessionHistoryData extends Packet {
    carIdx?: number;
    numLaps?: number;
    numTyreStints?: number;
    bestLapTimeLapNum?: number;
    bestSector1LapNum?: number;
    bestSector2LapNum?: number;
    bestSector3LapNum?: number;
    lapHistoryData?: Array<LapHistoryData>;
    tyreStintHistoryData?: Array<TyreStintHistoryData>;
}
