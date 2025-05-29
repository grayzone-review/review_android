package colors

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

private fun String.asComposeColor(): Color = Color(this.toColorInt())

object CS {
    object Gray {
        const val WhiteHex = "#FFFFFF"
        const val G10Hex   = "#F5F5F5"
        const val G20Hex   = "#E0E0E0"
        const val G30Hex   = "#BDBDBD"
        const val G40Hex   = "#9E9E9E"
        const val G50Hex   = "#757575"
        const val G60Hex   = "#616161"
        const val G70Hex   = "#424242"
        const val G80Hex   = "#212121"
        const val G90Hex   = "#141414"
        const val BlackHex = "#000000"

        val White = WhiteHex.asComposeColor()
        val G10   = G10Hex.asComposeColor()
        val G20   = G20Hex.asComposeColor()
        val G30   = G30Hex.asComposeColor()
        val G40   = G40Hex.asComposeColor()
        val G50   = G50Hex.asComposeColor()
        val G60   = G60Hex.asComposeColor()
        val G70   = G70Hex.asComposeColor()
        val G80   = G80Hex.asComposeColor()
        val G90   = G90Hex.asComposeColor()
        val Black = BlackHex.asComposeColor()
    }

    object System {
        const val RedHex    = "#D92727"
        const val YellowHex = "#FCC425"
        const val BlueHex   = "#2786D9"

        val Red    = RedHex.asComposeColor()
        val Yellow = YellowHex.asComposeColor()
        val Blue   = BlueHex.asComposeColor()
    }

    object PrimaryOrange {
        const val O10Hex = "#FFF5F2"
        const val O20Hex = "#FFBBA6"
        const val O30Hex = "#FF9573"
        const val O40Hex = "#FF6E40"
        const val O50Hex = "#FF5B26"
        const val O60Hex = "#FF470D"
        const val O70Hex = "#D93400"

        val O10 = O10Hex.asComposeColor()
        val O20 = O20Hex.asComposeColor()
        val O30 = O30Hex.asComposeColor()
        val O40 = O40Hex.asComposeColor()
        val O50 = O50Hex.asComposeColor()
        val O60 = O60Hex.asComposeColor()
        val O70 = O70Hex.asComposeColor()
    }

    object SemanticYellow {
        const val Y10Hex = "#fffbf2"
        const val Y20Hex = "#FFE3A6"
        const val Y30Hex = "#FFD373"
        const val Y40Hex = "#FFC340"
        const val Y50Hex = "#FFB30D"
        const val Y60Hex = "#D99500"
        const val Y70Hex = "#A67200"

        val Y10 = Y10Hex.asComposeColor()
        val Y20 = Y20Hex.asComposeColor()
        val Y30 = Y30Hex.asComposeColor()
        val Y40 = Y40Hex.asComposeColor()
        val Y50 = Y50Hex.asComposeColor()
        val Y60 = Y60Hex.asComposeColor()
        val Y70 = Y70Hex.asComposeColor()
    }

    object SecondBlue {
        const val B10Hex = "#F2FAFF"
        const val B20Hex = "#D9EFFF"
        const val B30Hex = "#A6D9FF"
        const val B40Hex = "#40AEFF"
        const val B50Hex = "#0D99FF"
        const val B60Hex = "#007ED9"
        const val B70Hex = "#0060A6"

        val B10: Color = B10Hex.asComposeColor()
        val B20: Color = B20Hex.asComposeColor()
        val B30: Color = B30Hex.asComposeColor()
        val B40: Color = B40Hex.asComposeColor()
        val B50: Color = B50Hex.asComposeColor()
        val B60: Color = B60Hex.asComposeColor()
        val B70: Color = B70Hex.asComposeColor()
    }

    object SemanticRed {
        const val R10Hex = "#FFF2F2"
        const val R20Hex = "#FFA6A6"
        const val R30Hex = "#FF7373"
        const val R40Hex = "#FF4040"
        const val R50Hex = "#FF0D0D"
        const val R60Hex = "#D90000"
        const val R70Hex = "#A60000"

        val R10: Color = R10Hex.asComposeColor()
        val R20: Color = R20Hex.asComposeColor()
        val R30: Color = R30Hex.asComposeColor()
        val R40: Color = R40Hex.asComposeColor()
        val R50: Color = R50Hex.asComposeColor()
        val R60: Color = R60Hex.asComposeColor()
        val R70: Color = R70Hex.asComposeColor()
    }

    object SemanticBlue {
        const val B10Hex = "#F2F8FF"
        const val B20Hex = "#A6CFFF"
        const val B30Hex = "#73B4FF"
        const val B40Hex = "#4099FF"
        const val B50Hex = "#0D7EFF"
        const val B60Hex = "#0065D9"
        const val B70Hex = "#004DA6"

        val B10: Color = B10Hex.asComposeColor()
        val B20: Color = B20Hex.asComposeColor()
        val B30: Color = B30Hex.asComposeColor()
        val B40: Color = B40Hex.asComposeColor()
        val B50: Color = B50Hex.asComposeColor()
        val B60: Color = B60Hex.asComposeColor()
        val B70: Color = B70Hex.asComposeColor()
    }
}
