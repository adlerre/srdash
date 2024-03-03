export interface Config {
    entry?: Array<ConfigEntry>;
}

export interface ConfigEntry {
    key: string;
    value: string;
}
