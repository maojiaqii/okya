import CustomPalette from "./CustomPalette";
import CustomContextPadProvider from "./CustomContextPadProvider";

export default {
  __init__: ["paletteProvider"],
  paletteProvider: ["type", CustomPalette],
  contextPadProvider: ["type", CustomContextPadProvider]
};
