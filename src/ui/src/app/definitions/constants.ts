export interface CompoundInfo {
    short: string;
    label: string;
    temperature?: Array<number>;
}

export const TYRE_SPECS: { [key: number]: CompoundInfo } = {
    0: {
        short: "HS",
        label: "Hyper Soft",
        temperature: [85, 105]
    },
    1: {
        short: "US",
        label: "Ultra Soft",
        temperature: [90, 110]
    },
    2: {
        short: "SS",
        label: "Super Soft",
        temperature: [90, 110]
    },
    3: {
        short: "S",
        label: "Soft",
        temperature: [105, 135]
    },
    4: {
        short: "M",
        label: "Medium",
        temperature: [110, 140]
    },
    5: {
        short: "H",
        label: "Hard",
        temperature: [105, 135]
    },
    6: {
        short: "SH",
        label: "Super Hard",
        temperature: [120, 145]
    },
    7: {
        short: "I",
        label: "Intermediate",
        temperature: []
    },
    8: {
        short: "W",
        label: "Wet",
        temperature: []
    },
    11: {
        short: "SS",
        label: "Super Soft",
        temperature: [90, 110]
    },
    12: {
        short: "S",
        label: "Soft",
        temperature: [105, 135]
    },
    13: {
        short: "M",
        label: "Medium",
        temperature: [110, 140]
    },
    14: {
        short: "H",
        label: "Hard",
        temperature: [105, 135]
    },
    15: {
        short: "W",
        label: "Wet",
        temperature: []
    },
    16: {
        short: "C5",
        label: "C5",
        temperature: [85, 110]
    },
    17: {
        short: "C4",
        label: "C4",
        temperature: [90, 120]
    },
    18: {
        short: "C3",
        label: "C3",
        temperature: [105, 135]
    },
    19: {
        short: "C2",
        label: "C2",
        temperature: [110, 135]
    },
    20: {
        short: "C1",
        label: "C1",
        temperature: [110, 140]
    }
};
