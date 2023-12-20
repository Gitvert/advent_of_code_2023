import kotlin.random.Random

fun day20 (lines: List<String>) {
    val connections = parseConnections(lines)
    val modules = parseModules(lines, connections)
    val random = Random(1337)
    
    var lowPulses = 0L
    var highPulses = 0L
    
    for (i in 1..1000) {
        val pulses = mutableListOf<Pulse>()
        pulses.add(Pulse("broadcaster", Signal.LOW, random.nextInt()))
        lowPulses++

        while (pulses.isNotEmpty()) {
            val nextPulse = pulses.first()
            pulses.remove(nextPulse)

            connections.filter { it.first == nextPulse.source }.forEach { connection ->
                if (nextPulse.signal == Signal.LOW) {
                    lowPulses++
                } else {
                    highPulses++
                }
                
                if (modules.containsKey(connection.second)) {
                    val module = modules[connection.second]!!
                    val result = module.receivePulse(nextPulse.signal, nextPulse.source)
                    if (result != null) {
                        pulses.add(Pulse(connection.second, result, random.nextInt()))
                    }
                }
            }
        }
    }
    
    println("Day 20 part 1: ${lowPulses * highPulses}")
    println("Day 20 part 2: ")
    println()
}

fun parseConnections(lines: List<String>): List<Pair<String, String>> {
    val connections = mutableListOf<Pair<String, String>>()
    
    lines.map { it.replace(" ", "").replace("%", "").replace("&", "") }.forEach { line ->
        val from = line.split("->")[0]
        val to = line.split("->")[1].split(",")
        
        to.forEach { 
            connections.add(Pair(from, it))
        }
    }
    
    return connections
}

fun parseModules(lines: List<String>, connections: List<Pair<String, String>>): Map<String, Module> {
    val modules = mutableMapOf<String, Module>()
    
    lines.map { it.replace(" ", "") }.forEach { line -> 
        val name = line.split("->")[0]
        if (name.startsWith("%")) {
            modules[name.removePrefix("%")] = FlipFlopModule()
        } else if (name.startsWith("&")) {
            val inputs = connections.filter { it.second == name.removePrefix("&") }
            modules[name.removePrefix("&")] = ConjunctionModule(inputs.map { it.first })
        } else {
            modules[name] = BroadcasterModule()
        }
    }
    
    modules["output"] = OutputModule()
    
    return modules
}

interface Module {
    fun receivePulse(signal: Signal, source: String): Signal?
}

enum class Signal {
    LOW,
    HIGH
}

data class Pulse(val source: String, val signal: Signal, val seed: Int)

class FlipFlopModule : Module {
    private var on = false
    
    override fun receivePulse(signal: Signal, source: String): Signal? {
        if (signal == Signal.LOW) {
            on = !on
            
            return if (on) { Signal.HIGH } else { Signal.LOW }
        }
        
        return null
    }

}

class ConjunctionModule(inputs: List<String>) : Module {
    private val recentPulses = mutableMapOf<String, Signal>()

    init {
        inputs.forEach { 
            recentPulses[it] = Signal.LOW
        }
    }
    
    override fun receivePulse(signal: Signal, source: String): Signal {
        recentPulses[source] = signal
        return if (recentPulses.values.all { it == Signal.HIGH }) {
            Signal.LOW
        } else {
            Signal.HIGH
        }
    }
}

class BroadcasterModule : Module {
    override fun receivePulse(signal: Signal, source: String): Signal {
        return signal
    }
}

class OutputModule : Module {
    override fun receivePulse(signal: Signal, source: String): Signal? {
        return null
    }
}