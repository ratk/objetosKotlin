//estudo realizado utilizando -> https://developer.android.com/codelabs/basic-android-kotlin-compose-classes-and-objects?hl=pt_br#0

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SmartDevice(val name: String, val category: String) {
    
    open val deviceType = "unknown"
    
    var deviceStatus = "online"
        protected set
    
    open var deviceTurnOnCount = 0
    
    constructor(name: String, category: String, statusCode: Int) : this(name, category) {
        deviceStatus = when (statusCode) {
            0 -> "offline"
            1 -> "online"
            else -> "unknown"
        }
    }

    open fun turnOn() {
        deviceStatus = "on"
    }

    open fun turnOff() {
        deviceStatus = "off"
    }
    
    fun printSmartTvInfo() {
        println("Device name: $name, category: $category, type: $deviceType")
    }
}

class SmartTvDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart TV"

    private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)

    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume.")
    }

    fun decreaseVolume() {
        speakerVolume--
        println("Speaker volume decreased to $speakerVolume.")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel number increased to $channelNumber.")
    }
    
    fun previousChannel() {
        channelNumber--
        println("Channel number decreased to $channelNumber.")
    }

    override fun turnOn() {
        //deviceStatus = "on"
        super.turnOn()
        println(
            "$name is turned on. Speaker volume is set to $speakerVolume and channel number is " +
                "set to $channelNumber."
        )
    }

    override fun turnOff() {
        super.turnOff()
        println("$name turned off")
    }
}
    
class SmartLightDevice(deviceName: String, deviceCategory: String) :
    SmartDevice(name = deviceName, category = deviceCategory) {
        
    override val deviceType = "Smart Light"

    private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 100)

//     private var brightnessLevel = 0
//         set(value) {
//             if (value in 0..100) {
//                 field = value
//             }
//         }

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness increased to $brightnessLevel.")
    }

    fun decreaseBrightness() {
        brightnessLevel++
        println("Brightness decreased to $brightnessLevel.")
    }

    override fun turnOn() {
		//deviceStatus = "on"
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff() {
        super.turnOff()
        brightnessLevel = 0
        println("Smart Light turned off")
    }
}

// The SmartHome class HAS-A smart TV device.
class SmartHome(
    val smartTvDevice: SmartTvDevice,
    val smartLightDevice: SmartLightDevice
) {

    fun turnOnTv() {
        if(smartTvDevice.deviceTurnOnCount <= 0) {
            smartTvDevice.deviceTurnOnCount++
            smartTvDevice.turnOn()
        }
    }

    fun turnOffTv() {
        if(smartTvDevice.deviceTurnOnCount > 0) {
            smartTvDevice.deviceTurnOnCount--
            smartTvDevice.turnOff()
        }
    }

    fun increaseTvVolume() {
        if(smartTvDevice.deviceTurnOnCount > 0) {
        	smartTvDevice.increaseSpeakerVolume()
        }
    }

    fun decreaseTvVolume() {
        if(smartTvDevice.deviceTurnOnCount > 0) {
        	smartTvDevice.decreaseVolume()
        }
    }

    fun changeTvChannelToNext() {
        if(smartTvDevice.deviceTurnOnCount > 0) {
        	smartTvDevice.nextChannel()
        }
    }

    fun changeTvChannelToPrevious() {
        if(smartTvDevice.deviceTurnOnCount > 0) {
        	smartTvDevice.previousChannel()
        }
    }

    fun turnOnLight() {
        if(smartLightDevice.deviceTurnOnCount <= 0) {
            smartLightDevice.deviceTurnOnCount++
            smartLightDevice.turnOn()
        }
    }

    fun turnOffLight() {
        if(smartLightDevice.deviceTurnOnCount > 0) {
            smartLightDevice.deviceTurnOnCount--
            smartLightDevice.turnOff()
        }
    }

    fun increaseLightBrightness() {
        if(smartLightDevice.deviceTurnOnCount > 0) {
        	smartLightDevice.increaseBrightness()
        }
    }

    fun decreaseLightBrightness() {
        if(smartLightDevice.deviceTurnOnCount > 0) {
        	smartLightDevice.decreaseBrightness()
        }
    }

    fun printSmartTvInfo() {
        smartTvDevice.printSmartTvInfo()
    }

    fun printSmartLightInfo() {
        smartLightDevice.printSmartTvInfo()
    }

    fun turnOffAllDevices() {
        if(smartTvDevice.deviceTurnOnCount > 0) {
        	turnOffTv()
        }
        if(smartLightDevice.deviceTurnOnCount > 0) {
        	turnOffLight()
        }
    }
}

class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {

    var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}
    
fun main() {
    var tv: SmartTvDevice = SmartTvDevice("Android TV", "Entertainment")
	var luz: SmartLightDevice = SmartLightDevice("Google Light", "Utility")
    
    var smartHome: SmartHome = SmartHome(tv, luz)
    
    smartHome.turnOnTv()
    smartHome.increaseTvVolume()
    smartHome.turnOnLight()
    smartHome.increaseTvVolume()
    smartHome.increaseLightBrightness()
    smartHome.turnOffAllDevices()
    smartHome.printSmartTvInfo()
    smartHome.printSmartLightInfo()
}